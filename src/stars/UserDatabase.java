package stars;


import java.io.IOException;
/**
 * Database class that implements FlatFileDatabase that handles stores and handles User_details objects.
 * This class can be instantiated with a file that contains User_detials data in flat file format,
 * and is able to write its contents back to a file in the same format.
 * Users in this database are indexed by their usernames. 
 * @author Lai Ming Hui
 * @version 1.0
 * @since 2020/11/14
 */
public class UserDatabase extends FlatFileDatabase<User_details>{

	/**
	 * Default constructor.
	 */
	public UserDatabase() {}
	
	/**
	 * Instantiates a UserDatabase object with a flat file
	 * @param filename The file to be parsed.
	 * @throws IOException When te file with the filename could not be oepened.
	 */
	public UserDatabase(String filename) throws IOException {
		openFile(filename);
	}

	
	/**
	 * Adds a user object into this database.
	 * users are indexed by their usernames
	 * if User_details does not have a non-null username, this method will not add the object
	 * and returns.
	 * @param obj A User_details object
	 */
	@Override
	public void add(String key, User_details obj) {
		if( key == "" || key == null)return;
		hashmap.put(key, obj);
	}
	
	
	/**
	 * Parses a line in flat file format into a User_details object.
	 * @param line A flat file object.
	 */
	@Override
	public User_details parseLine(String line) {		
		User_details user = new User_details();
		user.fromFlatFileString(line);
		return user;
	}
	
}
