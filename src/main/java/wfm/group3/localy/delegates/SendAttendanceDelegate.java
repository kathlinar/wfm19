package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class SendAttendanceDelegate implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(SendAttendanceDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String email = delegateExecution.getVariable("email").toString();
        long reservationId = Long.valueOf(delegateExecution.getVariable("reservationId").toString());
        String processInstanceId = delegateExecution.getVariable("processInstanceId").toString();

        LOGGER.info("Sending Message 'SendAttendanceDelegate'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("UserAttended")
                .setVariable("email", email)
                .setVariable("reservationId", reservationId)
                .setVariable("processInstanceId",processInstanceId)
                .correlate();

    }
}
