package requests.skeleton;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.*;

// Карта API: путь, модель запроса, модель ответа.
// Пути — относительно server + apiVersion из config.properties.
// {sourceUuid} и {appointmentUuid} — части пути, которые подставляются при вызове.
@Getter
@AllArgsConstructor
public enum Endpoint {
    // сессия и справочники
    SESSION("/session", BaseModel.class, SessionResponse.class),
    LOCATION("/location", BaseModel.class, RefResponse.class),
    PATIENT_IDENTIFIER_TYPE("/patientidentifiertype", BaseModel.class, RefResponse.class),
    IDGEN_SOURCE("/idgen/identifiersource", BaseModel.class, RefResponse.class),
    VISIT_TYPE("/visittype", BaseModel.class, RefResponse.class),
    ENCOUNTER_TYPE("/encountertype", BaseModel.class, RefResponse.class),
    APPOINTMENT_SERVICE("/appointmentService/all/default", BaseModel.class, AppointmentService.class),


    // пациент
    PATIENT("/patient", CreatePatientRequest.class, PatientResponse.class),
    IDGEN_IDENTIFIER("/idgen/identifiersource/{sourceUuid}/identifier", BaseModel.class, GeneratedIdentifierResponse.class),

    // встречи
    APPOINTMENT("/appointment", AppointmentRequest.class, AppointmentResponse.class),
    APPOINTMENT_STATUS_CHANGE("/appointments/{appointmentUuid}/status-change", StatusChangeRequest.class, AppointmentResponse.class),

    // приём
    VISIT("/visit", CreateVisitRequest.class, VisitResponse.class),
    ENCOUNTER("/encounter", EncounterRequest.class, EncounterResponse.class),
    OBS("/obs", ObsRequest.class, ObsResponse.class);

    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
