package wfm.group3.localy.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wfm.group3.localy.entity.Reservation;
import wfm.group3.localy.repository.ExperienceRepository;
import wfm.group3.localy.repository.ReservationRepository;
import wfm.group3.localy.services.ScheduleService;
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

    @Autowired
    private Scheduler scheduler;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info("Sending Message 'SendFeedbackReminder'");

        String email = delegateExecution.getVariable("email").toString();
        long reservationId = Long.valueOf(delegateExecution.getVariable("reservationId").toString());
        String processInstanceId = delegateExecution.getVariable("processInstanceId").toString();

        Reservation reservation = reservationRepository.findReservationId(reservationId);

        FeedbackReminder feedbackReminder;


        JobDataMap map = new JobDataMap();
        map.put("email",email);
        map.put("detail",experienceRepository.getOne(reservation.getExperienceId()).getName());

        JobDetail job = newJob(map);

        scheduler.scheduleJob(job,trigger(job, java.sql.Date.valueOf(reservation.getReservationDate())));
    }

    private JobDetail newJob(JobDataMap map) {
        return JobBuilder.newJob().ofType(FeedbackReminder.class).storeDurably()
                .setJobData(map)
                .build();
    }

    private Trigger trigger(JobDetail jobDetail, Date date) {

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        Date nDate = cal.getTime(); // returns new date object, one hour in the future

        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup())
                .startAt(nDate)
                .build();
    }
}
