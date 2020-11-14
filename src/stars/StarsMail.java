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

public class StarsMail implements StarsNotifier{
	
	
	private String username = "StarsNotifier2020@gmail.com";
	private String password = "NotifierStars123";
	private String senderEmail ="StarsNotifier2020@gmail.com";
	private String recipient = "";
	
	private Properties props;
	private Session session;
	
	public StarsMail() {		
		initializeProperties();
		initSession();
	}
	
	public StarsMail(String username, String password)
	{
		this.username = username;
		this.password = password;
		initializeProperties();
		initSession();
	}
	


	public void sendMessage(String recipientAddress, String subject, String messageText) throws AddressException, MessagingException
	{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(senderEmail));
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(recipientAddress)); // to be added an email addr
		message.setSubject(subject);
		message.setText(messageText);
		Transport.send(message);
	}
		
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public  String getUsername() {
		return username;
	}

	public  String getPassword() {
		return password;
	}

	public  String getSenderemail() {
		return senderEmail;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void initializeProperties()
	{
		this.props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
	}
	
	private void initSession()
	{
		this.session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
	}

	
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	
	
	@Override
	public void sendNotification(String subject, String message) {
		
		try {
			sendMessage(this.recipient, subject, message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setRecipient(User_details user) {
		this.setRecipient(user.getEmail());
	}



	
}
