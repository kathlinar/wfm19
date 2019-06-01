package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import wfm.group3.localy.utils.JavaMailUtil;

import java.util.Date;
import java.util.logging.Logger;

public class SendDateDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendDateDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Date date = (Date) delegateExecution.getVariable("date");
        String email = (String) delegateExecution.getVariable("email");
        LOGGER.info("Email extracted " + email);
        LOGGER.info("Date extracted " + date.toString());
        LOGGER.info("Sending Message 'ExpRequestReceived'");
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ExpRequestReceived")
                .setVariable("date", date)
                .setVariable("email",email)
                .correlate();
        
        // For convenience this is tested here (2nd task in Camunda), with our mail address
        // TODO: delete
        JavaMailUtil.sendMail(email,"User Confirmation");
        
    }
}
