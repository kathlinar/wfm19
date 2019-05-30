package wfm.group3.localy;

//import java.util.HashMap;
//import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {
	
	public static void sendMail(String recipient, String purpose) throws Exception {
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

		switch(purpose) {
			case "User Confirmation":
                //User makes successful reservation and receives confirmation
				message = prepareUserConfirmation(session, myAccountEmail, recipient);
				break;
			case "User Cancellation":
				//User cancels the experience and receives confirmation
				message = prepareUserCancellationConfirmation(session, myAccountEmail, recipient);
				break;
			case "Guide Cancellation":
				//Guide cancels the experience, user receives cancellation notice
                message = prepareUserCancellation(session, myAccountEmail, recipient);
                break;
		}

		Transport.send(message);

	}
	
	private static Message prepareMessage(Session session, String myAccountEmail, String recipient) {
		Message message = new MimeMessage(session);
		return message;
	}

	private static Message prepareUserConfirmation(Session session, String myAccountEmail, String recipient) {

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Your local.ly experience(s)!");
			String emailContent = "<h2>Thank you for reserving local.ly experiences</h2><br><p>Your local guides are " +
					"excited to meet you. Here's your requested experience(s):</p>" + listExperiences()+ "<br> " +
					"<p>See you!</p></br><p>local.ly</p>";
			message.setContent(emailContent, "text/html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

    private static Message prepareUserCancellation(Session session, String myAccountEmail, String
            recipient) {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("We're sorry ... ");
            String emailContent = "<p>Thank you for reserving experiences with local.ly. Unfortunately, we had to " +
                    "cancel your reservation for" + listExperiences() + "<br> " +
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
            recipient) {

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Your local.ly cancellation");
			String emailContent = "<p>Thank you for letting us know that you won't make it. We confirm your " +
					"cancellation for your experience(s):" +
					 listExperiences() + "<br> " +
					"<p>You can always look for other experiences on local.ly.</p><br><p>See you " +
					"next time!</p><br><p>local.ly</p>";
			message.setContent(emailContent, "text/html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

    private static String listExperiences(){
        String experienceList = "<ul>";
        String[] experiences = {"Experience 1", "Experience 2", "Experience 3"};
        for (String experience : experiences) {
            experienceList += "<li>" + experience + "</li>";
        }

        return experienceList;
    }

}
