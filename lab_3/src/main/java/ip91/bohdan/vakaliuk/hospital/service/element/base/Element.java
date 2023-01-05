package ip91.bohdan.vakaliuk.hospital.service.element.base;

import ip91.bohdan.vakaliuk.hospital.model.Allocation;
import ip91.bohdan.vakaliuk.hospital.model.ElementChancePair;
import ip91.bohdan.vakaliuk.hospital.model.Patient;
import ip91.bohdan.vakaliuk.hospital.service.FunRand;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Element {

  protected static final String UPPER_ARROW = "^ ";
  protected static final String ARROW = " =>";
  protected static final String VERTICAL_LINE = " | ";
  protected static final String SUB_DESC_START = "   ";
  private static final String ID_PREFIX = "element = ";
  protected static final String QUANTITY_MSG = " quantity = ";
  protected static final String STATE_MSG = " state = ";
  protected static final String T_NEXT_MSG = " tnext = ";
  protected static final String PATIENT_MOVE_FROM_TO = " patient <%s>  moved from <%s> to <%s>%n";

  private String name;
  private double tnext;
  private double delayMean, delayDev;
  private int erlangK;
  private Allocation distribution;
  private int quantity;
  private double tcurr;
  private int state;
  private List<ElementChancePair> nextElements;
  private static int nextId=0;
  private int id;
  private Patient activePatient;

  private Allocation secDistribution;

  public Element() {
    this.tnext = Double.MAX_VALUE;
    this.delayMean = 1.0;
    this.distribution = Allocation.EXP;
    this.tcurr = tnext;
    this.state = 0;
    this.nextElements = new ArrayList<>();
    this.id = nextId;
    nextId++;
    this.name = ID_PREFIX + this.id;
  }

  public Element(double delay) {
    this();
    this.delayMean = delay;
  }

  public Element(String nameOfElement, double delay) {
    this(delay);
    this.name = nameOfElement;
  }

  public double getDelay() {
    return switch (distribution) {
      case EXP -> FunRand.Exp(getDelayMean());
      case NORMAL -> FunRand.Norm(getDelayMean(), getDelayDev());
      case UNIFORM -> FunRand.Unif(getDelayMean(), getDelayDev());
      case ERLANG -> FunRand.Erlang(getDelayMean(), getErlangK());
      case MEAN_VALUE -> getDelayMean();
    };
  }

  public void inAct(Patient patient) {
    this.setActivePatient(patient);
  }

  public void outAct() {
    this.quantity++;
  }

  protected void callNextElementInAct(Patient patient) {
    double chance = Math.random();
    double chanceSum = 0;
    for (ElementChancePair pair : this.getNextElements()) {
      chanceSum += pair.getChance();
      if (chance < chanceSum) {
        System.out.printf(PATIENT_MOVE_FROM_TO, patient.getPatientType().name(), this.getName(), pair.getElement().getName());
        pair.getElement().inAct(patient);
        return;
      }
    }
  }

  public void printResult(){
    System.out.println(UPPER_ARROW + name + ARROW + QUANTITY_MSG + quantity);
  }

  public void printInfo(){
    System.out.println(
        UPPER_ARROW + name + ARROW + STATE_MSG + state + VERTICAL_LINE +
        QUANTITY_MSG + quantity + VERTICAL_LINE +
        T_NEXT_MSG + tnext
    );
  }

  public void doStatistics(double delta){

  }

}
