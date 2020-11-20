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
		program.printStudentDetails(student);
		program.setCurrentStudent(student);
		//program.CheckAndPrintRegistered();
		program.addIndex("10019");
		//student.addIndex("10019");
		program.printStudentDetails(student);

		//program.CheckAndPrintRegistered();
		program.dropIndex("10019");
		//program.CheckAndPrintRegistered();


		program.printStudentDetails(student);

		
//		try {
//			stars.saveStudents();
//			stars.saveUsers();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
	}
}
