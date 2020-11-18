package stars;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import stars.FlatFileObject;

public class Index_details implements stars.FlatFileObject{
	
	private String indexCode = "";
	private String courseCode = "";
	private ArrayList<IndexClass> classes = new ArrayList<>();
	private ArrayList<String> registered = new ArrayList<>();
	private ArrayList<String> waitlist = new ArrayList<>();
	private int capacity;
	
	public static class IndexClass
	{
		private final static String delimiter= "\\-";
		private String type = "";
		private String group = "";
		private LocalTime startTime;
		private LocalTime endTime;
		private DayOfWeek day;
		private String venue;
		
		IndexClass(String type, String group, LocalTime start, LocalTime end, DayOfWeek day, String venue)
		{
			this.type =type;
			this.group = group;
			this.startTime = start;this.endTime =end;
			this.day = day;
			this.venue = venue;
			
		};
		IndexClass(){};
		
		public String toFlatFileString()
		{
			String s = "";
			s += type.toString() + '-';
			s += group.toString() + '-';
			s += startTime.toString()+'-';
			s+= endTime.toString() + '-';
			s+= day.toString() + '-';
			s+= venue.toString();
			return s;		
		}
	
		public void fromFlatFileString(String s)
		{
			String[] array = s.split("\\-");
			type = array[0];
			group = array[1];
			startTime = LocalTime.parse(array[2]);
			endTime = LocalTime.parse(array[3]);
			day = DayOfWeek.valueOf(array[4]);
			venue = array[5];
		}
		
		public DayOfWeek getDay()
		{
			return this.day;
		}
		
		public ArrayList<LocalTime> getTime()
		{
			ArrayList<LocalTime> result = new ArrayList<LocalTime>();
			result.add(startTime);
			result.add(endTime);
			return result;
		}
		
		/**
	     * Used to determine if two classes gets clash
	     * But you will need to confirm that both course happens on the same Day beforehand
	     * 
	     * a clash return true, otherwise false
	     * @param start1
	     * @param end1
	     * @param start2
	     * @param end2
	     * @return
	     */
	    public boolean clash(IndexClass otherClass)
	    {
	    	DayOfWeek day1 = this.day;
	    	DayOfWeek day2 = otherClass.getDay();
	    	if(day1!=day2)
	    	{
	    		return false;
	    	}
	    	LocalTime start1 = this.startTime;
	    	LocalTime end1 = this.endTime;
	    	LocalTime start2 = otherClass.getTime().get(0);
	    	LocalTime end2 = otherClass.getTime().get(1);
	    	if(start1.compareTo(start2)<0)//start1 before start2
	    	{
	    		if(end1.compareTo(start2)>=0)
	    			return true;
	    		else
	    			return false;
	    	}else {
	    		//start1 is after start2
	    		if(start1.compareTo(end2)<0)
	    			return true;
	    		else
	    			return false;
	    	}
	    }
		
	}
	
	

	public Index_details() {
	}

	@Override
	public String toFlatFileString() {

		String s = "";
		s += indexCode + delimiter;
		s += courseCode + delimiter;
		
		for(IndexClass c : classes)
		{
			s += c.toFlatFileString() + ',';
		}
		s += delimiter;
		
		for(String str : registered)
		{
			s +=  str + ',';
		}
		s += delimiter;
		
		for(String str : waitlist)
		{
			s +=  str + ',';
		}
		s += delimiter;
		
		return s;
	
	}

	@Override
	public void fromFlatFileString(String s) {
		String[] array = s.split(delimiter);
		
		indexCode = array[0];
		courseCode = array[1];
		
		String[] classes_array = array[2].split(IndexClass.delimiter);
		for( String str : classes_array)
		{
			IndexClass indexClass = new IndexClass();
			indexClass.fromFlatFileString(str);
			this.classes.add(indexClass);
		}
		
		String[] registered_array = array[3].split("\\,");
		for( String str : registered_array)
		{
			this.registered.add(str);
		}
		
		String[] waitlist_array = array[4].split("\\,");
		for( String str : waitlist_array)
		{
			this.waitlist.add(str);
		}		
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
	public void setCourseCode(String courseCode) { this.courseCode = courseCode;}
	public String getCourseCode() { return this.courseCode;}

	public void setCapacity( int cap ) {this.capacity  =cap;}
	public int getCapacity() { return this.capacity;}
	
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

	public void removeFromRegistered(String studentName) 
	{
		this.registered.remove(studentName);
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

	public void removeFromWaitlist(String studentName)
	{
		this.waitlist.remove(studentName);
	}
	
	public int getVacancy()
	{
		return this.capacity - this.registered.size();
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
		return classes.toArray();
	}
	
	
	public static void main(String args[])
	{
		
		Index_details index = new Index_details();
		index.setIndexCode("abc");
		index.setCapacity(1);
		{
		Index_details.IndexClass c = new IndexClass("Lec","SE1",LocalTime.now(), LocalTime.now(),DayOfWeek.FRIDAY,"lab4s");
		index.addIndexClass(c);
		}
		index.registerStudent("Jack");
		index.addStudentToWaitlist("Jim");
		
		Index_details.IndexClass c2 = (IndexClass) index.getIndexClasses()[0];

		String s = c2.toFlatFileString();
		System.out.println(c2.toFlatFileString());

		
		Index_details.IndexClass c3 =new IndexClass(); 
		c3.fromFlatFileString(s);
		System.out.println(index.toFlatFileString());

		
		
		
	}

}
