package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Идентификатор пациента в ответе: тип — уже не строка, а объект
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientIdentifierResponse extends BaseModel {
    private String uuid;
    private String identifier;
    private RefResponse identifierType;
    private Boolean preferred;
}
