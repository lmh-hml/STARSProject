package stars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	private String name=EmptyString;
	/**Matriculation number of the student*/
	private String matric_num = EmptyString;
	/**Student's gender*/
	private String gender = EmptyString;
	/**Student's AU.*/
	private int AU = 0;
	/**Student's nationality*/
	private String nationality = EmptyString;
	/**Study year of this student**/
	private int studyYear = 0;
	/**Email of this student**/
	private String userName = EmptyString;
	/**Courses registered by this student*/
	private Set<String> indexRegistered = new HashSet<>();
	/**Courses that this student is in the waitlist for.*/
	private Set<String> indexWaitlist = new HashSet<>();
	/**Number of fields in this class that should be read/written to flat file**/
	private static final int NumFields = 9;
	
	/**Default constructor*/
	public Student_details() {}



	/**
	 * Gets the indexes registered by this student.
	 * @return A list of the registered indexes' names
	 */
	public Set<String> getIndexRegistered() {
		return Collections.unmodifiableSet(indexRegistered);
	}
	/**
	 * Gets the indexes that this student is in the waiting list for.
	 * @return A list of index codes that is in the student's wait list.
	 */
	public Set<String> getIndexWaitlist() {
		return Collections.unmodifiableSet(indexWaitlist);
	}
	/**
	 * @return Return student's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets this Student's name
	 * @param name Name of the student
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
	 * @return This student's gender
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
	public int getAU() {
		return AU;
	}
	/**
	 * Sets this student's AU
	 * @param i Academic units of this student
	 */
	public void setAU(int i) {
		AU = i;
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
	 * @return the studyYear
	 */
	public int getStudyYear() {
		return studyYear;
	}
	/**
	 * @param studyYear the studyYear to set
	 */
	public void setStudyYear(int studyYear) {
		this.studyYear = studyYear;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @param indexWaitlist the indexWaitlist to set
	 */
	public void setindexWaitlist(Set<String> indexWaitlist) {
		this.indexWaitlist = indexWaitlist;
	}	
	/**
	 * Adds a course to the waitlist of this student
	 * @param courses The courses to be added to this student's waitlist
	 */
	public void addWaitlist(String... courses ) {
		for(String s: courses)
		{
			indexWaitlist.add(s);
		}
	}
	/**
	 * Removes the specified course from the waitlist of this student.
	 * @param course name of course to be removed
	 */
	public void removeFromWaitlist(String course)
	{
		indexWaitlist.remove(course);
	}
	/**
	 * Checks if the student has registered in the specified index course
	 * @param index The specified index
	 * @return True if the student is registered in the index, false otherwise
	 */
	public boolean isWaiting(String index)
	{
		return this.indexWaitlist.contains(index);
	}
	
	/**
	 * Add the courses' names to the list of courses registered by this student.
	 * @param indexCodes Names of courses to be added
	 */
	public void addIndex(String... indexCodes ) {
		for(String s: indexCodes)
		{
			indexRegistered.add(s);
		}
	}
	/**
	 * Removes the specified course from the courses registered by this student.
	 * @param indexCode name of course to be removed
	 */
	public void removeIndex(String indexCode)
	{
		indexRegistered.remove(indexCode);
	}
	/**
	 * Sets list names of courses registered by this student.
	 * @param indexCodes List of names  of courses to be added.
	 */
	public void setIndexRegistered(Set<String> indexCodes) {
		indexRegistered = indexCodes;
	}	
	/**
	 * Checks if the student is in waitlist of the specified index course
	 * @param index The specified index
	 * @return True if the student is in the waitlist of the index, false otherwise
	 */	
	public boolean isRegistered(String index)
	{
		return indexRegistered.contains(index);
	}
	
	
	/**
	 * Returns flat file string representation of this student object
	 * @return Flat file string representation of this object
	 */
	public String toFlatFileString() {
		
		String coursesString = FlatFileObject.collectionToFlatFileString(indexRegistered);
		String waitlistString = FlatFileObject.collectionToFlatFileString(indexWaitlist);

		return FlatFileObject.buildFlatFileString(name, matric_num, gender, AU, nationality,studyYear ,userName, coursesString, waitlistString ) ;
	}
	/**
	 * Initializes this object with a flat file string read from a flat file.
	 * This method does nothing if the flat file string has insufficient columns to initialize this object.
	 * @return True if the deserialization is successsful, flase otherwise.
	 */
	@Override
	public boolean fromFlatFileString(String s) {
		ArrayList<String> array = new ArrayList<String>( Arrays.asList(s.split("\\|")) );
		if(array.size()<NumFields)return false;
		this.name = array.get(0);
		this.matric_num = array.get(1);
		this.gender = array.get(2);
		this.AU = Integer.parseInt(array.get(3));
		this.nationality = array.get(4);
		this.studyYear = Integer.parseInt(array.get(5));
		this.userName = array.get(6);
		
		String registeredData = array.get(7);
		FlatFileObject.flatFileStringToCollection(registeredData, indexRegistered);
		
		String waitlistData = array.get(8);
		FlatFileObject.flatFileStringToCollection(waitlistData, indexRegistered);

		return true;
	}
	/**
	 * Returns the value of this flat file object's id in the database
	 * @return The string id used to index this object in a flatfile database.
	 */
	@Override
	public String getDatabaseId() {
		return this.userName;
	}
	/**Prints the etails of this student**/
	public void printDetails()
	{
		System.out.format("\nStudent Name: %s\n", getName());
		System.out.format("Matric Number: %s\n", getMatric_num());
		System.out.format("Study year: %d\n", getStudyYear());
		System.out.format("Gender: %s\n", getGender());
		System.out.format("Nationality: %s\n", getNationality());
		System.out.format("Total AU of registered courses:  %d\n\n", getAU());
	}	


}