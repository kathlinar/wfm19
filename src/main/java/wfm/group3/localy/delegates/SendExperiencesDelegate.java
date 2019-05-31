package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class SendExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendExperiencesDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'ReceiveExperiences");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReceiveExperiences")
                .setVariable("allExperiences", "testAllExperiences")
                .correlate();
    }
}
