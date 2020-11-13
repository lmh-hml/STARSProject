package stars;
import java.awt.desktop.UserSessionEvent;
import java.io.Console;
import java.io.IOException;
import java.util.UUID;

public class StarsMain {
	
	Console console = System.console();
	UserDatabase users;
	UserDatabase students;
	
	StarsMain() throws IOException
	{
		users = new UserDatabase("Users.txt");
		students = new UserDatabase("Student.txt");
		
	}

	
	User logIn()
	{
		console.printf("Welcome to STARS.\n");
		
		boolean username_correct = false;
		User user = null;
		
		while(username_correct == false )
		{
			System.out.println("Please enter your username: ");
			String input_username = console.readLine();
			user = users.get(input_username);
			if(user == null)
			{
				System.out.println("Username is incorrect or does not exist.");
			}
			else
			{
				username_correct = true;
			}
		}
		
		boolean pwd_correct = false;
		while(pwd_correct == false)
		{
			console.printf("Please enter password: ");
			String input_pwd = String.valueOf(console.readPassword());
			pwd_correct = PasswordMaker.verifyPassword(input_pwd, user.getPassword());
			if( ! pwd_correct)
			{
				System.out.println("Password is incorrect. ");
			}
			else
			{
				pwd_correct = true;
			}
		}
		
		return user;
	}

	void logOut()
	{
		System.out.println("Logging off...");
	}
	
	public static void main(String args[])
	{
	
		try {
			StarsMain stars = new StarsMain();
			User user = stars.logIn();
			if(user == null)
			{
				System.out.println("Unable to find user account");
			}
			else
			{

				switch(user.getAccountType())
				{
				
				case "Student":
				{
					System.out.println("User is student");
				}break;

				case "Admin":
				{
					System.out.println("User is admin");
				}break;


				default:
				{
					System.out.println(user.getAccountType());
				}
				
				}//end switch

			}
			
			
			stars.logOut();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
