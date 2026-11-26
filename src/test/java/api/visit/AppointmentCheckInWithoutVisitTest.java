package api.visit;

import base.BaseTest;
import io.restassured.specification.RequestSpecification;
import models.AppointmentResponse;
import models.TestPatient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.steps.AppointmentSteps;
import requests.steps.PatientSteps;
import requests.steps.VisitSteps;
import specs.RequestSpecs;

import static org.assertj.core.api.Assertions.assertThat;

public class AppointmentCheckInWithoutVisitTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("VIS-02: запись проходит Scheduled → CheckedIn → Completed, отметка о приходе не открывает визит")
    public void appointmentCompletesWithoutOpeningVisitOnCheckIn() {
        // ПУ-2: запросы от имени admin
        RequestSpecification adminSpec = RequestSpecs.adminSpec();
        // ПУ-4: пациент создан через API
        TestPatient patient = PatientSteps.createPatient(adminSpec);
        // ПУ-5: для пациента создана встреча в статусе Scheduled
        AppointmentResponse appointment = AppointmentSteps.createAppointment(adminSpec, patient);

        // Шаг 1. Сменить статус на CheckedIn: POST /appointments/{uuid}/status-change, ожидаем 200
        AppointmentSteps.changeStatus(adminSpec, appointment.getUuid(), "CheckedIn");

        // Шаг 3 тест-плана после шага 1: получить встречу — статус CheckedIn
        softly.check(() -> StepLogger.log("Проверить сохранённый статус CheckedIn", () -> {
            AppointmentResponse savedAppointment = AppointmentSteps.getAppointment(adminSpec, appointment.getUuid());
            assertThat(savedAppointment.getStatus()).isEqualTo("CheckedIn");
        }));
        // правило R12: отметка о приходе через API визит не создаёт
        softly.check(() -> StepLogger.log("Проверить, что отметка о приходе не открыла визит", () -> {
            assertThat(VisitSteps.getActiveVisits(adminSpec, patient.getUuid())).isEmpty();
        }));

        // Шаг 2. Сменить статус на Completed, ожидаем 200
        AppointmentSteps.changeStatus(adminSpec, appointment.getUuid(), "Completed");

        // Шаг 3 тест-плана после шага 2: получить встречу — статус Completed
        softly.check(() -> StepLogger.log("Проверить сохранённый статус Completed", () -> {
            AppointmentResponse savedAppointment = AppointmentSteps.getAppointment(adminSpec, appointment.getUuid());
            assertThat(savedAppointment.getStatus()).isEqualTo("Completed");
        }));
    }
}
