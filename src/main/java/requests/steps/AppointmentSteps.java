package requests.steps;

import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.*;
import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.SearchRequester;
import requests.skeleton.requests.ValidatedActionRequester;
import specs.ResponseSpecs;

import java.time.ZonedDateTime;
import java.util.Map;

// Встречи: подготовка данных и чтение для проверок.
// Модуль appointments устроен иначе, чем остальной OpenMRS:
// создание отвечает 200 (а не 201), а списки приходят обычным массивом без "results".
public class AppointmentSteps {

    // Запрос встречи на завтра в 10:00 на 30 минут в Outpatient Department
    public static AppointmentRequest createAppointmentRequest(TestPatient patient) {
        ZonedDateTime start = TestDates.tomorrowAt(10);
        return AppointmentRequest.builder()
                .patientUuid(patient.getUuid())
                .serviceUuid(ReferenceSteps.getAppointmentServiceUuid())
                .locationUuid(ReferenceSteps.getLocationUuid())
                .startDateTime(TestDates.format(start))
                .endDateTime(TestDates.format(start.plusMinutes(30)))
                .appointmentKind("Scheduled")
                .comments("autotest")
                .build();
    }

    // ПУ-5: встреча в статусе Scheduled — для тестов, где встреча только предусловие
    public static AppointmentResponse createAppointment(RequestSpecification spec, TestPatient patient) {
        return StepLogger.log("Создать запись на приём",
                () -> new ValidatedActionRequester<AppointmentResponse>(spec, Endpoint.APPOINTMENT,
                        ResponseSpecs.requestReturnsOk()).post(createAppointmentRequest(patient)));
    }

    public static AppointmentResponse changeStatus(RequestSpecification spec, String appointmentUuid, String status) {
        return StepLogger.log("Изменить статус записи на " + status,
                () -> new ValidatedActionRequester<AppointmentResponse>(spec, Endpoint.APPOINTMENT_STATUS_CHANGE,
                        ResponseSpecs.requestReturnsOk()).post(new StatusChangeRequest(status), appointmentUuid));
    }

    // GET /appointment?uuid=... — uuid передаётся параметром, ответ — один объект
    public static AppointmentResponse getAppointment(RequestSpecification spec, String uuid) {
        return new SearchRequester(spec, Endpoint.APPOINTMENT, ResponseSpecs.requestReturnsOk())
                .search(Map.of("uuid", uuid))
                .extract().as(AppointmentResponse.class);
    }
}
