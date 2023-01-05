package ip91.bohdan.vakaliuk.service.element;

public class Exit extends Element {

  public Exit(String name) {
    super(name, 0);
  }

  @Override
  public void inAct() {
    super.inAct();
    super.setQuantity(super.getQuantity() + 1);
  }

  @Override
  public void outAct() {}

  @Override
  public void printInfo() {
    super.printResult();
  }
}
