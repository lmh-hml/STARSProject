package stars;
import java.io.Console;
import java.util.*;

/**
 * A class that represents the program that a student will use during add/drop period.
 * This class serves as a boundary class between the user and the databases of the MySTARS system.
 * @author Lucius Lee, Lai Ming Hui
 * @since  10/11/2020
 * @version 1.0.0
 */

public class StudentProgram
{
	/**The Student_details object associated with the current user.**/
	private Student_details currentUser;
	/**Member CourseManager object. This  object is injected through the constructor.**/
	private CourseManager courseManager;
	/**Member StarsDatabase object. This  object is injected through the constructor.**/
	private StarsDatabase starsDatabase;
	private Scanner scanner = new Scanner(System.in);
	/**An object implementing StarsNotifier used to send notifications**/
	private StarsNotifier notifier;
	/**AU limit**/
	private final int AU_LIMIT = 21;
	/**Console for pasword input**/
	private Console console = System.console();
	
	/**The messages foreach option available in student program.**/
	private final static String[] StudentOptions = {"1. Add course"
			, "2. Drop course"
			, "3. Check/Print Courses Registered"
			, "4. Check vacancies available"
			, "5. Change Index Number of course"
			, "6. Swap index with another student"
			, "7. Print index details"
			, "8. Show options"
			, "0. Quit"
	};

			
	/**StudentProgram has attributes of Student_details, CourseManager and StarsDatabase
	 * @param courseManager The courseManager object to be passed into this program
	 * @param starsDatabase The StarsDatabase object to be passed into this program
	 */
	public StudentProgram( CourseManager courseManager, StarsDatabase starsDatabase) {
		this.courseManager=courseManager;
		this.starsDatabase=starsDatabase;
		this.notifier = StarsNotifier.getNotifificationMethod(StarsNotificationType.Email);
	}
	/**
	 * Set the current user's Student_details object. When set, all program operations such as add, drop will be performed for this specified student.
	 * @param student The specified student to be the current user.
	 */
	public void setCurrentStudent(Student_details student)
	{
		currentUser = student;
	}
	/**Method called by host application to run this instance of the student program.
	 * @param student Student_details object associated with the current user of the program.
	 */
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
		for(String option: StudentOptions)System.out.println(option);

		while( loopInput || !quit )
		{
			System.out.format("Main Menu:Please enter an option (%d-%d): ", 0, StudentOptions.length-1);
			int input  = -1;
			try {  input = scanner.nextInt(); } 
			catch(InputMismatchException e) {System.out.println("Please enter a proper input"); scanner.nextLine();}

			switch( input )
			{
			
			case 1: { run_AddIndex(); }break;
			case 2: { run_DropIndex(); }break;
			case 3: {
				printRegisteredIndexes(student);
				printWaitingIndexes(student);
			}break;
			
			case 4: {
				run_CheckVacancies();
			}break;
			
			case 5: {
				run_ChangeIndex();
			}break;
			
			case 6: {
				run_SwapIndex();
			} break;
			
			case 7: {
				run_PrintIndex();
			}break;
			case 8:
			{
				for(String option: StudentOptions)System.out.println(option);
			}break;
			
			case 0: { 
				System.out.println("Are you sure you want to quit ?"); 
				scanner.nextLine();
				quit = promptYesNo("Please enter Y or N:"); 
			} break;
			
			default: 
			{ 
				System.out.println("Please enter a number among options provided.");
				loopInput = true;
			}
			}//end switch
			if(quit)break;
		}
		notifyConfirmationEmail();
	}

