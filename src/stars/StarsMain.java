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
import stars.exception.*;

public class StarsMain {
	
	Console console = System.console();
	Scanner scanner = new Scanner(System.in);
	UserDatabase users;
	StudentDatabase students;
	
	
	LocalDate accessPeriodStart = LocalDate.of(2020, 12, 1);
	LocalDate accessPeriodEnd = LocalDate.of(2020, 12, 31);;
	LocalTime access_time_start = LocalTime.of(9, 0);
	LocalTime access_time_end = LocalTime.of(23,  59);
	
	
	private final String studentOptions = "1. Add course\n"
			+ "2. Drop course\n"
			+ "Check/Print Courses Registered\n"
			+ "4. Check vacancies available\n"
			+ "5. Change Index Number of course\n"
			+ "6. Swap index with another student\n"
			+ "7. Show options\n"
			+ "0. Quit";
	
	private final String adminOptions = "1.Edit student access period\n"
			+ "2.Add a student (name, matric number, gender, nationality, etc)\n"
			+ "3.Add/Update a course (course code, school, its index numbers and vacancy)."
			+ "Check available slot for an index number (vacancy in a class)\n"
			+ "5.Print student list by index number.\n"
			+ "6.Print student list by course [ print only student’s name, gender and nationality ]\n"
			+ "7. Show options\n"
			+ "0. Quit";
	
	
	StarsMain() throws IOException
	{
		users = new UserDatabase("Users.txt");
		students = new StudentDatabase("Students.txt");
	}

	
	public User_details logIn()
	{
		System.out.println("Welcome to STARS.\n");
		
		User_details user = null;
		
		while(user == null )
		{
			System.out.println("Please enter your username: ");
			String input_username = scanner.nextLine();
			user = users.get(input_username);
			if(user == null)
			{
				System.out.println("Username is incorrect or does not exist.");
				System.out.println(users.printContents());
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
	
	
	
	
	
	public Student_details getStudentDetails(User_details user) throws IdNotFoundException
	{
		Student_details ret =  students.getByID(user.getId());
		if(ret == null) throw new IdNotFoundException("Unable to find student details assocated with this user %s account!", user.getUsername());
		return ret;
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
					Student_details student = stars.getStudentDetails(user);
					System.out.format("Welcome to STARS Students, %s !\n", student.getName());
					System.out.print(stars.studentOptions);
					System.out.format("Please enter an option: ");

				   
					boolean loopInput = false;
					boolean quit = false;
					while( loopInput || !quit )
					{
						
						System.out.format("Please enter an option: ");
						int input  = -1;
						try {  input = stars.scanner.nextInt(); } 
						catch(InputMismatchException e) {System.out.println("Please enter a proper input"); stars.scanner.nextLine();}
						
						switch( input )
						{
						
						case 1: {}break;
						
						case 2: {}break;
						
						case 3: {}break;
						
						case 4: {}break;
						
						case 5: {}break;
						
						case 6: {} break;
						
						case 7: {System.out.print(stars.studentOptions);}
						
						case 0: { quit = true; } break;
						
						default: 
						{ 
							System.out.println("Please enter a number among options provided.");
							loopInput = true;
						}
						}//end switch
						if(quit)break;
					}
					
					
					
			
				}break;

				case "Admin":
				{
					System.out.format("Welcome to STARS Admin, %s !\n", user.getUsername());
					System.out.print(stars.adminOptions);
					System.out.format("Please enter an option: ");
					
					boolean loopInput = false;
					boolean quit = false;
					System.out.format("Please enter an option: ");
					int input  = -1;
					try {  input = stars.scanner.nextInt(); } 
					catch(InputMismatchException e) {System.out.println("Please enter a proper input"); stars.scanner.nextLine();}
					
					switch( input )
					{
					
					case 1: {}break;
					
					case 2: {}break;
					
					case 3: {}break;
					
					case 4: {}break;
					
					case 5: {}break;
					
					case 6: {} break;
					
					case 7: {System.out.print(stars.adminOptions);}
					
					case 0: { quit = true; } break;
					
					default: 
					{ 
						System.out.println("Please enter a number among options provided.");
						loopInput = true;
					}
					}//end switch
					if(quit)break;
				}break;


				default:
				{
					System.out.println(user.getAccountType());
				}
				
				}//end switch

			}
			
			
			stars.logOut();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}
	}

}
