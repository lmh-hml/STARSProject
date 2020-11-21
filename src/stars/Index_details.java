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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import stars.FlatFileObject;

public class Index_details implements stars.FlatFileObject{		

	private String indexCode = "";
	private String courseCode = "";
	private int capacity=0;

	/**A list containing the different classes (lectures, lab, tutorials) of this index.**/
	private ArrayList<IndexClass> classes = new ArrayList<>();
	/**A list containing the Matriculation number of students registered in this index.**/
	private Set<String> registered = new LinkedHashSet<>();
	/**A list containing the Matriculation number of students waiting in this index.**/
	private Set<String> waitlist = new LinkedHashSet<>();
	
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
		return String.format("Index Code: %s, Total capacity: %d", indexCode, capacity);
	}
	@Override
	public String getDatabaseId() {
		return this.indexCode;
	}
		
	public String getIndexCode() { return this.indexCode;}
	public void setIndexCode(String s) { this.indexCode = s;}	
	public void addIndexClass(IndexClass c)
	{
		c.setIndexCode(this.indexCode);
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
	public boolean registerStudent(Student_details student) 
	{ return registerStudent(student.getMatric_num());}	
	public boolean removeFromRegistered(String matricNum) 
	{
		boolean success = this.registered.remove(matricNum);
		return success;
		
	}
	
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
	public boolean addStudentToWaitlist(Student_details student) 
	{ return addStudentToWaitlist(student.getMatric_num());}
	public boolean removeFromWaitlist(String matricNum)
	{
		return this.waitlist.remove(matricNum);

	}
	public String  getFirstWaitingStudent()
	{
		if(waitlist.isEmpty())return null;
		return waitlist.iterator().next();
	}
	
	public boolean isRegistered(String matricNum)
	{
		return registered.contains(matricNum);
	}	
	public boolean isWaiting(String matricNum)
	{
		return waitlist.contains(matricNum);
	}
	
	public Set<String> getRegisteredStudents()
	{
		return  Collections.unmodifiableSet(this.registered);
	}
	
	public Set<String> getWaitingStudents()
	{
		return Collections.unmodifiableSet(this.waitlist);
	}
	
	public IndexClass[] getIndexClasses()
	{
		return this.classes.toArray(new IndexClass[this.classes.size()]);
	}
	
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
	
	public static void main(String args[])
	{
		
		Index_details index = new Index_details();
		index.setIndexCode("10019");
		index.setCapacity(1);
		{
			IndexClass c = new IndexClass("LEC","SE1",LocalTime.of(8, 30), LocalTime.of(9,30),DayOfWeek.FRIDAY,"lab4");
			index.addIndexClass(c);
		}
		index.registerStudent("Jack");
		index.addStudentToWaitlist("Jim");
		
		IndexClass c2 = (IndexClass) index.getIndexClasses()[0];

		String s = c2.toFlatFileString();
		System.out.println(c2.toFlatFileString());

		
		IndexClass c3 =new IndexClass(); 
		c3.fromFlatFileString(s);
		System.out.println(c3.toFlatFileString());
	}

}
