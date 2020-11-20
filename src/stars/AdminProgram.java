package stars;
import java.io.File;
import stars.Index_details.IndexClass;
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
    private String Student_Database_dir = "Students.txt";//"D:/Eclipse/STARS/src/stars/Students.txt"
    public final static String delimiter = "|";
    private StarsDatabase starsDatabase;
    private CourseManager courseManager;
    private String default_password = "password" ;
    private String hashed_default_password = PasswordMaker.generatePasswordHash(default_password);
    //public CourseManager courseManager;
    private Scanner sc = new Scanner(System.in);//you will need to delete this later
    
	private final String adminOptions = "1.Edit student access period\n"
			+ "2.Add a student (name, matric number, gender, nationality, etc)\n"
			+ "3.Add/Update a course (course code, school, its index numbers and vacancy).\n"
			+ "4.Check available slot for an index number (vacancy in a class)\n"
			+ "5.Print student list by index number.\n"
			+ "6.Print student list by course [ print only student�s name, gender and nationality ]\n"
			+ "7. Show options\n"
			+ "0. Quit\n";

    AdminProgram()//only used for testing, please remove later.
    {
    }
    
    AdminProgram(StarsDatabase StarDatabase)//CourseManger CourseManager, Scanner sc)
    {
        //courseManager = CourseManager;
    	starsDatabase = StarDatabase;
    }

    void EditAccessPeriod() 
    {
        String date = "";
        String time = "";
        boolean error = false;
        LocalDate date_start = null;
        LocalDate date_end = null;
        LocalTime time_start = null;
        LocalTime time_end = null;
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
                    System.out.println("The Starting time is same or later than Endding time.");
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
    
    
    
    
    

    /**
     * This function adds a student
     * it will prompt you to enter the student info and create a student with an empty registered course and waitlist
     * later add it to student database and rewrite the database
     */
    void AddaStudent()
    {
    	
    	Student_details new_student = new Student_details();
    	User_details new_user = new User_details();
    	
    	new_student.setAU(0);
        System.out.println("Adding a new student");
        //String name, String matric, String gender, String nationality
        
        System.out.println("Enter the name");
        String name = sc.nextLine();
        new_student.setName(name);
        
        System.out.println("Enter the matric number");
        String matric = sc.nextLine();
        new_student.setMatric_num(matric);
        
        String gender = "";
        do {
        	System.out.println("Enter the gender(M/F)");
        	gender = sc.nextLine();
            if(gender != "M" || gender!="F")
            	continue;
        }while(false);
        //I know that it is a bit awkward here, invalid input calls continue, if nothing goes wrong, exit.
        new_student.setGender(gender);
        
        System.out.println("Enter the nationality");
        String nationality = sc.nextLine();
        new_student.setNationality(nationality);
        
        new_student.setId(matric);
        
        //this part will update the user info, as userName and password.
        System.out.println("Enter the UserName");
        String userName = sc.nextLine();
        new_user.setUsername(userName);
        
        System.out.println("The password is setted to default");
        new_user.setPassword(hashed_default_password);

        new_user.setEmail(userName + "@e.ntu.edu.sg");
        
        String accountType = "";
        do {
        	System.out.println("Enter the accountType(Admin/Student)");
            accountType = sc.nextLine();
            if(accountType != "Admin" || accountType!="Student")
            	continue;
            
        }while(false);
        new_user.setAccountType(accountType);
        
        //the id is entered before, so just set it here
        new_user.setId(matric);
        
        String write = "n";
        do {
        	System.out.println("You sure that you have correctly entered all your info?(y/n)");
            write = sc.nextLine();
            
            if(write!="y" || write!="n")continue;
            
        }while(false);
        if(write=="n")
        {
        	System.out.println("Your inputs are not saved.");
        }
        
        starsDatabase.addUser(new_user);
        starsDatabase.addStudent(new_student);

    }
    
    /**
     * This function deletes the student from the database and rewrites the database 
     * warning, this is not yet complete and doesn't garanteen the integrity of database after using
     */
    public void deleteStudent()
    {
    	//cause id is the key.
    	System.out.println("Enter the id");
        String id = sc.nextLine();
        starsDatabase.removeStudent(id);
        starsDatabase.removeUser(id);
        //here we remove what else is relevent to this student.
        //it should be mostly courseDatabase
        
    }
    
    
    /**
     * add/update the course
     * add/update is checked first, and warning will be give if new course shares the index
     * 
     * update a course -> rewrite what was in the course
     * 
     * special case: update a course, and expanded the vacancy while there are student in waitlist
     * for such cases, this function would add the course randomly for the students.
     */
    void updateCourse()
    {
    	//before doing anything we need to figure we'are updating or adding
		{
			//things are a bit dangerous here, cause CourseManager passes reference to entire database
			//additional curly bracket added so reference is destroyed after using

			Course[] courseDB = null;
			Index_details[] indexDB = null;
			try {
				Object[] courseArray = courseManager.getAllCourses().toArray();
    			Object[] indexArray = courseManager.getAllIndexes().toArray();
//        			Please be entemely careful with this two DB
    			courseDB = (Course[])courseArray;
    			indexDB = (Index_details[])indexArray;
			}catch(Exception e) {
				e.printStackTrace();
			}

			System.out.println("PLease enter the courseCode");
			String courseCode =  sc.nextLine();
			
			boolean found = false;
			if(courseDB.length>0)
			{
				found = true;
				for(Course c: courseDB)
    				if(c.getcoursecode()==courseCode)
    					break;
				found = false;
			}
			Course oldCourse = null;
			if(found) {
				System.out.println("The Course exists, your input will overwrite this Course");
				System.out.println("Here are the previous course information");
				
				oldCourse = courseManager.getCourse(courseCode);
				System.out.printf("Course Code:%s\n",oldCourse.getcoursecode());
				System.out.printf("Course Name:%s\n",oldCourse.getcourseName());
				System.out.printf("Course AU:%d\n",oldCourse.getAU());
				
			}
			else {
				System.out.println("The CourseCode doesn't exists previously, you will be creating a new Course");
			}
			
			System.out.println("PLease enter the courseName");
			String courseName = sc.nextLine();
			
			int AU = -1;
			do {
				try {
    				System.out.println("PLease enter the AU");
        			AU = sc.nextInt();	
    			}catch(Exception e) {
    				System.out.println("Please enter a number.");
    				continue;
    			}
			}while(false);
			
			if(found)
			{
				//cause the oldCourse is a reference, it is actually refering to the data in database
				oldCourse.setcoursecode(courseCode);
				oldCourse.setcourseName(courseName);
				oldCourse.setAU(AU);
				//you will need to replace the name in the index as well
				//lucky, can find the index using method
				//you would also need to change the CourseCode if it exist somewhere else
				ArrayList<String> index_in_old_c= oldCourse.getIndexName();
				for(String indexCode: index_in_old_c)
					courseManager.getIndex(indexCode).setCourseCode(courseCode);
				
				//cause student also got courseName here
				
			}else
			{
				Course newCourse = new Course(courseCode, courseName, AU);
				courseManager.addCourse(newCourse);
			}
   		}
    }
    
    /**
     * add/update the index
     * btw the index is immutable(by now)
     * so be careful when you create them
     */
    public void Update_Index()
    {
    	//gaining all the required info beforehand
    	//might delete later
    	Course[] courseDB = null;
		Index_details[] indexDB = null;
		try {
			Object[] courseArray = courseManager.getAllCourses().toArray();
			Object[] indexArray = courseManager.getAllIndexes().toArray();
			courseDB = (Course[])courseArray;
			indexDB = (Index_details[])indexArray;
		}catch(Exception e) {
			e.printStackTrace();
		}
    	
		
		//entering the index
    	System.out.println("Please enter the index");
    	String indexCode = sc.nextLine();
    	
    	
    	//try read, if null, then it seems no previous index_details
    	Index_details old_idx = courseManager.getIndex(indexCode);
    	
    	if(old_idx==null)
    	{
    		//no previous idx
    		createIndex(courseDB, indexCode);
    	}else
    	{
    		//has previous index, everthing is in old_idx
    		//print the previous first
    		System.out.println("This is an existing index:");
    		System.out.println("existing.");
    		return;
    		
    		//this part never reached, unfinished for updating
    		
//    		System.out.printf("Index Code: %s\n",old_idx.getIndexCode());
//    		System.out.printf("Course Code: %s(can only be changed by UpdateCourse)\n",old_idx.getCourseCode());
//    		System.out.printf("Vacancy: %d\n",old_idx.getVacancy());
//    		
//    		System.out.println("Do you wish to change these attribute?(y/n)");
//    		String choice = sc.nextLine();
//    		if(choice=="y")
//    		{
//    			//user want to change these
//    			courseManager.removeIndex(indexCode);
//    			createIndex(courseDB, indexCode);
//    		}
    	}
    }
    
    /**
     * helper function for updateIndex
     * update the CourseDatabase
     */
    private void createIndex(Course[] courseDB, String indexCode)
    {
    	boolean success = false;
    	String courseCode = null;
    	do {
    	System.out.println("Please enter the courseCode");
    	courseCode = sc.nextLine();
    	
    	//entering courseCode
    	success = false;
    	for(Course c: courseDB)
    	{
    		success = false;
    		if(courseCode.equals(c.getcoursecode()))
    		{
    			success = true;
    			break;
    		}
    		System.out.println("courseCode doesn't exist.");
    		System.out.println("PLease enter again.");
    	}
    	}while(success = false);
    	
    	//entering the capacity
    	int capacity = 0;
    	do {
			try {
				System.out.println("PLease enter the capacity");
    		capacity = sc.nextInt();
    			if(capacity < 0)
    			{
    				System.out.println("The capacity can't be nagetive.");
    				continue;
    			}
			}catch(Exception e) {
				System.out.println("Please enter a number.");
				continue;
			}
		}while(false);
    	
    	//saving things to database
    	Index_details new_index = new Index_details();
    	new_index.setCourseCode(courseCode);
    	new_index.setIndexCode(indexCode);
    	new_index.setCapacity(capacity);
    	courseManager.addIndex(new_index, courseCode);
    }
    
    
    public void UpdateClass()
    {
    	System.out.println("Enter the index");
    	String indexCode = sc.nextLine();
    	Index_details oldIndex = courseManager.getIndex(indexCode);
    	if(oldIndex==null)
    	{
    		System.out.println("No such index, exit");
    		return;
    	}
    	System.out.println("Index found");
    	System.out.println("All existing classes:");
		for(IndexClass c: (IndexClass[])oldIndex.getIndexClasses() )
		{
			System.out.println(c.toFlatFileString());
		}
		String choice = null;
    	do {
    		
    		System.out.println("1.add a class");
    		System.out.println("2.remove a class");
    		System.out.println("q.exit");
    		System.out.println("Your choice:");
    		choice = sc.nextLine();
    		switch(choice)
    		{
    			case "1":
    			{
    				//right now assume that
    				String Type, group, venue;
    				
    				DayOfWeek day=null;
    				System.out.println("Enter Type");
    				Type = sc.nextLine();
    				System.out.println("Enter Group");
    				group = sc.nextLine();
					System.out.println("Enter day");
					//day = sc.nextLine();
					String dayy = sc.nextLine();
					
					System.out.println("Enter venue");
					venue = sc.nextLine();
					
					do {
						switch(dayy)
						{
							case "Mon":
								day = DayOfWeek.of(1);
								break;
							case "Tue":
								day = DayOfWeek.of(2);
								break;
							case "Wed":
								day = DayOfWeek.of(3);
								break;
							case "Thur":
								day = DayOfWeek.of(4);
								break;
							case "Fri":
								day = DayOfWeek.of(5);
								break;
							case "Sat":
								day = DayOfWeek.of(6);
								break;
							case "Sun":
								day = DayOfWeek.of(7);
								break;
							default: 
								System.out.println("Invalid input");
								continue;
						}
					}while(false);
					
					ArrayList<LocalTime> time;
					do {
						time = readPeriod();
					}while(time==null);
					//check for clashes
    				Index_details.IndexClass newClass = new IndexClass(Type, group, time.get(0), time.get(2), day, venue);
    				
    				for(IndexClass idxClass: oldIndex.getIndexClass())
					{
    					
    					if(idxClass.clash(newClass))
    					{
    						System.out.println("Got clash");
    						break;
    					}
					}
    				
    				((Index_details)courseManager.getIndex(indexCode)).addIndexClass(newClass);
    				
    			}break;
    			case "2":
    			{
    				//yet we don't have removing
    			}break;
    			
    			case "q":
    			{
    				return;
    			}
    		}
    	}while(true);//exit when called case 'q'
    }
    
    private ArrayList<LocalTime> readPeriod()
    {
    	LocalTime startTime=null;
    	LocalTime endTime = null;
    	boolean error;
		String time;
		do
        {
            error = false;
            try{
                System.out.println("Please enter the starting Time(hh:mm)");
                time = sc.nextLine();
                startTime = LocalTime.parse(time+":00");
            }catch(DateTimeException e){
                System.out.println("Date entered is not a valid time.");
                error = true;
            }
        }while(error == true);
		do
        {
            try{
                System.out.println("Please enter the endding Time(hh:mm)");
                time = sc.nextLine();
                endTime = LocalTime.parse(time+":00");
            }catch(DateTimeException e){
                System.out.println("Date entered is not a valid time.");
                continue;
            }
        }while(false);
		
        int compare_time = startTime.compareTo(endTime);
        if (compare_time>=0)
        {
            error = true;
            System.out.println("The Starting time is same or later than Endding time.");
            System.out.println("Please enter again");
            return null;
        }
    	
    	ArrayList<LocalTime> arraylist = new ArrayList<LocalTime>();
    	arraylist.add(startTime);
    	arraylist.add(endTime);
    	
    	return arraylist;
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

			case 4: {
				System.out.println("Enter the index that you wanted.");
				String indexCode = sc.nextLine();
				CheckAvailableSlot(indexCode);
			}break;

			case 5: {
				System.out.println("Enter the index that you wanted.");
				String indexCode = sc.nextLine();
				PrintStudentByIndex(indexCode);
			}break;

			case 6: {
				System.out.println("Enter the index that you wanted.");
				String indexCode = sc.nextLine();
				PrintStudentByCourse(indexCode);
			} break;

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
		starsDatabase.writeDatabaseFiles();
    }
    
    

    void CheckAvailableSlot(String indexCode)
    {
    	Index_details target = courseManager.getIndex(indexCode); 
    	if(target==null)
    	{
    		System.out.println("index not found");
    		return;
    	}
    	
    	System.out.printf("Index: %s\n",indexCode);
    	System.out.printf("Avaiable Slot: %d\n",target.getCapacity()-target.getRegisteredStudents().length);

        //get the indexClass from index_detail and print all of them out
    	
    }


    void PrintStudentByIndex(String indexCode)
    {
    	Index_details target = courseManager.getIndex(indexCode); 
    	if(target==null)
    	{
    		System.out.println("index not found");
    		return;
    	}
    	
    	System.out.printf("Index: %s",indexCode);
    	for(String student: target.getRegisteredStudents())
    	{
    		System.out.printf("%s ",student);
    	}
    	System.out.printf("\n");
    }

    void PrintStudentByCourse(String courseCode)
    {
        ArrayList<String> indexlist = courseManager.getCourse(courseCode).getIndexName();
    	for(String index: indexlist)
    	{	
    		PrintStudentByIndex(index);
    	}
    }
  
    public static void main(String[] args)
    {
    	
    	StarsDatabase exampleDatabase = new StarsDatabase();
		AdminProgram Admin = new AdminProgram(exampleDatabase);
		User_details user = new User_details();
    	Admin.run(user);
    	exampleDatabase.writeDatabaseFiles();//hello
    }
}