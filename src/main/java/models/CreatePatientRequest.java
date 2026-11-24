package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Тело POST /patient
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePatientRequest extends BaseModel {
    private PersonRequest person;
    private List<PatientIdentifierRequest> identifiers;
}
