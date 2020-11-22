package stars;

public interface StarsNotifier{
	
	public void sendNotification(String subject, String messageText, User_details mainRecipient, User_details ...otherRecipients);
	public void setRecipient(String recipient);
	public void setRecipient(User_details user);
	public static StarsNotifier getNotificationMethod(String method)
	{
		StarsMail mail = new StarsMail();
		return mail;
	}
}
