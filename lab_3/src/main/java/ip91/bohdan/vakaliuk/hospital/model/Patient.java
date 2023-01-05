package ip91.bohdan.vakaliuk.hospital.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient {

  private PatientType patientType;
  private double creationTime;

  public Patient(PatientType patient, double creationTime) {
    this.patientType = patient;
    this.creationTime = creationTime;
  }

}
