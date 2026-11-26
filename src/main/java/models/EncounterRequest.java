package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Тело POST /encounter: приём с показателями внутри визита
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncounterRequest extends BaseModel {
    private String patient;
    private String encounterType;
    private String visit;
    private String location;
    private String encounterDatetime;
    private List<ObsRequest> obs;
}
