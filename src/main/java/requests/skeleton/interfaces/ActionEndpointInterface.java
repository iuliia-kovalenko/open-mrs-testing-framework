package requests.skeleton.interfaces;

import models.BaseModel;

// POST: запрос с телом, без чтения и удаления по uuid.
// Например, сохранить встречу, сменить её статус, получить новый OpenMRS ID.
// pathParams подставляются в путь вместо {sourceUuid}, {appointmentUuid}.
public interface ActionEndpointInterface {
    Object post(BaseModel model, Object... pathParams);
}
