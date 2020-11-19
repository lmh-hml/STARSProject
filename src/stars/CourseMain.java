package stars;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;



public class CourseMain {
	static Scanner scanner  = new Scanner(System.in);

	
	static Course createCourse()
	{
		Course course = new Course();
		System.out.println("Course_code Course_name AU");
		String s = scanner.nextLine();
		String[] sa = s.split("\\ ");
		course.setcoursecode(sa[0]);
		course.setcourseName(sa[1]);
		course.setAU(Integer.parseInt(sa[2]));
		return course;
	}

	static Index_details  createIndex()
	{
		Index_details index  = new Index_details();
		System.out.println("Index_code Course_code Cap");
		String s = scanner.nextLine();
		String[] sa = s.split("\\ ");
		index.setIndexCode(sa[0]);
		index.setCourseCode(sa[1]);
		index.setCapacity(Integer.parseInt(sa[2]));
		return index;
	}
	
	static IndexClass createClass()
	{
		IndexClass ic = new IndexClass();
		System.out.println("Group Type Day startTime endTime venue");
		String s = scanner.nextLine();
		if(s=="0")return null;
		String[] sa = s.split("\\ ");
		
		ic.setGroup(sa[0]);
		ic.setType(sa[1].toUpperCase());
		ic.setDay(DayOfWeek.valueOf(sa[2].toUpperCase()));
		ic.setStartTime(LocalTime.parse(sa[3]));
		ic.setEndTime(LocalTime.parse(sa[4]));
		ic.setVenue(sa[5]);
		return ic;
	}
	
	
	
	
	
	public static void main(String args[])
	{

		CourseDatabase courses = new CourseDatabase();
		IndexDatabase indexes = new IndexDatabase();
		
		
		try {
			courses.openFile("Courses.txt");
			indexes.openFile("Indexes.txt");
			
			for(int i = 0; i< 4; i++)
			{
				Index_details index = createIndex();
				for(int j = 0; j<4;j++)
				{
					IndexClass c = createClass();
					if(c==null)continue;
					index.addIndexClass(c);
				}
				indexes.add(index);
				System.out.println(index.toFlatFileString());
			}
		
			
			
			


			indexes.writeFile("Indexes.txt");
			//courses.writeFile("Courses.txt");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
