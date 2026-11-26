package api.patient;

import base.BaseTest;
import io.restassured.specification.RequestSpecification;
import models.PatientResponse;
import models.TestPatient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reporting.StepLogger;
import requests.steps.PatientSteps;
import specs.RequestSpecs;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientSearchTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("SRCH-02: поиск пациента по OpenMRS ID")
    public void patientFoundByIdentifier() {

        RequestSpecification spec = RequestSpecs.adminSpec();

        TestPatient patient = PatientSteps.createPatient(spec);
        List<PatientResponse> found = PatientSteps.searchByIdentifier(spec, patient.getOpenMrsId());

        softly.check(() -> StepLogger.log("Проверить, что найден ровно один пациент", () -> {
            assertThat(found).hasSize(1);}));
        softly.check(() -> StepLogger.log("Проверить соответствие UUID найденного пациента", () -> {
            assertThat(found).extracting(PatientResponse::getUuid).containsExactly(patient.getUuid());}));
    }
}