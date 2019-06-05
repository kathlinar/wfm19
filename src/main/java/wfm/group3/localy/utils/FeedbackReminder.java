package wfm.group3.localy.utils;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import wfm.group3.localy.services.ScheduleService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class FeedbackReminder implements Job {

    @Autowired
    private ScheduleService scheduleService;

    private String recipient ="";
    private String detail = "";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDetail job = jobExecutionContext.getJobDetail();
        JobDataMap map = job.getJobDataMap();

        this.recipient = map.getString("email");
        this.detail = map.getString("detail");

        try {
            scheduleService.sendFeedbackMail(recipient,detail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
