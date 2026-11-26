package requests.steps;

import generators.RandomData;
import generators.RandomModelGenerator;
import generators.TestDates;
import io.restassured.specification.RequestSpecification;
import models.*;
import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.ValidatedActionRequester;
import requests.skeleton.requests.ValidatedCrudRequester;
import requests.skeleton.requests.ValidatedSearchRequester;
import specs.ResponseSpecs;

import java.util.List;
import java.util.Map;

public class PatientSteps {

    // Получение нового OpenMRS ID
    public static String generateOpenMrsId(RequestSpecification spec) {
        return StepLogger.log("Получить новый OpenMRS ID", () -> {
            GeneratedIdentifierResponse response =
                    new ValidatedActionRequester<GeneratedIdentifierResponse>(
                            spec,
                            Endpoint.IDGEN_IDENTIFIER,
                            ResponseSpecs.entityWasCreated())
                            .post(null, ReferenceSteps.getIdgenSourceUuid());

            return response.getIdentifier();
        });
    }

    // Формирование запроса на регистрацию пациента
    public static CreatePatientRequest createPatientRequest(String openMrsId) {
        PersonNameRequest name =
                RandomModelGenerator.generate(PersonNameRequest.class);

        name.setFamilyName(name.getFamilyName() + RandomData.getUniqueSuffix());

        PersonRequest person = PersonRequest.builder()
                .names(List.of(name))
                .gender(RandomData.getGender())
                .birthdate(TestDates.birthdateForAge(RandomData.getAge()))
                .build();

        PatientIdentifierRequest identifier = PatientIdentifierRequest.builder()
                .identifier(openMrsId)
                .identifierType(ReferenceSteps.getIdentifierTypeUuid())
                .location(ReferenceSteps.getLocationUuid())
                .preferred(true)
                .build();

        return CreatePatientRequest.builder()
                .person(person)
                .identifiers(List.of(identifier))
                .build();
    }

    // ПУ-4: создание пациента со случайными данными
    public static TestPatient createPatient(RequestSpecification spec) {
        return createPatient(spec,
                createPatientRequest(generateOpenMrsId(spec)));
    }

    // Создание пациента по готовому запросу
    public static TestPatient createPatient(
            RequestSpecification spec, CreatePatientRequest request) {

        return StepLogger.log("Создать тестового пациента", () -> {
            PatientResponse response =
                    new ValidatedCrudRequester<PatientResponse>(
                            spec,
                            Endpoint.PATIENT,
                            ResponseSpecs.entityWasCreated())
                            .post(request);

            return toTestPatient(request, response);
        });
    }

    // UUID из ответа, остальные данные из запроса
    private static TestPatient toTestPatient(
            CreatePatientRequest request, PatientResponse response) {

        PersonNameRequest name = request.getPerson().getNames().get(0);

        return new TestPatient(
                response.getUuid(),
                request.getIdentifiers().get(0).getIdentifier(),
                name.getGivenName(),
                name.getFamilyName(),
                request.getPerson().getGender(),
                request.getPerson().getBirthdate());
    }

    // Получение пациента по UUID
    public static PatientResponse getPatient(
            RequestSpecification spec, String uuid) {

        return new ValidatedCrudRequester<PatientResponse>(
                spec,
                Endpoint.PATIENT,
                ResponseSpecs.requestReturnsOk())
                .get(uuid);
    }

    // Поиск пациента по OpenMRS ID
    public static List<PatientResponse> searchByIdentifier(
            RequestSpecification spec, String identifier) {

        return new ValidatedSearchRequester<PatientResponse>(
                spec,
                Endpoint.PATIENT,
                ResponseSpecs.requestReturnsOk())
                .search(Map.of("identifier", identifier));
    }

    // Поиск пациента по имени или фамилии
    public static List<PatientResponse> searchByName(
            RequestSpecification spec, String name) {

        return new ValidatedSearchRequester<PatientResponse>(
                spec,
                Endpoint.PATIENT,
                ResponseSpecs.requestReturnsOk())
                .search(Map.of("q", name));
    }
}