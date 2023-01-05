package ip91.bohdan.vakaliuk.service.element;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Process extends Element {

  private static final String MAX_QUEUE_MSG = "max queue = ";
  private static final String QUEUE_MSG = "queue = ";
  private static final String FAILURE_MSG = "failure = ";
  private static final String MEAN_LENGTH_OF_QUEUE_MSG = "mean length of queue = ";
  private static final String FAILURE_PROBABILITY_MSG = "\n   probability of failure = ";
  private static final String MEAN_LOAD_MSG = "\n   mean load of process = ";
  private static final String DEVICE_HAS_BEEN_CHOSEN = "device: <%s> was chosen%n";
  private static final String EMPTY_DEVICE_NOT_FOUND = "empty device hasn't been found";

  private int queue;
  private int maxQueue;
  private int failure;
  private double meanQueue;
  private double meanLoad;
  private List<Device> devices;

  public Process(String name, int maxQueue) {
    super(name, 0);
    this.maxQueue = maxQueue;
  }

  @Override
  public void inAct() {
    for (Device device : devices) {
      if (device.getState() == 0) {
        System.out.printf(DEVICE_HAS_BEEN_CHOSEN, device.getName());
        device.inAct();
        if (device.getTnext() < getTnext()) {
          setTnext(device.getTnext());
        }
        return;
      }
    }

    System.out.println(EMPTY_DEVICE_NOT_FOUND);
    if (getQueue() < getMaxQueue()) {
      setQueue(getQueue() + 1);
    } else {
      failure++;
    }
  }

  @Override
  public void outAct() {
    devices.forEach(device -> {
      if (device.getTnext() == this.getTnext()) {
        System.out.printf(DEVICE_HAS_BEEN_CHOSEN, device.getName());
        setQuantity(getQuantity() + 1);
        device.outAct();
        this.callNextElementInActByChance();
      }
    });

    super.setTnext(Double.MAX_VALUE);
    devices.forEach(device -> {
      if (device.getTnext() < getTnext()) {
        setTnext(device.getTnext());
      }
    });
  }

  @Override
  public void printInfo() {
    super.printInfo();
    System.out.println(
        SUB_DESC_START + MAX_QUEUE_MSG + this.getMaxQueue() + VERTICAL_LINE +
        QUEUE_MSG + this.getQueue() + VERTICAL_LINE +
        FAILURE_MSG + this.getFailure()
    );

    devices.forEach(Device::printInfo);
  }

  @Override
  public void printResult() {
    super.printResult();
    System.out.println(
        SUB_DESC_START + MEAN_LENGTH_OF_QUEUE_MSG + (this.meanQueue / super.getTcurr()) +
        FAILURE_PROBABILITY_MSG + (this.failure / (double) super.getQuantity())
    );

    devices.forEach(Device::printResult);
  }

  @Override
  public void doStatistics(double delta) {
    meanQueue = getMeanQueue() + queue * delta;
    devices.forEach(device -> device.doStatistics(delta));
  }

  @Override
  public void setTcurr(double tcurr) {
    super.setTcurr(tcurr);
    devices.forEach(device -> device.setTcurr(tcurr));
  }
}
