package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class RetrieveExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RetrieveExperiencesDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Retrieving experiences");
        delegateExecution.setVariable("Experiences","testExperience");
    }
}
