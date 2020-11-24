package stars;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Implementation of StarsNotifier that provides functionality of sending email through the MySTARS system.
 * @author Lai Ming Hui
 * @since 11/11/2020
 * @version 1.0.0
 */
public class StarsMail implements StarsNotifier{
	
	/**
	 * Username of the email account used to send notifications from.
	 */
	private String username = "StarsNotifier2020@gmail.com";
	/**
	 * Password of the email account used to send notifications from
	 */
	private String password = "NotifierStars123";
	/**
	 * The email used to send notifications from
	 */
	private String senderEmail ="StarsNotifier2020@gmail.com";
	
	/**Properties object used to configure email session.**/
	private Properties props;
	private Session session;
	
	/**
	 * Default constructor of this class.
	 */
	public StarsMail() {		
		initializeProperties();
		initSession();
	}
	

	/**
	 * Creates an instance of this object using the specified username, password and email.
	 * @param username The username of the account associated with specified email.
	 * @param password The password of the account associated with the specified email
	 * @param email The email that will be used to send the notifications from.
	 */
	private StarsMail(String username, String password, String email)
	{
		this.username = username;
		this.password = password;
		this.senderEmail = email;
		initializeProperties();
		initSession();
	}
	
/**
 * Gets the email used to send notifications from by this notifier.
 * @return The email set to send notifications from.
 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 * Initializes this email notifier.
	 */
	private void initializeProperties()
	{
		this.props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
	}
	
	/**
	 * Begins a session using the credentials and email set within this notifier
	 */
	private void initSession()
	{
		this.session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
	}
	
	/**
	 * Sends an email notification containing the specified subject text and message text to the main recipient. If there are any recipients provided
	 * in otherRecipinets, they will be in the CC list of the email.
	 * @param subject The subject text of the email.
	 * @param messageText The message to be sent in the email
	 * @param mainRecipient The email of the main receiver of the email. 
	 * @param otherRecipients Any recipient email passed to this parameter will be in the CC list of the email.
	 * */
	@Override
	public void sendNotification(String subject, String messageText, String mainRecipient, String ...otherRecipients) {
		
		try {
			sendMessage( subject, messageText, mainRecipient, otherRecipients);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends an email containing the specified subject text and message text to the main recipient. If there are any recipients provided
	 * in otherRecipinets, they will be in the CC list of the email.
	 * @param subject The subject text.
	 * @param messageText THe message text.
	 * @param mainRecipient Email of the main recipient of the email.
	 * @param otherRecipients Email to be put in the CC list of the email.
	 * @throws AddressException Thrown when there is an address exception
	 * @throws MessagingException Thrown when there is a messaging exception.
	 */
	private void sendMessage( String subject, String messageText, String mainRecipient, String ...otherRecipients) throws AddressException, MessagingException
	{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(senderEmail));
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(mainRecipient)); // to be added an email addr

		for(String userEmail : otherRecipients)
		{
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(userEmail));
		}
		
		message.setSubject(subject);
		message.setText(messageText);
		Transport.send(message);
		
	}	
}
