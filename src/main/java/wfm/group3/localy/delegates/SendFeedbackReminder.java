package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.utils.FeedbackReminder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class SendFeedbackReminder implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(SendAttendanceDelegate.class.getName());

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'SendFeedbackReminder'");

        String email = delegateExecution.getVariable("email").toString();
        long reservationId = Long.valueOf(delegateExecution.getVariable("reservationId").toString());
        String processInstanceId = delegateExecution.getVariable("processInstanceId").toString();

        Reservation reservation = reservationRepository.findReservationId(reservationId);

        /*Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        Trigger trigger;

        JobDataMap map = new JobDataMap();
        map.put("email",email);
        map.put("detail",experienceRepository.getOne(reservation.getExperienceId()).getName());

        JobDetail job = newJob(FeedbackReminder.class)
                .withIdentity("feedback-reminder")
                .setJobData(map)
                .storeDurably()
                .build();


        trigger = newTrigger().withIdentity("attendedEvent")
                .startAt(new Date(System.currentTimeMillis()+5*60*1000))
                .forJob(job)
                .build();

        scheduler.scheduleJob(job,trigger);*/

    }
}
