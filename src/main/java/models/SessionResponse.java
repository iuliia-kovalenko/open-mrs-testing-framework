package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Ответ GET /session. При неверном пароле сервер отвечает 200, но authenticated = false
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionResponse extends BaseModel {
    private Boolean authenticated;
    private SessionUser user;
}
