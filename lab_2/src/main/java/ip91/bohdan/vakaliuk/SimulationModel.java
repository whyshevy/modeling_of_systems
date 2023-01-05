package ip91.bohdan.vakaliuk;

import ip91.bohdan.vakaliuk.model.ElementChancePair;
import ip91.bohdan.vakaliuk.service.Model;
import ip91.bohdan.vakaliuk.service.element.*;
import ip91.bohdan.vakaliuk.service.element.Process;

import java.util.List;

import static ip91.bohdan.vakaliuk.config.Config.*;

public class SimulationModel {

  public static void main(String[] args) {
    Create creator = new Create("creator", 3);
    Process process_1 = new Process("first_process",  6);
    Process process_2 = new Process("second_process", 4);
    Process process_3 = new Process("third_process", 6);
    Exit exit = new Exit("exit");

    List<Device> p1Devices = List.of(
        new Device("first_process_device_one", 4.0, process_1)
    );
    List<Device> p2Devices = List.of(
        new Device("second_process_device_one", 6.0, process_2)
    );
    List<Device> p3Devices = List.of(
        new Device("third_process_device_one", 3, process_3)
    );

    process_1.setDevices(p1Devices);
    process_2.setDevices(p2Devices);
    process_3.setDevices(p3Devices);

    creator.setNextElements(List.of(
        new ElementChancePair(process_1, 1.0))
    );
    process_1.setNextElements(List.of(
        new ElementChancePair(process_2, 1.0))
    );
    process_2.setNextElements(List.of(
        new ElementChancePair(process_3, 1.0)
    ));
    process_3.setNextElements(List.of(
        new ElementChancePair(exit, 1.0))
    );

    List<Element> list = List.of(creator, process_1, process_2, process_3, exit);
    Model model = new Model(list);
    model.simulate(IMITATION_TIME);
  }

}
