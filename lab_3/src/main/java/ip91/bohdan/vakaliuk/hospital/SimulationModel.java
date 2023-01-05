package ip91.bohdan.vakaliuk.hospital;

import ip91.bohdan.vakaliuk.hospital.config.Config;
import ip91.bohdan.vakaliuk.hospital.model.PatientChancePair;
import ip91.bohdan.vakaliuk.hospital.model.PatientType;
import ip91.bohdan.vakaliuk.hospital.model.ElementChancePair;
import ip91.bohdan.vakaliuk.hospital.service.Model;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Create;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Device;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Element;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Exit;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Process;
import ip91.bohdan.vakaliuk.hospital.service.element.task.LoyaltyDoctor;
import ip91.bohdan.vakaliuk.hospital.service.element.task.LaboratoryProcess;
import ip91.bohdan.vakaliuk.hospital.service.element.task.LaboratoryWalkingProcess;
import ip91.bohdan.vakaliuk.hospital.service.element.task.ReceptionProcess;

import java.util.List;
import java.util.stream.IntStream;

public class SimulationModel {

  private static final double PATIENT_TYPE_1_CHANCE = 0.5;
  private static final double PATIENT_TYPE_2_CHANCE = 0.1;
  private static final double PATIENT_TYPE_3_CHANCE = 0.4;
  private static final double CREATOR_MEAN_TIME = 15;
  private static final double WARD_WALKING_TIME_MIN = 3;
  private static final double WARD_WALKING_TIME_MAX = 8;
  private static final int LAB_WALKING_DEVICE_COUNT = 10;
  private static final double LAB_WALKING_TIME_MIN = 2;
  private static final double LAB_WALKING_TIME_MAX = 5;
  private static final double REGISTRY_TIME_MEAN = 4.5;
  private static final int REGISTRY_ERLANG_K = 3;
  private static final double LAB_EXAMINATION_TIME_MEAN = 4;
  private static final int LAB_EXAMINATION_ERLANG_K = 2;
  private static final double MAX_CHANCE = 1;
  private static final double NO_CHANCE = -1;
  private static final double NO_DELAY = -1;

  public static void main(String[] args) {

    List<PatientChancePair> patientChancePairs = List.of(
            new PatientChancePair(PatientType.TYPE_1, PATIENT_TYPE_1_CHANCE),
            new PatientChancePair(PatientType.TYPE_2, PATIENT_TYPE_2_CHANCE),
            new PatientChancePair(PatientType.TYPE_3, PATIENT_TYPE_3_CHANCE)
    );

    Create creator = new Create("creator", CREATOR_MEAN_TIME, patientChancePairs);
    Process reception = new ReceptionProcess("reception", Integer.MAX_VALUE);
    Process wardWalking = new Process("ward walking", Integer.MAX_VALUE);
    Process laboratoryWalking = new LaboratoryWalkingProcess("laboratory walking", Integer.MAX_VALUE);
    Process registry = new Process("registry", Integer.MAX_VALUE);
    Process laboratory = new LaboratoryProcess("laboratory", Integer.MAX_VALUE);
    Exit exit = new Exit("exit");

    List<Device> dutyDoctors = List.of(
        new LoyaltyDoctor("reception_loyalty_doctor_one", NO_DELAY, reception),
        new LoyaltyDoctor("reception_loyalty_doctor_two", NO_DELAY, reception)
    );

    List<Device> accompanyingPersonnel = List.of(
        new Device("accompanying_person_one", WARD_WALKING_TIME_MIN, WARD_WALKING_TIME_MAX, wardWalking),
        new Device("accompanying_person_two", WARD_WALKING_TIME_MIN, WARD_WALKING_TIME_MAX, wardWalking),
        new Device("accompanying_person_three", WARD_WALKING_TIME_MIN, WARD_WALKING_TIME_MAX, wardWalking)
    );

    List<Device> laboratoryWalkingDevices = IntStream.range(0, LAB_WALKING_DEVICE_COUNT).mapToObj(index ->
            new Device("lab_walking_device_" + (index + 1), LAB_WALKING_TIME_MIN, LAB_WALKING_TIME_MAX, laboratoryWalking)
    ).toList();

    List<Device> registryDoctors = List.of(
        new Device("registry_doctor_one", REGISTRY_TIME_MEAN, REGISTRY_ERLANG_K, registry)
    );

    List<Device> laboratoryAssistants = List.of(
        new Device("lab_assistant_one", LAB_EXAMINATION_TIME_MEAN, LAB_EXAMINATION_ERLANG_K, laboratory),
        new Device("lab_assistant_two", LAB_EXAMINATION_TIME_MEAN, LAB_EXAMINATION_ERLANG_K, laboratory)
    );

    reception.setDevices(dutyDoctors);
    wardWalking.setDevices(accompanyingPersonnel);
    laboratoryWalking.setDevices(laboratoryWalkingDevices);
    registry.setDevices(registryDoctors);
    laboratory.setDevices(laboratoryAssistants);

    creator.setNextElements(List.of(
        new ElementChancePair(reception, MAX_CHANCE)
    ));

    reception.setNextElements(List.of(
        new ElementChancePair(wardWalking, NO_CHANCE),
        new ElementChancePair(laboratoryWalking, NO_CHANCE)
    ));

    wardWalking.setNextElements(List.of(
        new ElementChancePair(exit, MAX_CHANCE)
    ));

    laboratoryWalking.setNextElements(List.of(
        new ElementChancePair(registry, NO_CHANCE),
        new ElementChancePair(reception, NO_CHANCE)
    ));

    registry.setNextElements(List.of(
        new ElementChancePair(laboratory, MAX_CHANCE)
    ));

    laboratory.setNextElements(List.of(
        new ElementChancePair(laboratoryWalking, NO_CHANCE),
        new ElementChancePair(exit, NO_CHANCE)
    ));

    List<Element> list = List.of(creator, reception, wardWalking, laboratoryWalking, registry, laboratory, exit);
    Model model = new Model(list);
    model.simulate(Config.IMITATION_TIME);
  }

}
