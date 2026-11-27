package api.visit;

import base.BaseTest;
import generators.RandomModelGenerator;
import io.restassured.specification.RequestSpecification;
import models.EncounterResponse;
import models.ObsResponse;
import models.RefResponse;
import models.TestPatient;
import models.VisitResponse;
import models.VitalsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.steps.EncounterSteps;
import requests.steps.PatientSteps;
import requests.steps.ReferenceSteps;
import requests.steps.VisitSteps;
import specs.RequestSpecs;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class VitalsTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("VIS-05: показатели (вес, рост, температура, давление) сохраняются и читаются")
    public void vitalsAreSavedAndRead() {
        // ПУ-2: запросы от имени admin
        RequestSpecification adminSpec = RequestSpecs.adminSpec();
        // ПУ-4: пациент создан через API
        TestPatient patient = PatientSteps.createPatient(adminSpec);
        // ПУ-6: для пациента открыт активный визит
        VisitResponse visit = VisitSteps.startVisit(adminSpec, patient);

        // Генерируем один набор показателей для запроса и проверки ответа.
        VitalsData vitals = RandomModelGenerator.generate(VitalsData.class);

        // Шаг 1. POST /encounter типа Vitals с показателями в рамках визита. Ожидаем 201.
        EncounterResponse encounter = EncounterSteps.recordVitals(adminSpec, visit, vitals);
        assertThat(encounter.getUuid()).as("UUID созданного приёма").isNotBlank();

        // Перечитываем приём по UUID, чтобы проверить сохранённую привязку к визиту.
        softly.check(() -> StepLogger.log("Проверить привязку приёма к открытому визиту", () -> {
            EncounterResponse savedEncounter = EncounterSteps.getEncounter(adminSpec, encounter.getUuid());
            assertThat(savedEncounter.getVisit())
                    .as("Визит сохранённого приёма")
                    .returns(visit.getUuid(), RefResponse::getUuid);
        }));

        // Шаг 2. GET /obs?patient={uuid}. Сверяем пять пар «UUID концепта, значение».
        softly.check(() -> StepLogger.log("Проверить сохранённые показатели", () -> {
            List<ObsResponse> observations = EncounterSteps.getObservations(adminSpec, patient.getUuid());
            assertThat(observations)
                    .extracting("concept.uuid", "value")
                    .containsExactlyInAnyOrder(
                            tuple(ReferenceSteps.WEIGHT_CONCEPT, vitals.getWeight()),
                            tuple(ReferenceSteps.HEIGHT_CONCEPT, vitals.getHeight()),
                            tuple(ReferenceSteps.TEMPERATURE_CONCEPT, vitals.getTemperature()),
                            tuple(ReferenceSteps.SYSTOLIC_CONCEPT, vitals.getSystolic()),
                            tuple(ReferenceSteps.DIASTOLIC_CONCEPT, vitals.getDiastolic())
                    );
        }));
    }
}
