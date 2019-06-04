package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import wfm.group3.localy.entity.Experience;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SendExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendExperiencesDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'ReceiveExperiences");
        Experience[] experiences = (Experience[]) delegateExecution.getVariable("Experiences");
        LOGGER.info("Retrieved " + experiences.length + " experiences.");
        ObjectValue typedObjectValue = Variables
                .objectValue(experiences)
                .serializationDataFormat(Variables.SerializationDataFormats.JAVA)
                .create();
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReceiveExperiences")
                .setVariable("allExperiences", typedObjectValue)
                .correlate();
    }
}
