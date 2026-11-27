package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Тело POST /visit/{uuid} — завершение визита
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloseVisitRequest extends BaseModel {
    private String stopDatetime;
}
