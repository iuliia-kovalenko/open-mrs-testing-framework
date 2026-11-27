package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Тело POST /appointments/{uuid}/status-change: {"toStatus": "CheckedIn"}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusChangeRequest extends BaseModel {
    private String toStatus;
}
