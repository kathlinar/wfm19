package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import wfm.group3.localy.controller.MainController;
import wfm.group3.localy.services.ReservationService;

import java.util.logging.Logger;

public class SendNewReservationDelegate implements JavaDelegate {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MainController mainController;

    private final Logger LOGGER = Logger.getLogger(SendNewReservationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) {

        LOGGER.info("Sending Message 'MakeReservationRequest'");

        String email = delegateExecution.getVariable("email").toString();
        long experienceId = Long.valueOf(delegateExecution.getVariable("experienceToReserve").toString());
        String date = delegateExecution.getVariable("date").toString();

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("MakeReservationRequest")
                .setVariable("email", email)
                .setVariable("experienceId", experienceId)
                .setVariable("date", date)
                .setVariable("processDefinitionId",  delegateExecution.getVariable("processDefinitionId").toString())
                .correlate();
    }
}
