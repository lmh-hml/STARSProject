package stars;
import java.awt.desktop.UserSessionEvent;
import java.io.Console;
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
	
	
	Console console = System.console();
	Scanner scanner = new Scanner(System.in);
	
	StarsDatabase starsDatabase = new StarsDatabase();
	CourseManager courseManager = new CourseManager();
	StarsNotifier notifier;
	
	AdminProgram adminProgram = new AdminProgram( starsDatabase);	
	StudentProgram studentProgram = new StudentProgram(courseManager, starsDatabase, notifier);

	
	
	
	LocalDate accessPeriodStart = LocalDate.of(2020, 12, 1);
	LocalDate accessPeriodEnd = LocalDate.of(2020, 12, 31);;
	LocalTime access_time_start = LocalTime.of(9, 0);
	LocalTime access_time_end = LocalTime.of(23,  59);
	
	

	
	StarsMain() 
	{
	}

	
	public User_details logIn()
	{
		System.out.println("Welcome to STARS.\n");
		
		User_details user = null;
		
		while(user == null )
		{
			System.out.println("Please enter your username: ");
			String input_username = scanner.nextLine();
			user = starsDatabase.getUserByUsername(input_username);
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
			pwd_correct = PasswordMaker.verifyPassword(input_pwd, user.getPassword());
			if( ! pwd_correct)
			{
				System.out.println("Password is incorrect. ");
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
	
	
	public void run() throws IdNotFoundException
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
				studentProgram.run(student);
				
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
	
		try {
			StarsMain stars = new StarsMain();
			stars.run();
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}
	}

}
