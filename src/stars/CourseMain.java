package stars;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;



public class CourseMain {
	
	public static void main(String args[])
	{
		
		CourseDatabase courses = new CourseDatabase();
		IndexDatabase indexes = new IndexDatabase();
		try {
			courses.openFile("Courses.txt");
			indexes.openFile("Indexes.txt");
			
			LocalTime start = LocalTime.of(8, 30);
			LocalTime end = LocalTime.of(9, 30);

			
			
			Index_details index = new Index_details();
			index.setIndexCode("10122");
			index.setCapacity(10);
			
			Course algorithms = new Course("CE2001", "Algorithms", 3);
			Index_details algo1  = new Index_details();
			algo1.setCourseCode(algorithms.getcoursecode());
			algo1.setCapacity(10);
			algo1.setIndexCode("10019");
			
			IndexClass indexClass = new IndexClass();
			indexClass.setType(IndexClassType.Lecture);
			indexClass.setGroup("CE2");
			indexClass.setDay(DayOfWeek.FRIDAY);
			indexClass.setStartTime(start);
			indexClass.setEndTime(end);
			indexClass.setVenue("LT10");
			algo1.addIndexClass(indexClass);
			indexes.add(algo1);
			courses.add(algorithms);
						

			System.out.println(algo1.toFlatFileString());
			
			Index_details index2 = new Index_details();
			index2.fromFlatFileString(algo1.toFlatFileString());
			System.out.println(index2.toFlatFileString());


			indexes.writeFile("Indexes.txt");
			courses.writeFile("Courses.txt");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
