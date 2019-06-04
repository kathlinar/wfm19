package wfm.group3.localy.utils;

//import java.util.HashMap;
//import java.util.Map;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class JavaMailUtil {
    public static void sendMail(String recipient, String detail, Enums.MailPurpose purpose) throws Exception {
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

        Message message = prepareMessage(session, myAccountEmail, recipient);

        switch (purpose) {
            case USER_CONFIRMATION:
                //User makes successful reservation and receives confirmation
                message = prepareUserConfirmation(session, myAccountEmail, recipient, detail);
                break;
            case USER_CANCELLATION:
                //User cancels the experience and receives confirmation
                message = prepareUserCancellationConfirmation(session, myAccountEmail, recipient, detail);
                break;
            case GUIDE_CANCELLATION:
                //Guide cancels the experience, user receives cancellation notice
                message = prepareUserCancellation(session, myAccountEmail, recipient, detail);
                break;
            case GROUP_FULL:
                //We cancel the reservation because the experience group size is reached, user receives cancellation notice
                message = prepareGroupFullCancellation(session, myAccountEmail, recipient, detail);
                break;
            case OVERLAP:
                //We cancel the reservation because the user would have an experience overlap, user receives cancellation notice
                message = prepareOverlapCancellation(session, myAccountEmail, recipient, detail);
                break;
            case GUIDE_CONFIRMATION:
                message = prepareGuideConfirmation(session, myAccountEmail, recipient, detail);
                break;
        }

        Transport.send(message);

    }

    private static Message prepareOverlapCancellation(Session session, String myAccountEmail, String recipient, String detail) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("We're sorry ... ");
            String emailContent = "<p>Thank you for reserving experiences with local.ly. Unfortunately, we had to " +
                    "cancel your reservation for <br>" + detail + "<br> because you would have an overlap with another already confirmed experience." +
                    "<p>You can always look for other experiences on local.ly.</p><br><p>Hope to see you at another " +
                    "experience!</p><br><p>local.ly</p>";
            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }

    private static Message prepareGroupFullCancellation(Session session, String myAccountEmail, String recipient, String detail) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("We're sorry ... ");
            String emailContent = "<p>Thank you for reserving experiences with local.ly. Unfortunately, we had to " +
                    "cancel your reservation for <br>" + detail + "<br> because the experience group for the booked date is already full." +
                    "<p>You can always look for other experiences on local.ly.</p><br><p>Hope to see you at another " +
                    "experience!</p><br><p>local.ly</p>";
            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient) {
        Message message = new MimeMessage(session);
        return message;
    }

    private static Message prepareUserConfirmation(Session session, String myAccountEmail, String recipient,
                                                   String detail) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Your local.ly experience!");
            String emailContent = "<h2>Thank you for reserving a local.ly experience</h2><br><p>Your local guides are " +
                    "excited to meet you. Here's your requested experience:</p>" + detail + "<br> " +
                    "<p>See you!</p></br><p>local.ly</p>";
            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }

    private static Message prepareUserCancellation(Session session, String myAccountEmail, String
            recipient, String detail) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("We're sorry ... ");
            String emailContent = "<p>Thank you for reserving experiences with local.ly. Unfortunately, we had to " +
                    "cancel your reservation for <br>" + detail + "<br> " +
                    "<p>You can always look for other experiences on local.ly.</p><br><p>Hope to see you at another " +
                    "experience!</p><br><p>local.ly</p>";
            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }


    private static Message prepareUserCancellationConfirmation(Session session, String myAccountEmail, String
            recipient, String detail) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Your local.ly cancellation");
            String emailContent = "<p>Thank you for letting us know that you won't make it. We confirm your " +
                    "cancellation for your experience:" +
                    detail + "<br> " +
                    "<p>You can always look for other experiences on local.ly.</p><br><p>See you " +
                    "next time!</p><br><p>local.ly</p>";
            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }

    private static Message prepareGuideConfirmation(Session session, String myAccountEmail, String
            recipient, String detail) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("WFM: New Reservation requests");
            String emailContent = "<p>" + detail + "</p>";
            message.setContent(emailContent, "text/html");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message;
    }

}
