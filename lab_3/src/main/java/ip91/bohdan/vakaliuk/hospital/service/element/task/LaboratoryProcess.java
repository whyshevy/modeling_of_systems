package ip91.bohdan.vakaliuk.hospital.service.element.task;

import ip91.bohdan.vakaliuk.hospital.model.Patient;
import ip91.bohdan.vakaliuk.hospital.model.PatientType;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Exit;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Process;
import lombok.Getter;

@Getter
public class LaboratoryProcess extends Process {

  private static final String MEAN_ARRIVE_INTERVAL_MSG = "   mean lab interval = ";

  public LaboratoryProcess(String name, int maxQueue) {
    super(name, maxQueue);
  }

  private double arriveSum;
  private double arriveCount;
  private double lastArrive;

  @Override
  public void inAct(Patient patient) {
    processArriveStats();
    super.inAct(patient);
  }

  @Override
  protected void callNextElementInAct(Patient patient) {
    if (patient.getPatientType().equals(PatientType.TYPE_2)) {
      LaboratoryWalkingProcess laboratoryWalkingProcess = (LaboratoryWalkingProcess) super.getNextElements().get(0).getElement();
      System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), laboratoryWalkingProcess.getName());
      patient.setPatientType(PatientType.TYPE_1);
      laboratoryWalkingProcess.inAct(patient);
    } else if (patient.getPatientType().equals(PatientType.TYPE_3)) {
      Exit exit = (Exit) super.getNextElements().get(1).getElement();
      System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), exit.getName());
      exit.inAct(patient);
    }
  }

  @Override
  public void printResult() {
    super.printResult();
    System.out.println(MEAN_ARRIVE_INTERVAL_MSG + (this.arriveSum / (this.arriveCount - 1)));
  }

  private void processArriveStats() {
    if (lastArrive != 0) {
      arriveSum += super.getTcurr() - lastArrive;
    }
    lastArrive = super.getTcurr();
    arriveCount++;
  }

}
