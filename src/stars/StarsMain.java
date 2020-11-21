package stars;
import java.awt.desktop.UserSessionEvent;
import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

import javax.mail.MessagingException;

import stars.exception.*;

public class StarsMain {
	
	private final String SettingsFile = "Settings.txt";
	private Console console = System.console();
	private Scanner scanner = new Scanner(System.in);
	private StarsDatabase starsDatabase = new StarsDatabase();
	private CourseManager courseManager = new CourseManager();
	private StarsNotifier notifier = new StarsMail();
	private AdminProgram adminProgram = new AdminProgram( starsDatabase);	
	private StudentProgram studentProgram = new StudentProgram(courseManager, starsDatabase, notifier);	
	private LocalDate accessPeriodStart = null;
	private LocalDate accessPeriodEnd = null;
	private LocalTime accessTimeStart = null;
	private LocalTime accessTimeEnd = null;

	/**Default constructor of this class**/
	public StarsMain() 
	{
		openSettingsFile(SettingsFile);
	}
	
	public User_details logIn()
	{
		System.out.println("Welcome to STARS.\n");
		
		User_details user = null;
		
		while(user == null )
		{
			System.out.println("Please enter your username: ");
			String input_username = scanner.nextLine();
			user = starsDatabase.getUser(input_username);
			if(user == null)
			{
				System.out.println("Username is incorrect or does not exist.");
			}
		}
		
		boolean pwd_correct = false;
		while(pwd_correct == false)
		{
//			console.printf("Please enter password: ");
//			String input_pwd = String.valueOf(console.readPassword());
			System.out.println("Please enter password: ");
			String input_pwd = scanner.nextLine();
			pwd_correct = PasswordModule.verifyPassword(input_pwd, user.getPassword());
			if( ! pwd_correct)
			{
				System.out.println("Password is incorrect. ");
			}
		}
		
		return user;
	}
	public void logOut()
	{
		System.out.println("Logging off...");
	}	
	private void openSettingsFile(String settingsFile)
	{
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(settingsFile));
			String line = "";
			while((line = bfr.readLine())!= null)
			{
				String[] array = line.split("\\|");
				if(array[0].equals("AccessPeriod"))
				{
					accessPeriodStart = LocalDate.parse(array[1]);
					accessTimeStart = LocalTime.parse(array[2]);
					accessPeriodEnd = LocalDate.parse(array[3]);
					accessTimeStart = LocalTime.parse(array[4]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	public void run() 
	{
		User_details user = logIn();			
		
		
		if(user == null)
		{
			System.err.println("Unable to find user account");
		}
		else
		{
			
			switch(user.getAccountType())
			{
			
			case "Student":
			{
				Student_details student = starsDatabase.getStudent(user);
				if(student == null)
				{
					System.err.println("Unable to find student account for this user. Please approach admin for assistance.");
				}
				else if(LocalDate.now().isBefore(accessPeriodStart) || LocalDate.now().isAfter(accessPeriodEnd))
				{
					System.out.format("Unable to access STARS outside of access period from %s to %s !\n", accessPeriodStart, accessPeriodEnd);
				}
				else
				{
					studentProgram.run(student);
				}
				
			}break;

			case "Admin":
			{
				adminProgram.run(user);	
			}break;


			default:
			{
				System.out.println(user.getAccountType());
			}
			
			}//end switch

		}
		
//		notifier = StarsNotifier.getNotificationMethod("Email");
//		notifier.setRecipient("laim0012@e.ntu.edu.sg");
//		notifier.sendNotification("Hey", "Test");
//		System.out.println("Notification sent!");
		logOut();
		starsDatabase.writeDatabaseFiles();
		System.out.println("Goodbye!");

		
	}
	
	
	public static void main(String args[])
	{

		StarsMain stars = new StarsMain();
		stars.run();

	}

}
