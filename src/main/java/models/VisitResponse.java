package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Визит в ответе
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitResponse extends BaseModel {
    private String uuid;
    private RefResponse patient;
    private String startDatetime;
    private String stopDatetime;   // null, пока визит активен
}
