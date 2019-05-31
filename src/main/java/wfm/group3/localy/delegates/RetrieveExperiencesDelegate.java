package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import wfm.group3.localy.services.ReservationService;

import java.util.logging.Logger;

public class RetrieveExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RetrieveExperiencesDelegate.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Retrieving experiences");
        delegateExecution.setVariable("Experiences", "testExperience");

        this.reservationService.doStuff();
    }
}
