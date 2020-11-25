package stars;
import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * MySTARS class containing the entry point of the program.
 * THis class represents an instance of a MySTARS program.
 * @author Lai Ming Hui
 * @version 1.0.0
 * @since 10/11/2020
 */
public class MySTARS{
	
	/**Default file name of the settings file.**/
	private final String SettingsFile = "Settings.txt";
	/**Console used for password input.**/
	private Console console = System.console();
	/**Scanner used for IO between user and program for non-password IO.**/
	private Scanner scanner = new Scanner(System.in);
	/**Member StarsDatabase object. Represents the database that stores users and students information in the system.**/
	private StarsDatabase starsDatabase = new StarsDatabase("Data/TestStudents.txt", "Data/TestUsers.txt");
	/**Member CourseManager object. Represents the database that stores courses and index information in the system.**/
	private CourseManager courseManager = new CourseManager("Data/TestCourses.txt","Data/TestIndexes.txt");
	/**Member AdminProgram. Control is transferred to this object if an admin logs into the system.**/
	private AdminProgram adminProgram = null;
	/**Member student program. Control is handed over to this object if a student logs into the program.**/
	private StudentProgram studentProgram = null;	
	/**Access period start date.**/
	private LocalDateTime accessPeriodStart = null;
	/**Access period end date.**/
	private LocalDateTime accessPeriodEnd = null;


	/**Default constructor of this class**/
	public MySTARS() 
	{
		openSettingsFile(SettingsFile);
		studentProgram = new StudentProgram(courseManager, starsDatabase);
		adminProgram = new AdminProgram(starsDatabase, courseManager, scanner, accessPeriodStart, accessPeriodEnd);
	}
	/**Opens the settings file and sets up the program based on settings provided.
	 * @param settingsFile The name of the file that contains the settings to be read from.
	 */
	private void openSettingsFile(String settingsFile)
	{
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(settingsFile));
			String line = "";
			while((line = bfr.readLine())!= null)
			{
				String[] array = line.split("\\|");
				if(array[0].equals("accessPeriod"))
				{
					accessPeriodStart = LocalDateTime.of(LocalDate.parse(array[1]), LocalTime.parse(array[2]));
					accessPeriodEnd = LocalDateTime.of(LocalDate.parse(array[3]),LocalTime.parse(array[4]));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	
	/**
	 * Method that begins the log in sequence of the MySTARS program. 
	 * @return A User_details object that contains information regarding the current user who successfully logged in; null 
	 * if the user's credentials cannot be associated with an account in the system.
	 */
	private  User_details logIn()
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
			console.printf("Please enter password: ");
			String input_pwd = String.valueOf(console.readPassword());
			//System.out.println("Please enter password:");
			//String input_pwd = scanner.nextLine();
			pwd_correct = PasswordModule.verifyPassword(input_pwd, user.getPassword());
			if( ! pwd_correct)
			{
				System.out.println("Password is incorrect. ");
			}
		}
		
		return user;
	}
	/**Method that is called when the program quits. Perofrms clean up and other routines needed during shut down of program.
	 */
	private void quit()
	{
		starsDatabase.save();
		courseManager.save();
		System.out.println("Goodbye!");
	}	
	/**Main method that is called to run the MyStars program.
	 */
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
				else if(LocalDateTime.now().isBefore(accessPeriodStart) || LocalDateTime.now().isAfter(accessPeriodEnd))
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
		quit();
	}
	
	
	public static void main(String args[])
	{
		MySTARS stars = new MySTARS();
		stars.run();
	}

}
