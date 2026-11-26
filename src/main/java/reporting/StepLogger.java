package reporting;

import io.qameta.allure.Allure;

public final class StepLogger {
    private StepLogger() {
    }

    public static <T> T log(String title, Allure.ThrowableRunnable<T> action) {
        return Allure.step(title, action);
    }

    public static void log(String title, Allure.ThrowableRunnableVoid action) {
        Allure.step(title, action);
    }
}
