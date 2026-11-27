package models;

import lombok.AllArgsConstructor;
import lombok.Data;

// Пациент, созданный тестом. Нужен потому, что тесту нужны данные и из запроса, и из ответа:
// OpenMRS ID и имя есть только в запросе, а uuid пациента сервер присваивает и возвращает в ответе.
// (В nbank AdminSteps.createUser() по той же причине возвращает запрос: пароль есть только в нём.)
@Data
@AllArgsConstructor
public class TestPatient {
    private String uuid;
    private String openMrsId;
    private String givenName;
    private String familyName;
    private String gender;
    private String birthdate;
}
