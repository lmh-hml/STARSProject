package stars;
import java.util.*;


public class StudentProgram
{
	private Student_details currentUser;
	private CourseManager courseManager;
	private StarsDatabase starsDatabase;
	private Scanner scanner;
	private StarsNotifier notifier;
	
	private final String studentOptions = "1. Add course\n"
			+ "2. Drop course\n"
			+ "3.Check/Print Courses Registered\n"
			+ "4. Check vacancies available\n"
			+ "5. Change Index Number of course\n"
			+ "6. Swap index with another student\n"
			+ "7. Show options\n"
			+ "0. Quit";
	
	/**StudentProgram has attributes of Student_details, CourseManager and StarsDatabase*/
	public StudentProgram( CourseManager courseManager, StarsDatabase starsDatabase, StarsNotifier notifier) {
		this.courseManager=courseManager;
		this.starsDatabase=starsDatabase;
		this.notifier = notifier;
	}
	
	public void setCurrentStudent(Student_details student)
	{
		currentUser = student;
	}
	
	public void run(Student_details student)
	{
		assert (student) != null;
		this.currentUser = student;
		boolean loopInput = false;
		boolean quit = false;
		
		if(currentUser== null)
		{
			System.out.println("Unable to find an account asociated with this user. Please contact adminstrator for assistance.");
			quit = true;
			return;
		}
		System.out.format("Welcome to STARS Students, %s !\n", currentUser.getName());
		System.out.print(studentOptions);
		System.out.format("Please enter an option: ");

		while( loopInput || !quit )
		{
			
			System.out.format("Please enter an option: ");
			int input  = -1;
			try {  input = scanner.nextInt(); } 
			catch(InputMismatchException e) {System.out.println("Please enter a proper input"); scanner.nextLine();}
			
			switch( input )
			{
			
			case 1: 
			{
				//Add
			}break;
			
			case 2: {
				//drop
			}break;
			
			case 3: {
				//check print courses registered
			}break;
			
			case 4: {

				//check vacancies available
			}break;
			
			case 5: {
				
				//Change course index
			}break;
			
			case 6: {
				
				//swap with another student.
				
			} break;
			
			case 7: {System.out.print(studentOptions);}
			
			case 0: { quit = true; } break;
			
			default: 
			{ 
				System.out.println("Please enter a number among options provided.");
				loopInput = true;
			}
			}//end switch
			if(quit)break;
		}
		
		
	}

	
	//Method to register a index of course for the student
	public void addIndex(String indexCode) {
		
		//check if student in other index of same course
		Index_details index = courseManager.getIndex(indexCode);
		indexCode = index.getIndexCode();
		Course course  = courseManager.getCourse(index.getCourseCode());
		
		if (index==null) {
			System.out.format("Index %s does not exist.\n", indexCode);
		}
		else if (index.isRegistered(currentUser.getName())== true) 
		{
			System.out.format("Student is already in index %s!\n", indexCode);
		}
		else if ( currentUser.getAU()+ course.getAU() >20) {
			System.out.println("Student's cuurent amount of AU exceeds maximum AU");
		}
		else {
			currentUser.setAU(currentUser.getAU()+ course.getAU() );
			int vacancy = courseManager.getVacancy(indexCode);
			System.out.format("There were %d slots left in this index %s\n", vacancy, index.getCourseCode() );
			if (vacancy==0) {
				currentUser.addWaitlist(indexCode);//not using setcoursewaitlist
				index.addStudentToWaitlist(currentUser); //add student to waitlist
				System.out.format("Added Index %s to registered list.", index.getCourseCode());
			}
			else {
				index.registerStudent(currentUser); //add student to course
				currentUser.addIndex(index.getIndexCode());
				System.out.format("There are %d slots left in this index %s\n", index.getVacancy(), index.getCourseCode() );
			}
		}
	}
	
	//Method to drop a course for the student
	public void dropIndex(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		currentUser.removeIndex(indexCode);
		
		int newAU = currentUser.getAU() - courseManager.getIndexAU(index); //shouldn't this be add ?
		currentUser.setAU(newAU);
		courseManager.removeStudentFromIndex(indexCode, currentUser);
		
		if (index.getWaitingStudents().length!=0) {
			Student_details newstudent = starsDatabase.getStudentByUsername(index.getWaitingStudents()[0]);//first out student
			if(newstudent!= null)
			{
				index.removeFromWaitlist(newstudent.getName()); //remove first out student from course waitlist
				newstudent.removeFromWaitlist(indexCode);;//remove first out course from student waitlist
				index.registerStudent(newstudent); //add student name to course
				newstudent.addIndex(indexCode);
			}
		}
	}
	
