package requests.skeleton.requests;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import models.PatientResponse;
import requests.skeleton.Endpoint;
import requests.skeleton.HttpRequest;
import requests.skeleton.interfaces.CrudEndpointInterface;
import requests.storage.CreatedPatientStorage;

// сразу возвращает модель ответа. Внутри — сырой CrudRequester.
public class ValidatedCrudRequester<T extends BaseModel> extends HttpRequest implements CrudEndpointInterface {
    CrudRequester crudRequester;

    public ValidatedCrudRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
        this.crudRequester = new CrudRequester(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public T post(BaseModel model) {
        T response = (T) crudRequester.post(model).extract().as(endpoint.getResponseModel());

        // запоминаем созданного пациента, чтобы удалить его после теста
        if (endpoint == Endpoint.PATIENT && response instanceof PatientResponse createdPatient) {
            CreatedPatientStorage.add(createdPatient.getUuid());
        }
        return response;
    }

    @Override
    public T get(String uuid) {
        return (T) crudRequester.get(uuid).extract().as(endpoint.getResponseModel());
    }

    @Override
    public T update(String uuid, BaseModel model) {
        return (T) crudRequester.update(uuid, model).extract().as(endpoint.getResponseModel());
    }

    @Override
    public String delete(String uuid) {
        String response = crudRequester.delete(uuid).extract().asString();

        // пациент удалён в тесте — убираем его из storage, чтобы очистка не удаляла его повторно
        if (endpoint == Endpoint.PATIENT) {
            CreatedPatientStorage.remove(uuid);
        }
        return response;
    }
}
