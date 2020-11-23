package stars;

enum StarsNotificationType { Email };
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
	
	public void sendNotification(String subject, String messageText, String mainRecipient, String ...otherRecipients);
}
