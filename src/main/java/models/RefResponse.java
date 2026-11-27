package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Ссылка на объект в ответе: {"uuid": "...", "display": "Outpatient Clinic"}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefResponse extends BaseModel {
    private String uuid;
    private String display;
}
