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

/**
 * Class that represents a course that a student can register for during add/drop period.
 * @author Lai Ming Hui
 *
 */
public class Course implements FlatFileObject
{
	/**The course code of this course**/
    private String courseCode;
    /**The name of this course**/
    private String courseName;
    /**The AU of this course**/
    private int AU;
    /**The set of index codes that belong under this course.**/
    private Set<String> indexes = new HashSet<>();
	/**Number of fields in this class that should be read/written to flat file**/
    private final static int NumFields = 4;

    /**
     * Constructs an instance of this class using the specified code, name and AU.
     * @param code The course code for this course
     * @param name The name of this course
     * @param AU The AU of this course
     */
	public Course(String code, String name, int AU) 
	{
		this.courseCode=code;
		this.courseName=name;
		this.AU = AU;
	}
	Course(){}
	
//GETTERS AND SETTERS
	/**Returns the course code of this course.
	 * @return THe course code of this course.
	 */
	public String getcoursecode() {return courseCode;}
	/**Sets the course code of this course.
	 * @param courseCode The specified course code.
	 */
	public void setcoursecode(String courseCode) {this.courseCode = courseCode;}
	/**Gets the course name of this course
	 * @return The course name of this course.
	 */
	public String getcourseName() {return courseName;}
	/**Sets the course name of this course.
	 * @param courseName The specified course name.
	 */
	public void setcourseName(String courseName) {this.courseName = courseName;}
	/**Gets the AU of this course.*
	 * @return The AU of this course.
	 */
	public int getAU() {return AU;}
	/**Sets the AU of this course
	 * @param au The specified AU for this course.
	 */
	public void   setAU(int au) { this.AU = au;}
	
//METHODS REGARDING INDEXES
	/**Retrieves the set of codes of indexes under this course.
	 * @return An unmodifiable set containing the codes of indexes under this course.
	 */
	public Set<String> getIndexCodes()
	{
		Set<String> copy = Collections.unmodifiableSet(indexes);
		return copy;
	}
	
	/**
	 * Adds an index code into this course.
	 * @param index The specified index code.
	 */
	public void addIndexCode(String index)
	{
		this.indexes.add(index);
	}	
	/**
	 * Removes the specified index code from this course.
	 * @param indexCode The specified index code
	 */
	public void removeIndexCode(String indexCode)
	{
		this.indexes.remove(indexCode);
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
