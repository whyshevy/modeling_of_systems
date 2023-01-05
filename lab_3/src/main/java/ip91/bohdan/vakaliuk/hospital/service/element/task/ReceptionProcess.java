package ip91.bohdan.vakaliuk.hospital.service.element.task;

import ip91.bohdan.vakaliuk.hospital.model.Patient;
import ip91.bohdan.vakaliuk.hospital.model.PatientType;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Element;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Process;

import java.util.Optional;

public class ReceptionProcess extends Process {

    public ReceptionProcess(String name, int maxQueue) {
        super(name, maxQueue);
    }

  @Override
  protected void callNextElementInAct(Patient patient) {
    if (patient.getPatientType().equals(PatientType.TYPE_1)) {
      Element wardWalking = super.getNextElements().get(0).getElement();
      System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), wardWalking.getName());
      wardWalking.inAct(patient);
    } else {
      LaboratoryWalkingProcess laboratoryWalking = (LaboratoryWalkingProcess) super.getNextElements().get(1).getElement();
      System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), laboratoryWalking.getName());
      laboratoryWalking.inAct(patient);
    }
  }

  @Override
  public Patient reduceQueue() {
    Optional<Patient> patientType_1_Optional  = super.getQueue().stream()
        .filter(p -> p.getPatientType().equals(PatientType.TYPE_1))
        .findFirst();

    if (patientType_1_Optional.isPresent()) {
      Patient patient = patientType_1_Optional.get();
      super.getQueue().remove(patient);
      return patient;
    } else {
      return super.getQueue().remove(0);
    }
  }

}
