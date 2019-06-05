package wfm.group3.localy.utils;

import org.quartz.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class FeedbackReminder implements Job {

    private String recipient ="";
    private String detail = "";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDetail job = jobExecutionContext.getJobDetail();
        JobDataMap map = job.getJobDataMap();

        this.recipient = map.getString("email");
        this.detail = map.getString("detail");

        try {
            sendFeedbackMail(recipient,detail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendFeedbackMail(String recipient, String detail) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.gmail", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.user", "locally.wfm@gmail.com");
        properties.put("mail.smtp.password", "group3locally");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");


        String myAccountEmail = "locally.wfm@gmail.com";
        String password = "group3locally";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });


        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Did you like your experience at " + detail + "?");
            String emailContent =
                    "<h2>How did you like your local.ly experience?</h2><br><p>We'd love if you could give us " +
                            "some feedback about </p>" + detail + "!<br> " +
                    "<p>Thank you!</p></br><p>local.ly</p>";

            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Transport.send(message);

    }


}
