package requests.steps;

import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.*;
import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.ActionRequester;
import requests.skeleton.requests.SearchRequester;
import requests.skeleton.requests.ValidatedActionRequester;
import specs.ResponseSpecs;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AppointmentSteps {

    // Формирование запроса встречи на завтра в 10:00 на 30 минут
    public static AppointmentRequest createAppointmentRequest(
            TestPatient patient) {

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

    // ПУ-5: создание встречи в статусе Scheduled
    public static AppointmentResponse createAppointment(
            RequestSpecification spec, TestPatient patient) {

        return createAppointment(spec,
                createAppointmentRequest(patient));
    }

    // Создание встречи по готовому запросу
    public static AppointmentResponse createAppointment(
            RequestSpecification spec, AppointmentRequest request) {

        return StepLogger.log("Создать запись на приём",
                () -> new ValidatedActionRequester<AppointmentResponse>(
                        spec,
                        Endpoint.APPOINTMENT,
                        ResponseSpecs.requestReturnsOk())
                        .post(request));
    }

    // Изменение статуса встречи
    public static AppointmentResponse changeStatus(
            RequestSpecification spec,
            String appointmentUuid,
            String status) {

        return StepLogger.log("Изменить статус записи на " + status,
                () -> new ValidatedActionRequester<AppointmentResponse>(
                        spec,
                        Endpoint.APPOINTMENT_STATUS_CHANGE,
                        ResponseSpecs.requestReturnsOk())
                        .post(new StatusChangeRequest(status),
                                appointmentUuid));
    }

    // Получение встречи по UUID
    public static AppointmentResponse getAppointment(
            RequestSpecification spec, String uuid) {

        return new SearchRequester(
                spec,
                Endpoint.APPOINTMENT,
                ResponseSpecs.requestReturnsOk())
                .search(Map.of("uuid", uuid))
                .extract()
                .as(AppointmentResponse.class);
    }

    // Поиск встреч пациента на указанную дату
    public static List<AppointmentResponse> searchByDate(
            RequestSpecification spec,
            String patientUuid,
            ZonedDateTime day) {

        ZonedDateTime start = day.toLocalDate()
                .atStartOfDay(day.getZone());

        AppointmentSearchRequest request =
                AppointmentSearchRequest.builder()
                        .patientUuid(patientUuid)
                        .startDate(TestDates.format(start))
                        .endDate(TestDates.format(
                                start.plusDays(1).minusSeconds(1)))
                        .build();

        AppointmentResponse[] responses =
                new ActionRequester(
                        spec,
                        Endpoint.APPOINTMENT_SEARCH,
                        ResponseSpecs.requestReturnsOk())
                        .post(request)
                        .extract()
                        .as(AppointmentResponse[].class);

        return Arrays.asList(responses);
    }
}