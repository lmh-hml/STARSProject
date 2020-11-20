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
		
		program.printStudentDetails(student);

		
		program.setCurrentStudent(student);
		program.addIndex("10019");
		program.printRegisteredIndexes(student);
		
		program.addStudentIndexRegistered(student2, index);
		program.printRegisteredIndexes(student2);
		program.printRegisteredIndexes(student);


		
		try {
			cm.saveCourse("Courses.txt");
			stars.saveStudents();
			stars.saveUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
