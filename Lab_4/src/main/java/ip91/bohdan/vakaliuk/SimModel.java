package ip91.bohdan.vakaliuk;

import ip91.bohdan.vakaliuk.model.ElementChancePair;
import ip91.bohdan.vakaliuk.service.Model;
import ip91.bohdan.vakaliuk.service.element.*;
import ip91.bohdan.vakaliuk.service.element.Process;
import ip91.chui.oleh.service.element.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ip91.bohdan.vakaliuk.config.Config.*;

@RequiredArgsConstructor
public class SimModel {

  public Map<Integer, Long> start(List<Integer> SMO_N_list) {
    Map<Integer, Long> resultData = new HashMap<>();

    SMO_N_list.forEach(smo_n -> {
      long start = System.currentTimeMillis();
      testModel(smo_n);
      long end = System.currentTimeMillis();
      long millSecDiff = end - start;
      long secDiff = millSecDiff / 1000;
      resultData.put(smo_n, secDiff);
    });

    return resultData;
  }

  private void testModel(int SMO_N) {
    List<Element> list = new ArrayList<>();

    Create start = new Create("Start", START_MEAN_DELAY);
    Exit exit = new Exit("Exit");

    list.add(start);

    for (int i = 0; i < SMO_N; i++) {
      Process process = new Process("PROCESS_" + i, MAX_QUEUE);
      List<Device> devices = List.of(new Device(process.getName() + "_DEVICE_1", DEVICE_MEAN_DELAY, process));
//      List<Device> devices = List.of(
//          new Device(process.getName() + "_DEVICE_1", DEVICE_MEAN_DELAY, process),
//          new Device(process.getName() + "_DEVICE_2", DEVICE_MEAN_DELAY, process)
//      );
      process.setDevices(devices);
      list.add(process);
      list.get(i).setNextElements(List.of(new ElementChancePair(process, MAX_CHANCE)));
    }

    list.get(list.size() - 1).setNextElements(List.of(new ElementChancePair(exit, MAX_CHANCE)));
    list.add(exit);

    Model model = new Model(list);
    model.simulate(IMITATION_TIME);
  }

}
