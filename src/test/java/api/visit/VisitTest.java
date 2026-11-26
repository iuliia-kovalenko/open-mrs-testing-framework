package api.visit;

import base.BaseTest;
import io.restassured.specification.RequestSpecification;
import models.TestPatient;
import models.VisitResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.steps.PatientSteps;
import requests.steps.VisitSteps;
import specs.RequestSpecs;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class VisitTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("VIS-03: создание визита — у пациента появляется активный визит")
    public void startedVisitIsActive() {
        // ПУ-2. Запросы от имени admin
        RequestSpecification adminSpec = RequestSpecs.adminSpec();
        // ПУ-4. Пациент создан через API
        TestPatient patient = PatientSteps.createPatient(adminSpec);

        // Шаг 1. POST /visit с пациентом, типом Facility Visit и локацией. Ожидаем 201.
        VisitResponse visit = VisitSteps.startVisit(adminSpec, patient);

        // UUID из ответа нужен, чтобы найти именно созданный визит в списке.
        assertThat(visit.getUuid()).isNotBlank();

        // Шаг 2. GET /visit?patient={uuid}&includeInactive=false&v=default
        StepLogger.log("Проверить, что созданный визит активен", () -> {
            List<VisitResponse> activeVisits = VisitSteps.getActiveVisits(adminSpec, patient.getUuid());
            // Проверяем размер списка перед обращением к единственному элементу.
            assertThat(activeVisits).hasSize(1);
            VisitResponse activeVisit = activeVisits.get(0);
            assertSoftly(check -> {
                check.assertThat(activeVisit.getUuid()).isEqualTo(visit.getUuid());
                check.assertThat(activeVisit.getStopDatetime()).isNull();
            });
        });
    }

    @Test
    @Tag("P0")
    @DisplayName("VIS-06: завершение визита — время окончания заполнено, визит неактивен")
    public void closedVisitIsNotActive() {
        // ПУ-2. Запросы от имени admin
        RequestSpecification adminSpec = RequestSpecs.adminSpec();
        // ПУ-4. Пациент создан через API
        TestPatient patient = PatientSteps.createPatient(adminSpec);
        // ПУ-6. Для пациента открыт активный визит
        VisitResponse visit = VisitSteps.startVisit(adminSpec, patient);

        // Проверяем UUID для запроса на закрытие и отсутствие времени окончания до запроса.
        assertThat(visit.getUuid()).isNotBlank();
        assertThat(visit.getStopDatetime()).isNull();

        // Шаг 1. POST /visit/{uuid} с текущим временем в stopDatetime. Ожидаем 200.
        VisitSteps.closeVisit(adminSpec, visit.getUuid());

        // Шаг 2. Читаем сохранённый визит по UUID и получаем список активных визитов пациента.
        softly.check(() -> StepLogger.log("Проверить сохранённое время окончания визита", () -> {
            VisitResponse savedVisit = VisitSteps.getVisit(adminSpec, visit.getUuid());
            assertThat(savedVisit.getStopDatetime()).isNotBlank();
        }));
        softly.check(() -> StepLogger.log("Проверить отсутствие активных визитов", () -> {
            List<VisitResponse> activeVisits = VisitSteps.getActiveVisits(adminSpec, patient.getUuid());
            assertThat(activeVisits).isEmpty();
        }));
    }
}
