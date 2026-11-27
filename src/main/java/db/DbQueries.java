package db;

// SQL-запросы E2E-02 (раздел А.6 тест-плана).
// voided = 0 — только действующие записи: OpenMRS не удаляет строки, а помечает их voided = 1.
public class DbQueries {

    public static final String PERSON =
            "SELECT gender, birthdate FROM person WHERE uuid = ? AND voided = 0";

    public static final String PERSON_NAME =
            "SELECT pn.given_name, pn.family_name FROM person_name pn "
                    + "JOIN person p ON p.person_id = pn.person_id "
                    + "WHERE p.uuid = ? AND pn.voided = 0";

    public static final String PATIENT =
            "SELECT pt.patient_id FROM patient pt "
                    + "JOIN person p ON p.person_id = pt.patient_id "
                    + "WHERE p.uuid = ? AND pt.voided = 0";

    public static final String PATIENT_IDENTIFIER =
            "SELECT pi.identifier FROM patient_identifier pi "
                    + "JOIN person p ON p.person_id = pi.patient_id "
                    + "WHERE p.uuid = ? AND pi.voided = 0";

    public static final String VISIT =
            "SELECT date_stopped FROM visit WHERE uuid = ? AND voided = 0";

    public static final String OBS_OF_ENCOUNTER =
            "SELECT o.obs_id FROM obs o "
                    + "JOIN encounter e ON e.encounter_id = o.encounter_id "
                    + "WHERE e.uuid = ? AND o.voided = 0";

    public static final String APPOINTMENT =
            "SELECT status FROM patient_appointment WHERE uuid = ? AND voided = 0";
}
