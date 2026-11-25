package requests.skeleton;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

// Общий конструктор для всех Requester-ов.
public abstract class HttpRequest {
    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;
    protected Endpoint endpoint;

    public HttpRequest(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        this.requestSpecification = requestSpecification;
        this.responseSpecification = responseSpecification;
        this.endpoint = endpoint;
    }
}
