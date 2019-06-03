package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Experience;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.Enums;
import wfm.group3.localy.utils.JavaMailUtil;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class SendConfirmationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(SendConfirmationDelegate.class.getName());

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending Message 'PositiveResponse'");

        String email = delegateExecution.getVariable("email").toString();
        Long reservationId = Long.parseLong(delegateExecution.getVariable("reservationId").toString());

        Optional<Reservation> reservationOptional = this.reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            reservationOptional.get().setStatus(Enums.ReservationStatus.CONFIRMED);
            Optional<Experience> experienceOptional = this.experienceRepository.findById(reservationOptional.get().getExperienceId());
            if (experienceOptional.isPresent()) {
                String detail = experienceOptional.get().getName() + ", on " + reservationOptional.get().getReservationDate().format(formatter);

                try {
                    JavaMailUtil.sendMail(email, detail, "User Confirmation");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("PositiveResponse")
                .correlate();
    }
}
