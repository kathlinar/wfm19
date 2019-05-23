package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class SendFeedbackDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendFeedbackDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'FeedbackMessage'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("FeedbackMessage")
                .correlate();
    }
}
