package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.Enums;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class GuideSendCancellationDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(GuideSendCancellationDelegate.class.getName());

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Sending message ReservationCancellation");
        String email = delegateExecution.getVariable("email").toString();
        Long reservationId = Long.valueOf(delegateExecution.getVariable("reservationId").toString());
        String instanceId = delegateExecution.getVariable("processInstanceId").toString();

        Optional<Reservation> reservationOptional = this.reservationRepository.findById(reservationId);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            reservation.setStatus(Enums.ReservationStatus.CANCELLED);
            this.reservationRepository.save(reservation);
        }
        List<HistoricProcessInstance> instances = delegateExecution.getProcessEngineServices().getHistoryService().createHistoricProcessInstanceQuery()
                .processDefinitionKey("WebApplication").active().orderByProcessInstanceStartTime().asc().list();


        List<HistoricProcessInstance> instances1 = delegateExecution.getProcessEngineServices().getHistoryService().createHistoricProcessInstanceQuery()
                .processDefinitionKey("LocalGuide").active().orderByProcessInstanceStartTime().asc().list();

        int i;
        for(i=0;i<instances1.size();i++){
            if(delegateExecution.getProcessInstanceId().equals(instances1.get(i).getId())){
                break;
            }
        }

        delegateExecution.getProcessEngineServices().getRuntimeService()
                .createMessageCorrelation("ReservationCancellation")
                .setVariable("reservationId", reservationId)
                .setVariable("email", email)
                .setVariable("processInstanceId",instanceId)
                .processInstanceId(instances.get(i).getId())
                .correlate();
    }
}
