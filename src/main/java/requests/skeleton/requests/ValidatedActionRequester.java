package requests.skeleton.requests;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skeleton.Endpoint;
import requests.skeleton.HttpRequest;
import requests.skeleton.interfaces.ActionEndpointInterface;

// POST, сразу возвращает модель ответа.
public class ValidatedActionRequester<T extends BaseModel> extends HttpRequest implements ActionEndpointInterface {
    ActionRequester actionRequester;

    public ValidatedActionRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
        this.actionRequester = new ActionRequester(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public T post(BaseModel model, Object... pathParams) {
        return (T) actionRequester.post(model, pathParams).extract().as(endpoint.getResponseModel());
    }
}
