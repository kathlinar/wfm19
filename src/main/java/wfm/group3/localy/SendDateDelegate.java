package wfm.group3.localy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;
import java.util.logging.Logger;

public class SendDateDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendDateDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Date date = (Date) delegateExecution.getVariable("date");
        LOGGER.info("Date extracted " + date.toString());
        LOGGER.info("Sending Message 'ExpRequestReceived'");
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ExpRequestReceived")
                .setVariable("date", date)
                .correlate();
    }
}
