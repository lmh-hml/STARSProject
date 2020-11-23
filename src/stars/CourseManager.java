package stars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A class that manages the data of of courses and indexes.
 * @author Work
 *
 */
public class CourseManager{

	/**String that stores the name of the file that contain course information.**/
	private String courseFile = "";
	/**String that stores the name of the file that contain index information.**/
	private String indexFile = "";
	
	/**Member CourseDatabase object**/
	private CourseDatabase courses = new CourseDatabase();
	/**Member CourseDatabase object**/
	private IndexDatabase  indexes = new IndexDatabase();
	/**Member CourseDatabase object**/
	private HashMap<String, Set<String>> studentsInCourseCache = new HashMap<>();

	/**
	 * Initializes this manager from data using files that contain course information and index information.
	 * @param courseFile The name of the file that contain course information in flat file format.
	 * @param indexFile  The name of the file that contains indexes in flat file format.
	 */
	public CourseManager(String courseFile, String indexFile) {
		try {
			courses.openFile(courseFile);
			indexes.openFile(indexFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.courseFile = courseFile;
		this.indexFile = indexFile;
	}

	
	/**Writes the contents of this database
	 * back to the files information was read from..
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
	
	
//METHODS REGARDING INDEXES
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
		course.addIndexCode(index.getIndexCode());
	}
	public void removeIndex( String indexCode)
	{
		Index_details index = this.indexes.get(indexCode);
		Course c = this.courses.get(index.getCourseCode());
		if(c != null)
		{
			c.removeIndexCode(indexCode);
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
	
//METHODS REGARDING COURSES
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
	/**
	 * Checks if a course with the specified course code exists in the database.
	 * @param courseCode The specified course code.
	 * @return True if such a course exists, false otherwise.
	 */
	public boolean checkCourseExists(String courseCode)
	{
		return this.courses.getKeys().contains(courseCode);
	}
		
	
//ACCESSING INDEX INFORMATION
	/**
	 * Gets the classes under the index with the specified index code with the specified Index Class type.
	 * @param indexCode Code of the target index
	 * @param type A IndexClassType enum member
	 * @return A list of Index Class objects with the specified index class type.
	 */	
	public List<IndexClass> getIndexClasses(Index_details index,IndexClassType type) 
	{
		ArrayList<IndexClass> lecList = new ArrayList<>();
		for(IndexClass ic : index.getIndexClasses())
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
	public List<IndexClass> getLectures(Index_details index) 
	{
		return getIndexClasses(index, IndexClassType.Lecture);
	}
	/**
	 * Gets the lab classes under the index with the specified index code
	 * @param indexCode COde of the target index
	 * @return A list of Index Class objects which are the lab classes of the index.
	 */
	public List<IndexClass> getLab(Index_details index) 
	{
		return getIndexClasses(index, IndexClassType.Lab);
	}		
	/**
	 * Gets the tutorials classes under the index with the specified index code
	 * @param indexCode COde of the target index
	 * @return A list of Index Class objects which are the tutorial classes of the index.
	 */
	public List<IndexClass> getTut(Index_details index)
	{
		return getIndexClasses(index, IndexClassType.Tutorial);	
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
	/**Checks if the specified index is under the specified course.
	 * @param index The specified index.
	 * @param course The specified course
	 * @return True if the index is under the course, false otherwise.
	 */
	public boolean isIndexUnderCourse(Index_details index, Course course)
	{
		return index.getCourseCode().equals(course.getcoursecode());
	}
	public Set<Index_details> getIndexUnderCourse(Course course)
	{
		Set<Index_details> indexSet = new HashSet<>();
		for(String indexCode: course.getIndexCodes())
		{
			indexSet.add(indexes.get(indexCode));
		}
		return indexSet;
	}

	/**
	 * Retrieves a set of matriculation number of students registered under the specified course
	 * @param courseCode Course code of the target course.
	 * @return A set of matriculation number of students registered under the specified course.
	 */
	public Set<String> getStudentsInCourse(Course course)  
	{
		//Creates and caches the collection of students in the specified course if it not already created.
		Set<String> students = new HashSet<>();
		for(Index_details index : this.getIndexUnderCourse(course))
		{
			for(String matricNum : index.getRegisteredStudents())
			{
				students.add(matricNum);
			}
		}
		return students;
	}
	/**
	 * Checks if the specified student matric number is registered under a course.
	 * @param matricNum The matriculation number of the student
	 * @param courseCode The course code tof the course to be checked
	 * @return True if the student is listed under the course, false otherwise.
	 */
	public boolean isStudentInCourse(String matricNum, Course course)
	{
		boolean found = false;
		for( String matric : getStudentsInCourse(course))
		{
			if(matricNum.equals(matric))found = true;
		}
		return found;
	}
	/**
	 * Gets the AU of the course with the specified course code.
	 * @param courseCode Course code of the target course.
	 * @return The AU of the course if the course exists, otherwise -1.
	 */
	public int getCourseAU(Course course)
	{
		return course.getAU();
	}
	/**
	 * Gets the AU of the specified index.
	 * @param index The specified Index_object object.
	 * @return The AU of the specified index.
	 */
	public int getIndexAU(Index_details index)
	{
		return getCourseAU( courses.get(index.getCourseCode()));
	}

}