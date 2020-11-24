package stars;
import java.io.File;
//import IndexClass;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import java.util.Arrays;

/**
 * A class that represents the program a admin will use when accessing MySTARS.
 * @author Yu Peng
 * @since  20/11/2020
 * @version 1.0.0
 */
public class AdminProgram
{
	/**
	 * Current_User 		record the type of user has logged in
	 * starsDatabase		a reference to StarsDatabase, used to call access and manage the data about student and user in the database
	 * courseManager		a reference to CourseManager, used to call access and manage the data about course, index and indexClass in the database
	 * default_password		the default password given to the new account that is just created
	 * hashed_default_password	the hashed value of the default password, stored in the local files.
	 */
    public String Current_User = "Admin";
    private StarsDatabase starsDatabase=null;
    private CourseManager courseManager=null;
    private StarsNotifier notifier = null;
    public Scanner sc;
    
	private final String adminOptions = "1.Edit student access period\n"
			+ "2.Add a student (name, matric number, gender, nationality, etc)\n"
			+ "3.Add/Update a course (course code, course name).\n"
			+ "4.Add/Update an index (index number, vacancy).\n"
			+ "5.Add/Update a class (class type, class time).\n"
			+ "6.Change the Capacity of a class (indexCode, newCapacity).\n"
			+ "7.Check available slot for an index number (vacancy in a class)\n"
			+ "8.Print student list by index number.\n"
			+ "9.Print student list by course [ print only student�s name, gender and nationality ]\n"
			+ "10. Show options\n"
			+ "11. Show Student info\n"
			+ "12. Show Course info\n"
			+ "-1. Quit\n";
    
	/**
	 * 
	 * @param StarDatabase 		a reference to StarsDatabase, used to call access and manage the data about student and user in the database
	 * @param CourseManager		a reference to CourseManager, used to call access and manage the data about course, index and indexClass in the database
	 * @param sc				a reference to Scanner, used to input the data
	 */
    AdminProgram(StarsDatabase StarDatabase, CourseManager CourseManager, Scanner sc)
    {
        courseManager = CourseManager;
    	starsDatabase = StarDatabase;
    	this.notifier = StarsNotifier.getNotifificationMethod(StarsNotificationType.Email);
    	this.sc =sc;
    }

    /**
     * edit the access period for the student to do add/drop
     * prompt the user to enter the start time and end time of the access period
     * the access period is written to an external file, which can be accesses by the StarsMain
     * 
     * error will be checked
     * 1: illegal input for date and time
     * 2: start time is later than end time
     */
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
     * it will prompt you to enter the student info 
     * including: Name, matric number, gender, nationality, useranme and accountType(Student or Admin)
     * id of the user is set to matric number
     * 
     * We allow students to have the same name but matric number has to be unique
     * the newly created student would have an empty waitlist and regisetered list
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
        
        String matric = null;
        do {
        	System.out.println("Enter the matric number");
        	matric = sc.nextLine();
            if(starsDatabase.getStudentbyMatric(matric)==null)
            {
            	new_student.setMatric_num(matric);
            	break;
            }
            System.out.println("The matric number has been used, enter a new one.");
        }while(true);
        
        
        String gender = "";
        do {
        	System.out.println("Enter the gender(M/F)");
        	gender = sc.nextLine();
            if(gender.equals("M") || gender.equals("F"))
            {
            	break;
            }
        }while(true);
        //I know that it is a bit awkward here, invalid input calls continue, if nothing goes wrong, exit.
        new_student.setGender(gender);
        
        System.out.println("Enter the nationality");
        String nationality = sc.nextLine();
        new_student.setNationality(nationality);
        
        boolean loop =true;
        while(loop)
        {
            System.out.println("Enter the student study year:");
            try {
            int year = sc.nextInt();
            if(0<year || year>6) 
            {
            	System.out.println("Study year should be between 0 and 6.");
            	continue;
            }
            new_student.setStudyYear(year);
            loop =false;
            }catch(InputMismatchException e)
            {
            	System.out.println("Input not a numbe, please try again.");
            }      
            sc.nextLine();
        }

        
        
        //this part will update the user info, as userName and password.
        String userName = null;
        do {
        	System.out.println("Enter the UserName");
        	userName = sc.nextLine();
            if(starsDatabase.getUser(userName)==null)
            {
            	new_user.setUsername(userName);
            	new_student.setUserName(userName);
            	break;
            }
            System.out.println("The UserName has been used, enter a new one.");
        }while(true);
        
        
    
