package stars;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public class ProgramTest {

	public static void main(String args[])
	{
		CourseManager cm = new CourseManager();
		StarsDatabase stars = new StarsDatabase();
		StarsNotifier notifier = new StarsMail();
		StudentProgram program = new StudentProgram( cm , stars, notifier);
		
		Student_details student = stars.getStudentByUsername("WeiJie67");
		Student_details student2 = stars.getStudentByUsername("Richard41");
		
		program.setCurrentStudent(student);
		program.addIndex("10019");
		
		program.setCurrentStudent(student2);
		program.addIndex("10020");
		
		program.SwapIndex("10020", student, "10019");

		
		
		
//		try {
//			cm.save();
//			stars.saveStudents();
//			stars.saveUsers();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
