package stars;

import java.lang.instrument.UnmodifiableClassException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import stars.FlatFileObject;

public class Index_details implements stars.FlatFileObject{		

	private String indexCode = "";
	private String courseCode = "";
	private int capacity=0;

	private ArrayList<IndexClass> classes = new ArrayList<>();
	private ArrayList<String> registered = new ArrayList<>();
	private ArrayList<String> waitlist = new ArrayList<>();
	
	/**Number of fields in this class that should be read/written to flat file**/
	private static final int NumFields = 6;
	

	public Index_details() {
	}

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
		
		s += FlatFileObject.listToFlatFileString(registered);
		s += FlatFileObject.listToFlatFileString(waitlist);

		
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
		
		String[] registered_array = array[4].split("\\,");
		for( String str : registered_array)
		{
			this.registered.add(str);
		}
		
		String[] waitlist_array = array[5].split("\\,");
		for( String str : waitlist_array)
		{
			this.waitlist.add(str);
		}		
		return true;
	}

	@Override
	public String getDatabaseId() {
		return this.indexCode;
	}

	
	
	public String getIndexCode() { return this.indexCode;}
	public void setIndexCode(String s) { this.indexCode = s;}	
	public void addIndexClass(IndexClass c)
	{
		this.classes.add(c);
	}
	public ArrayList<IndexClass> getIndexClass()
	{
		return classes;
	}

	public void setCourseCode(String courseCode)
	{ this.courseCode = courseCode;}
	public String getCourseCode() 
	{ return this.courseCode;}

	public void setCapacity( int cap ) {this.capacity  =cap;}
	public int getCapacity() { return this.capacity;}
	public int getVacancy()	{	return this.capacity - this.registered.size();}

	
	
	public boolean registerStudent(String studentName)
	{
		if(this.getVacancy() > 0)
		{
			this.registered.add(studentName);
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean registerStudent(Student_details student) 
	{ return registerStudent(student.getName());}	
	public boolean removeFromRegistered(String studentName) 
	{
		boolean success = this.registered.remove(studentName);
		return success;
		
	}
	
	public boolean addStudentToWaitlist(String studentName)
	{
		if(this.getVacancy() == 0)
		{
			this.waitlist.add(studentName);
			return true;
		}
		else
		{
			return false;
		}

	}
	public boolean addStudentToWaitlist(Student_details student) 
	{ return addStudentToWaitlist(student.getName());}
	public boolean removeFromWaitlist(String studentName)
	{
		boolean success = this.registered.remove(studentName);
		return success;
	}
	
	
	public boolean isRegistered(String studentName)
	{
		return registered.contains(studentName);
	}	
	public boolean isWaiting(String studentName)
	{
		return waitlist.contains(studentName);
	}
	
	public String[] getRegisteredStudents()
	{
		return (String[]) registered.toArray();
	}
	
	public String[] getWaitingStudents()
	{
		return  (String[]) waitlist.toArray();
	}
	
	public Object[] getIndexClasses()
	{
		return this.classes.toArray();
	}
	
	
	public static void main(String args[])
	{
		
		Index_details index = new Index_details();
		index.setIndexCode("abc");
		index.setCapacity(1);
		{
			IndexClass c = new IndexClass("Lecture","SE1",LocalTime.now(), LocalTime.now(),DayOfWeek.FRIDAY,"lab4s");
			index.addIndexClass(c);
		}
		index.registerStudent("Jack");
		index.addStudentToWaitlist("Jim");
		
		IndexClass c2 = (IndexClass) index.getIndexClasses()[0];

		String s = c2.toFlatFileString();
		System.out.println(c2.toFlatFileString());

		
		IndexClass c3 =new IndexClass(); 
		c3.fromFlatFileString(s);
		System.out.println(index.toFlatFileString());
	}

}
