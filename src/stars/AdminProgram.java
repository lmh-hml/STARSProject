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

import java.util.Arrays;

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
    private String hashed_default_password = PasswordModule.generatePasswordHash(default_password);
    //public CourseManager courseManager;
    private Scanner sc = new Scanner(System.in);//you will need to delete this later
    
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
			+ "-1. Quit\n";
    
    AdminProgram(StarsDatabase StarDatabase, CourseManager CourseManager)//, Scanner sc)
    {
        courseManager = CourseManager;
    	starsDatabase = StarDatabase;    
    }

    
    /**
     * edit the access period for the student to do add/drop
     * prompt the user to enter the start time and end time of the access period
     * the access period is written to an external file, which can be accesses by the StarsMain
     * 
     * error will be checked
     * 1: illegal input for date and time
     * 2: start time is later than end time
     * 
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
     * create a student with an empty registered course and waitlist
     * 
     * We allow students to have the same name but id has to be unique
     * 
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
        
        new_student.setMatric_num(matric);
        
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
    
        System.out.println("The password is setted to default");
        new_user.setPassword(hashed_default_password);

        new_user.setEmail(userName + "@e.ntu.edu.sg");
        
        String accountType = "";
        do {
        	System.out.println("Enter the accountType(Admin/Student)");
            accountType = sc.nextLine();
            if(accountType.equals("Admin") || accountType.equals("Student"))
            	break;
        }while(true);
        new_user.setAccountType(accountType);
        
        
        String write = "n";
        do {
        	System.out.println("You sure that you have correctly entered all your info?(y/n)");
            write = sc.nextLine();
            if(write.equals("y")||write.equals("n"))
            	break;
        }while(true);
        
        if(write=="n")
        {
        	System.out.println("Your inputs are not saved.");
        }
        
        starsDatabase.addUser(new_user);
        starsDatabase.addStudent(new_student);

        //displaying all the students' info
        DisplayStudentInfo();
        
    }
    
    public void DisplayStudentInfo()
    {
    	System.out.println("Rows\t|\tStudent name\t|\tStudent's id\t|\tUser name");
        int rows = 1;
        for(Student_details student : starsDatabase.getAllStudents())
        {
        	System.out.printf("%d\t|\t%s\t\t|\t%s\t|\t",rows, student.getName(),student.getMatric_num());
        	rows++;
        	//just in case you need the user info as well
        	User_details user = starsDatabase.getUser(student.getUserName());
        	System.out.printf("%s",user.getUsername());
        	System.out.printf("\n");
        }
    }
    
    /**
     * not used function, delete it if you don't really need this
     * 
     * This function deletes the student from the database and rewrites the database 
     * warning, this is not yet complete and doesn't garanteen the integrity of database after using
     */
    private void deleteStudent()
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
     * display the information about the course
     */
    public void DisplayCourseInfo()
    {
    	System.out.println("Rows\t|\tCourse code\t|\tCourse Name\t|\tAU\t|\tindexes");
        int rows = 1;
        for(Course course : courseManager.getAllCourses())
        {
        	System.out.printf("%d\t|\t%s\t\t|\t%s\t|\t%d",rows,course.getcoursecode(),course.getcourseName(),course.getAU());
        	for(String index: course.getIndexCodes())
        	{
        		System.out.printf("%s",index);
        	}
        	rows++;
        	System.out.printf("\n");
        }
    }
    
    /**
     * add/update the index
     * btw the index is immutable(by now)
     * so be careful when you create them
     * 
     * a bit from others, updating is done if you enter y for changes
     * and you actually entered legal input
     * 
     * others do update at the end, so both don't have to worry about program crashing in the middle
     * and gives you weird data
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
    	System.out.println("Rows\t|\tIndex code\t|\tCourse code\t|\tAU\t|\tCapacity");
        int rows = 1;
        for(Index_details index: courseManager.getAllIndexes())
        {
        	System.out.printf("%d\t|\t%s\t\t|\t%s\t|\t%d",rows,index.getIndexCode(),index.getCourseCode(),index.getCapacity());
        	rows++;
        	System.out.printf("\n");
        }
    }
    
    /**
     * helper function for updateIndex
     * update the CourseDatabase
     * 
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
     * 
     * example: LEC, TUT, LAB
     * together with group, venue and time
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
    		System.out.println("2.remove a class(not available)");
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
     * display the info about indexClass in index
     *
     * @param indexCode
     */
    public void DisplayClassesInfo(String indexCode)
    {
    	System.out.printf("index is %s\n",indexCode);
		int rows =1;
		for(IndexClass classes : courseManager.getIndex(indexCode).getIndexClasses())
		{
			System.out.printf("%d\t|\t",rows);
			System.out.println(classes.toFlatFileString());
		}
    }
    
    /**
     * helper function
     * this can get the input of time
     * 
     * check two errors
     * 1. illegal input
     * 2. start time is later than end time
     * 
     * returns a ArrayList<LocalTime>, index 0 is start time, index 1 is end time 
     * @return
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
     * the only function that allows you to modify data after the start of registration.
     * 
     * @param indexCode
     * @param newCapacity
     * @throws IllegalArgumentException
     */
    public void UpdateCapacity(String indexCode, int newCapacity) throws IllegalArgumentException
    {
    	try {
    		if(newCapacity<courseManager.getIndex(indexCode).getRegisteredStudents().size())
    		{
    			throw new IllegalArgumentException("new Capacity is less than number of student registered.");
    		}
    		if(courseManager.getIndex(indexCode)==null)
    		{
    			throw new IllegalArgumentException("IndexCode not found");
    		}
    	}finally{}
    	
    	courseManager.getIndex(indexCode).setCapacity(newCapacity);
    	
    	
    	Collection<Student_details> studentDB = starsDatabase.getAllStudents();
    	
    	Index_details index = courseManager.getIndex(indexCode);
		indexCode = index.getIndexCode();
		Set<String> studentList = index.getWaitingStudents();
    	int studentRegister = courseManager.getIndex(indexCode).getRegisteredStudents().size();
    	String studentName;
    	Student_details currentUser = null;
    	
    	while(studentList.size()>0 && newCapacity>studentRegister)
    	{
    		studentName = index.getFirstWaitingStudent();
    		for(Student_details student : studentDB)
    		{
    			if(student.getName()==studentName||student.isWaiting(indexCode))
				{
    				//if found
					currentUser = student;
					currentUser.removeFromWaitlist(indexCode);
		    		index.removeFromWaitlist(studentName);
		    		index.registerStudent(currentUser); //add student to course
		    		studentList.remove(studentName);
		    		studentRegister++;
				}
    		}
    	}
    }

    /**
     * print out the available slots for all indexes
     * @param indexCode
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
     * @param indexCode
     */
    void PrintStudentByIndex(String indexCode)
    {
    	Index_details target = courseManager.getIndex(indexCode);
    	if(target==null)
    	{
    		System.out.println("index not found");
    		return;
    	}
    	System.out.printf("Index: %s\n",indexCode);
    	System.out.println("Rows\t|\tStudent name\t|\tMatric number\t|\tGender\t|\tNationality");
    	int rows = 1;
    	for(String student : target.getRegisteredStudents())
		{
    		for(Student_details student_info: starsDatabase.getAllStudents())
    			if(student_info.getName()==student&&student_info.isRegistered(indexCode)&&(!student.equals(" ")))
    			{
    				System.out.printf("%d\t|\t%s\t|\t%s\t|\t%s\t|\t%s\n",rows, student, student_info.getMatric_num(),student_info.getGender(),student_info.getNationality());
    				rows++;
    			}
		}
    	System.out.printf("\n");
    }
    
    /**
     * print out the student info for those who has taken courseCode
     * @param indexCode
     */
    void PrintStudentByCourse(String courseCode)
    {
        Set<String> indexlist = courseManager.getCourse(courseCode).getIndexCodes();
        System.out.printf("Course code:%s\n", courseCode);
    	for(String index: indexlist)
    	{	
    		PrintStudentByIndex(index);
    	}
    }
    
    
    public static void main(String[] args)
    {
    	
    	StarsDatabase exampleDatabase = new StarsDatabase("TestStudents.txt","TestUsers.txt");
    	CourseManager courseManager = new CourseManager("TestCourses.txt","TestIndexes.txt");
		AdminProgram Admin = new AdminProgram(exampleDatabase, courseManager);
		User_details user = new User_details();
    	Admin.run(user,exampleDatabase,courseManager);
    	
    }
    
    /**
     * the execution of the our program
     * ask users for instruction and use proper methods
     * 
     * data is saved to local until exiting this program
     * @param user
     * @param exampleDatabase
     * @param courseManager
     */
    void run(User_details user,StarsDatabase exampleDatabase,CourseManager courseManager)
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
			}

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
				}while(true);
				PrintStudentByCourse(courseCode);
			} break;

			case 10: {System.out.print(this.adminOptions);}

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