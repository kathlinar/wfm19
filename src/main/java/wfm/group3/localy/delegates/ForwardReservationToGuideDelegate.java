package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class ForwardReservationToGuideDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(ForwardReservationToGuideDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String email = delegateExecution.getVariable("email").toString();
        String reservationId = delegateExecution.getVariable("reservationId").toString();

        String instanceId = delegateExecution.getVariable("processInstanceId").toString();
        LOGGER.info("instanceID "+instanceId);

        LOGGER.info("Sending Message 'ReceiveNewRequest'");
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReceiveNewRequest")
                .setVariable("email", email)
                .setVariable("reservationId", reservationId)
                .setVariable("processInstanceId",instanceId)
                .processInstanceId(instanceId)
                .processInstanceBusinessKey(instanceId)
                .correlate();
    }
}
