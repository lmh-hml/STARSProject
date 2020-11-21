package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import stars.Course;
import stars.IndexClass;
import stars.IndexDatabase;
import stars.Index_details;



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
	
	public static HashMap<String, ArrayList<IndexClass>> readIndexClass(String filename)
	{
		HashMap<String, ArrayList<IndexClass>> map = new HashMap<>();
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:mm");
			
			BufferedReader bfr;
			try {
				bfr = new BufferedReader(new FileReader(filename));
				String line = "";
				int count = 0;
				while ( (line = bfr.readLine())!= null)
				{
					count++;
					if(count == 1)continue;
					
					IndexClass id = new IndexClass();
					String[] array = line.split("\\,");
					id.setType(array[0]);
					id.setGroup(array[1]);
					id.setStartTime(LocalTime.parse(array[2], fmt));
					id.setEndTime(LocalTime.parse(array[3], fmt));
					id.setDay(DayOfWeek.valueOf(array[4].toUpperCase()));
					id.setVenue(array[5]);
					id.setIndexCode(array[6]);
					
					if(!map.containsKey(id.getIndexCode()))
					{
						map.put(id.getIndexCode(), new ArrayList<IndexClass>());
					}
					map.get(id.getIndexCode()).add(id);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return map;
	}
	
	public static HashMap<String, Index_details> readIndex(String filename)
	{
		HashMap<String, Index_details> map = new HashMap<>();
					
			BufferedReader bfr;
			try {
				bfr = new BufferedReader(new FileReader(filename));
				String line = "";
				int count = 0;
				while ( (line = bfr.readLine())!= null)
				{
					count++;
					if(count == 1)continue;
					String[] array = line.split("\\,");
					Index_details id = new Index_details();
					id.setIndexCode(array[0]);
					id.setCourseCode(array[1]);
					id.setCapacity(Integer.parseInt(array[2]));
					map.put(id.getIndexCode(), id);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
			
	}
	
	
	public static HashMap<String, Course> readCourse(String filename)
	{
		HashMap<String, Course> map = new HashMap<>();
		
		BufferedReader bfr;
		try {
			bfr = new BufferedReader(new FileReader(filename));
			String line = "";
			int count = 0;
			while ( (line = bfr.readLine())!= null)
			{
				count++;
				if(count == 1)continue;
				String[] array = line.split("\\,");
				Course c = new Course();
				c.setcoursecode(array[0]);
				c.setcourseName(array[1]);
				c.setAU(Integer.parseInt(array[2]));
				map.put(c.getcoursecode(), c);
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public static void printIndexClasses( HashMap<String, ArrayList<IndexClass>> indexClassesMap )
	{
		for( String indexCode : indexClassesMap.keySet())
		{
			System.out.format("%s\n",indexCode);
			for(IndexClass indexClass : indexClassesMap.get(indexCode))
			{
				System.out.format("    %s\n", indexClass.toFlatFileString());
			}
		}
	}

	public static void main(String args[])
	{

		try {
		IndexDatabase indexes = new IndexDatabase();
		indexes.openFile("Indexes.txt");
		

		HashMap<String, ArrayList<IndexClass>> indexClassesMap = readIndexClass("CourseData - IndexClass.csv");
		printIndexClasses(indexClassesMap);

		
		HashMap< String , Index_details> indexMap = readIndex("CourseData - Indexes.csv");
		for(Index_details id : indexMap.values())
		{
			System.out.format("%s\n", id.toFlatFileString());
			for( IndexClass key : indexClassesMap.get(id.getIndexCode()))
			{
				System.out.format("%s: Adding %s \n",id.getIndexCode(),key.toFlatFileString());
				id.addIndexClass(key);
			}
			System.out.format("%s:%s\n", id.getCourseCode(),id.toFlatFileString());
			indexes.add(id.getIndexCode(), id);;
		}
		
		
		indexes.writeFile("Indexes.txt");

		for(Index_details c : indexes.getContents())
		{
			System.out.println(c.toFlatFileString());
		}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
