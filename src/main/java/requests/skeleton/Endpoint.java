package requests.skeleton;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.*;

@Getter
@AllArgsConstructor
public enum Endpoint {

    // Сессия и справочники
    SESSION("/session", BaseModel.class, SessionResponse.class),
    LOCATION("/location", BaseModel.class, RefResponse.class),
    PATIENT_IDENTIFIER_TYPE("/patientidentifiertype", BaseModel.class, RefResponse.class),
    IDGEN_SOURCE("/idgen/identifiersource", BaseModel.class, RefResponse.class),
    VISIT_TYPE("/visittype", BaseModel.class, RefResponse.class),
    ENCOUNTER_TYPE("/encountertype", BaseModel.class, RefResponse.class),
    APPOINTMENT_SERVICE("/appointmentService/all/default", BaseModel.class, AppointmentService.class),

    // Пациент
    PATIENT("/patient", CreatePatientRequest.class, PatientResponse.class),
    IDGEN_IDENTIFIER("/idgen/identifiersource/{sourceUuid}/identifier", BaseModel.class, GeneratedIdentifierResponse.class),

    // Встречи
    APPOINTMENT("/appointment", AppointmentRequest.class, AppointmentResponse.class),
    APPOINTMENT_STATUS_CHANGE("/appointments/{appointmentUuid}/status-change", StatusChangeRequest.class, AppointmentResponse.class),
    APPOINTMENT_SEARCH("/appointments/search", AppointmentSearchRequest.class, AppointmentResponse.class),
    APPOINTMENT_CONFLICTS("/appointments/conflicts", AppointmentRequest.class, BaseModel.class),

    // Приём
    VISIT("/visit", CreateVisitRequest.class, VisitResponse.class),
    ENCOUNTER("/encounter", EncounterRequest.class, EncounterResponse.class),
    OBS("/obs", ObsRequest.class, ObsResponse.class);

    private final String url;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}