package stars;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import stars.FlatFileObject;


public class Course implements FlatFileObject
{
    private String courseCode;
    private String courseName;
    private int AU;
    private Set<String> indexes = new HashSet<>();
	/**Number of fields in this class that should be read/written to flat file**/
    private final static int NumFields = 4;

	public Course(String code, String name, int AU) 
	{
		this.courseCode=code;
		this.courseName=name;
		this.AU = AU;
	}
	Course(){}
	
//GETTERS AND SETTERS
	public String getcoursecode() {return courseCode;}
	public void setcoursecode(String courseCode) {this.courseCode = courseCode;}
	public String getcourseName() {return courseName;}
	public void setcourseName(String courseName) {this.courseName = courseName;}
	public int getAU() {return AU;}
	public void   setAU(int au) { this.AU = au;}
	
//METHODS REGARDING INDEXES
	public Set<String> getIndexCodes()
	{
		Set<String> copy = Collections.unmodifiableSet(indexes);
		return copy;
	}
	public void addIndexCode(String index)
	{
		this.indexes.add(index);
	}	
	public void removeIndexCode(String name)
	{
		this.indexes.remove(name);
	}
	
	
//FLATFILE OBJECT INTERFACE	
	@Override
	public String toFlatFileString() {
		String courseStr = FlatFileObject.buildFlatFileString(courseCode, courseName, AU);
		courseStr += FlatFileObject.collectionToFlatFileString(indexes)+delimiter;
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

}
