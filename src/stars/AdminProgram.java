package stars;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class AdminProgram
{
    public String Current_User = "Admin";
    public StarsMain main;
    private String AccessPeriodFile = "AccessPeriod.txt";
    public final static String delimiter = "|";
    //public CourseManager courseManager;
    private Scanner sc = new Scanner(System.in);//you will need to delete this later
    private UserDatabase userDatabase;
    private StudentDatabase  studentDatabase;
    
	private final String adminOptions = "1.Edit student access period\n"
			+ "2.Add a student (name, matric number, gender, nationality, etc)\n"
			+ "3.Add/Update a course (course code, school, its index numbers and vacancy).\n"
			+ "4.Check available slot for an index number (vacancy in a class)\n"
			+ "5.Print student list by index number.\n"
			+ "6.Print student list by course [ print only student’s name, gender and nationality ]\n"
			+ "7. Show options\n"
			+ "0. Quit\n";

    AdminProgram()//used for testing
    {
    }
    
    AdminProgram( UserDatabase UserDatabase, StudentDatabase StudentDatabase)//CourseManger CourseManager, UserDatabase UserDatabase, Scanner sc)
    {
        //courseManager = CourseManager;
        userDatabase = UserDatabase;
        studentDatabase = StudentDatabase;
    }

    void EditAccessPeriod() 
    {
        String date = "";
        String time = "";
        boolean error = false;
        LocalDate date_start = LocalDate.of(2020, 12, 1);
        LocalDate date_end = LocalDate.of(2020, 12, 31);;
        LocalTime time_start = LocalTime.of(9, 0);
        LocalTime time_end = LocalTime.of(23,  59);
        //getting the starting data and time

        do
        {
        //outter loop to check if the access time is valid
        error = false;

            do
            {
                //inner loop to check if input is valid
                error = false;
                try{
                    System.out.println("Please enter the Starting Date(yyyy-mm-dd)");
                    date = sc.nextLine();
                    date_start = LocalDate.parse(date);
                }catch(DateTimeException e){
                    System.out.println("Date entered is not a valid date.");
                    error = true;
                }
            }while(error == true);
            
            do
            {
                error = false;
                try{
                    System.out.println("Please enter the Starting Time(hh:mm:ss)");
                    time = sc.nextLine();
                    time_start = LocalTime.parse(time);
                }catch(DateTimeException e){
                    System.out.println("Date entered is not a valid time.");
                    error = true;
                }
            }while(error == true);


            //getting the endding data and time
            do
            {
                error = false;
                try{
                    System.out.println("Please enter the Endding Date(yyyy-mm-dd)");
                    date = sc.nextLine();
                    date_end = LocalDate.parse(date);
                }catch(DateTimeException e){
                    System.out.println("Date entered is not a valid date.");
                    error = true;
                }
            }while(error == true);
                
            do
            {
                error = false;
                try{
                    System.out.println("Please enter the Endding Time(hh:mm:ss)");
                    time = sc.nextLine();
                    time_end = LocalTime.parse(time);
                }catch(DateTimeException e){
                    System.out.println("Date entered is not a valid time.");
                    error = true;
                }
            }while(error == true);

            //debugging on the date and time entered.
            int compare_date = date_start.compareTo(date_end);

            if (compare_date>0)
            {
                error = true;
                System.out.println("The Starting date is later than Enddnig date.");
                System.out.println("Please enter again");
            }
            if (compare_date == 0)
            {
                int compare_time = time_start.compareTo(time_end);
                if (compare_time>=0)
                {
                    error = true;
                    System.out.println("The Starting time is same or earlier than Endding time.");
                    System.out.println("Please enter again");
                }
            }
        }while(error == true);
        
         //convert the date and time to strings
	     String date_start_str = date_start.toString();
	     String date_end_str = date_end.toString();
	     String time_start_str = time_start.toString();
	     String time_end_str = time_end.toString();
         
	     //writting to file
	    PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter("Settings.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String accessperiodString = "accessPeriod"+"|"+date_start_str + '|'+ time_start_str + '|'+ date_end_str+'|'+time_end_str+'|';
		
		out.println(accessperiodString);
		out.close();

	     //you will also need to write the thing to a file
	     System.out.println("Access Period has been setted");
	     

    }

    void AddaStudent()
    {
    	
    	Student_details student = new Student_details();
    	
    	student.setAU("0");
    	
        System.out.println("Adding a new student");
        //String name, String matric, String gender, String nationality
        System.out.println("Enter the name");
        String name = sc.nextLine();
        student.setName(name);
        
        System.out.println("Enter the matric number");
        String matric = sc.nextLine();
        student.setMatric_num(matric);
        
        System.out.println("Enter the gender");
        String gender = sc.nextLine();
        student.setGender(gender);
        
        System.out.println("Enter the nationality");
        String nationality = sc.nextLine();
        student.setNationality(nationality);
        
        System.out.println("Enter the id");
        String id = sc.nextLine();
        student.setId(id);
        
        studentDatabase.add(student);
        try {
        	studentDatabase.writeFile("D:/Eclipse/STARS/src/stars/Students.txt");
        }catch(Exception e)
        {
        	System.out.println("Error occurs when Admin Program tries to rewrite student database to add a new student");
        	e.printStackTrace();
        	return;
        }
        
    }
    
    void updateCourse()
    {
    	
    }

    void run(User_details user)
    {
    	
    	Scanner scanner= this.sc;
    	
    	System.out.format("Welcome to STARS Admin, %s !\n", user.getUsername());
		System.out.print(adminOptions);
		System.out.format("Please enter an option: ");
			
		boolean loopInput = false;
		boolean quit = false;
		while( loopInput || !quit )
		{
			System.out.format("Please enter an option: ");
			int input  = -1;
			try {  input = scanner.nextInt(); scanner.nextLine(); } 
			catch(InputMismatchException e) {System.out.println("Please enter a proper input"); scanner.nextLine();}
		
			switch( input )
			{

			case 1: {
				EditAccessPeriod();
			}break;

			case 2: {
				AddaStudent();
			}break;

			case 3: {
				updateCourse();
			}break;

			case 4: {}break;

			case 5: {}break;

			case 6: {} break;

			case 7: {System.out.print(this.adminOptions);}

			case 0: { quit = true; } break;

			default: 
			{ 
				System.out.println("Please enter a number among options provided.");
				loopInput = true;
			}
			}//end switch
			if(quit)return;
		}
    }
    
    
/*
    void CheckAvailableSlot(int index)
    {
        Course current_course = courseManager.GetCourse(index);
        int Slot = current_course.indexes[index].vaccancy;
        System.out.println("there are ",Slot," vacancies for index ",index);
    }


    void PrintStudentByIndex(int index)
    {
        Course current_course = courseManager.GetCourse(index);
        StudentList = current_course.indexes[index].printStudnetList();
    }

    void PrintStudentByCourse(String CourseName)
    {
        //not sure how i could find all the indexes
    }
  */  
    public static void main(String args[])
    {
		AdminProgram Admin = new AdminProgram();
    	//Admin.AddaStudent();
		
		User_details user = new User_details();
		user.setUsername("Admin");
		
    	Admin.run(user);
    }
}