package ip91.bohdan.vakaliuk.hospital.service.element.base;

import ip91.bohdan.vakaliuk.hospital.model.Patient;

public class Exit extends Element {

  private static final String PATIENT_COMPLETE_CYCLE = "patient <%s> completed the processing cycle %n";
  private static final String MEAN_CYCLE_INTERVAL_MSG = "   mean cycle interval = ";

  private double cycleTimeSum;

  public Exit(String name) {
    super(name, 0);
  }

  @Override
  public void inAct(Patient patient) {
    super.inAct(patient);
    super.setQuantity(super.getQuantity() + 1);
    processStats(patient);
    System.out.printf(PATIENT_COMPLETE_CYCLE, patient.getPatientType().name());
  }

  private void processStats(Patient patient) {
    cycleTimeSum += super.getTcurr() - patient.getCreationTime();
  }

  @Override
  public void outAct() {}

  @Override
  public void printResult() {
    super.printResult();
    System.out.println(MEAN_CYCLE_INTERVAL_MSG + (cycleTimeSum / super.getQuantity()));
  }

  @Override
  public void printInfo() {
    super.printResult();
  }
}
