package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.services.PersonService;
import wfm.group3.localy.services.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component("checkRequestDelegate")
public class CheckRequestDelegate implements JavaDelegate {

    @Autowired
    private PersonService personService;

    @Autowired
    private ReservationService reservationService;

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        boolean groupFull = false;
        boolean overlap = false;

        String email = delegateExecution.getVariable("email").toString();
        long experienceId = Long.valueOf(delegateExecution.getVariable("experienceId").toString());
        LocalDate date = LocalDate.parse(delegateExecution.getVariable("date").toString(), formatter);

        overlap = this.personService.hasOverlap(email, date, experienceId);
        if (!overlap) {
            groupFull = this.reservationService.isGroupFull(experienceId, date);
        }

        delegateExecution.setVariable("overlap", overlap);
        delegateExecution.setVariable("groupFull", groupFull);
    }
}
