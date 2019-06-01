package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jboss.jandex.Main;
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
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'MakeReservationRequest'");

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("MakeReservationRequest")
                .setVariable("new Reservation", "Test Reservation")
                .correlate();

    }
}
