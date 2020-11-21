package stars;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import stars.FlatFileDatabase;
import stars.FlatFileObject;
import stars.exception.NotFoundInDatabaseException;

public class CourseManager{

	public static final String SEPARATOR = "|";

	final String courseFile = "Courses.txt";
	final String indexFile = "Indexes.txt";
	
	private CourseDatabase courses = new CourseDatabase();
	private IndexDatabase  indexes = new IndexDatabase();
	private HashMap<String,HashMap<String,Collection<String>>> studentsInCourseCache = new HashMap<>();
	public CourseManager() {
		
		try {
			courses.openFile(courseFile);
			indexes.openFile(indexFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**Writes the contents of this database
	 * back to storage.
	 */
	public void save()
	{
		try {
			courses.writeFile(courseFile);
			indexes.writeFile(indexFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Add the specified index to the course with the specified course code.
	 * @param index IndexCode of the specified index.
	 * @param courseCode CourseCode of the specified index.
	 */
	public void addIndex(Index_details index, String courseCode)
	{
		this.indexes.add(index.getIndexCode(),index);
		Course course;
		course = this.courses.get(courseCode);
		course.addIndexName(index.getIndexCode());
	}
	public void removeIndex( String indexCode)
	{
		Index_details index = this.indexes.get(indexCode);
		Course c = this.courses.get(index.getCourseCode());
		if(c != null)
		{
			c.removeIndexname(indexCode);
		}
		indexes.remove(indexCode);	

	}
	public Index_details getIndex(String indexCode)
	{
		return this.indexes.get(indexCode);
	}	
	public Collection<Index_details> getAllIndexes() 
	{
		return this.indexes.getContents();
	}
	public boolean checkIndexExists(String indexCode)
	{
		return this.indexes.getKeys().contains(indexCode);
	}

	public void addCourse(Course course)
	{
		this.courses.add(course.getcoursecode(),course);
	}
	public void removeCourse(String courseCode){
		this.courses.remove(courseCode);
	}
	public Collection<Course> getAllCourses()
	{
		return this.courses.getContents();
	}
	public Course getCourse(String courseCode)
	{
		return this.courses.get(courseCode);
	}
	public boolean checkCourseExists(String courseCode)
	{
		return this.courses.getKeys().contains(courseCode);
	}
		
	/**
	 * Gets the classes under the index with the specified index code with the specified Index Class type.
	 * @param indexCode Code of the target index
	 * @param type A IndexClassType enum member
	 * @return A list of Index Class objects with the specified index class type.
	 */	
	public List<IndexClass> getIndexClasses(String indexCode,IndexClassType type) 
	{
		ArrayList<IndexClass> lecList = new ArrayList<>();
		IndexClass[] indexClasses = (IndexClass[])this.indexes.get(indexCode).getIndexClasses();
		for(IndexClass ic : indexClasses)
		{
			if(ic.getType() == type)
			{
				lecList.add(ic);
			}
		}
		return lecList;
	}		
	/**
	 * Gets the lectures classes under the index with the specified index code
	 * @param indexCode Code of the target index
	 * @return A list of Index Class objects which are the lectures classes of the index.
	 */		
	public List<IndexClass> getLectures(String indexCode) 
	{
		return getIndexClasses(indexCode, IndexClassType.Lecture);
	}
	/**
	 * Gets the lab classes under the index with the specified index code
	 * @param indexCode COde of the target index
	 * @return A list of Index Class objects which are the lab classes of the index.
	 */
	public List<IndexClass> getLab(String indexCode) 
	{
		return getIndexClasses(indexCode, IndexClassType.Lab);
	}		
	/**
	 * Gets the tutorials classes under the index with the specified index code
	 * @param indexCode COde of the target index
	 * @return A list of Index Class objects which are the tutorial classes of the index.
	 */
	public List<IndexClass> getTut(String indexCode)
	{
		return getIndexClasses(indexCode, IndexClassType.Tutorial);	
	}
	/**
	 * Gets the vacancy of the index with the course code.
	 * @param indexCode Code of the target index.
	 * @return The vacancies left in an index.
	 */
	public int getVacancy(String indexCode)
	{
		Index_details index = this.indexes.get(indexCode);
		if(index==null)return -1;
		return index.getVacancy();
	}

	/**
	 * Retrieves all the students in a course in a hashmap, where its keys are the
	 * indexes of the course, and the corresponding value is the array of student's matriculation numbers who is 
	 * registered in the index.
	 * @param courseCode Course code of the target course.
	 * @return A hashmap where the keys are the indexes of the course, and the values are arrays containing the matriculation no. of students 
	 * who registered in the course.
	 */
	public HashMap<String,Collection<String>> getStudentsInCourse(String courseCode)  
	{
		//Creates and caches the collection of students in the specified course if it not already created.
		if(studentsInCourseCache.get(courseCode)==null)
		{
			HashMap<String, Collection<String>> studentsInCourse = new HashMap<>();
			Course course = this.courses.get(courseCode);
			for( String indexCode : course.getIndexName())
			{
				studentsInCourse.put(indexCode, this.indexes.get(indexCode).getRegisteredStudents());
			}		
			studentsInCourseCache.put(courseCode, studentsInCourse);
		}
		return studentsInCourseCache.get(courseCode);
	}
	public boolean isStudentInCourse(String matricNum, String courseCode)
	{
		boolean found = false;
		for( Collection<String> c : getStudentsInCourse(courseCode).values())
		{
			if(c.contains(matricNum)) { 
				found  = true;
				break;
			}
		}
		return found;
	}
	/**
	 * Gets the AU of the course with the specified course code.
	 * @param courseCode Course code of the target course.
	 * @return The AU of the course if the course exists, otherwise -1.
	 */
	public int getCourseAU(String courseCode)
	{
		Course course = courses.get(courseCode);
		if(course==null)return -1;
		return course.getAU();
	}
	/**
	 * Gets the AU of the specified index.
	 * @param index The specified Index_object object.
	 * @return The AU of the specified index.
	 */
	public int getIndexAU(Index_details index)
	{
		return getCourseAU( index.getCourseCode());
	}

}