	/**
	 * Prints the specifiedindex and the classes under it.
	 * @param indexCode The code of the index.
	 */
	public void printIndex(String indexCode)
	{
		Index_details index = courseManager.getIndex(indexCode);
		if(index==null)
		{
			System.err.println("Specified Index with code %s does not exist.");
			return;
		}
		System.out.println(index+": ");
		System.out.println("\t"+courseManager.getLab(indexCode));
		System.out.println("\t"+courseManager.getLectures(indexCode)+" ");
		System.out.println("\t"+courseManager.getTut(indexCode));  
	}
	
	/**
	 * Prints the indexes the student is registered in and waiting for.
	 */
	public void CheckAndPrintRegistered() {
		System.out.format("Courses currently registered in: %d courses.\n", currentUser.getCourseRegistered().size());
		for (String indexCode : currentUser.getCourseRegistered())
		{
			if(indexCode == FlatFileObject.EmptyString)continue;
			printIndex(indexCode);
		}
		
		System.out.format("Courses currently waiting for: %d courses.\n",currentUser.getCourseWaitlist().size());
		for (String indexCode : currentUser.getCourseWaitlist())
		{
			if(indexCode == FlatFileObject.EmptyString)continue;
			printIndex(indexCode);
		}
	}
	
	/**Method to check the vacancies of a course index**/
	public void CheckVacancies(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		if (index == null) {
			System.out.format("Specified index with code %s does not exist\n",indexCode);
		}
		else {
			System.out.format("%s: Number of vacancies left: %d\n"+indexCode, index.getVacancy());
		}
	}
	
	//Method to change the course index for the student
	public void changeCourseIndex(String oldindex, String newindex) {
		//check if student in other index of same course
		if (courseManager.getIndex(newindex).isRegistered(currentUser.getName())== true) {
			System.out.println("Already in course"); //if student already in newindex
		}
		else if (courseManager.getIndex(oldindex).isRegistered(currentUser.getName())== false) {
			System.out.println("Not in previous course"); //else if student not in oldindex
		}
		else if ( courseManager.getIndex(oldindex)==null ||courseManager.getIndex(oldindex)==null) {
			System.out.println("Course does not exist"); //else if any index does not exist
		}
		else {
			this.dropIndex(oldindex);
			this.addIndex(newindex);
			System.out.println("Index Number "+oldindex+" has been changed to "+newindex);
		}
	}
	
	//Method to swap index of the student and another student
	public void SwapIndex(String userindex, Student_details newstudent, String stuindex) {
		if (courseManager.getIndex(userindex).getCourseCode()!=courseManager.getIndex(stuindex).getCourseCode()) {
			System.out.println("Indexes not in same course");
		}
		else if (courseManager.getIndex(userindex).isRegistered(currentUser.getName())== false) {
			System.out.println("User not in course"); //if student not in userindex
		}
		else if (courseManager.getIndex(stuindex).isRegistered(newstudent.getName())== false) {
			System.out.println("New student not in course"); //else if newstudent not in stuindex
		}
		else {
			currentUser.removeIndex(userindex);
			currentUser.addIndex(stuindex);
			newstudent.removeIndex(stuindex);
			newstudent.addIndex(userindex);
			courseManager.removeStudentFromIndex(userindex, currentUser);  //swap names of students from both courses
			courseManager.addStudentToIndex(stuindex, currentUser);
			courseManager.addStudentToIndex(userindex, newstudent);
			courseManager.removeStudentFromIndex(userindex, newstudent);     //swap names of student from both courses
			starsDatabase.writeDatabaseFiles();
			System.out.println(currentUser.getMatric_num()+"-Index Number "+userindex+" swapped with "+ newstudent.getMatric_num()+"-Index Number "+stuindex);
		}
	}
	


	public void printStudentDetails(Student_details student)
	{
		System.out.format("Student Name: %s\n", student.getName());
		System.out.format("Matric Number: %s\n", student.getMatric_num());
		System.out.format("Study year: %d\n", student.getStudyYear());
		System.out.format("Gender: %s\n", student.getGender());
		System.out.format("Nationality: %s\n", student.getNationality());
		System.out.format("Total AU of registered courses:  %d\n", student.getAU());
	}
}
