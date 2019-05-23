package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class SendConfirmationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendConfirmationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'PositiveResponse'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("PositiveResponse")
                .correlate();
    }
}
