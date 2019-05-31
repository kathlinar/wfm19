package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class SendNewReservationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendNewReservationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'MakeReservationRequest'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("MakeReservationRequest")
                .setVariable("new Reservation", "Test Reservation")
                .correlate();
    }
}
