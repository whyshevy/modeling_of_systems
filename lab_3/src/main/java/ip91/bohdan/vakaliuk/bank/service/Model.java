package ip91.bohdan.vakaliuk.bank.service;

import ip91.bohdan.vakaliuk.bank.service.element.Element;
import ip91.bohdan.vakaliuk.bank.service.element.Exit;
import ip91.bohdan.vakaliuk.bank.service.element.Process;

import java.util.List;

public class Model {

  private static final String RESULT_TITLE = "\n================FINAL==RESULT===================";
  private static final String TIME_EVENT_MSG = "\nevent time for ";
  private static final String TIME_MSG = ", time = ";
  private static final String MEAN_PEOPLE_IN_BANK_MSG = "mean people in bank = ";
  private static final String MEAN_CLIENT_TIME_IN_BANK_MSG = "mean time in bank for client = ";
  private static final String QUEUE_1_NAME = "first_queue = ";
  private static final String QUEUE_2_NAME = "second_queue = ";
  private static final String SWAP_QUEUE_RESULT_MSG = "clients swapped queue %d times%n";
  private static final String CAR_SWAP_QUEUE = "car moved from %s to %s %n";

  private final List<Element> list;
  private double tnext, tcurr;
  private Element event;
  private double meanPeopleInBank;
  private int queueSwapping;

  public Model(List<Element> elements) {
    this.list = elements;
  }

  public void simulate(double time) {

    while (tcurr < time) {
      tnext = Double.MAX_VALUE;

      list.forEach(el -> {
        if (el.getTnext() < tnext) {
          tnext = el.getTnext();
          event = el;
        }
      });
      System.out.println(TIME_EVENT_MSG + event.getName() + TIME_MSG + tnext);

      doStatistics(tnext - tcurr);
      list.forEach(el -> el.doStatistics(tnext - tcurr));
      tcurr = tnext;
      list.forEach(el -> el.setTcurr(tcurr));

      event.outAct();
      list.stream().filter(el -> el.getTnext() == tcurr).forEach(Element::outAct);

      swapQueueIfPossible((Process) list.get(1), (Process) list.get(2));

      printInfo();
    }
    printResult();
  }

  private void printInfo() {
    list.forEach(Element::printInfo);
  }

  private void printResult() {
    final Exit exit = (Exit) list.get(3);

    System.out.println(RESULT_TITLE);
    System.out.println(MEAN_PEOPLE_IN_BANK_MSG + (meanPeopleInBank / tcurr));
    System.out.println(MEAN_CLIENT_TIME_IN_BANK_MSG + (meanPeopleInBank / exit.getQuantity()));
    list.forEach(Element::printResult);
    System.out.printf(SWAP_QUEUE_RESULT_MSG, queueSwapping);
  }

  private void swapQueueIfPossible(Process cashier_1, Process cashier_2) {
    if (cashier_1.getQueue() == cashier_1.getMaxQueue() && (cashier_1.getQueue() - cashier_2.getQueue() >= 2)) {
      cashier_1.setQueue(cashier_1.getQueue() - 1);
      cashier_2.setQueue(cashier_2.getQueue() + 1);
      queueSwapping++;
      System.out.printf(CAR_SWAP_QUEUE, QUEUE_1_NAME, QUEUE_2_NAME);
    } else if (cashier_2.getQueue() == cashier_2.getMaxQueue() && (cashier_2.getQueue() - cashier_1.getQueue() >= 2)) {
      cashier_2.setQueue(cashier_2.getQueue() - 1);
      cashier_1.setQueue(cashier_1.getQueue() + 1);
      queueSwapping++;
      System.out.printf(CAR_SWAP_QUEUE, QUEUE_2_NAME, QUEUE_1_NAME);
    }
  }

  private void doStatistics(double delta) {
    Process cashier_1 = (Process) list.get(1);
    Process cashier_2 = (Process) list.get(2);

    int queueSum = cashier_1.getQueue() + cashier_2.getQueue();
    int processingSum = cashier_1.getDevices().get(0).getState() + cashier_2.getDevices().get(0).getState();

    meanPeopleInBank += (queueSum + processingSum) * delta;
  }

}
