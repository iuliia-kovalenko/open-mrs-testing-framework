package requests.skeleton.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skeleton.Endpoint;
import requests.skeleton.HttpRequest;
import requests.skeleton.interfaces.ActionEndpointInterface;

import static io.restassured.RestAssured.given;

//  POST. Если тела нет (model == null), отправляем пустой JSON {}.
public class ActionRequester extends HttpRequest implements ActionEndpointInterface {

    public ActionRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model, Object... pathParams) {
        var body = model == null ? "{}" : model;
        return given()
                .spec(requestSpecification)
                .body(body)
                .post(endpoint.getUrl(), pathParams)
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
