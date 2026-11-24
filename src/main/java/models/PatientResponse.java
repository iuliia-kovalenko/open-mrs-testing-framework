package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Пациент в ответе. В результатах поиска заполнены только uuid и display
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse extends BaseModel {
    private String uuid;
    private String display;
    private List<PatientIdentifierResponse> identifiers;
    private PersonResponse person;
    private Boolean voided;
}
