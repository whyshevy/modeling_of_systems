package ip91.bohdan.vakaliuk.hospital.service.element.task;

import ip91.bohdan.vakaliuk.hospital.model.Patient;
import ip91.bohdan.vakaliuk.hospital.model.PatientType;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Element;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Process;

public class LaboratoryWalkingProcess extends Process {

  public LaboratoryWalkingProcess(String name, int maxQueue) {
    super(name, maxQueue);
  }

  @Override
  protected void callNextElementInAct(Patient patient) {
    if (patient.getPatientType().equals(PatientType.TYPE_1)) {
      ReceptionProcess reception = (ReceptionProcess) super.getNextElements().get(1).getElement();
      System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), reception.getName());
      reception.inAct(patient);
    } else {
      Element registry = super.getNextElements().get(0).getElement();
      System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), registry.getName());
      registry.inAct(patient);
    }
  }

}
