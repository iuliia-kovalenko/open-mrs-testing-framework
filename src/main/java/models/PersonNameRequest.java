package models;

import generators.GeneratingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Имя пациента. Значения генерирует RandomModelGenerator по регулярному выражению
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonNameRequest extends BaseModel {
    @GeneratingRule(regex = "^[A-Z][a-z]{5,9}$")
    private String givenName;
    @GeneratingRule(regex = "^[A-Z][a-z]{5,9}$")
    private String familyName;
}
