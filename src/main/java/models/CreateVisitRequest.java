package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Тело POST /visit
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVisitRequest extends BaseModel {
    private String patient;
    private String visitType;
    private String location;
    private String startDatetime;
}
