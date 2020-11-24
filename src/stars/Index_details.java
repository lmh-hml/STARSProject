package stars;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A class that represents a course index that a student can register for
 * during add/drop period.
 * @author Lai MingHui, Low Wei Xian
 * @since  10/11/2020
 * @version 1.0.0
 *
 */
public class Index_details implements stars.FlatFileObject{		

	/**The index code of the this index**/
	private String indexCode = "";
	/**The code of the course this index is under.**/
	private String courseCode = "";
	/**The maximum number of students that can be registered in this index**/
	private int capacity=0;

	/**A list containing the different classes (lectures, lab, tutorials) of this index.**/
	private Set<IndexClass> classes = new HashSet<>();
	/**A list containing the Matriculation number of students registered in this index.**/
	private Set<String> registered = new LinkedHashSet<>();
	/**A list containing the Matriculation number of students waiting in this index.**/
	private Set<String> waitlist = new LinkedHashSet<>();
	
	/**Number of fields in this class that should be read/written to flat file**/
	private static final int NumFields = 6;
	public Index_details() {}

//GETTERS AND SETTERS
	/**
	 * Returns the index code of this index.
	 * @return The index code of this index.
	 */
	public String getIndexCode() { return this.indexCode;}
	/**
	 * Sets the index code of this index.
	 * @param indexCode The specified index code to set.
	 */
	public void setIndexCode(String indexCode) { this.indexCode = indexCode;}	
	/**Adds an index class to this index.
	 * @param c The specified index class object.
	 */
	public void addIndexClass(IndexClass c)
	{
		c.setIndexCode(this.indexCode);
		this.classes.add(c);
	}
	/**Returns an unmodifiable set containing all the index classes that are
	 * under this index.
	 * @return An unmodifiable set containing all index classes in this index.
	 */
	public Set<IndexClass> getIndexClasses()
	{
		return Collections.unmodifiableSet(classes);
	}
	/**Sets the code of the course this index is under
	 * @param courseCode The specified course code.
	 */
	public void setCourseCode(String courseCode)
	{ this.courseCode = courseCode;}
	/**Returns the course code of this index.
	 * @return THe course code of this index.
	 */
	public String getCourseCode() 
	{ return this.courseCode;}
	/**
	 * Sets the capacity of this index.
	 * @param cap The capacity value to be set.
	 */
	public void setCapacity( int cap ) {this.capacity  =cap;}
	/**
	 * Returns the capacity of this index.
	 * @return The capacity of this index. 
	 */
	public int getCapacity() { return this.capacity;}
	/**
	 * Returns the difference between the maximum capacity of this index and the number of students in this index
	 * registered list.
	 * @return The vacancies left in this index.
	 */
	public int getVacancy()	{	return this.capacity - this.registered.size();}
	/**
	 * Adds the specified matriculation number to the index. This method will return false and not modify the state of this index 
	 * if the index can no longer fit any more students.
	 * @param matricNum 
	 * @return True if the matric number is succesfully added, false otherwise.
	 */
	public boolean registerStudent(String matricNum)
	{
		if(this.getVacancy() > 0)
		{
			return this.registered.add(matricNum);
		}
		else
		{
			return false;
		}
	}
	/**Adds the specified student to this index. This method will return false and notmodify the state of this index if 
	 * the index can no longer fit any more students.
	 * @param student A Student_detail object associated with the specified student.
	 * @return True if the student is successfully added, false otherwise.
	 */
	public boolean registerStudent(Student_details student) 
	{ return registerStudent(student.getMatric_num());}	
	/**
	 * Removes the matriculation number from this index.
	 * @param matricNum The specified matriculation number.
	 * @return True if the matric number is removed, false if thematric number is not int the index.
	 */
	public boolean removeFromRegistered(String matricNum) 
	{
		boolean success = this.registered.remove(matricNum);
		return success;
		
	}
	/**
	 * Adds the specified matric number to the waitlist of the index.
	 * @param matricNum The specified matric number.
	 * @return True if the matric number is successfully added, false otherwise.
	 */
	public boolean addStudentToWaitlist(String matricNum)
	{
		if(this.getVacancy() == 0)
		{
			this.waitlist.add(matricNum);
			return true;
		}
		else
		{
			return false;
		}

	}
	/**
	 * Adds a student to the waitlist of this index.
	 * @param student The specified student
	 * @return True if the student is successfully added, false otherwise.
	 */
	public boolean addStudentToWaitlist(Student_details student) 
	{ return addStudentToWaitlist(student.getMatric_num());}
	/**
	 * Removes the student from the waitlist of this student.
	 * @param matricNum The specified matric number
	 * @return True if student is successfully added, false otherwise.
	 */
	public boolean removeFromWaitlist(String matricNum)
	{
		return this.waitlist.remove(matricNum);

	}
	/**
	 * Gets the matric number of the earliest student added to the waiting list of this index.
	 * @return
	 */
	public String  getFirstWaitingStudent()
	{
		if(waitlist.isEmpty())return null;
		return waitlist.iterator().next();
	}
	/**
	 * Checks the specified matriculation number is listed among theindex's registered list.
	 * @param matricNum
	 * @return True if the number is listed, false otherwise.
	 */
	public boolean isRegistered(String matricNum)
	{
		return registered.contains(matricNum);
	}	
	/**
	 * Checks if the specified matric number is listed in this index's waiting list.
	 * @param matricNum The specified matric number 
	 * @return True if the numberis listed, false otherwise.
	 */
	public boolean isWaiting(String matricNum)
	{
		return waitlist.contains(matricNum);
	}
	
