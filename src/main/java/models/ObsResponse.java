package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Показатель в ответе
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObsResponse extends BaseModel {
    private String uuid;
    private RefResponse concept;
    private Double value;
}
