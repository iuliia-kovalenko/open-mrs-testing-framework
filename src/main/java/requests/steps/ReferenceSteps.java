package requests.steps;

import models.AppointmentService;
import models.RefResponse;
import requests.skeleton.Endpoint;
import requests.skeleton.requests.SearchRequester;
import requests.skeleton.requests.ValidatedSearchRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;

import java.util.List;
import java.util.Map;

// UUID справочников: локация, тип идентификатора, тип визита и т. д.
// По тест-плану (раздел 5.7) их нельзя зашивать в код — получаем запросом.
// Чтобы не спрашивать сервер в каждом тесте, запоминаем UUID в поле при первом вызове.
public class ReferenceSteps {
    private static String locationUuid;
    private static String identifierTypeUuid;
    private static String idgenSourceUuid;
    private static String visitTypeUuid;
    private static String vitalsEncounterTypeUuid;
    private static String appointmentServiceUuid;

    // Понятия показателей — UUID словаря CIEL. Они одинаковы во всех установках OpenMRS,
    // поэтому на этапе P0 решили держать их константами.
    public static final String WEIGHT_CONCEPT = "5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String HEIGHT_CONCEPT = "5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String TEMPERATURE_CONCEPT = "5088AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String SYSTOLIC_CONCEPT = "5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String DIASTOLIC_CONCEPT = "5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    public static String getLocationUuid() {
        if (locationUuid == null) {
            locationUuid = findFirst(Endpoint.LOCATION, "Outpatient Clinic");
        }
        return locationUuid;
    }

    public static String getVisitTypeUuid() {
        if (visitTypeUuid == null) {
            visitTypeUuid = findFirst(Endpoint.VISIT_TYPE, "Facility Visit");
        }
        return visitTypeUuid;
    }

    public static String getVitalsEncounterTypeUuid() {
        if (vitalsEncounterTypeUuid == null) {
            vitalsEncounterTypeUuid = findFirst(Endpoint.ENCOUNTER_TYPE, "Vitals");
        }
        return vitalsEncounterTypeUuid;
    }

    // Поиск по q у типов идентификаторов не работает — берём весь список и ищем по названию
    public static String getIdentifierTypeUuid() {
        if (identifierTypeUuid == null) {
            List<RefResponse> types = new ValidatedSearchRequester<RefResponse>(RequestSpecs.adminSpec(),
                    Endpoint.PATIENT_IDENTIFIER_TYPE, ResponseSpecs.requestReturnsOk())
                    .search(Map.of());
            for (RefResponse type : types) {
                if (type.getDisplay().equals("OpenMRS ID")) {
                    identifierTypeUuid = type.getUuid();
                }
            }
        }
        return identifierTypeUuid;
    }

    // Генератор OpenMRS ID на стенде один — берём первый
    public static String getIdgenSourceUuid() {
        if (idgenSourceUuid == null) {
            idgenSourceUuid = new ValidatedSearchRequester<RefResponse>(RequestSpecs.adminSpec(),
                    Endpoint.IDGEN_SOURCE, ResponseSpecs.requestReturnsOk())
                    .search(Map.of())
                    .get(0)
                    .getUuid();
        }
        return idgenSourceUuid;
    }

    // Сервисы встреч приходят обычным массивом (без "results") — читаем как AppointmentService[]
    public static String getAppointmentServiceUuid() {
        if (appointmentServiceUuid == null) {
            AppointmentService[] services = new SearchRequester(RequestSpecs.adminSpec(),
                    Endpoint.APPOINTMENT_SERVICE, ResponseSpecs.requestReturnsOk())
                    .search(Map.of())
                    .extract().as(AppointmentService[].class);
            for (AppointmentService service : services) {
                if (service.getName().equals("Outpatient Department")) {
                    appointmentServiceUuid = service.getUuid();
                }
            }
        }
        return appointmentServiceUuid;
    }

    // Поиск справочника по названию: GET /ресурс?q=название, берём первый результат
    private static String findFirst(Endpoint endpoint, String name) {
        List<RefResponse> found = new ValidatedSearchRequester<RefResponse>(RequestSpecs.adminSpec(),
                endpoint, ResponseSpecs.requestReturnsOk())
                .search(Map.of("q", name));
        return found.get(0).getUuid();
    }
}