//CLIENT METHODS: 
	/**
	 * Prompts the user for an index code. This method will loop until a valid index code is 
	 * entered, or the user enters 0, whereby this method will return null.
	 * @param message The message to prompt the user before input.
	 * @return The Index_detail object with the input index code from the current database, or null
	 * if such an object cannot be found.
	 */
	private Index_details promptForIndexCode(String message)
	{
		Index_details index = null;
		String inputIndex = "";
		while(index == null && !inputIndex.equals("0"))
		{
			System.out.println(message);
			inputIndex = scanner.nextLine();
			if(inputIndex.equals("0"))return null;
			index = courseManager.getIndex(inputIndex);
			if(index == null) {System.out.println("No such index exists. Please enter the code again.");}
		}
		return index;	
	}
	/**
	 * Prompts the user for a Y/N input. This method will loop until the user enters a "Y", "y", "N" or "n".
	 * If the user enters "Y" or"y", the method returns true. If the user enters "N" or "n", the method returns false.
	 * @param message The message to prompt the user before input.
	 * @return True if the user input "Y" or "y", false if the user input "N" or "n".
	 */
	private boolean promptYesNo(String message)
	{
		String input = "";
		while(!input.equals("Y") && !input.equals("N"))
		{
			System.out.println(message);
			input = scanner.nextLine().toUpperCase();
		}
		boolean yesNo = false;
		if(input.equals("Y"))yesNo = true;
		else if(input.equals("N"))yesNo =false;
		return yesNo;	
	}
	/**
	 * Method implementing the "Add" function for the student program.
	 * This methods prompts the user for an index code, then attempts to add register the current user into the index specified.
	 */
	private void run_AddIndex()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter the code of the index that you want to register: (Press 0 to go back) ");
		if(index==null)return;
		addIndex(index.getIndexCode());
	}
	/**Method implementing the "Drop" function for the student program.
	 * This methods will prompt the user for an index code, then attempts to drop the index specified for the current user.
	 */
	private void run_DropIndex()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter the code of the index that you want to drop: (Press 0 to go back) ");
		if(index==null)return;
		dropIndex(index.getIndexCode());
	}
	/**
	 * Method implementing the "Check vacancies" fucntion for the student program.
	 * This method prompts the user for an index code, the prints out the index's vacancies.
	 */
	private void run_CheckVacancies()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter the index code you want to check:");
		if(index==null)return;
		CheckVacancies(index.getIndexCode());
	}
	/**Method implementing the "Change index" function for the student program.
	 * This method prompts the user for two indexes: the first index should be one that the user has registered for,
	 * and the other is an index under the same course that the user has not registered or in the waitlist for.
	 * This method will then attempt to exchange the two indexes.
	 */
	private void run_ChangeIndex()
	{
		scanner.nextLine();
		Index_details id1 = promptForIndexCode("Please enter the index you want to change:");
		if(id1==null)return;
		Index_details id2 = promptForIndexCode("Please enter the index you want to change with:");
		if(id2==null)return;
		changeCourseIndex(id1.getIndexCode(), id2.getIndexCode());
	}
	/**
	 * Method implementing the "Swap index" function of the student program.
	 * This method will prompt the current user for the index to swap, and then prompts the index and credentials of the other student.
	 * If the credentials given are correct and the indexes are valid and already registered by both students, this method will swap their indexes.
	 */
	private void run_SwapIndex()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter index you want to swap:");
		if(index==null)return;
		
		
		String input = "";
		User_details user = null;
		
		System.out.println("Enter matriculation number of student to swap with:");
		
		while(user == null && input!="0")
		{
			input = scanner.nextLine();
			if(input == currentUser.getMatric_num())
			{
				System.out.println("You can not swap index with yourself.");
				continue;
			}
			user =  starsDatabase.getUser(starsDatabase.getStudentbyMatric(input).getUserName());
			if(user == null) {System.out.println("Matriculation number input does not belong to a student.");break;}
		}
		
		if(user!=null)
		{
			System.out.println("Enter password of the student to swap with:");
			boolean correct = false;
			input = "";
			while(!correct && input!="0")
			{
				//input = scanner.nextLine();
				input = String.valueOf(console.readPassword());
				correct = PasswordModule.verifyPassword(input, user.getPassword());
				if(!correct)System.out.println("Password is incorrect. Please try again.");
			}
		}
		
		if(input=="0")return;
		Index_details index2 = promptForIndexCode("Please enter index you want to swap with:");
		if(index2 == null)return;
		SwapIndex( index.getIndexCode(), starsDatabase.getStudent(user), index2.getIndexCode());
		
	}
	/**Method implementing the print index function of the student program.
	 * This method will prompt the user for the index to print, and then print the index's details if the specified index is valid.
	 */
	private void run_PrintIndex()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter theindex code of the index you want to see details of:");
		if(index==null)return;
		printIndex(index.getIndexCode());
	}

	//PROGRAM METHODS
	/**
	 * Method to register a an index for a student.
	 * If the index has vacancies, the student is added to it.
	 * If the index has no more vacancies, the student is put into the waitlist instead.
	 * @param indexCode The specified index code of the index to register.
	 */
	private void addIndex(String indexCode) {
		//check if student in other index of same course
		Index_details index = courseManager.getIndex(indexCode);
		if (index==null) { System.out.format("Add failed: Index %s does not exist.\n", indexCode); return;}
		Course course  = courseManager.getCourse(index.getCourseCode());
		ArrayList<Index_details> clashes = isIndexClashWithStudentRegistered(index, currentUser);
				
		if (isStudentRegistered(currentUser, index)== true) 
		{
			System.out.format("Add failed: Student is already registered in index %s!\n", indexCode);
		}
		else if (isStudentRegisteredCourse(currentUser.getMatric_num(), course))
		{
			System.out.format("Add failed: Student is already registered in the same course: %s!\n", course.getcoursecode());
		}
		else if ( currentUser.getAU()+ course.getAU() >AU_LIMIT) {
			System.out.println("Add failed: Student's cuurent amount of AU exceeds maximum AU");
		}
		else if( clashes.size()!=0)
		{
			System.out.format("Add failed: There is a clash between index %s and the follwoing indexes: \n", indexCode);
			for(Index_details id : clashes)
			{
				System.out.format("    %s", id.getIndexCode());
			}
		}
		else {
			int vacancy = courseManager.getVacancy(indexCode);
			if (vacancy==0) {
				addStudentIndexWaitlist(currentUser, index);
				System.out.format("Add: Specified index is full. Added index %s to waitlist.\n", index.getIndexCode());
			}
			else {
				addStudentIndexRegistered(currentUser, index);
				System.out.format("Add successful: There are %d slots left in this index %s\n", index.getVacancy(), index.getCourseCode() );
			}
		}
	}	
	/**
	 * Method to drop an index from a student's registration.
	 * This method will drop the index from a student's registration if it exist and the student is registered in it.
	 * If there are any students waiting for this index, students will be added from the wait list to the registered list in firstin first out order.
	 * @param indexCode The specified code of the index to drop.
	 */
	private void dropIndex(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		
		if(isStudentRegistered(currentUser, index))
		{
			removeStudentFromIndexRegistered(currentUser,index);
			System.out.format("Drop successful: Dropped %s.\n", indexCode);
		}
		else if(isStudentWaiting(currentUser, index))
		{
			removeStudentFromIndexWaitlist(currentUser,index);
			System.out.format("Drop successful from waitlist: Dropped %s.\n", indexCode);
		}
		else
		{
			System.out.println("Drop failed: Student is not in this index!");
		}
		
		if (index.getWaitingStudents().size()!=0) {
			Student_details newstudent = starsDatabase.getStudentbyMatric(index.getFirstWaitingStudent());//first out student
			if(newstudent!= null)
			{
				index.removeFromWaitlist(newstudent.getMatric_num()); //remove first out student from course waitlist
				newstudent.removeFromWaitlist(indexCode);;//remove first out course from student waitlist
				index.registerStudent(newstudent); //add student name to course
				newstudent.addIndex(indexCode);
			}
		}
	}
	/**Method to check the vacancies of a course index
	 * @param indexCode The code of the index to check
	 */
	private void CheckVacancies(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		if (index == null) {
			System.out.format("Specified index with code %s does not exist\n",indexCode);
		}
		else {
			System.out.format("%s: Number of vacancies left: %d/%d\n",indexCode, index.getVacancy(), index.getCapacity());
		}
	}
	/**
	 * Change the index specified in old index with the index specified in new index.
	 * @param oldindex The code of the index registered by the user to be changed.
	 * @param newindex The code of the index to be changed with. 
	 */
	private void changeCourseIndex(String oldindex, String newindex) {
		Index_details oldIndex = courseManager.getIndex(oldindex);
		Index_details newIndex = courseManager.getIndex(newindex);
		//check if student in other index of same course
		if (isStudentRegistered(currentUser,  oldIndex )== false) {
			System.out.format("Student is not registered in the first index:%s\n", oldindex); //else if student not in oldindex
		}
		else if (isStudentRegistered(currentUser, newIndex) ) {
			System.out.format("Already registred in requested index:%s", newindex); //else if student already in newindex
		}
		else if (!oldIndex.getCourseCode().equals(newIndex.getCourseCode())) {
			System.out.println("Indexes not in same course");
		}
		else if ( courseManager.getIndex(oldindex)==null ||courseManager.getIndex(oldindex)==null) {
			System.out.println("Course does not exist"); //else if any index does not exist
		}
		else {
			if(newIndex.getVacancy()==0)
			{
				System.out.println("Index to change to is already full.");
				return;
			}
			this.dropIndex(oldindex);
			this.addIndex(newindex);
			System.out.println("Index Number "+oldindex+" has been changed to "+newindex);
		}
	}	

	/**
	 * Method to swap the indexes between the current user and the student specified in newstudent.
	 * If the index codes specified are valid, the indexes will be swapped and a notification email will be 
	 * send to the current user and  CCed to the swapping student. Otherwise no swapping will happen and no email is sent.
	 * This method only performs swapping. Credentials entry is implemented in run_swapIndex().
	 * @param userindex The code of the index registered by the user to be swapped.
	 * @param newstudent The Student_details object of the swapping student.
	 * @param stuindex The code of the index registered by the swapping student to be swapped with.
	 */
	private void SwapIndex(String userindex, Student_details newstudent, String stuindex) {
		
		Index_details userIndex = courseManager.getIndex(userindex);
		Index_details stuIndex = courseManager.getIndex(stuindex);
		
		if (!userIndex.getCourseCode().equals(stuIndex.getCourseCode())) {
			System.out.println("Indexes not in same course");
		}
		else if (isStudentRegistered(currentUser, userIndex)== false) {
			System.out.format("Student %s is not in index %s!\n", currentUser.getName(), userindex); //if student not in userindex
		}
		else if (isStudentRegistered(newstudent, stuIndex)== false) {
			System.out.format("Student %s is not in index %s!\n", newstudent.getName(), stuindex); //if student not in userindex
		}
		else {
			swapIndex(currentUser, userIndex, newstudent, stuIndex );
			System.out.format("%s - Index %s swapped successfully with %s - Index %s\n", currentUser.getMatric_num(), userindex,newstudent.getMatric_num(), stuindex);
			notifySwapSuccessful(currentUser, userindex, newstudent, stuindex);
		}
	}
	/**Sends a notifcation to the specified students that their index swapping is successful.
	 * This method does not check if the indexes has actually been swapped or registered. This method only
	 * creates the notifications and sends them to the specified students.
	 * @param student1 The first student doing the swap
	 * @param index1 The index of the first student that is to be swapped
	 * @param student2 The second student doing the swap with student1
	 * @param index2 The index of student2 that is to be swapped with index1.
	 */
	private void notifySwapSuccessful(Student_details student1, String index1, Student_details student2, String index2)
	{		
		String matricNum1 = student1.getMatric_num();
		String matricNum2 = student2.getMatric_num();
		
		String message = "";
		message += String.format("This is to confirm that your swapping of the following course with student %s is successful:\n", matricNum2);
		message += String.format( "Before swap:\nStudent %s: Index %s\nStudent %s: Index %s\n", matricNum1, index1, matricNum2, index2);
		message += String.format( "After swap:\nStudent %s: Index %s\nStudent %s: Index %s\n", matricNum1, index2, matricNum2, index1);
		notifier.sendNotification("Successful swapping of indexes.", message, 
				starsDatabase.getUser(student1.getUserName()).getEmail(), 
				starsDatabase.getUser(student2.getUserName()).getEmail());
	}
	/**
	 * Sends a notification to the current user confirming their registered indexes and indexes that they are waiting for,
	 */
	private void notifyConfirmationEmail()
	{
		String message = "";
		message += String.format("This is to confirm your registrations of courses during this add/drop period:\n");
		message += String.format("Courses you are registered in:\n");
		Set<String> indexes =  currentUser.getIndexRegistered();
		if(indexes.isEmpty())message += "You have not been registered in any indexes yet!\n";
		else
		{
			for(String index : indexes )
			{
				message += String.format("%s\n", index);
			}
		}

		message += "\n";
		indexes =  currentUser.getIndexWaitlist();
		message += String.format("\nCourses you are in waitlist of:\n");
		if(indexes.isEmpty())message += "You are not in the waiting for any indexes yet!\n";
		else
		{
			for(String index : indexes )
			{
				message += String.format("%s\n", index);
			}
		}
		message += "\n";
		notifier.sendNotification("Confirmation of index registrations", message, starsDatabase.getStudentEmail(currentUser));
	}
	
