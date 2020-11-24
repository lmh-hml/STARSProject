package stars;

/**Enum used to select the appropriate type of notifer ot receive from getNotificationMethod**/
enum StarsNotificationType { Email };
/**
 * Interface implemented by notification service provider classes used in the MySTARS program system.
 * Any user class in the MySTARS psystem should be dependent on this interface and call getNotificationMethod with the 
 * appropriate StarsNotificationType to get the correct StarsNotifier implementation.
 * @author Lai Ming Hui
 * @since 11/11/2020
 * @version 1.0.0
 *
 */
public interface StarsNotifier{
	
	/**Returns an implementation of StarsNotifier based on the specified notification type.
	 * If the type passed in is currently unsupported, the method returns null.
	 * @param type The specified type. Currently only Email is supported.
	 * @return The corresponding implementation of StarsNotifier based on the specified type, of null if the type is not
	 * supported.
	 */
	public static StarsNotifier getNotifificationMethod(StarsNotificationType type)
	{
		switch(type)
		{
		case Email:{ return new StarsMail();}
		default:
		{
			System.err.println("Unsupported notification type passed as parameter.");
		}
		}
		return null;
	}
	
	/**
	 * Sends a notification with the specified subject and message to the specified mainRecipient and any additional recipients in otherRecipients.
	 * @param subject The subject text
	 * @param messageText The message text
	 * @param mainRecipient The address of the main recipient
	 * @param otherRecipients The addresses of any other additional recipients.
	 */
	public void sendNotification(String subject, String messageText, String mainRecipient, String ...otherRecipients);
}
