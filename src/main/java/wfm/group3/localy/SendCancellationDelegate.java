package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class SendCancellationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendCancellationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'NegativeResponse'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("NegativeResponse")
                .correlate();
    }
}
