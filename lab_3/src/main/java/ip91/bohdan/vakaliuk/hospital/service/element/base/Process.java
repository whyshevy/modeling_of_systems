package ip91.bohdan.vakaliuk.hospital.service.element.base;

import ip91.bohdan.vakaliuk.hospital.model.Patient;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Process extends Element {

  private static final String MAX_QUEUE_MSG = "max queue = ";
  private static final String QUEUE_MSG = "queue = ";
  private static final String FAILURE_MSG = "failure =  ";
  private static final String MEAN_LENGTH_OF_QUEUE_MSG = "mean length of queue = ";
  private static final String FAILURE_PROBABILITY_MSG = "\n    probability of failure = ";
  private static final String MEAN_LOAD_MSG = "\n   mean load of process = ";
  private static final String DEVICE_HAS_BEEN_CHOSEN = "device: <%s>  was chosen%n";
  private static final String DEVICE_HAS_COMPLETED_WORK = "device <%s> completed it's work%n";
  private static final String EMPTY_DEVICE_NOT_FOUND = "empty device hasn't been found";
  private static final String TOKEN_MOVED_TO_QUEUE = "patient moved to queue";
  private static final String TOKEN_LEFT_SYSTEM = "patient left the system - there are no place";

  private final List<Patient> queue;
  private int maxQueue;
  private int failure;
  private double meanQueue;
  private double meanLoad;
  private List<Device> devices;

  public Process(String name, int maxQueue) {
    super(name, 0);
    this.queue = new ArrayList<>();
    this.maxQueue = maxQueue;
  }

  @Override
  public void inAct(Patient patient) {
    for (Device device : devices) {
      if (device.getState() == 0) {
        System.out.printf(DEVICE_HAS_BEEN_CHOSEN, device.getName());
        device.inAct(patient);
        if (device.getTnext() < getTnext()) {
          setTnext(device.getTnext());
        }
        return;
      }
    }

    System.out.println(EMPTY_DEVICE_NOT_FOUND);
    if (getQueue().size() < getMaxQueue()) {
      System.out.println(TOKEN_MOVED_TO_QUEUE);
      queue.add(patient);
    } else {
      System.out.println(TOKEN_LEFT_SYSTEM);
      failure++;
    }
  }

  @Override
  public void outAct() {
    devices.forEach(device -> {
      if (device.getTnext() == this.getTnext()) {
        System.out.printf(DEVICE_HAS_COMPLETED_WORK, device.getName());
        setQuantity(getQuantity() + 1);
        Patient devicePatient = device.getActivePatient();
        device.outAct();
        this.callNextElementInAct(devicePatient);
      }
    });

    super.setTnext(Double.MAX_VALUE);
    devices.forEach(device -> {
      if (device.getTnext() < getTnext()) {
        setTnext(device.getTnext());
      }
    });
  }

  public Patient reduceQueue() {
    return queue.remove(0);
  }

  @Override
  public void printInfo() {
    super.printInfo();
    System.out.println(
        SUB_DESC_START + MAX_QUEUE_MSG + this.getMaxQueue() + VERTICAL_LINE +
        QUEUE_MSG + this.getQueue().stream().map(Patient::getPatientType).toList() + VERTICAL_LINE +
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
    meanQueue = getMeanQueue() + queue.size() * delta;
    devices.forEach(device -> device.doStatistics(delta));
  }

  @Override
  public void setTcurr(double tcurr) {
    super.setTcurr(tcurr);
    devices.forEach(device -> device.setTcurr(tcurr));
  }
}
