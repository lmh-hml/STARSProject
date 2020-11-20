package stars;

import java.io.IOException;

public class ProgramTest {

	public static void main(String args[])
	{
		CourseManager cm = new CourseManager();
		StarsDatabase stars = new StarsDatabase();
		StarsNotifier notifier = new StarsMail();
		StudentProgram program = new StudentProgram( cm , stars, notifier);
		
		
		
		Student_details student = stars.getStudent("WeiJie67");
		Student_details student2 = stars.getStudent("Richard41");
		Index_details index = cm.getIndex("10019");
		
		program.addStudentIndexRegistered(student2, index);
		program.printStudentDetails(student2);
		program.printRegisteredIndexes(student2);
		
		program.setCurrentStudent(student);
		program.addIndex("10019");
		program.printRegisteredIndexes(student);
		
//		program.SwapIndex("10019", student2, "10020");
//
//
//		program.printRegisteredIndexes(student);
//		program.printRegisteredIndexes(student2);

		

		
//		program.printStudentDetails(student);
//		program.setCurrentStudent(student);
//		program.printRegisteredIndexes(student);
//		program.addIndex("10019");
//		program.printRegisteredIndexes(student);
//		program.dropIndex("10019");
//		program.printRegisteredIndexes(student);




		
//		try {
//			stars.saveStudents();
//			stars.saveUsers();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
	}
}