        System.out.println("The password is setted to the same as username");
        String hashedPwd = PasswordModule.generatePasswordHash(userName);
        new_user.setPassword(hashedPwd);

        new_user.setEmail(userName + "@e.ntu.edu.sg");
        new_user.setAccountType("Student");
        
        
        String write = "n";
        do {
        	System.out.println("You sure that you have correctly entered all your info?(y/n)");
            write = sc.nextLine();
            if(write.equals("y")||write.equals("n"))
            	break;
        }while(true);
        
        if(write.equals("n"))
        {
        	System.out.println("Your inputs are not saved.");
        }
        else
        {
            starsDatabase.addUser(new_user);
            starsDatabase.addStudent(new_student);
            DisplayStudentInfo();     	
        }
    }
    
    /**
     * display the information of all students currently in the system.
     */
    public void DisplayStudentInfo()
    {
    	String str = String.format("|%-20s|%-20s|%-20s|%-20s|","Rows" ,"Student name","Student's id","User name");
    	System.out.printf(str);
    	System.out.printf("\n");
        int rows = 1;
        for(Student_details student : starsDatabase.getAllStudents())
        {
        	String str1 = String.format("|%-20d|%-20s|%-20s|",rows ,student.getName(),student.getMatric_num());
        	System.out.printf(str1);
        	rows++;
        	//just in case you need the user info as well
        	User_details user = starsDatabase.getUser(student.getUserName());
        	System.out.printf("%-20s|",user.getUsername());
        	System.out.printf("\n");
        }
    }
    
    /**
     * add/update the course
     * a course contains basic information and a list of indexes
     * 
     * user will be prompt to enter the courseCode first
     * the program will check if this courseCode exists previously
     * 
     *  if there is pre-existed courseCode, you can change the courseName, and AU
     * 	if the courseCode is new, you will need to enter information like course name and AU to compete the creation.
     * 		when the course is newly created, it will have no index
     * 
     * however, this function should be used after the student has registered, which might damage the integrity of the data
     */
    void UpdateCourse()
    {
    	//before doing anything we need to figure we'are updating or adding
		{
			//things are a bit dangerous here, cause CourseManager passes reference to entire database
			//additional curly bracket added so reference is destroyed after using

			Collection<Course> courseDB = null;
			try {
				courseDB = courseManager.getAllCourses();
			}catch(Exception e) {
				e.printStackTrace();
			}

			System.out.println("PLease enter the courseCode");
			String courseCode =  sc.nextLine();
			
			boolean found = false;
			
			if(courseDB.size()>0)
			{
				for(Course c: courseDB)
    				if(c.getcoursecode().equals(courseCode)) {
    					System.out.println("reached");
    					found = true;
    					break;
    				}
				
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
        			if(AU>0)break;
        			System.out.println("Please enter a positive number.");
    			}catch(Exception e) {
    				System.out.println("Please enter a number.");
    				sc.nextLine();
    			}
			}while(true);
			
			if(found)
			{
				//cause the oldCourse is a reference, it is actually refering to the data in database
				oldCourse.setcoursecode(courseCode);
				oldCourse.setcourseName(courseName);
				oldCourse.setAU(AU);
				//you will need to replace the name in the index as well
				//lucky, can find the index using method
				//you would also need to change the CourseCode if it exist somewhere else
				Set<String> index_in_old_c= oldCourse.getIndexCodes();
				if(!index_in_old_c.isEmpty()) 
					for(String indexCode: index_in_old_c) 
						if(!indexCode.equals(" "))
							courseManager.getIndex(indexCode).setCourseCode(courseCode);
			}else
			{
				Course newCourse = new Course(courseCode, courseName, AU);
				courseManager.addCourse(newCourse);
			}
			DisplayCourseInfo();
   		}
    }
    
    /**
     * display the information of all courses
     */
    public void DisplayCourseInfo()
    {
        String str = String.format("|%-20s|%-20s|%-20s|%-20s","Rows" ,"Course code","AU","Course Name");
    	System.out.printf(str);
    	System.out.printf("\n");
        int rows = 1;
        for(Course course : courseManager.getAllCourses())
        {
        	String str1 = String.format("|%-20d|%-20s|%-20s|%-20s",rows ,course.getcoursecode(),course.getAU(),course.getcourseName());
        	System.out.printf(str1);
        	rows++;

        	System.out.printf("\n");
        }
    }
    
    /**
     * add/update the index
     * 
     * similar to UpdateCourse()
     * prompt the user to enter the index, and check if the index is used before 
     * 
     * if the index is not used, the program will create a new index, you would enter information:course code and capacity
     * if the index is used, the program will rewrite the old data, and user can use y to choose if they want to rewrite
     *  
     *  note that you shouldn't enter a course code that is not in a database
     *  an index has to be created after its course is created
     *  
     * same to UpdateCourse(), this function should be used after the student has registered, which might damage the integrity of the data
     */
    public void Update_Index()
    {
    	//gaining all the required info beforehand
    	//might delete later\
    	Collection<Course> courseDB = null;
		Collection<Index_details> indexDB = null;
		try {
			courseDB = courseManager.getAllCourses();
			indexDB = courseManager.getAllIndexes();
		}catch(Exception e) {
			e.printStackTrace();
		}
    	
		//entering the index
		String indexCode = null;
		do {
			System.out.println("Please enter the index");
	    	indexCode = sc.nextLine();
	    	if(!indexCode.equals(" "))
	    		break;
		}while(true);
    	
    	
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
    		System.out.println("index info as follows:");
    		System.out.printf("indexCode: %s\n", old_idx.getIndexCode());
    		System.out.printf("courseCode: %s\n", old_idx.getCourseCode());
    		System.out.printf("capacity: %s\n", old_idx.getCapacity());
    		String oldIndexCode = old_idx.getIndexCode();
    		String oldCourseCode = old_idx.getCourseCode();
    		
    		String choice = null;
    		System.out.println("Do you want to change the indexCode(y for yes)");
    		choice = sc.nextLine();
    		
    		//put it outside cause if inside can be destroyed and cause nullpointer errors
    		if(choice.equals("y"))
    		{
    			String newIndexCode = new String();
    			do {
    				System.out.println("Please enter the index");
    		    	newIndexCode = sc.nextLine();
    		    	if(!newIndexCode.equals(" ")&&(courseManager.getIndex(newIndexCode)==null))
    		    		break;
    			}while(true);
    			//for index_details
    			old_idx.setIndexCode(newIndexCode);
    			
    			//for course
    			courseManager.getCourse(oldCourseCode).removeIndexCode(oldIndexCode);
    			courseManager.getCourse(oldCourseCode).addIndexCode(newIndexCode);
    			
    			oldIndexCode = newIndexCode;
    			//this is because later to find the one with this index
    			//will need to use the new index
    		}
    		
    		System.out.println("Do you want to change the CourseCode(y for yes)");
    		choice = sc.nextLine();
    		if(choice.equals("y"))
    		{
    			String newCourseCode = null;
    			boolean success = false;
    	    	do {
			    	System.out.println("Please enter the courseCode");
			    	newCourseCode = sc.nextLine();
			    	for(Course c: courseDB)
			    	{
			    		if(newCourseCode.equals(c.getcoursecode()))
			    		{
			    			success = true;
			    			break;
			    		}
			    	}
			    	if(!success)
			    	{
			    		System.out.println("Course Code doesn't exist.");
			    		System.out.println("PLease enter again.");
			    	}
    	    	}while(!success);
    	    	old_idx.setCourseCode(newCourseCode);
    	    	courseManager.getCourse(oldCourseCode).removeIndexCode(oldIndexCode);
    			courseManager.getCourse(newCourseCode).addIndexCode(oldIndexCode);
    		}
    		
    		System.out.println("Do you want to change the capacity(y for yes)");
    		choice = sc.nextLine();
    		if(choice.equals("y"))
    		{
    			//changing capacity
    			int capacity = 0;
    			do {
    				try {
        				System.out.println("PLease enter the capacity");
        				capacity = sc.nextInt();	
        				if(capacity> 0)break;
        				System.out.println("Can't set the vacanacy to this");
    					System.out.println("new vacancy has to be bigger than 0");
        			}catch(Exception e) {
        				System.out.println("Please enter a number.");
        			}
    			}while(true);
    			old_idx.setCapacity(capacity);
    		}
    	}
    	//print all info
    	DisplayIndexInfo();
		
    }
    
    /**
     * display the index's info
     */
    public void DisplayIndexInfo()
    {
        String str = String.format("|%-20s|%-20s|%-20s|%-20s|","Rows" ,"Index code","Course code","Capacity");
    	System.out.printf(str);
    	System.out.printf("\n");
        int rows = 1;
        for(Index_details index: courseManager.getAllIndexes())
        {
        	String str1 = String.format("|%-20d|%-20s|%-20s|%-20d|",rows,index.getIndexCode(),index.getCourseCode(),index.getCapacity());
        	System.out.printf(str1);
        	rows++;
        	System.out.printf("\n");
        }
    }
    
   /**
    * Method to create an index
    * @param courseDB When creating index, this collection of courses will be referred to when user enters a course code to check if the 
    * course with the course code is already created.
    * @param indexCode The index code of the index to be created.
    */
     
    private void createIndex(Collection<Course> courseDB, String indexCode)
    {
    	boolean success = false;
    	
    	//entering courseCode
    	String courseCode = null;
    	do {
	    	System.out.println("Please enter the courseCode");
	    	courseCode = sc.nextLine();
	    	for(Course c: courseDB)
	    	{
	    		if(courseCode.equals(c.getcoursecode()))
	    		{
	    			success = true;
	    			break;
	    		}
	    	}
	    	if(!success)
	    	{
	    		System.out.println("Course Code doesn't exist.");
	    		System.out.println("PLease enter again.");
	    	}
    	}while(!success);
    	
    	//entering the capacity
		int capacity = -1;
		do {
			try {
				System.out.println("PLease enter the capacity");
				capacity = sc.nextInt();	
    			if(capacity>0)break;
    			System.out.println("Please enter a positive number.");
			}catch(Exception e) {
				System.out.println("Please enter a number.");
			}
		}while(true);
    	
    	
    	//saving things to database
    	Index_details new_index = new Index_details();
    	new_index.setCourseCode(courseCode);
    	new_index.setIndexCode(indexCode);
    	new_index.setCapacity(capacity);
    	courseManager.addIndex(new_index, courseCode);
   	}


    /**
     * used to add class to the index
     * need the user to enter the information to create a new class for the index
     * class type is needed:example: LEC, TUT, LAB
     * together with group, venue and time
     *
     * index has to exist previously to add am IndexClass to it
     * doesn't allow clash with previous classes, and would ask you to enter again
     *
     *doesn't provide the delete the class
     *
     */
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
		for(Object c: oldIndex.getIndexClasses() )
		{
			System.out.println(((IndexClass)c).toFlatFileString());
		}
		String choice = null;
    	do {
    		
    		System.out.println("1.add a class");
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
    				do {
    					
        				System.out.println("Enter Type");
        				Type = sc.nextLine();
        				try {
        					IndexClassType.valueOf(Type);
        				}catch(IllegalArgumentException e)
        				{
        					System.out.println("Please enter one of \"Lecture, Tutorial, Lab\"");
        					continue;
        				}
        				break;
    				}while(true);
    				
    				System.out.println("Enter Group");
    				group = sc.nextLine();
					
					System.out.println("Enter venue");
					venue = sc.nextLine();
					
					String dayy = new String();
					do {
						System.out.println("Enter day");
						dayy = sc.nextLine();
						try{day = DayOfWeek.valueOf(dayy.toUpperCase());}
						catch(IllegalArgumentException e)
						{
							System.out.println("Please enter a weekday (ex: MONDAY)");continue;
						}
						break;
					}while(true);
					
					ArrayList<LocalTime> time;
					do {
						time = readPeriod();
					}while(time==null);
					//check for clashes
    				IndexClass newClass = new IndexClass(Type, group, time.get(0), time.get(1), day, venue);
    				
    				for(IndexClass idxClass: oldIndex.getIndexClasses())
					{
    					
    					if(idxClass.clash(newClass))
    					{
    						System.out.println("Got clash.");
    						break;
    					}
					}
    				
    				courseManager.getIndex(indexCode).addIndexClass(newClass);
    				
    			}break;
    			case "2":
    			{
    				System.out.println("Remove not avaiable yet.");
    			}break;
    			
    			case "q":
    			{
    				DisplayClassesInfo(indexCode);
    				return;
    			}
    		}
    	}while(true);//exit when called case 'q'
    	
    }
    
    /**
     * display the info about all of the indexClasses in index
     *
     * @param indexCode		index of the course
     */
    public void DisplayClassesInfo(String indexCode)
    {
        String str = String.format("|%-20s|%-60s|","Rows" ,"Class Info");
    	System.out.printf(str);
    	System.out.printf("\n");
        int rows = 1;
        for(IndexClass classes : courseManager.getIndex(indexCode).getIndexClasses())
        {
        	String str1 = String.format("|%-20d|%-60s|",rows,classes.toFlatFileString());
        	System.out.printf(str1);
        	rows++;
        	System.out.printf("\n");
        }
    }
    
    /**
     * helper function
     * this can get the input of time and check for errors
     * 
     * check two errors
     * 1. illegal input
     * 2. start time is later than end time
     * 
     * returns a ArrayList where index 0 is start time, index 1 is end time 
     * @return  An arraylist whose first element is the start time and second element is end time, both in LocalTime format
     */
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
            error = false;
            try{
                System.out.println("Please enter the endding Time(hh:mm)");
                time = sc.nextLine();
                endTime = LocalTime.parse(time+":00");
            }catch(DateTimeException e){
                System.out.println("Date entered is not a valid time.");
                error = true;
            }
        }while(error == true);
		
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

    /**
     * change the vacancy of a specific index
     * different from UpdateCourse(), UpdateIndex() and UpdateindexClass, this is the only function that allows you to modify data after the start of registration.
     * 
     * if the index enter is not found in the database, an exception will be thrown
     * if the capacity is lower than the student registered, which might cause error, the program will also throw out the exception
     * 
     * @param indexCode						index of the course
     * @param newCapacity					the new Capacity for this index
     */
    public void UpdateCapacity(String indexCode, int newCapacity)
    {
    	Index_details index = courseManager.getIndex(indexCode);
    	if(index==null)
    	{
    		System.out.println("Index with the code entered does not exist.");
    		return;
    	}

    	if(newCapacity<index.getRegisteredStudents().size())
    	{
    		System.out.println("Entered capacity of index is smaller than number of students registered in the index.");
    		return;
    	}
	
    	courseManager.getIndex(indexCode).setCapacity(newCapacity);
		System.out.format("Updated capacity of index %s to %d.\n", index.getIndexCode(),index.getCapacity());
		sc.nextLine();
    	//two conditions to stop 1.no student in waitlist 2.no more vacancy
    	while(index.getWaitingStudents().size()>0 && index.getVacancy()>0)
    	{
    		Student_details newstudent = starsDatabase.getStudentbyMatric(index.getFirstWaitingStudent());//first out student
    		if(newstudent!= null)
    		{
    			index.removeFromWaitlist(newstudent.getMatric_num()); //remove first out student from course waitlist
    			newstudent.removeFromWaitlist(indexCode);;//remove first out course from student waitlist
    			index.registerStudent(newstudent); //add student name to course
    			newstudent.addIndex(indexCode);
    			sendNotification_courseAdded(indexCode, newstudent);
    			System.out.format("Added student %s from waitlist to registered list of this index.\n", newstudent.getMatric_num());
    		}
    	}
    }
    
    /**
     * send the notification to student when a course has been added from the waitlist
     * 
     * @param indexCode		the index of the course
     * @param student		the student that got this course
     */
    private void sendNotification_courseAdded(String indexCode, Student_details student)
    {
    	
    	String message = "";
		message += String.format("Dear %s\n",student.getName());
		message += String.format("\tIndex %s has been added to your register list.\n",indexCode);
		message += String.format("Best Wishes\n");
		message += String.format("Stars");
    	notifier.sendNotification("Course added from waitlist", message, starsDatabase.getUser(student.getUserName()).getEmail()  );
    }

    /**
     * print out the available slots for an index
     * @param indexCode		index of the course
     */
    void CheckAvailableSlot(String indexCode)
    {
    	Index_details target = courseManager.getIndex(indexCode); 
    	if(target==null)
    	{
    		System.out.println("index not found");
    		return;
    	}
    	
    	if(target.getRegisteredStudents().contains(" "))
    	{
    		System.out.printf("Index: %s\n",indexCode);
    		System.out.printf("Avaiable Slot: %d/%d\n",target.getCapacity(),target.getCapacity());
    		return;
    	}
    	System.out.printf("Index: %s\n",indexCode);    	
    	System.out.printf("Avaiable Slot: %d/%d\n",target.getCapacity()-target.getRegisteredStudents().size(),target.getCapacity());

        //get the indexClass from index_detail and print all of them out
    	
    }

    /**
     * print out the student info for those who has taken indexCode
     * printed info includes name, matric number, gender and nationality
     * 
     * @param indexCode		the index of the course
     */
    void PrintStudentByIndex(String indexCode)
    {
    	Index_details index = courseManager.getIndex(indexCode);
    	if(index==null)
    	{
    		System.out.println("index not found");
    		return;
    	}
    	System.out.printf("Index: %s Vacancies: %d/%d\n",indexCode, index.getVacancy(), index.getCapacity());
    	String format = "|%-5s|%-20s|%-20s|%-20s|%-20s|";
        String str = String.format(format,"Rows" ,"Student name","Matric number","Gender","Nationality");
    	System.out.printf(str);
    	System.out.printf("\n");
    	int rows = 1;
		for(Student_details student_info: starsDatabase.getAllStudents())
			if(student_info.isRegistered(indexCode)&&(!student_info.getName().equals(" ")))
			{
	        	String str1 = String.format(format,rows, student_info.getName(), student_info.getMatric_num(),student_info.getGender(),student_info.getNationality());
	        	System.out.printf(str1);
	        	rows++;
	        	System.out.printf("\n");
			}
    }
    
    /**
     * print out the student info for those who has taken courseCode
     * @param courseCode	the code of the course to print
     */
    void PrintStudentByCourse(String courseCode)
    {
        Set<String> indexlist = courseManager.getCourse(courseCode).getIndexCodes();
        Collection<Student_details> allStudents = starsDatabase.getAllStudents();
        System.out.printf("Course code:%s\n", courseCode);
    	for(String indexCode: indexlist)
    	{
    		System.out.printf("Index: %s\n",indexCode);
    		System.out.printf(String.format("|%-20s|%-20s|%-20s|\n", "Student name", "Gender","Nationality"));
    		for(Student_details student_info: allStudents)
    			if(student_info.isRegistered(indexCode))
    			{
    	        	System.out.printf(String.format("|%-20s|%-20s|%-20s|\n", student_info.getName(),student_info.getGender(),student_info.getNationality()));
    			}
    	}
    }

    
    /**
     * the execution of the our program
     * provide the UI for the user to choose the things that they want to use 
     * ask users for instruction and use proper methods
     * 
     * data is saved to local until exiting this program
     * @param user				the class of the current user
     */
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
			catch(InputMismatchException e) {System.out.println("Please enter a proper input"); scanner.nextLine();continue;}
		
			switch( input )
			{

			case 1: {
				EditAccessPeriod();
			}break;

			case 2: {
				AddaStudent();
			}break;

			case 3: {
				UpdateCourse();
			}break;
			
			case 4: {
				Update_Index();
			}break;
			
			case 5: {
				UpdateClass();
			}break;
			
			case 6:{
				System.out.println("Enter the index that you wanted.");
				String indexCode = sc.nextLine();
				System.out.println("Enter the capacity that you wanted.");
				int newCapcity = sc.nextInt();
				UpdateCapacity(indexCode, newCapcity);
			}break;

			case 7: {
				String indexCode = null;
				do {
					System.out.println("Enter the index that you wanted.");
					indexCode = sc.nextLine();
					if(courseManager.checkIndexExists(indexCode))break;
				}while(true);
				CheckAvailableSlot(indexCode);
			}break;

			case 8: {
				String indexCode = null;
				do {
					System.out.println("Enter the index that you wanted.");
					indexCode = sc.nextLine();
					if(courseManager.checkIndexExists(indexCode))break;
				}while(true);
				PrintStudentByIndex(indexCode);
			}break;

			case 9: {
				String courseCode = null;
				do {
					System.out.println("Enter the course code that you wanted.");
					courseCode = sc.nextLine();
					if(courseManager.getCourse(courseCode)!=null)break;
					System.out.println("No course with specified code exists. Please try again.");
				}while(true);
				PrintStudentByCourse(courseCode);
			} break;

			case 10: {System.out.print(this.adminOptions);}break;

			case 11:
			{
				DisplayStudentInfo();
			}break;
			
			case 12:
			{
				DisplayCourseInfo();
				DisplayIndexInfo();
			}break;
			
			case -1: { quit = true; } break;

			default: 
			{ 
				System.out.println("Please enter a number among options provided.");
				loopInput = true;
			}
			}//end switch
			if(quit)
			{
				starsDatabase.save();
		    	courseManager.save();
		    	return;
			}
		}
    }
}
