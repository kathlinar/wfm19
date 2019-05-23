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
	
	public static void sendMail(String recipient/*, String purpose*/) throws Exception {
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
		
		Message message = prepareMessage(session, myAccountEmail, recipient/*, purpose*/);
		
		Transport.send(message);
		
	}
	
	private static Message prepareMessage(Session session, String myAccountEmail, String recipient/*, String purpose*/) {
		//TODO: Adjust content of E-Mail depending on purpose
		/*Map<String, String> purposeContent = new HashMap();
		purposeContent.put("userConfirmation","");
		purposeContent.put("userCancellation","");
		purposeContent.put("guideConf", "");
		purposeContent.put("guideCancellation", "");
		purposeContent.put("userFeedback", "");*/
		
		
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Test subject");
			message.setText("This is a test email");
			//message.setContent(purposeContent.get(purpose), "text/html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

}
