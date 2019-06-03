package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class ForwardCancellationToGuide implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(ForwardCancellationToGuide.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending Message 'ReveiveNewCancellation'");

        String reservationId = delegateExecution.getVariable("reservationId").toString();

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReceiveNewCancellation")
                .setVariable("reservationId", reservationId)
                .correlate();
    }
}
