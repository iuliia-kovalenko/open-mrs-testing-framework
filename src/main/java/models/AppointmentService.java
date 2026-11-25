package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Сервис встреч, например Outpatient Department
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentService extends BaseModel {
    private String uuid;
    private String name;
}
