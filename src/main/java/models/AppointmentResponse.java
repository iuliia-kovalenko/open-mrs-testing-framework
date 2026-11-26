package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Встреча в ответе. Время здесь — число миллисекунд, а не строка, как в запросе
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentResponse extends BaseModel {
    private String uuid;
    private String appointmentNumber;
    private AppointmentPatient patient;
    private AppointmentService service;
    private Long startDateTime;
    private Long endDateTime;
    private String appointmentKind;
    private String status;      // Scheduled, CheckedIn, Completed, Cancelled, Missed
    private String comments;
}
