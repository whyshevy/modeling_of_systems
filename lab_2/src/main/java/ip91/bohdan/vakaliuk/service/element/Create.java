package ip91.bohdan.vakaliuk.service.element;

public class Create extends Element {

  public Create(String name, double delay) {
    super(name, delay);
    super.setTcurr(0);
    super.setTnext(0);
  }

  @Override
  public void outAct() {
    super.outAct();
    super.setTnext(super.getTcurr() + super.getDelay());

    this.callNextElementInActByChance();
  }

}