	/**
	 * Returns an unmodifiable set containing the matriculation number of all students
	 * in the registered list of this index.
	 * @return An modifiable set containing the matriculation number of all students registered under this index.
	 */
	public Set<String> getRegisteredStudents()
	{
		return  Collections.unmodifiableSet(this.registered);
	}	
	/**
	 * Returns an unmodifiable set containing the matriculation number of all students under this index's
	 * waiting list.
	 * @return An unmodifiable set containing the matriculation number of all students under this index's
	 * waiting list.
	 */
	public Set<String> getWaitingStudents()
	{
		return Collections.unmodifiableSet(this.waitlist);
	}

	/**
	 * Checks if another index overlaps with this index in terms of day and time.
	 * @param otherIndex The specified index to check against.
	 * @return True if the two indexes overlaps,  false otherwise.
	 */
	public boolean clash(Index_details otherIndex)
	{
		boolean clash = false;
		for(IndexClass c : this.classes)
		{
			for(IndexClass c2 : otherIndex.getIndexClasses())
			{
				clash = c.clash(c2);
				if(clash==true)break;
			}
		}
		return clash;
	}
	
//FLATFILE INTERFACE AND OVERRIDES
		@Override
	public String toFlatFileString() {

			String s = "";
			s += indexCode + delimiter;
			s += courseCode + delimiter;
			s += capacity + delimiter;
			for(IndexClass c : classes)
			{
				s += c.toFlatFileString() + ',';
			}
			s += delimiter;
			
			s += FlatFileObject.collectionToFlatFileString(registered)+delimiter;
			s += FlatFileObject.collectionToFlatFileString(waitlist)+delimiter;
			return s;
		
	}
		@Override
	public boolean fromFlatFileString(String s) {
			String[] array = s.split(regexDelimiter);
			if(array.length < NumFields )return false;
			
			indexCode = array[0];
			courseCode = array[1];
			capacity = Integer.parseInt(array[2]);
			
			String[] classes_array = array[3].split("\\,");
			for( String str : classes_array)
			{
				IndexClass indexClass = new IndexClass();
				indexClass.fromFlatFileString(str);
				this.classes.add(indexClass);
			}
			
			FlatFileObject.flatFileStringToCollection(array[4], registered);		
			FlatFileObject.flatFileStringToCollection(array[5], waitlist);		
			return true;
		}
	public String toString()
	{
		return String.format("Index Code: %s, %d/%d slots left.", indexCode, getVacancy(), capacity);
	}
	@Override
	public String getDatabaseId() {
		return this.indexCode;
	}
			
}