//PRINTING METHODS	
	public void printStudentDetails(Student_details student)
	{
		student.printDetails();
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
		System.out.println("\t"+courseManager.getLectures(index)+" ");
		System.out.println("\t"+courseManager.getLab(index));
		System.out.println("\t"+courseManager.getTut(index));  
	}	
	/**Prints the indexes the student is registered in and waiting for.
	 * @param student The specified student
	 */
	public void printRegisteredIndexes(Student_details student) {
		System.out.format("%s: Courses currently registered in: %d courses.\n", student.getName(),student.getIndexRegistered().size());
		for (String indexCode : student.getIndexRegistered())
		{
			if(indexCode == FlatFileObject.EmptyString)continue;
			System.out.format("Index code:%-10s\n",indexCode);
			Set<IndexClass> indexClasses = courseManager.getIndex(indexCode).getIndexClasses();
			for(IndexClass classes: indexClasses)
			{
				System.out.format("|%-10s|%-10s|%-10s|%-10s|\n",classes.getType(),classes.getDay(),classes.getTime().get(0).toString(),classes.getTime().get(1).toString());
			}
			
		}
	}
	/**
	 * Prints the indexes that this student is in the wait list of.
	 * @param student The specified student
	 */
	public void printWaitingIndexes(Student_details student)
	{
		System.out.format("Courses currently waiting for: %d courses.\n",student.getIndexWaitlist().size());
		for (String indexCode : student.getIndexWaitlist())
		{
			if(indexCode == FlatFileObject.EmptyString)continue;
			System.out.format("%s\n",indexCode);
		}
	}
	
