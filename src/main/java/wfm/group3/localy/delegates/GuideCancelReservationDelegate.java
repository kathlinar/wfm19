package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class GuideCancelReservationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(GuideCancelReservationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //TODO: Delete Reservation from DB
        LOGGER.info("Delete Reservation from DB");
    }
}
