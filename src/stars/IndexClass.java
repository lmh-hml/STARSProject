package stars;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**Enum of the typesof classes possible under a course.**/
enum IndexClassType { Lecture, Tutorial, Lab;}

/**
 * Class representing a lesson under a course and containing its details.
 * @author Lai Ming Hui
 * @version 1.0.0
 * @since 10/11/2020
 */
public class IndexClass implements FlatFileObject
{
	/**
	 * Delimiter to separate each column when serializing to flat file format.
	 */
	public final static String delimiter= "\\-";
	/**Lesson type of this index class.**/
	private String type = " ";
	/**Group code of this class**/
	private String group = "  ";
	/**The starting time of this class*/
	private LocalTime startTime = LocalTime.MIN;
	/**The ending time of this class**/
	private LocalTime endTime = LocalTime.MIN;;
	/**The day of the week that this class isheld on.**/
	private DayOfWeek day = null;
	/**The venue name of this class.**/
	private String venue = " ";
	/**The code of the index this class belongs to.**/
	private String indexCode = " ";
	/**Number of fields in this class that should be read/written to flat file**/
	private final static int NumFields = 7;
	
	/**
	 * Constructs an instance of index class with the specified parameters.
	 * @param type lesson type of this class, one of "Lecture", "Lab" or "Tutorial"
	 * @param group Group code of this class.
	 * @param start Startiing time of this class
	 * @param end Ending timeof this class
	 * @param day The day this class is held in
	 * @param venue THe venue ona name of this class.
	 */
	IndexClass(String type, String group, LocalTime start, LocalTime end, DayOfWeek day, String venue)
	{
		this.setType(type);
		this.group = group;
		this.startTime = start;this.endTime =end;
		this.day = day;
		this.venue = venue;
	};
	/**
	 * Default constructor of this class.
	 */
	IndexClass(){};
	
	/**
	 * @return the lesson type of this class (Lecture, Tutorial or Lab)
	 */
	public String getTypeStr() { return this.type;}
	public IndexClassType getType() { return IndexClassType.valueOf(type);}
	/**
	 * @param classType the type of index class to set, among Lecture, Lab and Tuorial.
	 */
	public void setType(IndexClassType classType) 
	{
		this.type = classType.toString();
	}		
	/**
	 * Sets the class type of this index class
	 * The options should be among Lectures, Lab or Tutorial.
	 * If the parameter does not fit the above types, this method will not set anything.
	 * @param classType One among "Lectures", "Lab" or "Tutorial".
	 * @return True if setting the typeis successful, False if the string passed in is not among the supported class types
	 * and the method fails to set this object's index class type.
	 */
	public boolean setType(String classType)  
	{
		boolean success = true;
		try
		{
			 setType(IndexClassType.valueOf(classType));
		}
		catch(IllegalArgumentException e)
		{
			System.err.format("Argument %s is not one of : %s \n'", classType, Arrays.toString(IndexClassType.values()));
			success = false;
		}
		return false;
	}
	
	
	/**
	 * @return The group code of this class
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group code to set for this class
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * Returns an array containing the start and end time of this class.
	 * The first item in the list is the starting time, the second time is the ending time.
	 * @return A list of LocalTime objects, object at index 0 is the starting time , theobject at index 1 is the ending time.
	 */
	public ArrayList<LocalTime> getTime()
	{
		ArrayList<LocalTime> result = new ArrayList<LocalTime>();
		result.add(startTime);
		result.add(endTime);
		return result;
	}
	/**
	 * Sets the starting time of this class.
	 * @param startTime The starting time of this class.
	 */
	public void setStartTime(LocalTime startTime) {this.startTime = startTime;}
	/**
	 * The ending time of this class.
	 * @param endTime The ending time of this class.
	 */
	public void setEndTime(LocalTime endTime) { this.endTime = endTime;}
		
	/**
	 * Gets the day of week this day is held on.
	 * @return A DayOfWeek enum corresponding to the day this class is held on, null if the day as yet to be set.
	 */
	public DayOfWeek getDay()
	{
		return this.day;
	}
	/**
	 * Sets the day of the week that this class is held on.
	 * @param day The day of the week this class is held on.
	 */
	public void setDay(DayOfWeek day) {this.day = day;}
	
	/**
	 * Gets the venue that this class is held at.
	 * @return The name of the venue this class is held at.
	 */
	public String getVenue() { return this.venue;}
	/**
	 * Sets the name of the venue that this class is held at.
	 * @param venue The name of the venue this class is held in.
	 */
	public void setVenue(String venue) { this.venue = venue;}
	/**
	 * Gets the code of the index this class id under.
	 * @return The code of the index this class is under.
	 */
	public String getIndexCode() { return this.indexCode;}
	/**
	 * Sets the code of the index this class is under.
	 * @param indexCode The specified code of the index to set.
	 */
	public void setIndexCode(String indexCode) {  this.indexCode = indexCode;}
	
	/**
	 * Determines if this indexClass clashes with another indexClass.
	 *@param otherClass The specified IndexClass to check against.
     * @return True if the two index class overlaps, false otherwise.
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
  
    public String toFlatFileString()
	{
		String s = "";
		s += type.toString() + '-';
		s += group.toString() + '-';
		s += startTime.toString()+'-';
		s+= endTime.toString() + '-';
		s+= day.toString() + '-';
		s+= venue.toString() + '-';
		s+= indexCode + '-';
		return s;		
	}
	public boolean fromFlatFileString(String s)
	{
		String[] array = s.split("\\-");
		if(array.length<NumFields)return false;
		this.setType(array[0]);
		group = array[1];
		startTime = LocalTime.parse(array[2]);
		endTime = LocalTime.parse(array[3]);
		day = DayOfWeek.valueOf(array[4]);
		venue = array[5];
		indexCode = array[6];
		return true;
	}	
	@Override
	public String toString()
	{
		return String.format("%s %s, %s to %s at %s", type, group, startTime, endTime, venue);
	}	
	@Override	
	public String getDatabaseId() {
		return this.group;
	}
	
}
