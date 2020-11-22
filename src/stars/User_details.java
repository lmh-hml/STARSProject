package stars;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that holds details regarding a user's account 
 * A user can be either a student or an admin.
 * in the Stars system.
 * Users are identified by their usernames, which must be unique.
 * @author Lai Ming Hui
 * @version 1.0
 * @since 2020/11/14
 *
 */
public class User_details implements FlatFileObject 
{

	/**Username of this user **/
	private String username = EmptyString;
	
	/**Hashed password of this user **/
	private String password = EmptyString;
	
	/**Email of this user **/
	private String email = EmptyString;
	
	/**Account type of this user **/
	private String accountType= EmptyString;

	/**Number of fields in this class that should be read/written to flat file**/
	private static final int NumFields= 4;
	
	/*
	 * Base constructor.
	 */
	public User_details() {}

	/**
	 * Serialize this object's fields in flat file format representation.
	 * @return The flat file representation of this object.
	 */
	public String toFlatFileString() {
		return FlatFileObject.buildFlatFileString(username, password, email, accountType) ;
	}
	

	/**
	 * Initializes this object with a string read from a flat file.
	 * This method does nothing if there are not enough columns in the flat file string to initialize this object.
	 */
	public boolean fromFlatFileString(String s) {
		ArrayList<String> array = new ArrayList<String>( Arrays.asList(s.split("\\|")) );
		if(array.size()<NumFields)return false;
		this.username = array.get(0);
		this.password = array.get(1);
		this.email = array.get(2);
		this.accountType = array.get(3);
		return true;
	}

//	/**
//	 * @return Returns a list of strings that are the names of this class' fields.
//	 */
//	public static List<String> getFields()
//	{
//		ArrayList<String> list = new ArrayList<String>();
//		for(Field f : User_details.class.getDeclaredFields())
//		{
//			list.add(f.getName());
//		}
//		return list;
//	}


	//GETTERS AND SETTERS
	/**@return Return this user's username, nil if not initialized.*/
	public String getUsername() {
		return username;
	}
	
	/**@param username Set this user's username*/
	public void setUsername(String username) {
		this.username = username;
	}

	/**@return Return this user's hashed password*/
	public String getPassword() {
		return password;
	}

	/**@param Set this user's password. Before setting, a plaintext password should be hashed.*/
	public void setPassword(String password) {
		this.password = password;
	}

	/**@return Return this user's email*/
	public String getEmail() {
		return email;
	}

	/**@return Set this user's email*/
	public void setEmail(String email) {
		this.email = email;
	}

	/**@return Return this user's account type*/
	public String getAccountType() {
		return accountType;
	}

	/**@return Set this user's account type, either "student" or "admin"*/
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public String getDatabaseId() {
		return this.username;
	}

}