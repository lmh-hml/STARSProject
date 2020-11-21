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

		try {
			cm.save();
			stars.saveStudents();
			stars.saveUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
