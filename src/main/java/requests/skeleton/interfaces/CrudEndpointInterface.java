package requests.skeleton.interfaces;

import models.BaseModel;

// Ресурс с uuid в пути: /patient, /visit, /encounter.
// В OpenMRS изменение — это POST /ресурс/{uuid}, а не PUT.
public interface CrudEndpointInterface {
    Object post(BaseModel model);               // POST   /patient
    Object get(String uuid);                    // GET    /patient/{uuid}
    Object update(String uuid, BaseModel model); // POST   /visit/{uuid}
    Object delete(String uuid);                 // DELETE /patient/{uuid}
}
