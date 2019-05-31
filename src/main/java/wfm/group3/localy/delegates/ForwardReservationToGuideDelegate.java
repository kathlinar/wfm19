package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class ForwardReservationToGuideDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(ForwardReservationToGuideDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'ReceiveNewRequest'");
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReceiveNewRequest")
                .setVariable("experienceID", 0)
                .correlate();
    }
}
