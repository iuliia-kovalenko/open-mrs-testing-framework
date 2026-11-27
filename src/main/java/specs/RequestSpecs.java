package specs;

import configs.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import reporting.AllureHttpStepFilter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

// OpenMRS не выдаёт токен при логине, а принимает Basic-авторизацию (логин:пароль в Base64) в каждом запросе — поэтому заголовок собираем сами.
public class RequestSpecs {

    private RequestSpecs() {}

    private static RequestSpecBuilder defaultRequestBuilder() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilters(List.of(
                        new AllureHttpStepFilter(),
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter(),
                        new AllureRestAssured()))
                // порт 80 указан в server явно: без порта RestAssured по умолчанию идёт на 8080
                .setBaseUri(Config.getProperty("server") + Config.getProperty("apiVersion"));
    }

    public static RequestSpecification unauthSpec() {
        return defaultRequestBuilder().build();
    }

    public static RequestSpecification adminSpec() {
        return authAsUser(Config.getProperty("admin.username"), Config.getProperty("admin.password"));
    }

    public static RequestSpecification authAsUser(String username, String password) {
        String credentials = username + ":" + password;
        String basicHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return defaultRequestBuilder()
                .addHeader("Authorization", basicHeader)
                .build();
    }
}
