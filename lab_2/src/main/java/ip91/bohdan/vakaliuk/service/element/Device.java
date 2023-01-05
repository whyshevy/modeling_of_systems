package ip91.bohdan.vakaliuk.service.element;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Device extends Element {

  private static final String MEAN_LOAD_MSG = "\n      mean load of device = ";
  private static final String INFO_START = "   ";

  private final Process parentProcess;
  private double meanLoad;

  public Device(String name, double delay, Process parentProcess) {
    super(name, delay);
    this.parentProcess = parentProcess;
  }

  @Override
  public void inAct() {
    super.setState(1);
    super.setTnext(super.getTcurr() + super.getDelay());
  }

  @Override
  public void outAct() {
    super.outAct();
    super.setTnext(Double.MAX_VALUE);
    super.setState(0);

    if (parentProcess.getQueue() > 0) {
      parentProcess.setQueue(parentProcess.getQueue() - 1);
      super.setState(1);
      super.setTnext(super.getTcurr() + super.getDelay());
    }
  }

  @Override
  public void printInfo() {
    System.out.println(
        INFO_START + UPPER_ARROW + this.getName() + ARROW + STATE_MSG + this.getState() + VERTICAL_LINE +
        QUANTITY_MSG + this.getQuantity() + VERTICAL_LINE +
        T_NEXT_MSG + this.getTnext()
    );
  }

  @Override
  public void printResult() {
    System.out.println(
        INFO_START + UPPER_ARROW + this.getName() + ARROW + QUANTITY_MSG + this.getQuantity() +
        MEAN_LOAD_MSG + this.meanLoad / super.getTcurr()
    );
  }

  @Override
  public void doStatistics(double delta) {
    meanLoad = getMeanLoad() + super.getState() * delta;
  }

}
