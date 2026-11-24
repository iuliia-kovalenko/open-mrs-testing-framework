package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Персональные данные в ответе (при ?v=full)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonResponse extends BaseModel {
    private String uuid;
    private String display;
    private String gender;
    private Integer age;
    private String birthdate;   // 1990-05-10T00:00:00.000+0000 — формат отличается от запроса
    private PersonNameResponse preferredName;
}
