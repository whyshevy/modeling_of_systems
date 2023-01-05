package ip91.bohdan.vakaliuk.bank.service.element;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Process extends Element {

  private static final String MAX_QUEUE_MSG = "max queue = ";
  private static final String QUEUE_MSG = "queue = ";
  private static final String MEAN_LENGTH_OF_QUEUE_MSG = "mean length of queue = ";
  private static final String MEAN_LOAD_MSG = "\n   mean load of cashier = ";
  private static final String DEVICE_HAS_BEEN_CHOSEN = "device: <%s> was chosen%n";
  private static final String DEVICE_HAS_COMPLETED_WORK = "device <%s> completed it's work%n";
  private static final String EMPTY_DEVICE_NOT_FOUND = "empty device hasn't been found";
  private static final String CAR_MOVED_TO_QUEUE = "car has moved to queue";
  private static final String MEAN_DEPARTURE_INTERVAL_MSG = "\n   mean departure interval = ";

  private int queue;
  private int maxQueue;
  private int failure;
  private double meanQueue;
  private List<Device> devices;
  private double departureSum;
  private double departureCount;
  private double lastDeparture;

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
      System.out.println(CAR_MOVED_TO_QUEUE);
      setQueue(getQueue() + 1);
    } else {
      failure++;
    }
  }

  @Override
  public void outAct() {
    devices.forEach(device -> {
      if (device.getTnext() == this.getTnext()) {
        System.out.printf(DEVICE_HAS_COMPLETED_WORK, device.getName());
        setQuantity(getQuantity() + 1);
        device.outAct();
        processDepartureStats();
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
        QUEUE_MSG + this.getQueue()
    );

    devices.forEach(Device::printInfo);
  }

  @Override
  public void printResult() {
    super.printResult();
    System.out.println(
        SUB_DESC_START + MEAN_LENGTH_OF_QUEUE_MSG + (this.meanQueue / super.getTcurr()) +
        MEAN_DEPARTURE_INTERVAL_MSG + (this.departureSum / (this.departureCount - 1))
    );

    devices.forEach(Device::printResult);
  }

  @Override
  public void doStatistics(double delta) {
    meanQueue = getMeanQueue() + queue * delta;
    devices.forEach(device -> device.doStatistics(delta));
  }

  private void processDepartureStats() {
    if (lastDeparture != 0) {
      departureSum += super.getTcurr() - lastDeparture;
    }
    lastDeparture = super.getTcurr();
    departureCount++;
  }

  @Override
  public void setTcurr(double tcurr) {
    super.setTcurr(tcurr);
    devices.forEach(device -> device.setTcurr(tcurr));
  }
}
