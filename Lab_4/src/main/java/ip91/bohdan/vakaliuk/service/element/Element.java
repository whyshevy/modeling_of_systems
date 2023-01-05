package ip91.bohdan.vakaliuk.service.element;

import ip91.bohdan.vakaliuk.model.Distribution;
import ip91.bohdan.vakaliuk.model.ElementChancePair;
import ip91.bohdan.vakaliuk.service.FunRand;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Element {

  protected static final String STAR = "* ";
  protected static final String ARROW = " =>";
  protected static final String VERTICAL_LINE = " | ";
  protected static final String SUB_DESC_START = "   ";
  private static final String ID_PREFIX = "element";
  protected static final String QUANTITY_MSG = " quantity: ";
  protected static final String STATE_MSG = " state: ";
  protected static final String T_NEXT_MSG = " tnext: ";
  private static final String TOKEN_MOVE_FROM_TO = "The Token has moved from <%s> to <%s>%n";
  private static final String TOKEN_COMPLETE_CYCLE = "The Token has completed the processing cycle";

  private String name;
  private double tnext;
  private double delayMean, delayDev;
  private Distribution distribution;
  private int quantity;
  private double tcurr;
  private int state;
  private List<ElementChancePair> nextElements;
  private static int nextId=0;
  private int id;

  public Element() {
    this.tnext = Double.MAX_VALUE;
    this.delayMean = 1.0;
    this.distribution = Distribution.MEAN_VALUE;
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
      case MEAN_VALUE -> getDelayMean();
    };
  }

  public void inAct() {

  }

  public void outAct() {
    this.quantity++;
  }

  protected void callNextElementInActByChance() {
    double chance = Math.random();
    double chanceSum = 0;
    for (ElementChancePair pair : this.getNextElements()) {
      chanceSum += pair.getChance();
      if (chance < chanceSum) {
//        System.out.printf(TOKEN_MOVE_FROM_TO, this.getName(), pair.getElement().getName());
        pair.getElement().inAct();
        return;
      }
    }
    System.out.println(TOKEN_COMPLETE_CYCLE);
  }

  public void printResult(){
    System.out.println(STAR + name + ARROW + QUANTITY_MSG + quantity);
  }

  public void printInfo(){
    System.out.println(
        STAR + name + ARROW + STATE_MSG + state + VERTICAL_LINE +
        QUANTITY_MSG + quantity + VERTICAL_LINE +
        T_NEXT_MSG + tnext
    );
  }

  public void doStatistics(double delta){

  }

}
