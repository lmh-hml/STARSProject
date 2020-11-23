package stars;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * A class that serves as intermediary between the databases that contains user and student information, and programs that access
 * the data in those databases.
 * Each user and student information object can be accessed by using their usernames: i.getUser( username ), getStudent( username )
 * Methods provided by this class are capable of accessing and modifying databases' contents.
 * @author Work
 *
 */
public class StarsDatabase {
	
	/**String used to store the name of the file that stores student information.**/
	private String studentFile = "";
	/**String used to store the name of the file that stores user information.**/
	private String userFile = "";
	
	/**
	 * Member UserDatabase object
	 */
	private UserDatabase userDatabase;
	/**
	 * Member StudentDatabase object.
	 */
	private StudentDatabase studentDatabase;
	
	public StarsDatabase(String studentFile, String userFile) {
		try 
		{
			userDatabase = new UserDatabase(userFile);
			studentDatabase = new StudentDatabase(studentFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.studentFile = studentFile;
		this.userFile = userFile;
	}	

	/**
	 * Adds a User_details object to the database.
	 * @param user User_details object to be added
	 */
	void addUser(User_details user)
	{
		userDatabase.add(user.getUsername(),user);
	}	
	/**
	 * Gets the user in the database associated with the username.
	 * @param username Username of the user to be retrieved.
	 * @return The User_detail object associated with the username
	 */
	User_details getUser(String username)
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
	/**
	 * Removes user with the specified id from the database.
	 * @param id Id of specified user.
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
		studentDatabase.add(student.getUserName(),student);
	}	
	/**
	 * Gets a student by their id
	 * @param ID of the student.
	 * @return The Student with the id, null if no such student with ID exists
	 */
	Student_details getStudentbyMatric(String matricNum)
	{
		Student_details student = null;
		for(Student_details item : studentDatabase.getContents())
		{
			if(item.getMatric_num().equals(matricNum))student = item;
		}
		return student;
	}
	/**
	 * Get the student details associated with a user
	 * THis method returns null if no such student_details object is found.
	 * @param user The user_details to be looked up
	 * @return The student_details object associated with the user-details object.
	 */
	Student_details getStudent(User_details user)
	{
		Student_details student = studentDatabase.get(user.getUsername());
		return student;
	}
	/**
	 * Gets a collection view of all the students in this database.
	 * @return A collection view of all the student_details objects stored in this database.
	 */
	Collection<Student_details> getAllStudents()
	{
		return this.studentDatabase.getContents();
	}
	/**
	 * Retrieves a student's details associated with the email.
	 * @param email Email of the student 
	 * @return The Student_details object associated with the email. Null if no such object is found.
	 */
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
	/**
	 * Get the student associated with the username.
	 * @param username username of the student
	 * @return The Student_details object associated with the username. Null if no suh object is found.
	 */
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
	/**Returns the email of the student.**/
	String getStudentEmail(Student_details student)
	{
		return this.getUser(student.getUserName()).getEmail();
	}
	/**
	 * Removes a student with the id from the database.
	 * @param id The id of the Student_details object to be removed.
	 */
	void removeStudent(String id)
	{
		studentDatabase.remove(id);
	}
	/**
	 * Updates the database with the current contents of this instance of StarsDatabase.
	 */
	void save()
	{
		try {
			studentDatabase.writeFile(studentFile);
			userDatabase.writeFile(userFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	


}
