package api.appointment;

import base.BaseTest;
import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.AppointmentRequest;
import models.AppointmentResponse;
import models.TestPatient;
import models.comparison.ModelAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.ValidatedActionRequester;
import requests.steps.AppointmentSteps;
import requests.steps.PatientSteps;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AppointmentTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("APT-02: создание встречи в статусе Scheduled")
    public void appointmentIsCreated() {

        RequestSpecification spec = RequestSpecs.adminSpec();

        // ПУ-4: создаём пациента
        TestPatient patient = PatientSteps.createPatient(spec);

        // Формируем запрос
        AppointmentRequest request = AppointmentSteps.createAppointmentRequest(patient);

        // POST /appointment, ожидаем 200
        AppointmentResponse created = new ValidatedActionRequester<AppointmentResponse>(
                spec, Endpoint.APPOINTMENT, ResponseSpecs.requestReturnsOk()
        ).post(request);

        softly.check(() -> StepLogger.log("Проверить, что UUID созданной встречи не пустой", () -> {
            assertThat(created.getUuid()).isNotBlank();}));
        softly.check(() -> StepLogger.log("Проверить, что встреча имеет статус Scheduled", () -> {
            assertThat(created.getStatus()).isEqualTo("Scheduled");}));
        softly.check(() -> StepLogger.log("Проверить соответствие данных встречи запросу", () -> {
            ModelAssertions.assertThatModels(request, created).match();}));
        softly.check(() -> StepLogger.log("Проверить время начала встречи", () -> {
            assertThat(created.getStartDateTime()).isEqualTo(TestDates.toEpochMillis(request.getStartDateTime()));}));
        softly.check(() -> StepLogger.log("Проверить время окончания встречи", () -> {
            assertThat(created.getEndDateTime()).isEqualTo(TestDates.toEpochMillis(request.getEndDateTime()));}));
    }

    @Test
    @Tag("P0")
    @DisplayName("APT-03: встреча находится поиском на дату")
    public void appointmentFoundByDate() {

        RequestSpecification spec = RequestSpecs.adminSpec();

        TestPatient patient = PatientSteps.createPatient(spec);
        AppointmentResponse created = AppointmentSteps.createAppointment(spec, patient);

        softly.check(() -> StepLogger.log("Проверить, что UUID созданной встречи не пустой", () -> {
            assertThat(created.getUuid()).isNotBlank();}));

        // Для поиска берём дату созданной встречи
        ZonedDateTime appointmentDate = Instant.ofEpochMilli(created.getStartDateTime()).atZone(ZoneOffset.UTC);

        List<AppointmentResponse> found = AppointmentSteps.searchByDate(spec, patient.getUuid(), appointmentDate);

        softly.check(() -> StepLogger.log("Проверить наличие созданной встречи в результатах поиска", () -> {
            assertThat(found).extracting(AppointmentResponse::getUuid).contains(created.getUuid());}));
    }
}