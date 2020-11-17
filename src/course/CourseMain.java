package course2;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

import course2.Index_details.IndexClass;


public class CourseMain {
	
	public static void main(String args[])
	{
		
		CourseDatabase courses = new CourseDatabase();
		IndexDatabase indexes = new IndexDatabase();
		try {
			courses.openFile("courses.txt");
			indexes.openFile("Indexes.txt");
			
			Index_details index = new Index_details();
			index.setIndexCode("10122");
			index.setCapacity(10);
			Index_details.IndexClass indexClass = new IndexClass("LEC","SE1",LocalTime.now(), LocalTime.now(),DayOfWeek.MONDAY,"LectureTheatre");
			index.addIndexClass(indexClass);
			indexes.add(index);
		
			
			for(Index_details c :indexes.getContents())
			{
				System.out.println(c.toFlatFileString());
			}
			
			
			Course course = new Course("CE2001","Algorithms",3);
			course.addIndexName(index.getIndexCode());

			indexes.writeFile("Indexes.txt");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
