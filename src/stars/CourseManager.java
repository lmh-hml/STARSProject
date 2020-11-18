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

	final String courseFile = "src\\studentCourse\\courses.txt";
	final String indexFile = "src\\studentCourse\\namelist";
	
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
		
		public int getAu(String courseCode) {

			return this.courses.get(courseCode).getAU();
		}
		
		/**
		 * Return a list of lectures of the index with the specified index code.
		 * @param indexCode Code of the index 
		 * @return An array list of index classes that are lectures under the specified index.
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
		
		public List<IndexClass> getLectures(String indexCode) 
		{
			return getIndexClasses(indexCode, IndexClassType.Lecture);
		}
		public List<IndexClass> getLab(String indexCode) 
		{
			return getIndexClasses(indexCode, IndexClassType.Lab);
		}

		public List<IndexClass> getTut(String indexCode)
		{
			return getIndexClasses(indexCode, IndexClassType.Tutorial);	
		}

		public int getVacancy(String indexCode){
			return this.indexes.get(indexCode).getVacancy();
		}

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

		public int getAU(String courseCode)
		{
			return this.courses.get(courseCode).getAU();
		}

		public boolean addStudentToIndex(String indexCode, Student_details student)
		{
			Index_details index = indexes.get(indexCode);
			if(index==null)return false;
			return index.registerStudent(student);
		}
		public void removeStudentFromIndex(String indexCode, Student_details student)
		{
			Index_details index  = indexes.get(indexCode);
			if(index!=null)
			{
				index.removeFromRegistered(student.getName());
				index.removeFromWaitlist(student.getName());
			}
		}

}	  