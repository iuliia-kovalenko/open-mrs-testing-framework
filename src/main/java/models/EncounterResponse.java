package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Приём в ответе
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncounterResponse extends BaseModel {
    private String uuid;
    private String encounterDatetime;
    private RefResponse visit;
}
