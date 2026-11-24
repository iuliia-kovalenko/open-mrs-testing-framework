package requests.skeleton.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import requests.skeleton.Endpoint;
import requests.skeleton.HttpRequest;
import requests.skeleton.interfaces.SearchEndpointInterface;

import java.util.Map;

import static io.restassured.RestAssured.given;

// Сырой поиск: GET с параметрами, возвращает ответ целиком.
public class SearchRequester extends HttpRequest implements SearchEndpointInterface {

    public SearchRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public ValidatableResponse search(Map<String, ?> params) {
        return given()
                .spec(requestSpecification)
                .queryParams(params)
                .get(endpoint.getUrl())
                .then()
                .assertThat()
                .spec(responseSpecification);
    }
}
