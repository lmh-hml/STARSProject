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
		
		if (index.isRegistered(currentUser.getName())== true) {
			System.out.println("Already in course");
		}
		else if ( currentUser.getAU()+ course.getAU() >20) {
			System.out.println("Amount of AU exceeds maximum AU");
		}
		else if (courseManager.getAllIndexes().contains(index)==false) {
			System.out.println("Index does not exist");
		}
		else {
			currentUser.setAU(currentUser.getAU()+ course.getAU() );
			int vacancy = courseManager.getVacancy(indexCode);
			System.out.format("There were %d slots left in this index %s\n", vacancy, index.getCourseCode() );
			if (vacancy==0) {
				currentUser.getCourseWaitlist().add(indexCode); //not using setcoursewaitlist
				index.addStudentToWaitlist(currentUser.getName()); //add student to waitlist
				System.out.println("Course added to waitlist");
			}
			else {
				System.out.format("There were %d slots left in this index %s\n", index.getVacancy(), index.getCourseCode() );
				index.registerStudent(currentUser); //add student to course
				System.out.format("There are %d slots left in this index %s\n", index.getVacancy(), index.getCourseCode() );
			}
			starsDatabase.writeDatabaseFiles();
		}
	}
	
	//Method to drop a course for the student
	public void dropIndex(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		currentUser.removeCourse(indexCode);
		int newAU = currentUser.getAU() - courseManager.getAU(indexCode); //shouldn't this be add ?
		currentUser.setAU(newAU);
		courseManager.removeStudentFromIndex(indexCode, currentUser);
		
		if (index.getWaitingStudents().length!=0) {
			Student_details newstudent;
			newstudent = starsDatabase.getStudentByUsername(index.getWaitingStudents()[0]);//first out student
			if(newstudent!= null)
			{
				index.removeFromWaitlist(newstudent.getName()); //remove first out student from course waitlist
				newstudent.getCourseWaitlist().remove(index);//remove first out course from student waitlist
				index.registerStudent(newstudent); //add student name to course
				newstudent.addCourse(indexCode);
			}
			//write back to student file
		}
		else {
			; //vacancy of course ++ (Already handled by index class)
		}
		starsDatabase.writeDatabaseFiles();


	}
	
	//Method to print all registered courses for the student
	public void CheckAndPrintRegistered() {
		System.out.println("Courses currently registered in: ");
		for (String indexCode : currentUser.getCourseRegistered())
		{
			System.out.print(indexCode+" ");
			System.out.print(courseManager.getAU(indexCode)+" ");
			System.out.print(courseManager.getLab(indexCode)+" ");
			System.out.print(courseManager.getLectures(indexCode)+" ");
			System.out.println(courseManager.getTut(indexCode));  //print using coursemanager for each i
		}
		
		System.out.println("Courses currently waiting in: ");
		for (String indexCode : currentUser.getCourseWaitlist())
		{
			System.out.print(indexCode+" ");
			System.out.print(courseManager.getAU(indexCode)+" ");
			System.out.print(courseManager.getLab(indexCode)+" ");
			System.out.print(courseManager.getLectures(indexCode)+" ");
			System.out.println(courseManager.getTut(indexCode));  //print using coursemanager for each i
		}
	}
	
	//Method to check the vacancies of a course index
	public void CheckVacancies(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		if (index == null) {
			System.out.println("Index does not exist");
		}
		else {
			System.out.println("Number of vacancy : "+index.getVacancy());
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
			currentUser.removeCourse(userindex);
			currentUser.addCourse(stuindex);
			newstudent.removeCourse(stuindex);
			newstudent.addCourse(userindex);
			courseManager.removeStudentFromIndex(userindex, currentUser);  //swap names of students from both courses
			courseManager.addStudentToIndex(stuindex, currentUser);
			courseManager.addStudentToIndex(userindex, newstudent);
			courseManager.removeStudentFromIndex(userindex, newstudent);     //swap names of student from both courses
			starsDatabase.writeDatabaseFiles();
			System.out.println(currentUser.getMatric_num()+"-Index Number "+userindex+" swapped with "+ newstudent.getMatric_num()+"-Index Number "+stuindex);
		}
	}
	
}
