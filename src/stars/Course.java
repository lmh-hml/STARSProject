package stars;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import stars.FlatFileObject;


public class Course implements FlatFileObject
{
    private String courseCode;
    private String courseName;
    private int AU;
    private ArrayList<String> indexes = new ArrayList<>();
	/**Number of fields in this class that should be read/written to flat file**/
    private final static int NumFields = 4;

	public Course(String code, String name, int AU) 
	{
		this.courseCode=code;
		this.courseName=name;
		this.AU = AU;
	}
	Course(){}
	
	
	public String getcoursecode() {return courseCode;}
	public void setcoursecode(String courseCode) {this.courseCode = courseCode;}
	public String getcourseName() {return courseName;}
	public void setcourseName(String courseName) {this.courseName = courseName;}
	public int getAU() {return AU;}
	public void   setAU(int au) { this.AU = au;}
	
	
	public ArrayList<String> getIndexName()
	{
		ArrayList<String> copy = new ArrayList<String>(); 
		for(String str : this.indexes) {
		    copy.add(str);
		}
		return copy;
	}
	
	
	
	public void addIndexName(String index)
	{
		this.indexes.add(index);
	}
	
	public void removeIndexname(String name)
	{
		this.indexes.remove(name);
	}
	
	
	
	@Override
	public String toFlatFileString() {
		String courseStr = FlatFileObject.buildFlatFileString(courseCode, courseName, AU);
		courseStr += FlatFileObject.listToFlatFileString(indexes);
		return courseStr;
	}	
	@Override
	public boolean fromFlatFileString(String s) {
		String[] array = s.split("\\|");
		if(array.length< NumFields)return false;
		this.courseCode = array[0];
		this.courseName = array[1];
		this.AU = Integer.valueOf(array[2]);
		
		String[] waitlist_array = array[3].split("\\,");
		for( String str : waitlist_array)
		{
			this.indexes.add(str);
		}	
		return true;
	}
	@Override
	public String getDatabaseId() {
		return this.courseCode;
	}
	
	public static void main(String args[])
	{
		Course course = new Course("CE2001","Algorithms",0);
		course.addIndexName("1101");
		course.addIndexName("1102");
		String s = course.toFlatFileString();
		
		Course c2 = new Course();
		c2.fromFlatFileString(s);
		System.out.println(s);
		System.out.println(c2.toFlatFileString());
	}

}
