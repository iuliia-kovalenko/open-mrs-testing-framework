package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

// 1) @JsonIgnoreProperties(ignoreUnknown = true): так как в ответах OpenMRS много служебных полей
//    (links, resourceVersion, auditInfo...). Без этой аннотации Jackson падает на каждом незнакомом поле.
// 2) @JsonInclude(NON_NULL): поля со значением null не отправляются в запросе.
//    Например, в запросе встречи без uuid не будет "uuid": null.
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseModel {
}
