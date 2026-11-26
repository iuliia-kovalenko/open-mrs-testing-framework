package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Тело POST /appointment. Время — строка 2026-11-24T10:00:00.000+0000, appointmentKind — "Scheduled"
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest extends BaseModel {
    private String patientUuid;
    private String serviceUuid;
    private String locationUuid;
    private String startDateTime;
    private String endDateTime;
    private String appointmentKind;
    private String comments;
}
