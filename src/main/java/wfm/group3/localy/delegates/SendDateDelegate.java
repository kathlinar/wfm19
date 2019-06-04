package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class SendDateDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendDateDelegate.class.getName());

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        LocalDate date = LocalDate.parse(delegateExecution.getVariable("date").toString(), formatter);
        String email = (String) delegateExecution.getVariable("email");
        LOGGER.info("Email extracted " + email);
        LOGGER.info("Date extracted " + formatter.format(date));
        LOGGER.info("Sending Message 'ExpRequestReceived'");
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ExpRequestReceived")
                .setVariable("date", formatter.format(date))
                .setVariable("email", email)
                .correlate();
    }
}
