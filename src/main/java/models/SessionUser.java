package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Пользователь сессии. У admin поле username пустое (null), логин лежит в systemId
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionUser extends BaseModel {
    private String uuid;
    private String display;
    private String username;
    private String systemId;
}
