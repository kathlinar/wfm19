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

            String emailContent = "<html>\n" +
                    "<head>\n" +
                    "\t<title>Thank you for reserving a local.ly experience</title>\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "</head>\n" +
                    "<body aria-readonly=\"false\"><strong><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Thank you for reserving experiences with local.ly.&nbsp;</span></span></strong>\n" +
                    "<p><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica," +
                    "sans-serif\">Unfortunately, we had to cancel your reservation for <strong " +
                    "style=\"font-size:12px\"><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, " +
                    "34)\">"+detail+" </span></strong> because you would </span></span><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">have an overlap with another already confirmed experience. You can always look for other experiences on local.ly.</span></span></p>\n" +
                    "\n" +
                    "<pre>\n" +
                    "<span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Hope to see you at another experience!</span></span></pre>\n" +
                    "<span style=\"font-size:12px\"><strong style=\"font-size:12px\"><span style=\"color:#800080\"><span style=\"font-family:courier new,courier,monospace\">local.ly</span></span></strong></span></body>\n" +
                    "</html>\n";
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

            String emailContent = "<html>\n" +
                    "<head>\n" +
                    "\t<title>Thank you for reserving a local.ly experience</title>\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "</head>\n" +
                    "<body aria-readonly=\"false\"><strong><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Thank you for reserving experiences with local.ly.&nbsp;</span></span></strong>\n" +
                    "<p><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica," +
                    "sans-serif\">Unfortunately, we had to cancel your reservation for <strong><span " +
                    "style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34)\">"+ detail+" </span></strong" +
                    "> because the experience group for the booked date is already full</span></span><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">. You can always look for other experiences on local.ly.</span></span></p>\n" +
                    "\n" +
                    "<pre>\n" +
                    "<span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Hope to see you at another experience!</span></span></pre>\n" +
                    "<span style=\"font-size:12px\"><strong><span style=\"color:#800080\"><span style=\"font-family:courier new,courier,monospace\">local.ly</span></span></strong></span></body>\n" +
                    "</html>\n";

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

            String emailContent = "<html>\n" +
                    "<head>\n" +
                    "\t<title>Thank you for reserving a local.ly experience</title>\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "</head>\n" +
                    "<body aria-readonly=\"false\">\n" +
                    "<h3><span style=\"font-family:arial,helvetica,sans-serif\">Thank you for reserving a&nbsp;</span><span style=\"color:#800080\"><span style=\"font-family:courier new,courier,monospace\">local.ly</span></span><span style=\"font-family:arial,helvetica,sans-serif\"><span style=\"color:#800080\">&nbsp;</span>experience</span></h3>\n" +
                    "<span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\"><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34)\">Your local guides are excited to meet you. Here&#39;s your requested experience:</span></span></span>\n" +
                    "\n" +
                    "<ul>\n" +
                    "\t<li><span style=\"font-size:12px\"><strong><span style=\"font-family:arial,helvetica," +
                    "sans-serif\"><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34)\">"+detail+"</span></span></strong></span></li>\n" +
                    "</ul>\n" +
                    "\n" +
                    "<p><span style=\"font-size:12px\"><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34); font-family:arial,helvetica,sans-serif\">See you!</span></span></p>\n" +
                    "<span style=\"font-size:12px\"> <strong><span style=\"color:#800080\"><span style=\"font-family:courier new,courier,monospace\">local.ly</span></span></strong></span></body>\n" +
                    "</html>\n";
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
            String emailContent = "<html>\n" +
                    "<head>\n" +
                    "\t<title>Thank you for reserving a local.ly experience</title>\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "</head>\n" +
                    "<body aria-readonly=\"false\"><strong><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Thank you for reserving experiences with local.ly.&nbsp;</span></span></strong>\n" +
                    "<p><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica," +
                    "sans-serif\">Unfortunately, we had to cancel your reservation for <strong><span " +
                    "style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34)\">"+detail+"</span></strong" +
                    "></span></span><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">. You can always look for other experiences on local.ly.</span></span></p>\n" +
                    "\n" +
                    "<pre>\n" +
                    "<span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Hope to see you at another experience!</span></span></pre>\n" +
                    "<span style=\"font-size:12px\"><strong><span style=\"color:#800080\"><span style=\"font-family:courier new,courier,monospace\">local.ly</span></span></strong></span></body>\n" +
                    "</html>\n";

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
            String emailContent = "<html>\n" +
                    "<head>\n" +
                    "\t<title>Thank you for reserving a local.ly experience</title>\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "\t<link href=\"https://svc.webspellchecker.net/spellcheck31/lf/scayt3/ckscayt/css/wsc.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                    "</head>\n" +
                    "<body aria-readonly=\"false\"><strong><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">Thank you for letting us know you can&#39;t make it.&nbsp;</span></span></strong>\n" +
                    "<p><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">We confirm your cancellation of the experience:</span></span></p>\n" +
                    "\n" +
                    "<ul>\n" +
                    "\t<li><span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica," +
                    "sans-serif\"><strong><span style=\"background-color:rgb(255, 255, 255); color:rgb(34, 34, 34)" +
                    "\">"+detail+"</span></strong></span></span><span style=\"font-size:12px\"><span " +
                    "style=\"font-family:arial,helvetica,sans-serif\">. </span></span></li>\n" +
                    "</ul>\n" +
                    "\u200B<span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">You can always look for other experiences on local.ly.</span></span>\n" +
                    "\n" +
                    "<pre>\n" +
                    "<span style=\"font-size:12px\"><span style=\"font-family:arial,helvetica,sans-serif\">See you next time!</span></span></pre>\n" +
                    "<span style=\"font-size:12px\"><strong><span style=\"color:#800080\"><span style=\"font-family:courier new,courier,monospace\">local.ly</span></span></strong></span></body>\n" +
                    "</html>\n";

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
