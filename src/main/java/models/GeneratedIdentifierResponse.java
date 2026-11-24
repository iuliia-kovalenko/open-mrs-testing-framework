package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Ответ генератора идентификаторов: {"identifier": "10003W1"}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneratedIdentifierResponse extends BaseModel {
    private String identifier;
}
