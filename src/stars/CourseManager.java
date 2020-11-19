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

public class CourseManager{

	public static final String SEPARATOR = "|";

	final String courseFile = "Courses.txt";
	final String indexFile = "Indexes.txt";
	
	private CourseDatabase courses = new CourseDatabase();
	private IndexDatabase  indexes = new IndexDatabase();
	public CourseManager() {
		
		try {
			courses.openFile(courseFile);
			indexes.openFile(indexFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveCourse(String filename)
	{
		try {
			courses.writeFile(courseFile);
			indexes.writeFile(indexFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void addIndex(Index_details index, String courseCode)
	{
		this.indexes.add(index);
		this.courses.get(courseCode).addIndexName(index.getIndexCode());
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
		this.courses.add(course);
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
		return getIndexClasses(indexCode, IndexClassType.LEC);
	}
	/**
	 * Gets the lab classes under the index with the specified index code
	 * @param indexCode COde of the target index
	 * @return A list of Index Class objects which are the lab classes of the index.
	 */
	public List<IndexClass> getLab(String indexCode) 
	{
		return getIndexClasses(indexCode, IndexClassType.LAB);
	}		
	/**
	 * Gets the tutorials classes under the index with the specified index code
	 * @param indexCode COde of the target index
	 * @return A list of Index Class objects which are the tutorial classes of the index.
	 */
	public List<IndexClass> getTut(String indexCode)
	{
		return getIndexClasses(indexCode, IndexClassType.TUT);	
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
	 * indexes of the course, and the corresponding value is the array of registered students in the course.
	 * @param courseCode Course code of the target course.
	 * @return A hashmap where the keys are the indexes of the course, and the values are arrays containing the names of students who registered
	 * in the course.
	 */
	public HashMap<String, String[]> getStudentsInCourse(String courseCode)  
	{
		HashMap<String, String[]> studentsInCourse = new HashMap<>();
		Course course = this.courses.get(courseCode);
		for( String indexCode : course.getIndexName())
		{
			studentsInCourse.put(indexCode, this.indexes.get(indexCode).getRegisteredStudents());
		}
		return studentsInCourse;
	}

	/**
	 * Gets the AU of the course with the specified course code.
	 * @param courseCode Course code of the target course.
	 * @return The AU of the course if the course exists, otherwise -1.
	 */
	public int getAU(String courseCode)
	{
		Course course = this.courses.get(courseCode);
		if(course==null)return -1;
		return course.getAU();
	}

	/**
	 * Add a student to the course index with the specified index code.
	 * @param indexCode Code of the course index.
	 * @param student   A Student_details object associated with the student to be added.
	 * @return True if the student is successfully added, False otherwise (No student will be addedto any index.)
	 */
	public boolean addStudentToIndex(String indexCode, Student_details student)
	{
		Index_details index = indexes.get(indexCode);
		if(index==null)return false;
		return index.registerStudent(student);
	}
	/**
	 * Removes a student from a course index with the specified code.
	 * @param indexCode Code of the course index.
	 * @param student A Student_details object associated with the student.
	 */
	public void removeStudentFromIndex(String indexCode, Student_details student)
	{
		Index_details index  = indexes.get(indexCode);
		if(index!=null)
		{
			index.removeFromRegistered(student.getName());
			index.removeFromWaitlist(student.getName());
		}
	}

	public static void main(String args[])
		{
			CourseManager cm= new CourseManager();
			for( Course c :cm.getAllCourses())
			{
				System.out.println(c.toFlatFileString());
			}
			for( Index_details c :cm.getAllIndexes())
			{
				System.out.println(c.toFlatFileString());
			}
			Index_details i1 = cm.getIndex("10019");
			Course c1 = cm.getCourse("CE2001");
			
			System.out.println(cm.getCourse(i1.getCourseCode()).toFlatFileString());



		}
}	  