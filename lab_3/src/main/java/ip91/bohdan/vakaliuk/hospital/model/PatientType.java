package ip91.bohdan.vakaliuk.hospital.model;

import ip91.bohdan.vakaliuk.hospital.config.Config;
import lombok.Getter;

@Getter
public enum PatientType {
    TYPE_1(Config.PATIENT_TYPE_1_MEAN_REGISTRATION_TIME, 1),
    TYPE_2(Config.PATIENT_TYPE_2_MEAN_REGISTRATION_TIME, 2),
    TYPE_3(Config.PATIENT_TYPE_3_MEAN_REGISTRATION_TIME, 2);

    private final int meanRegistrationTime;
    private final int priority;

    PatientType(int meanRegistrationTime, int priority) {
        this.meanRegistrationTime = meanRegistrationTime;
        this.priority = priority;
    }

}
