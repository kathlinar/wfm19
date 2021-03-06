package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class ForwardCancellationToGuide implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(ForwardCancellationToGuide.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'ReveiveNewCancellation'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReceiveNewCancellation")
                .setVariable("experienceID", 0)
                .correlate();
    }
}
