package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.services.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Component
public class MakeReservationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(MakeReservationDelegate.class.getName());

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    private ReservationService reservationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Save Reservation to DB");

        String email = delegateExecution.getVariable("email").toString();
        String processDefinitionId = delegateExecution.getVariable("processDefinitionId").toString();
        Long experienceId = Long.parseLong(delegateExecution.getVariable("experienceId").toString());
        LocalDate date = LocalDate.parse(delegateExecution.getVariable("date").toString(), formatter);

        Long reservationId = this.reservationService.makeReservation(experienceId, email, date, processDefinitionId);

        delegateExecution.setVariable("reservationId", reservationId);
    }
}