//BASIC METHODS
	/**
	 * Checks if the specified student is registered in the specified index
	 * @param student The specified student
	 * @param index   THe specified index
	 * @return True if the student has registered in the index, false, otherwise.
	 */
	private boolean isStudentRegistered(Student_details student, Index_details index)
	{
		return ( student.isRegistered(index.getIndexCode()) );
	}
	/**
	 * Checks if the specified student is in the waitlist in the specified index
	 * @param student The specified student
	 * @param index   THe specified index
	 * @return True if the student is in the waitlist of the index, false, otherwise.
	 */
	private boolean isStudentWaiting(Student_details student, Index_details index)
	{
		return ( student.isWaiting(index.getIndexCode()));
	}
	/**
	 * Adds the specified student to the specified index.
	 * This method does does nothing if the student is already registered in the index or
	 * the index has no more vacancies.
	 * This method also updates the student's AU according to the index's AU.
	 * If the index is in the student's waitlist, this method removes the index from the waitlist.
	 * @param student The sepcified student
	 * @param index THe specified index
	 */
	private void addStudentIndexRegistered(Student_details student, Index_details index)
	{
		if(isStudentRegistered(student, index) || index.getVacancy()<=0)return; //Student is already in the index
		student.addIndex(index.getIndexCode()); //Adds the index to the student's registered list
		index.registerStudent(student); //Adds the student to the index
		student.setAU(student.getAU()+courseManager.getIndexAU(index)); //Updates the AU
		if(isStudentWaiting(student, index))removeStudentFromIndexWaitlist(student, index);
	}
	/**
	 * Adds the specified student to the specified index's waitlist.
	 * This method does does nothing if the student is already registered or waiting for the index.
	 * This method also updates the student's AU according to the index's AU.
	 * @param student The sepcified student
	 * @param index THe specified index
	 */
	private void addStudentIndexWaitlist(Student_details student, Index_details index)
	{
		if(isStudentWaiting(student, index) || isStudentRegistered(student, index))return;
		student.addWaitlist(index.getIndexCode());
		index.addStudentToWaitlist(student.getMatric_num());
		student.setAU(student.getAU()+courseManager.getIndexAU(index));
	}
	/**Removes the specified student from the specified index's registrations
	 *This method also updates the student's AU according to the index's AU.
	 * @param student The specified student
	 * @param index THe specified index
	 */
	private void removeStudentFromIndexRegistered(Student_details student, Index_details index)
	{
		student.removeIndex(index.getIndexCode());
		index.removeFromRegistered(student.getMatric_num());
		int au = courseManager.getIndexAU(index);
		if(au > 0)student.setAU( student.getAU() - au );
	}
	/**Removes the specified student from the specified index's waitlist
	 *This method also updates the student's AU according to the index's AU.
	 * @param student The specified student
	 * @param index THe specified index
	 */
	private void removeStudentFromIndexWaitlist(Student_details student, Index_details index)
	{
		student.removeFromWaitlist(index.getIndexCode());
		index.removeFromWaitlist(student.getMatric_num());
	}
	/**
	 * Swaps a index registered by student1 with a index registered by student2.
	 * This method does nothing if: the specified indexes are not in the same course, studen1 is not
	 * registered in index1, or student2 is not registered in student2.
	 * @param student1  The student to initiate the swapping
	 * @param index1 The index registered by student1 to be swapped
	 * @param student2 The student to swap index with
	 * @param index2 The index of student2 to be swapped
	 * @return True is the swap is successful, false otherwise
	 */
	private boolean swapIndex(Student_details student1, Index_details index1, Student_details student2, Index_details index2)
	{
		if(!index1.getCourseCode().equals(index2.getCourseCode()))return false;
		if(!isStudentRegistered(student1, index1) || !isStudentRegistered(student2, index2))return false;
		removeStudentFromIndexRegistered( student1,index1);  //swap names of students from both courses
		removeStudentFromIndexRegistered(student2, index2);     //swap names of student from both courses
		addStudentIndexRegistered(student1,index2);
		addStudentIndexRegistered(student2, index1);
		return true;
	}
	/**Checks if a student is registered in the specified course
	 * @param matricNum The matriculation number of the student
	 * @param course The specified course
	 * @return True if the student is registered in the course.
	 */
	private boolean isStudentRegisteredCourse(String matricNum, Course course)
	{
		return courseManager.isStudentInCourse(matricNum, course);
	}
	/**Check if specified index clashes with the indexes registered by this student.
	 * @param index The specified index
	 * @param student A Student_detail object of the specified student.
	 * @return True if there is a clash, false otherwise.
	 */
	private ArrayList<Index_details> isIndexClashWithStudentRegistered(Index_details index ,  Student_details student)
	{
		ArrayList<Index_details> clashList = new ArrayList<>();
		for( String id : student.getIndexRegistered())
		{
			Index_details index2 = courseManager.getIndex(id);
			if(index2 == null)continue;
			if(index.clash(index2))clashList.add(index2);
		}
		return clashList;
	}
}
