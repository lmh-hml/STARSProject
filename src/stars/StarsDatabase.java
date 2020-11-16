package stars;

import java.io.IOException;
import java.util.Collection;

public class StarsDatabase {
	
	/**
	 * Default filename of flat file for students 
	 */
	private static String studentFile = "Students.txt";
	/**
	 * Default filename of flat file for users
	 */
	private static String userFile = "Users.txt";
	
	/**
	 * Member UserDatabase object
	 */
	private UserDatabase userDatabase;
	/**
	 * Member StudentDatabase object.
	 */
	private StudentDatabase studentDatabase;

	public StarsDatabase() {
		try {
			userDatabase = new UserDatabase(userFile);
			studentDatabase = new StudentDatabase(studentFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Adds a User_details object to the database.
	 * @param user User_details object to be added
	 */
	void addUser(User_details user)
	{
		userDatabase.add(user);
	}
	
	/**
	 * Gets a user from the database by its id.
	 * @param id The user's id.
	 * @return The User_details object that associated with the id.
	 */
	User_details getUser(String id)
	{
		return userDatabase.get(id);
	}
	
	/**
	 * Gets the user in the database associated with the username.
	 * @param username Username of the user to be retrieved.
	 * @return The User_detail object associated with the username
	 */
	User_details getUserByUsername(String username)
	{
		User_details user = null;
		for (User_details u  : userDatabase.getContents())
		{
			if(u.getUsername().equals(username) )
			{
				user = u;
				break;
			}
		}
		return user;
	}
	
	/**
	 * Gets all the users stored in the database.
	 * @return A collection view of all the users registered in the database.
	 */
	Collection<User_details> getAllUsers()
	{
		return this.userDatabase.getContents();
	}
	
	//TODO: Remove after updating the programs to use StarsDatabase only
	UserDatabase getUserDatabase()
	{
		return userDatabase;
	}
	
	
	/**
	 * 
	 * @param id
	 */
	void removeUser(String id)
	{
		userDatabase.remove(id);
	}

	
	
	
	/**
	 * Adds an instance of Student_details object to the database
	 * @param student A Student_details object to be added to database.
	 */
	void addStudent(Student_details student)
	{
		studentDatabase.add(student);
	}
	
	/**
	 * Gets a student by their id
	 * @param ID of the student.
	 * @return The Student with the id, null if no such student with ID exists
	 */
	Student_details getStudent(String id)
	{
		return studentDatabase.get(id);
	}

	Student_details getStudent(User_details user)
	{
		Student_details student = studentDatabase.get(user.getId());
		return student;
	}
	
	Collection<Student_details> getAllStudents()
	{
		return this.studentDatabase.getContents();
	}
	
	StudentDatabase getStudentDatabase()
	{
		return studentDatabase;
	}
	
	Student_details getStudentByEmail(String email)
	{
		Student_details student = null;
		for (User_details user  : userDatabase.getContents())
		{
			if(user.getAccountType().equals("Student")&& user.getEmail().equals(email))
			{
				student = getStudent(user);
			}
		}
		return student;
	}
	
	Student_details getStudentByUsername(String username)
	{
		Student_details student = null;
		for (User_details user  : userDatabase.getContents())
		{
			if(user.getAccountType().equals("Student")&& user.getUsername().equals(username))
			{
				student = getStudent(user);
			}
		}
		return student;
	}
		
	void removeStudent(String id)
	{
		studentDatabase.remove(id);
	}
	
	void writeDatabaseFiles()
	{
		try {
			studentDatabase.writeFile(studentFile);
			userDatabase.writeFile(userFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void main(String args[])
	{
		StarsDatabase stars = new StarsDatabase();
		
		User_details user = stars.getUserByUsername("Thrish");
		System.out.println(user.toFlatFileString());
		
		
		
		for(Student_details std : stars.getAllStudents())
		{
			System.out.println(std.toFlatFileString());
		}
		
		for(User_details std : stars.getAllUsers())
		{
			System.out.println(std.getUsername());
		}
		
		
	
	}

}
