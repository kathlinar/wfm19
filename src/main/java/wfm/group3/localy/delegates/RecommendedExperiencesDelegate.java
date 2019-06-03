package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.controller.MainController;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.services.RecommenderService;

import java.util.Arrays;
import java.util.logging.Logger;

@Component("recommendedExperiencesDelegate")
public class RecommendedExperiencesDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(RecommendedExperiencesDelegate.class.getName());

    @Autowired
    private MainController mainController;

    @Autowired
    private RecommenderService recommenderService;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Recommending experiences");
        Experience[] experiences = (Experience[]) delegateExecution.getVariable("Experiences");
        String email = delegateExecution.getVariable("email").toString();
        LOGGER.info(experiences.toString());
        LOGGER.info(email);
        Person person = personRepository.findByEmail(email);
        Experience[] recommendations = this.recommenderService.getRecommendation(person, experiences);
        this.mainController.getRecommendedExperiences(Arrays.asList(recommendations),delegateExecution.getVariable("email").toString());
    }
}
