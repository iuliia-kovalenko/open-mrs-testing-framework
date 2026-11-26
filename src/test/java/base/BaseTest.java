package base;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import requests.storage.CreatedPatientStorage;

public class BaseTest {
    public SoftAssertions softly;

    @BeforeEach
    public void setupTest() {
        this.softly = new SoftAssertions();
    }

    @AfterEach
    public void afterTest() {
        softly.assertAll();
    }

    // отдельный метод: JUnit выполнит его, даже если assertAll() выше упал
    @AfterEach
    public void cleanUpTestData() {
        CreatedPatientStorage.deleteAll();
    }
}
