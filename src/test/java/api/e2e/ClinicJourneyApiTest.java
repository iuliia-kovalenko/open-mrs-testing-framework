package api.e2e;

import base.BaseTest;
import db.DbClient;
import db.DbQueries;
import generators.RandomModelGenerator;
import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.steps.AppointmentSteps;
import requests.steps.EncounterSteps;
import requests.steps.PatientSteps;
import requests.steps.VisitSteps;
import specs.RequestSpecs;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Сквозной сценарий проверяет путь целиком, а не один запрос, поэтому все шаги — через Steps.
// SQL-проверки — отдельными шагами по таблицам: запросы к базе не HTTP, без шагов их не видно в Allure.
public class ClinicJourneyApiTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("E2E-02: основной сквозной сценарий через API с проверкой записей в БД")
    public void clinicJourneyThroughApi() {
        // ПУ-2: запросы от имени admin; ПУ-9: порт базы 3307 открыт (docker-compose.yml в корне проекта)
        RequestSpecification adminSpec = RequestSpecs.adminSpec();

        // ---------- Шаг 1. Сценарий 1 — регистрация (раздел 8.1 тест-плана) ----------
        // ПУ-4: OpenMRS ID у генератора и POST /patient — внутри Steps
        TestPatient patient = PatientSteps.createPatient(adminSpec);

        List<PatientResponse> foundById = PatientSteps.searchByIdentifier(adminSpec, patient.getOpenMrsId());
        softly.check(() -> StepLogger.log("Проверить, что по OpenMRS ID найден ровно один пациент — созданный", () -> {
            assertThat(foundById).extracting(PatientResponse::getUuid).containsExactly(patient.getUuid());
        }));

        List<PatientResponse> foundByName = PatientSteps.searchByName(adminSpec, patient.getFamilyName());
        softly.check(() -> StepLogger.log("Проверить, что пациент находится по фамилии", () -> {
            assertThat(foundByName).extracting(PatientResponse::getUuid).contains(patient.getUuid());
        }));

        // ---------- Шаг 2. SQL: в каждой таблице одна действующая запись, данные совпадают ----------
        softly.check(() -> StepLogger.log("Проверить запись в таблице person", () -> {
            assertThat(DbClient.countRows(DbQueries.PERSON, patient.getUuid())).isEqualTo(1);
            assertThat(DbClient.getValue(DbQueries.PERSON, patient.getUuid(), "gender")).isEqualTo(patient.getGender());
            assertThat(DbClient.getValue(DbQueries.PERSON, patient.getUuid(), "birthdate")).isEqualTo(patient.getBirthdate());
        }));
        softly.check(() -> StepLogger.log("Проверить запись в таблице person_name", () -> {
            assertThat(DbClient.countRows(DbQueries.PERSON_NAME, patient.getUuid())).isEqualTo(1);
            assertThat(DbClient.getValue(DbQueries.PERSON_NAME, patient.getUuid(), "given_name")).isEqualTo(patient.getGivenName());
            assertThat(DbClient.getValue(DbQueries.PERSON_NAME, patient.getUuid(), "family_name")).isEqualTo(patient.getFamilyName());
        }));
        softly.check(() -> StepLogger.log("Проверить запись в таблице patient", () -> {
            assertThat(DbClient.countRows(DbQueries.PATIENT, patient.getUuid())).isEqualTo(1);
        }));
        softly.check(() -> StepLogger.log("Проверить запись в таблице patient_identifier", () -> {
            assertThat(DbClient.countRows(DbQueries.PATIENT_IDENTIFIER, patient.getUuid())).isEqualTo(1);
            assertThat(DbClient.getValue(DbQueries.PATIENT_IDENTIFIER, patient.getUuid(), "identifier")).isEqualTo(patient.getOpenMrsId());
        }));

        // ---------- Шаг 3. Сценарии 2 и 3 — запись на встречу и приём ----------
        // Сценарий 2: конфликтов нет (204), встреча создана, в статусе Scheduled и находится поиском
        AppointmentRequest appointmentRequest = AppointmentSteps.createAppointmentRequest(patient);
        AppointmentSteps.checkNoConflicts(adminSpec, appointmentRequest);
        AppointmentResponse appointment = AppointmentSteps.createAppointment(adminSpec, appointmentRequest);

        AppointmentResponse savedAppointment = AppointmentSteps.getAppointment(adminSpec, appointment.getUuid());
        softly.check(() -> StepLogger.log("Проверить, что встреча в статусе Scheduled", () -> {
            assertThat(savedAppointment.getStatus()).isEqualTo("Scheduled");
        }));

        // createAppointmentRequest ставит встречу на завтра в 10:00 — ищем за этот день
        List<AppointmentResponse> foundAppointments =
                AppointmentSteps.searchByDate(adminSpec, patient.getUuid(), TestDates.tomorrowAt(10));
        softly.check(() -> StepLogger.log("Проверить, что встреча находится поиском", () -> {
            assertThat(foundAppointments).extracting(AppointmentResponse::getUuid).contains(appointment.getUuid());
        }));

        // Сценарий 3: визит, CheckedIn, показатели, Completed, закрытие визита
        VisitResponse visit = VisitSteps.startVisit(adminSpec, patient);
        AppointmentSteps.changeStatus(adminSpec, appointment.getUuid(), "CheckedIn");
        VitalsData vitals = RandomModelGenerator.generate(VitalsData.class);
        EncounterResponse encounter = EncounterSteps.recordVitals(adminSpec, visit, vitals);
        AppointmentSteps.changeStatus(adminSpec, appointment.getUuid(), "Completed");
        VisitSteps.closeVisit(adminSpec, visit.getUuid());

        List<VisitResponse> activeVisits = VisitSteps.getActiveVisits(adminSpec, patient.getUuid());
        softly.check(() -> StepLogger.log("Проверить, что активных визитов нет", () -> {
            assertThat(activeVisits).isEmpty();
        }));

        // ---------- Шаг 4. SQL: визит закрыт, все показатели сохранены, встреча в статусе Completed ----------
        softly.check(() -> StepLogger.log("Проверить запись в таблице visit", () -> {
            assertThat(DbClient.getValue(DbQueries.VISIT, visit.getUuid(), "date_stopped")).isNotNull();
        }));
        // в VitalsData пять показателей: вес, рост, температура, верхнее и нижнее давление
        softly.check(() -> StepLogger.log("Проверить показатели в таблице obs", () -> {
            assertThat(DbClient.countRows(DbQueries.OBS_OF_ENCOUNTER, encounter.getUuid())).isEqualTo(5);
        }));
        softly.check(() -> StepLogger.log("Проверить запись в таблице patient_appointment", () -> {
            assertThat(DbClient.getValue(DbQueries.APPOINTMENT, appointment.getUuid(), "status")).isEqualTo("Completed");
        }));
    }
}
