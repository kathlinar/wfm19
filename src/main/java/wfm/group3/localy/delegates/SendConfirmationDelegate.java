package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import wfm.group3.localy.utils.JavaMailUtil;

import java.util.logging.Logger;

public class SendConfirmationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendConfirmationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'PositiveResponse'");
        String email = (String) delegateExecution.getVariable("email");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("PositiveResponse")
                .correlate();
        
        // Send Confirmation E-mail to the User. E-mail address acquired in Task 1: Log-In
        // For testing purposes this is for now set as our mail address
        // TODO: exchange email address to recipient
        JavaMailUtil.sendMail(email,"User Confirmation");
    }
}
