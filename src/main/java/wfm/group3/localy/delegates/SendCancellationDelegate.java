package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.ReservationRepository;
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
    private ReservationRepository reservationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending Message 'NegativeResponse'");
        String email = delegateExecution.getVariable("email").toString();
        Long experienceId = Long.parseLong(delegateExecution.getVariable("experienceId").toString());
        LocalDate date = LocalDate.parse(delegateExecution.getVariable("date").toString(), formatter);

        Optional<Experience> experienceOptional = this.experienceRepository.findById(experienceId);
        if (experienceOptional.isPresent()) {
            String startTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(experienceOptional.get().getStartTime());
            String detail = experienceOptional.get().getName() + " (" + experienceOptional.get().getLocation().getInfoString() + "), on "
                    + date.format(formatter) + " starting at " + startTime;

            try {
                JavaMailUtil.sendMail(email, detail, "Guide Cancellation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("NegativeResponse")
                .correlate();
    }
}
