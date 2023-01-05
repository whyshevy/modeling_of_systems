package ip91.bohdan.vakaliuk.bank;

import ip91.bohdan.vakaliuk.bank.config.Config;
import ip91.bohdan.vakaliuk.bank.model.ElementChancePair;
import ip91.bohdan.vakaliuk.bank.service.Model;
import ip91.bohdan.vakaliuk.bank.service.element.Create;
import ip91.bohdan.vakaliuk.bank.service.element.Device;
import ip91.bohdan.vakaliuk.bank.service.element.Element;
import ip91.bohdan.vakaliuk.bank.service.element.Exit;
import ip91.bohdan.vakaliuk.bank.service.element.Process;

import java.util.List;

public class SimulationModel {

  private static final double MAX_CHANCE = 1.0;
  private static final double NO_CHANCE = -1.0;
  private static final double CREATOR_DELAY = 0.5;
  private static final double FIRST_CLIENT_TIME = 0.1;
  private static final double CASHIER_DELAY = 1;
  private static final double CASHIER_DELAY_DEV = 0.3;
  private static final int CASHIER_MAX_QUEUE = 3;
  private static final int CASHIER_START_QUEUE = 2;

  public static void main(String[] args) {
    Create creator = new Create("creator", CREATOR_DELAY);
    Process process_1 = new Process("cashier_one",  CASHIER_MAX_QUEUE);
    Process process_2 = new Process("cashier_two", CASHIER_MAX_QUEUE);
    Exit exit = new Exit("exit");

    List<Device> p1Devices = List.of(
        new Device("cashier_one_device_one", CASHIER_DELAY, CASHIER_DELAY_DEV, process_1)
    );
    List<Device> p2Devices = List.of(
        new Device("cashier_two_device_two", CASHIER_DELAY, CASHIER_DELAY_DEV, process_2)
    );

    process_1.setDevices(p1Devices);
    process_2.setDevices(p2Devices);

    creator.setNextElements(List.of(
        new ElementChancePair(process_1, NO_CHANCE),
        new ElementChancePair(process_2, NO_CHANCE))
    );
    process_1.setNextElements(List.of(
        new ElementChancePair(exit, MAX_CHANCE))
    );
    process_2.setNextElements(List.of(
        new ElementChancePair(exit, MAX_CHANCE)
    ));

    // прибуття першого клієнта заплановано на момент часу 0,1 од. часу;
    creator.setTnext(FIRST_CLIENT_TIME);

    // у кожній черзі очікують по два автомобіля.
    process_1.setQueue(CASHIER_START_QUEUE);
    process_2.setQueue(CASHIER_START_QUEUE);

    // обидва касири зайняті
    process_1.inAct();
    process_2.inAct();

    List<Element> list = List.of(creator, process_1, process_2, exit);
    Model model = new Model(list);
    model.simulate(Config.IMITATION_TIME);
  }

}
