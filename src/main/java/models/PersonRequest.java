package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Персональные данные пациента в запросе
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonRequest extends BaseModel {
    private List<PersonNameRequest> names;
    private String gender;      // M или F
    private String birthdate;   // 1990-05-10
}
