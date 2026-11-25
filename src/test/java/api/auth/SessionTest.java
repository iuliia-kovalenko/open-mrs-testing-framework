package api.auth;

import base.BaseTest;
import configs.Config;
import io.restassured.response.ValidatableResponse;
import models.SessionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.SessionRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

public class SessionTest extends BaseTest {

    @Test
    @Tag("P0")
    @DisplayName("AUTH-02: аутентификация через API создаёт сессию")
    public void authenticationCreatesSession() {
        // ПУ-1: стенд готов — проверяется до запуска тестов (scripts/wait-for-stand.sh)

        // Шаг 1. GET /session с Basic-аутентификацией admin, ожидаем 200
        ValidatableResponse response = new SessionRequester(RequestSpecs.adminSpec(), Endpoint.SESSION,
                ResponseSpecs.requestReturnsOk())
                .get();

        // Проверки: authenticated = true, логин admin, в ответе есть cookie JSESSIONID
        SessionResponse session = response.extract().as(SessionResponse.class);
        softly.assertThat(session.getAuthenticated()).isTrue();
        // у admin поле user.username пустое, логин лежит в user.systemId
        softly.assertThat(session.getUser().getSystemId()).isEqualTo(Config.getProperty("admin.username"));
        softly.assertThat(response.extract().cookie("JSESSIONID")).isNotBlank();
    }
}
