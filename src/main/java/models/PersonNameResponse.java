package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Имя пациента в ответе
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonNameResponse extends BaseModel {
    private String uuid;
    private String givenName;
    private String familyName;
}
