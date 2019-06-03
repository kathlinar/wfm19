package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.controller.MainController;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.services.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@Component("retrieveExperiencesDelegate")
public class RetrieveExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RetrieveExperiencesDelegate.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MainController mainController;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Retrieving experiences");
        String dateStr = delegateExecution.getVariable("date").toString();
        LOGGER.info(dateStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        List<Experience> experiences = this.reservationService.retrieveExperiences(date);
        Experience[] arr = new Experience[experiences.size()];
        experiences.toArray(arr);
        LOGGER.info("Found " + experiences.size() +  " experiences.");
        //this.mainController.getRecommendedExperiences(experiences,delegateExecution.getVariable("email").toString());

        delegateExecution.setVariable("Experiences", arr);
    }
}
