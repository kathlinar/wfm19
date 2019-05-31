package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class GuideMakeReservationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(GuideMakeReservationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //TODO: Save Reservation to DB
        LOGGER.info("Save Reservation to DB");
    }
}
