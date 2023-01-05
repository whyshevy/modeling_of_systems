package ip91.bohdan.vakaliuk.hospital.service.element.task;

import ip91.bohdan.vakaliuk.hospital.service.element.base.Device;
import ip91.bohdan.vakaliuk.hospital.service.element.base.Process;

public class LoyaltyDoctor extends Device {

    public LoyaltyDoctor(String name, double delay, Process parentProcess) {
        super(name, delay, parentProcess);
    }

    @Override
    public double getDelay() {
        super.setDelayMean(this.getActivePatient().getPatientType().getMeanRegistrationTime());
        return super.getDelay();
    }
}
