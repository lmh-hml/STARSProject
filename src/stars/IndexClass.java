package stars;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

enum IndexClassType { Lecture, Tutorial, Lab;}
public class IndexClass implements FlatFileObject
{
	public final static String delimiter= "\\-";
	private String type = " ";
	private String group = "  ";
	private LocalTime startTime = LocalTime.MIN;
	private LocalTime endTime = LocalTime.MIN;;
	private DayOfWeek day = DayOfWeek.MONDAY;
	private String venue = " ";
	private String indexCode = " ";
	/**Number of fields in this class that should be read/written to flat file**/
	private final static int NumFields = 7;
	
	IndexClass(String type, String group, LocalTime start, LocalTime end, DayOfWeek day, String venue)
	{
		this.setType(type);
		this.group = group;
		this.startTime = start;this.endTime =end;
		this.day = day;
		this.venue = venue;
	};
	IndexClass(){};
	
	/**
	 * @return the lesson type of this class (Lecture, Tutorial or Lab)
	 */
	public String getTypeStr() { return this.type;}
	public IndexClassType getType() { return IndexClassType.valueOf(type);}
	/**
	 * @param type the type to set
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
	 */
	public void setType(String classType) throws IllegalArgumentException
	{
		setType(IndexClassType.valueOf(classType));
	}
	
	
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	
	public ArrayList<LocalTime> getTime()
	{
		ArrayList<LocalTime> result = new ArrayList<LocalTime>();
		result.add(startTime);
		result.add(endTime);
		return result;
	}
	public void setStartTime(LocalTime startTime) {this.startTime = startTime;}
	public void setEndTime(LocalTime endTime) { this.endTime = endTime;}
		
	public DayOfWeek getDay()
	{
		return this.day;
	}
	public void setDay(DayOfWeek day) {this.day = day;}
	
	public String getVenue() { return this.venue;}
	public void setVenue(String venue) { this.venue = venue;}
	
	public String getIndexCode() { return this.indexCode;}
	public void setIndexCode(String indexCode) {  this.indexCode = indexCode;}
	
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
	
	
	public static void main(String args[])
	{
		IndexClass indexClass = new IndexClass();
		System.out.println(indexClass.toFlatFileString());
		
		indexClass.setType(IndexClassType.Lecture);
		indexClass.setGroup("SE2");
		indexClass.setDay(DayOfWeek.FRIDAY);
		indexClass.setStartTime(LocalTime.of(8, 30));
		indexClass.setEndTime(LocalTime.of(10, 30));
		indexClass.venue = "LT2";
		System.out.println(indexClass.toFlatFileString());
		
		IndexClass i2 = new IndexClass();
		i2.fromFlatFileString(indexClass.toFlatFileString());
		System.out.println(i2.toFlatFileString());


	}
}
