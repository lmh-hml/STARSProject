package stars;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDatabase extends FlatFileDatabase<String, User_details>{

	public UserDatabase() {}
	public UserDatabase(String filename) throws IOException {
		openFile(filename);
	}


	@Override
	public void add(User_details obj) {
		hashmap.put(obj.getUsername(), obj);
	}
	@Override
	public User_details parseLine(String line) {		
		User_details user = new User_details();
		user.fromFlatFileString(line);
		return user;
	}

}
