package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Идентификатор пациента в запросе: тип и локация передаются строками-UUID
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientIdentifierRequest extends BaseModel {
    private String identifier;
    private String identifierType;
    private String location;
    private Boolean preferred;
}
