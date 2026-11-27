package requests.steps;

import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.*;
import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.ValidatedCrudRequester;
import requests.skeleton.requests.ValidatedSearchRequester;
import specs.ResponseSpecs;

import java.util.List;
import java.util.Map;

// Приём (encounter) и показатели (obs): подготовка данных и чтение для проверок.
public class EncounterSteps {

    // Запрос приёма с показателями: каждый показатель — отдельный ObsRequest (понятие + значение).
    // Время приёма — на 5 секунд раньше текущего: сервер хранит его с точностью до секунды,
    // и без запаса завершение визита «сейчас» иногда оказывается раньше приёма (ответ 400).
    public static EncounterRequest createVitalsRequest(VisitResponse visit, VitalsData vitals) {
        List<ObsRequest> obs = List.of(
                new ObsRequest(ReferenceSteps.WEIGHT_CONCEPT, vitals.getWeight()),
                new ObsRequest(ReferenceSteps.HEIGHT_CONCEPT, vitals.getHeight()),
                new ObsRequest(ReferenceSteps.TEMPERATURE_CONCEPT, vitals.getTemperature()),
                new ObsRequest(ReferenceSteps.SYSTOLIC_CONCEPT, vitals.getSystolic()),
                new ObsRequest(ReferenceSteps.DIASTOLIC_CONCEPT, vitals.getDiastolic()));

        return EncounterRequest.builder()
                .patient(visit.getPatient().getUuid())
                .encounterType(ReferenceSteps.getVitalsEncounterTypeUuid())
                .visit(visit.getUuid())
                .location(ReferenceSteps.getLocationUuid())
                .encounterDatetime(TestDates.format(TestDates.now().minusSeconds(5)))
                .obs(obs)
                .build();
    }

    public static EncounterResponse recordVitals(RequestSpecification spec, VisitResponse visit, VitalsData vitals) {
        return StepLogger.log("Записать показатели в приёме Vitals",
                () -> new ValidatedCrudRequester<EncounterResponse>(spec, Endpoint.ENCOUNTER,
                        ResponseSpecs.entityWasCreated()).post(createVitalsRequest(visit, vitals)));
    }

    public static EncounterResponse getEncounter(RequestSpecification spec, String encounterUuid) {
        return new ValidatedCrudRequester<EncounterResponse>(spec, Endpoint.ENCOUNTER, ResponseSpecs.requestReturnsOk())
                .get(encounterUuid);
    }

    // v=default: без него поиск возвращает только uuid и display, без понятия и значения
    public static List<ObsResponse> getObservations(RequestSpecification spec, String patientUuid) {
        return new ValidatedSearchRequester<ObsResponse>(spec, Endpoint.OBS, ResponseSpecs.requestReturnsOk())
                .search(Map.of("patient", patientUuid, "v", "default"));
    }
}
