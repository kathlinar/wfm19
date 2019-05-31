package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class GuideSendConfirmationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(GuideSendConfirmationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending Message 'ReservationConfirmation'");
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReservationConfirmation")
                .correlate();
    }
}
