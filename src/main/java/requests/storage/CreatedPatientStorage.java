package requests.storage;

import reporting.StepLogger;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.CrudRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.ArrayList;
import java.util.List;

// Удаление в OpenMRS — пометка voided = 1: пациент пропадает из поиска, но остаётся в базе.
public class CreatedPatientStorage {
    private static final List<String> patientUuids = new ArrayList<>();

    public static void add(String uuid) {
        patientUuids.add(uuid);
    }

    public static void remove(String uuid) {
        patientUuids.remove(uuid);
    }

    public static void deleteAll() {
        if (patientUuids.isEmpty()) {
            return;
        }

        StepLogger.log("Удалить тестовых пациентов", () -> {
            for (String uuid : patientUuids) {
                new CrudRequester(RequestSpecs.adminSpec(), Endpoint.PATIENT, ResponseSpecs.requestReturnsNoContent())
                        .delete(uuid);
            }
            patientUuids.clear();
        });
    }
}
