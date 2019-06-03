package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.services.ReservationService;
import wfm.group3.localy.utils.Enums;
import wfm.group3.localy.utils.JavaMailUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class SendCancellationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendCancellationDelegate.class.getName());

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ReservationService reservationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending Message 'NegativeResponse'");
        String email = delegateExecution.getVariable("email").toString();
        Long experienceId = Long.parseLong(delegateExecution.getVariable("experienceId").toString());
        LocalDate date = LocalDate.parse(delegateExecution.getVariable("date").toString(), formatter);
        String processInstanceId = delegateExecution.getVariable("processInstanceId").toString();

        Optional<Experience> experienceOptional = this.experienceRepository.findById(experienceId);
        if (experienceOptional.isPresent()) {
            Boolean overlap = Boolean.valueOf(delegateExecution.getVariable("overlap").toString());
            Boolean groupFull = Boolean.valueOf(delegateExecution.getVariable("groupFull").toString());

            String startTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(experienceOptional.get().getStartTime());
            String detail = experienceOptional.get().getName() + " (" + experienceOptional.get().getLocation().getInfoString() + "), on "
                    + date.format(formatter) + " starting at " + startTime;

            Enums.MailPurpose mailPurpose;
            if (overlap || groupFull) {
                Enums.ReservationStatus reservationStatus;
                if (overlap) {
                    reservationStatus = Enums.ReservationStatus.OVERLAP;
                    mailPurpose = Enums.MailPurpose.OVERLAP;
                } else {
                    reservationStatus = Enums.ReservationStatus.GROUP_FULL;
                    mailPurpose = Enums.MailPurpose.GROUP_FULL;
                }
                this.reservationService.makeReservation(experienceId, email, date, processInstanceId, reservationStatus);
            } else {
                mailPurpose = Enums.MailPurpose.GUIDE_CANCELLATION;
            }

            try {
                JavaMailUtil.sendMail(email, detail, mailPurpose);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("NegativeResponse")
                .processInstanceId(processInstanceId)
                .correlate();
    }
}
