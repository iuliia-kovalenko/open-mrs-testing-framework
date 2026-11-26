package requests.steps;

import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.CloseVisitRequest;
import models.CreateVisitRequest;
import models.TestPatient;
import models.VisitResponse;
import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.ValidatedCrudRequester;
import requests.skeleton.requests.ValidatedSearchRequester;
import specs.ResponseSpecs;

import java.util.List;
import java.util.Map;

// Визиты: подготовка данных и чтение для проверок.
public class VisitSteps {

    // Запрос визита. Начало — минутой раньше текущего времени,
    // чтобы завершение визита «сейчас» всегда было позже начала, даже если часы стенда чуть отстают.
    public static CreateVisitRequest createVisitRequest(TestPatient patient) {
        return CreateVisitRequest.builder()
                .patient(patient.getUuid())
                .visitType(ReferenceSteps.getVisitTypeUuid())
                .location(ReferenceSteps.getLocationUuid())
                .startDatetime(TestDates.format(TestDates.now().minusMinutes(1)))
                .build();
    }

    // Запрос завершения визита: время окончания — сейчас
    public static CloseVisitRequest createCloseVisitRequest() {
        return new CloseVisitRequest(TestDates.format(TestDates.now()));
    }

    // ПУ-6. Открытие активного визита пациента.
    public static VisitResponse startVisit(RequestSpecification spec, TestPatient patient) {
        return StepLogger.log("Открыть визит пациента",
                () -> new ValidatedCrudRequester<VisitResponse>(spec, Endpoint.VISIT, ResponseSpecs.entityWasCreated())
                        .post(createVisitRequest(patient)));
    }

    public static VisitResponse closeVisit(RequestSpecification spec, String visitUuid) {
        return StepLogger.log("Закрыть визит пациента",
                () -> new ValidatedCrudRequester<VisitResponse>(spec, Endpoint.VISIT, ResponseSpecs.requestReturnsOk())
                        .update(visitUuid, createCloseVisitRequest()));
    }

    public static VisitResponse getVisit(RequestSpecification spec, String visitUuid) {
        return new ValidatedCrudRequester<VisitResponse>(spec, Endpoint.VISIT, ResponseSpecs.requestReturnsOk())
                .get(visitUuid);
    }

    // Активные визиты пациента: includeInactive=false отсекает завершённые
    public static List<VisitResponse> getActiveVisits(RequestSpecification spec, String patientUuid) {
        return new ValidatedSearchRequester<VisitResponse>(spec, Endpoint.VISIT, ResponseSpecs.requestReturnsOk())
                .search(Map.of("patient", patientUuid, "includeInactive", false, "v", "default"));
    }
}
