package ip91.bohdan.vakaliuk.hospital.service.element.base;

import ip91.bohdan.vakaliuk.hospital.model.Patient;
import ip91.bohdan.vakaliuk.hospital.model.PatientChancePair;

import java.util.List;

public class Create extends Element {

  private static final String PATIENT_COME = "patient <%s> has come.%n";

  private final List<PatientChancePair> patientChancePairs;

  public Create(String name, double delay, List<PatientChancePair> patientChancePairs) {
    super(name, delay);
    super.setTcurr(0);
    super.setTnext(0);
    this.patientChancePairs = patientChancePairs;
  }

  @Override
  public void outAct() {
    super.outAct();
    super.setTnext(super.getTcurr() + super.getDelay());

    Patient patient = null;
    double chance = Math.random();
    double chanceSum = 0;
    for (PatientChancePair pair : this.patientChancePairs) {
      chanceSum += pair.getChance();
      if (chance < chanceSum) {
        System.out.printf(PATIENT_COME, pair.getPatient());
        patient = new Patient(pair.getPatient(), super.getTcurr());
        break;
      }
    }

    super.callNextElementInAct(patient);
  }

}
