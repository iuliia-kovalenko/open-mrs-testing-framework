package api.patient;

import base.BaseTest;
import io.restassured.specification.RequestSpecification;
import models.*;
import models.comparison.ModelAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.steps.PatientSteps;
import specs.RequestSpecs;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("PAT-02: регистрация пациента через API")
    public void patientIsCreated() {

        RequestSpecification spec = RequestSpecs.adminSpec();

        String openMrsId = PatientSteps.generateOpenMrsId(spec);
        CreatePatientRequest request = PatientSteps.createPatientRequest(openMrsId);
        TestPatient created = PatientSteps.createPatient(spec, request);

        softly.check(() -> StepLogger.log("Проверить, что UUID созданного пациента не пустой", () -> {
            assertThat(created.getUuid()).isNotBlank();}));
        PatientResponse saved = PatientSteps.getPatient(spec, created.getUuid());
        softly.check(() -> StepLogger.log("Проверить UUID сохранённого пациента", () -> {
            assertThat(saved.getUuid()).isEqualTo(created.getUuid());}));
        softly.check(() -> StepLogger.log("Проверить наличие идентификаторов пациента", () -> {
            assertThat(saved.getIdentifiers()).isNotEmpty();}));
        softly.check(() -> StepLogger.log("Проверить соответствие данных пациента запросу", () -> {
            ModelAssertions.assertThatModels(request, saved).match();}));
        softly.check(() -> StepLogger.log("Проверить, что идентификатор пациента является предпочтительным", () -> {
            assertThat(saved.getIdentifiers()).isNotEmpty();
            assertThat(saved.getIdentifiers().get(0).getPreferred()).isTrue();}));
        softly.check(() -> StepLogger.log("Проверить дату рождения пациента", () -> {
            assertThat(saved.getPerson().getBirthdate()).startsWith(request.getPerson().getBirthdate());}));
    }

    @Test
    @Tag("P0")
    @DisplayName("PAT-03: два пациента получают разные OpenMRS ID")
    public void patientsGetDifferentIdentifiers() {

        RequestSpecification spec = RequestSpecs.adminSpec();

        TestPatient first = PatientSteps.createPatient(spec);
        TestPatient second = PatientSteps.createPatient(spec);

        softly.check(() -> StepLogger.log("Проверить, что UUID первого пациента не пустой", () -> {
            assertThat(first.getUuid()).isNotBlank();}));
        softly.check(() -> StepLogger.log("Проверить, что UUID второго пациента не пустой", () -> {
            assertThat(second.getUuid()).isNotBlank();}));
        softly.check(() -> StepLogger.log("Проверить, что UUID пациентов различаются", () -> {
            assertThat(first.getUuid()).isNotEqualTo(second.getUuid());}));
        softly.check(() -> StepLogger.log("Проверить, что OpenMRS ID пациентов различаются", () -> {
            assertThat(first.getOpenMrsId()).isNotEqualTo(second.getOpenMrsId());}));
    }
}