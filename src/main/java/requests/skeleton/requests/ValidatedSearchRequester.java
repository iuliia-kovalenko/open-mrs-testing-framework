package requests.skeleton.requests;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;
import requests.skeleton.Endpoint;
import requests.skeleton.HttpRequest;
import requests.skeleton.interfaces.SearchEndpointInterface;

import java.util.List;
import java.util.Map;

// Поиск, который сразу возвращает список моделей.
// OpenMRS отдаёт список не массивом, а в обёртке: {"results": [ {...}, {...} ]},
// поэтому список достаём по пути "results".
public class ValidatedSearchRequester<T extends BaseModel> extends HttpRequest implements SearchEndpointInterface {
    SearchRequester searchRequester;

    public ValidatedSearchRequester(RequestSpecification requestSpecification, Endpoint endpoint, ResponseSpecification responseSpecification) {
        super(requestSpecification, endpoint, responseSpecification);
        this.searchRequester = new SearchRequester(requestSpecification, endpoint, responseSpecification);
    }

    @Override
    public List<T> search(Map<String, ?> params) {
        return (List<T>) searchRequester.search(params)
                .extract()
                .jsonPath()
                .getList("results", endpoint.getResponseModel());
    }
}
