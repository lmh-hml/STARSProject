package stars;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that holds the details regarding a student who uses the Stars system.
 * A student can be enrolled into courses through the Stars system.
 * @author Low Wei Xian, Lai Ming Hui
 * @version 1.0
 * @since 2020/11/14
 *
 */
public class Student_details implements FlatFileObject{

	/**Name of the student*/
	private String name="";
	/**Matriculation number of the student*/
	private String matric_num = "";
	/**Student's gender*/
	private String gender = "";
	/**Student's AU.*/
	private String AU = "";
	/**Student's nationality*/
	private String nationality = "";
	/**Id of this particular student_details instance*/
	private String id = "";
	/**Courses registered by this student*/
	private List<String> courseRegistered = new ArrayList<>();
	/**Courses that this student is in the waitlist for.*/
	private List<String> courseWaitlist = new ArrayList<>();
	
	/**Default constructor*/
	public Student_details() {}


	public String toFlatFileString() {
		
		String s = "";
		for( String course : courseRegistered)
		{
			s += course + ',';
		}
		
		String courses = courseRegistered.toString();
		String waitlist = courseRegistered.toString();
		return FlatFileObject.buildFlatFileString(name, matric_num, gender, AU, nationality, id, s ) ;
	}
	
	@Override
	public void fromFlatFileString(String s) {
		ArrayList<String> array = new ArrayList<String>( Arrays.asList(s.split("\\|")) );
		this.name = array.get(0);
		this.matric_num = array.get(1);
		this.gender = array.get(2);
		this.AU = array.get(3);
		this.nationality = array.get(4);
		this.id  = array.get(5);
		
		for(String item: array.get(6).split("\\,"))
		{
			this.addCourse(item);
		}
		
	}

	/**
	 * Gets the courses registered by this student.
	 * @return A list of the registered courses' names
	 */
	public List<String> getCourseRegistered() {
		return courseRegistered;
	}

	/**
	 * @return Return student's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets this Student's name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Return student's matriculation number
	 */
	public String getMatric_num() {
		return matric_num;
	}

	/**
	 * Sets this user's matriculation number
	 * @param matric_num Student's matriculation number
	 */
	public void setMatric_num(String matric_num) {
		this.matric_num = matric_num;
	}

	/**
	 * Gets this student's gender.
	 * @return
	 */
	public String getGender() {
		return gender;
	}


	/**
	 * Set this student's gender "M" or "F"
	 * @param gender Student's gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Returns this student's AU
	 * @return String representation of this student'AU
	 */
	public String getAU() {
		return AU;
	}

	/**
	 * Sets this student's AU
	 * @param aU
	 */
	public void setAU(String aU) {
		AU = aU;
	}

	/**
	 * Returns this student's nationality
	 * @return Student's nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * Set this student's nationality
	 * @param nationality This student's nationality
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	/**
	 * @return Student's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set this student's id
	 * @param Student's Id
	 */
	public void setId(String id) {
		this.id = id;
	}




	/**
	 * @return the courseWaitlist
	 */
	public List<String> getCourseWaitlist() {
		return courseWaitlist;
	}


	/**
	 * @param courseWaitlist the courseWaitlist to set
	 */
	public void setCourseWaitlist(List<String> courseWaitlist) {
		this.courseWaitlist = courseWaitlist;
	}


	/**
	 * Add the courses' names to the list of courses registered by this student.
	 * @param courses Names of courses to be added
	 */
	public void addCourse(String... courses ) {
		for(String s: courses)
		{
			courseRegistered.add(s);
		}
	}
	
	/**
	 * Removes the specified course from the courses registered by this student.
	 * @param course name of course to be removed
	 */
	public void removeCourse(String course)
	{
		courseRegistered.remove(course);
	}

	/**
	 * Sets list names of courses registered by this student.
	 * @param courses List of names  of courses to be added.
	 */
	public void setCourseRegistered(List<String> courses) {
		courseRegistered = courses;
	}


}