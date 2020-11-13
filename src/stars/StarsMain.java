package stars;
import java.awt.desktop.UserSessionEvent;
import java.io.Console;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class StarsMain {
	
	Console console = System.console();
	UserDatabase users;
	StudentDatabase students;
	
	
	LocalDate accessPeriodStart = LocalDate.of(2020, 12, 1);
	LocalDate accessPeriodEnd = LocalDate.of(2020, 12, 31);;
	LocalTime access_time_start = LocalTime.of(9, 0);
	LocalTime access_time_end = LocalTime.of(23,  59);
	
	
	StarsMain() throws IOException
	{
		users = new UserDatabase("Users.txt");
	}

	
	User_details logIn()
	{
		console.printf("Welcome to STARS.\n");
		
		boolean username_correct = false;
		User_details user = null;
		
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
	
	
	public LocalDate[] getAccessPeriod()
	{
		return new LocalDate[] {accessPeriodStart, accessPeriodEnd };
	}
	
	public void setAccessPeriodStartDate(LocalDate date)
	{
		accessPeriodStart = date;
	}
	
	public void setAccessPeriodEndDate(LocalDate date)
	{
		accessPeriodEnd = date;
	}
	
	
	public static void main(String args[])
	{
	
		try {
			StarsMain stars = new StarsMain();
			User_details user = stars.logIn();
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
