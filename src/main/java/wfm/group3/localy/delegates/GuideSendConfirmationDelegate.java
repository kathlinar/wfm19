package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.Enums;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class GuideSendConfirmationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(GuideSendConfirmationDelegate.class.getName());

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending Message 'ReservationConfirmation'");

        String email = delegateExecution.getVariable("email").toString();
        Long reservationId = Long.valueOf(delegateExecution.getVariable("reservationId").toString());
        String instanceId = delegateExecution.getVariable("processInstanceId").toString();

        Optional<Reservation> reservationOptional = this.reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.setStatus(Enums.ReservationStatus.CONFIRMED);
            this.reservationRepository.save(reservation);
        }

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReservationConfirmation")
                .setVariable("reservationId", reservationId)
                .setVariable("email", email)
                .setVariable("processInstanceId",instanceId)
                .correlateAll();
    }
}
