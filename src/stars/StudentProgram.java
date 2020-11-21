package stars;
import java.util.*;




public class StudentProgram
{
	private Student_details currentUser;
	private CourseManager courseManager;
	private StarsDatabase starsDatabase;
	private Scanner scanner = new Scanner(System.in);
	private StarsNotifier notifier;
	private final int AU_LIMIT = 20;
	
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
		System.out.format("Please enter an option (0-7): ");

		while( loopInput || !quit )
		{
			System.out.format("Please enter an option: ");
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

//CLIENT METHODS
	Index_details promptForIndexCode(String message)
	{
		Index_details index = null;
		String inputIndex = "";
		while(index == null && !inputIndex.equals("0"))
		{
			System.out.println(message);
			inputIndex = scanner.nextLine();
			if(inputIndex == "0")return null;
			index = courseManager.getIndex(inputIndex);
			if(index == null) {System.out.println("No such index exists. Please enter the code again.");}
		}
		return index;	
	}
	void run_AddIndex()
	{
		Index_details index = promptForIndexCode("Please enter the code of the index that you want to register: (Press 0 to go back) ");
		if(index==null)return;
		addIndex(index.getIndexCode());
	}
	void run_DropIndex()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter the code of the index that you want to drop: (Press 0 to go back) ");
		if(index==null)return;
		dropIndex(index.getIndexCode());
	}
	void run_CheckVacancies()
	{
		scanner.nextLine();
		Index_details index = promptForIndexCode("Please enter the index code you want to check:");
		if(index==null)return;
		CheckVacancies(index.getIndexCode());
	}
	void run_ChangeIndex()
	{
		scanner.nextLine();
		Index_details id1 = promptForIndexCode("Please enter the index you want to change:");
		if(id1==null)return;
		Index_details id2 = promptForIndexCode("Please enter the index you want to change with:");
		if(id2==null)return;
		changeCourseIndex(id1.getIndexCode(), id2.getIndexCode());
	}
	void run_SwapIndex()
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
				input = scanner.nextLine();
				correct = PasswordModule.verifyPassword(input, user.getPassword());
				if(!correct)System.out.println("Password is incorrect. Please try again.");
			}
		}
		
		if(input=="0")return;
		Index_details index2 = promptForIndexCode("Please enter index you want to swap with:");
		if(index2 == null)return;
		SwapIndex( index.getIndexCode(), starsDatabase.getStudent(user), index2.getIndexCode());
		
	}

//PROGRAM METHODS
	//Method to register a index of course for the student
	public void addIndex(String indexCode) {
		//check if student in other index of same course
		Index_details index = courseManager.getIndex(indexCode);
		if (index==null) { System.out.format("Add failed: Index %s does not exist.\n", indexCode); return;}

		Course course  = courseManager.getCourse(index.getCourseCode());
		ArrayList<Index_details> clashes = isIndexClashWithStudentRegistered(index, currentUser);
				
		if (isStudentRegistered(currentUser, index)== true) 
		{
			System.out.format("Add failed: Student is already registered in index %s!\n", indexCode);
		}
		else if (isStudentRegisteredCourse(currentUser.getMatric_num(), index.getCourseCode()))
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
	//Method to drop a course for the student
	public void dropIndex(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		
		if(isStudentRegistered(currentUser, index))
		{
			removeStudentFromIndexRegistered(currentUser,index);
			System.out.format("Drop successful: Dropped %s.\n", indexCode);
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

	/**Method to check the vacancies of a course index**/
	public void CheckVacancies(String indexCode) {
		Index_details index = courseManager.getIndex(indexCode);
		if (index == null) {
			System.out.format("Specified index with code %s does not exist\n",indexCode);
		}
		else {
			System.out.format("%s: Number of vacancies left: %d/%d\n",indexCode, index.getVacancy(), index.getCapacity());
		}
	}
	
	//Method to change the course index for the student
	public void changeCourseIndex(String oldindex, String newindex) {
		Index_details oldIndex = courseManager.getIndex(oldindex);
		Index_details newIndex = courseManager.getIndex(newindex);
		//check if student in other index of same course
		if (isStudentRegistered(currentUser,  oldIndex )== false) {
			System.out.format("Student is not registered in the first index:%s\n", oldindex); //else if student not in oldindex
		}
		else if (isStudentRegistered(currentUser, newIndex) ) {
			System.out.format("Already registred in requested index:%s", newindex); //else if student already in newindex
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
			System.out.format("Student %s is not in index %s!\n", currentUser.getName(), userindex); //if student not in userindex
		}
		else if (isStudentRegistered(newstudent, stuIndex)== false) {
			System.out.format("Student %s is not in index %s!\n", newstudent.getName(), stuindex); //if student not in userindex
		}
		else {
			swapIndex(currentUser, userIndex, newstudent, stuIndex );
			System.out.format("%s - Index %s swapped successfully with %s - Index %s\n", currentUser.getMatric_num(), userindex,newstudent.getMatric_num(), stuindex);
		}
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
		System.out.println("\t"+courseManager.getLectures(indexCode)+" ");
		System.out.println("\t"+courseManager.getLab(indexCode));
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
			System.out.format("%s\n",indexCode);
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
	public boolean isStudentRegistered(Student_details student, Index_details index)
	{
		return ( student.isRegistered(index.getIndexCode()) );
	}
	/**
	 * Checks if the specified student is in the waitlist in the specified index
	 * @param student The specified student
	 * @param index   THe specified index
	 * @return True if the student is in the waitlist of the index, false, otherwise.
	 */
	public boolean isStudentWaiting(Student_details student, Index_details index)
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
	public void addStudentIndexRegistered(Student_details student, Index_details index)
	{
		if(isStudentRegistered(student, index) || index.getVacancy()<=0)return; //Student is already in the index
		student.addIndex(index.getIndexCode()); //Adds the index to the student's registered list
		index.registerStudent(student.getMatric_num()); //Adds the student to the index
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
	public void addStudentIndexWaitlist(Student_details student, Index_details index)
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
	public void removeStudentFromIndexRegistered(Student_details student, Index_details index)
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
	public void removeStudentFromIndexWaitlist(Student_details student, Index_details index)
	{
		student.removeFromWaitlist(index.getCourseCode());
		index.removeFromWaitlist(student.getMatric_num());
	}
	/**
	 * Swaps a index registered by student1 with a index registered by student2.
	 * This method does nothing if: the specified indexes are not in the same course, studen1 is not
	 * registered in index1, or student2 is not registered in student2.
	 * @param student1 
	 * @param index1
	 * @param student2
	 * @param index2
	 * @return True is the swap is successful, false otherwise
	 */
	public boolean swapIndex(Student_details student1, Index_details index1, Student_details student2, Index_details index2)
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
	 * @param courseCode The course code of the course.
	 * @return True if the student is registered in the course.
	 */
	public boolean isStudentRegisteredCourse(String matricNum, String courseCode)
	{
		return courseManager.isStudentInCourse(matricNum, courseCode);
	}
	/**Check if specified index clashes with the indexes registered by this student.
	 * @param index The specified index
	 * @param student A Student_detail object of the specified student.
	 * @return True if there is a clash, false otherwise.
	 */
	public ArrayList<Index_details> isIndexClashWithStudentRegistered(Index_details index ,  Student_details student)
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
