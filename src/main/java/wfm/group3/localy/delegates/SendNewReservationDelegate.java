package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.controller.MainController;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Person;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.PersonRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.services.ReservationService;
import wfm.group3.localy.utils.Enums;
import wfm.group3.localy.utils.JavaMailUtil;

import java.util.Set;
import java.util.logging.Logger;

@Component
public class SendNewReservationDelegate implements JavaDelegate {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private MainController mainController;

    private final Logger LOGGER = Logger.getLogger(SendNewReservationDelegate.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) {

        LOGGER.info("Sending Message 'MakeReservationRequest'");

        String email = delegateExecution.getVariable("email").toString();
        long experienceId = Long.valueOf(delegateExecution.getVariable("experienceToReserve").toString());
        String date = delegateExecution.getVariable("date").toString();

        Experience experience = this.experienceRepository.getOne(experienceId);

        Set<Person> personSet = experience.getOfferedBy();
        String guideEmail = personSet.iterator().next().getEmail();

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("MakeReservationRequest")
                .setVariable("email", email)
                .setVariable("experienceId", experienceId)
                .setVariable("date", date)
                .setVariable("processInstanceId",  delegateExecution.getVariable("processInstanceId").toString())
                .correlate();

        try {
            JavaMailUtil.sendMail(guideEmail,"New Reservation request available", Enums.MailPurpose.GUIDE_CONFIRMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
