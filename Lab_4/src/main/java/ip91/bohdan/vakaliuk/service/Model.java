package ip91.bohdan.vakaliuk.service;

import ip91.bohdan.vakaliuk.service.element.Element;

import java.util.List;

public class Model {

  private static final String RESULT_TITLE = "\n_____________RESULT_____________";
  private static final String TIME_EVENT_MSG = "\nEvent time for ";
  private static final String TIME_MSG = ", time = ";

  private final List<Element> list;
  double tnext, tcurr;
  Element event;

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
//      System.out.println(TIME_EVENT_MSG + event.getName() + TIME_MSG + tnext);

      list.forEach(el -> el.doStatistics(tnext - tcurr));
      tcurr = tnext;
      list.forEach(el -> el.setTcurr(tcurr));

      event.outAct();
      list.stream().filter(el -> el.getTnext() == tcurr).forEach(Element::outAct);

//      printInfo();
    }
//    printResult();
  }

  private void printInfo() {
    list.forEach(Element::printInfo);
  }

  private void printResult() {
    System.out.println(RESULT_TITLE);
    list.forEach(Element::printResult);
  }

}
