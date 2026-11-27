package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Пациент внутри ответа о встрече
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentPatient extends BaseModel {
    private String uuid;
    private String identifier;
    private String name;
}
