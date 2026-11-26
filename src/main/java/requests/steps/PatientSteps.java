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
import specs.ResponseSpecs;

import java.util.List;

// Пациент. Здесь — подготовка данных и чтение для проверок.
// Проверяемый запрос (например, POST /patient в PAT-02) тест отправляет сам, через Requester.
public class PatientSteps {

    // Новый OpenMRS ID у генератора idgen (ответ 201)
    public static String generateOpenMrsId(RequestSpecification spec) {
        GeneratedIdentifierResponse response = new ValidatedActionRequester<GeneratedIdentifierResponse>(spec,
                Endpoint.IDGEN_IDENTIFIER, ResponseSpecs.entityWasCreated())
                .post(null, ReferenceSteps.getIdgenSourceUuid());
        return response.getIdentifier();
    }

    // Запрос на регистрацию со случайными именем, полом и датой рождения и переданным OpenMRS ID.
    // Запрос ничего не отправляет — тест может поменять в нём поле перед отправкой.
    public static CreatePatientRequest createPatientRequest(String openMrsId) {
        PersonNameRequest name = RandomModelGenerator.generate(PersonNameRequest.class);
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

    // ПУ-4: пациент со случайными данными — для тестов, где пациент только предусловие
    public static TestPatient createPatient(RequestSpecification spec) {
        return StepLogger.log("Создать тестового пациента",
                () -> createPatient(spec, createPatientRequest(generateOpenMrsId(spec))));
    }

    // Регистрация по готовому запросу — когда тесту нужно видеть шаги (получить ID, собрать запрос) отдельно
    public static TestPatient createPatient(RequestSpecification spec, CreatePatientRequest request) {
        PatientResponse response = new ValidatedCrudRequester<PatientResponse>(spec, Endpoint.PATIENT,
                ResponseSpecs.entityWasCreated())
                .post(request);
        return toTestPatient(request, response);
    }

    // TestPatient: uuid из ответа, остальное из запроса
    private static TestPatient toTestPatient(CreatePatientRequest request, PatientResponse response) {
        PersonNameRequest name = request.getPerson().getNames().get(0);
        return new TestPatient(
                response.getUuid(),
                request.getIdentifiers().get(0).getIdentifier(),
                name.getGivenName(),
                name.getFamilyName(),
                request.getPerson().getGender(),
                request.getPerson().getBirthdate());
    }
}
