package requests.skeleton.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skeleton.Endpoint;
import requests.skeleton.HttpRequest;
import requests.skeleton.interfaces.CrudEndpointInterface;

import static io.restassured.RestAssured.given;

public class CrudRequester extends HttpRequest implements CrudEndpointInterface {

    public CrudRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post(endpoint.getUrl())
                .then()
                .assertThat()
                .spec(responseSpecification);
    }

    // v=full — полное представление объекта: без него OpenMRS возвращает урезанный набор полей
    @Override
    public ValidatableResponse get(String uuid) {
        return given()
                .spec(requestSpecification)
                .queryParam("v", "full")
                .get(endpoint.getUrl() + "/" + uuid)
                .then()
                .assertThat()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse update(String uuid, BaseModel model) {
        return given()
                .spec(requestSpecification)
                .body(model)
                .post(endpoint.getUrl() + "/" + uuid)
                .then()
                .assertThat()
                .spec(responseSpecification);
    }

    // OpenMRS не удаляет запись, а помечает её voided = 1; reason — обязательная причина удаления
    @Override
    public ValidatableResponse delete(String uuid) {
        return given()
                .spec(requestSpecification)
                .queryParam("reason", "autotest")
                .delete(endpoint.getUrl() + "/" + uuid)
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
