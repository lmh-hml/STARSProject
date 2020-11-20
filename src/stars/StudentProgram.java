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
		else if (isStudentRegistered(currentUser, index)== true) 
		{
			System.out.format("Student is already registered in index %s!\n", indexCode);
		}
		else if ( currentUser.getAU()+ course.getAU() >20) {
			System.out.println("Student's cuurent amount of AU exceeds maximum AU");
		}
		else {
			int vacancy = courseManager.getVacancy(indexCode);
			System.out.format("There were %d slots left in this index %s\n", vacancy, index.getCourseCode() );
			if (vacancy==0) {
				addStudentIndexWaitlist(currentUser, index);
				System.out.format("Added Index %s to waitlist.\n", index.getIndexCode());
			}
			else {
				addStudentIndexRegistered(currentUser, index);
				System.out.format("There are %d slots left in this index %s\n", index.getVacancy(), index.getCourseCode() );
			}
		}
	}
	
	//Method to drop a course for the student
	public void dropIndex(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		currentUser.removeIndex(indexCode);
		
		removeStudentFromIndexRegistered(currentUser,index);
		
		if (index.getWaitingStudents().size()!=0) {
			Student_details newstudent = starsDatabase.getStudentByUsername(index.getFirstWaitingStudent());//first out student
			if(newstudent!= null)
			{
				index.removeFromWaitlist(newstudent.getName()); //remove first out student from course waitlist
				newstudent.removeFromWaitlist(indexCode);;//remove first out course from student waitlist
				index.registerStudent(newstudent); //add student name to course
				newstudent.addIndex(indexCode);
			}
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
			System.out.println("Student is already in course "); //if student already in newindex
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
		
		Index_details userIndex = courseManager.getIndex(userindex);
		Index_details stuIndex = courseManager.getIndex(stuindex);
		
		if (!userIndex.getCourseCode().equals(stuIndex.getCourseCode())) {
			System.out.println("Indexes not in same course");
		}
		else if (isStudentRegistered(currentUser, userIndex)== false) {
			System.out.println("Current student not in index!"); //if student not in userindex
		}
		else if (isStudentRegistered(newstudent, stuIndex)== false) {
			System.out.println("Student to swap index with is not in the index!"); //else if newstudent not in stuindex
		}
		else {
			currentUser.removeIndex(userindex);
			currentUser.addIndex(stuindex);
			newstudent.removeIndex(stuindex);
			newstudent.addIndex(userindex);
			removeStudentFromIndexRegistered( currentUser,userIndex);  //swap names of students from both courses
			addStudentIndexRegistered(currentUser,stuIndex);
			addStudentIndexRegistered(newstudent, userIndex);
			removeStudentFromIndexRegistered(newstudent, stuIndex);     //swap names of student from both courses
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
	/**
	 * Prints the specified index and the classes under it.
	 * @param indexCode The code of the index.
	 */
	public void printIndex(String indexCode)
	{
		Index_details index = courseManager.getIndex(indexCode);
		if(index==null)
		{
			System.err.format("Specified Index with code %s does not exist.\n",indexCode);
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
	public void printRegisteredIndexes(Student_details student) {
		System.out.format("%s: Courses currently registered in: %d courses.\n", student.getName(),student.getIndexRegistered().size());
		for (String indexCode : student.getIndexRegistered())
		{
			if(indexCode == FlatFileObject.EmptyString)continue;
			printIndex(indexCode);
		}
		
		System.out.format("Courses currently waiting for: %d courses.\n",student.getIndexWaitlist().size());
		for (String indexCode : student.getIndexWaitlist())
		{
			if(indexCode == FlatFileObject.EmptyString)continue;
			printIndex(indexCode);
		}
	}
	

	public boolean isStudentRegistered(Student_details student, Index_details index)
	{
		return ( student.getIndexRegistered().contains(index.getIndexCode()) );
	}
	public boolean isStudentWaiting(Student_details student, Index_details index)
	{
		return ( student.getIndexWaitlist().contains(index.getIndexCode()));
	}
	public void addStudentIndexRegistered(Student_details student, Index_details index)
	{
		if(isStudentRegistered(student, index))return;
		if(index.getVacancy()==0)return;
		student.addIndex(index.getIndexCode());
		index.registerStudent(student.getMatric_num());
		student.setAU(student.getAU()+courseManager.getIndexAU(index));
		if(isStudentWaiting(student, index))removeStudentFromIndexWaitlist(student, index);
	}
	public void addStudentIndexWaitlist(Student_details student, Index_details index)
	{
		if(isStudentWaiting(student, index) || isStudentRegistered(student, index))return;
		student.addWaitlist(index.getIndexCode());
		index.registerStudent(student.getMatric_num());
		student.setAU(student.getAU()+courseManager.getIndexAU(index));
	}
	public void removeStudentFromIndexRegistered(Student_details student, Index_details index)
	{
		student.removeIndex(index.getIndexCode());
		index.removeFromRegistered(student.getMatric_num());
		int au = courseManager.getIndexAU(index);
		if(au > 0)student.setAU( student.getAU() - au );
	}
	public void removeStudentFromIndexWaitlist(Student_details student, Index_details index)
	{
		student.removeFromWaitlist(index.getCourseCode());
		index.removeFromWaitlist(student.getMatric_num());
	}
	public boolean swapIndex(Student_details student1, String index1, Student_details student2, String index2)
	{
		Index_details userIndex = courseManager.getIndex(index1);
		Index_details stuIndex = courseManager.getIndex(index2);
		if(userIndex == null || stuIndex == null)return false;
		student1.removeIndex(index1);
		student1.addIndex(index2);
		student2.removeIndex(index2);
		student2.addIndex(index1);
		removeStudentFromIndexRegistered( student1,userIndex);  //swap names of students from both courses
		addStudentIndexRegistered(student1,stuIndex);
		addStudentIndexRegistered(student2, userIndex);
		removeStudentFromIndexRegistered(student2, stuIndex);     //swap names of student from both courses
		return true;
	}

}
