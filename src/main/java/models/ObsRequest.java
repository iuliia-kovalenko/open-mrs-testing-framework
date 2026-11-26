package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Один показатель: понятие (что измерили) и значение
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObsRequest extends BaseModel {
    private String concept;
    private Double value;
}
