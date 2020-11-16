package stars;

import java.io.IOException;

public class StarsDatabase {
	
	private static String studentFile = "Students.txt";
	private static String userFile = "Users.txt";
	
	private UserDatabase userDatabase;
	private StudentDatabase studentDatabase;

	public StarsDatabase() {
		try {
			userDatabase = new UserDatabase(userFile);
			studentDatabase = new StudentDatabase(studentFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	void addUser(User_details user)
	{
		userDatabase.add(user);
	}
	
	User_details getUser(String id)
	{
		return userDatabase.get(id);
	}
	
	void removeUser(String id)
	{
		userDatabase.remove(id);
	}
	
	
	void addStudent(Student_details student)
	{
		studentDatabase.add(student);
	}
	
	Student_details getStudent(String id)
	{
		return studentDatabase.get(id);
	}

	Student_details getStudent(User_details user)
	{
		Student_details student = studentDatabase.get(user.getId());
		return student;
	}
	
	Student_details getStudentByEmail(String email)
	{
		Student_details student = null;
		for (User_details user  : userDatabase.getContents())
		{
			if(user.getAccountType()=="student"&& user.getEmail() == email)
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
			if(user.getAccountType()=="student"&& user.getUsername() == username)
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
}
