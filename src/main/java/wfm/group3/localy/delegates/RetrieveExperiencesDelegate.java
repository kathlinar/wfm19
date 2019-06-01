package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.services.ReservationService;

import java.util.List;
import java.util.logging.Logger;

@Component("retrieveExperiencesDelegate")
public class RetrieveExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RetrieveExperiencesDelegate.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Retrieving experiences");
        List<Experience> experiences = this.reservationService.retrieveExperiences();
        Experience[] arr = new Experience[experiences.size()];
        experiences.toArray(arr);
        LOGGER.info("Found " + experiences.size() +  " experiences.");
        delegateExecution.setVariable("Experiences", arr);
    }
}
