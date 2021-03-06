package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class RecommendedExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RecommendedExperiencesDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Recommending experiences");
        delegateExecution.setVariable("recommended","testRecommendation");
    }
}
