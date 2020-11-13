package stars;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student_details implements FlatFileObject{


	public String name="";
	public String matric_num = "";
	public String gender = "";
	public String AU = "";
	public String nationality = "";
	private String id = "";
	public List<String> courseRegistered = new ArrayList<>();

	
	/*public Student_details(String name, String matric_num, String gender, int AU, String nationality, String[] CourseRegistered)
	{
		this.name = name;
		this.matric_num = matric_num;
		this.gender = gender;
		this.AU = AU;
		this.nationality = nationality;
		this.CourseRegistered = CourseRegistered;
	}*/
	public Student_details() {}

	public String toFlatFileString() {
		
		String s = "";
		for( String course : courseRegistered)
		{
			s += course + ',';
		}
		
		String courses = courseRegistered.toString();
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

	public static List<String> getFields()
	{
		ArrayList<String> list = new ArrayList<String>();
		for(Field f : User_details.class.getDeclaredFields())
		{
			list.add(f.getName());
		}
		return list;
	}

	public List<String> getCourseRegistered() {
		return courseRegistered;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatric_num() {
		return matric_num;
	}

	public void setMatric_num(String matric_num) {
		this.matric_num = matric_num;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}



	

	public String getAU() {
		return AU;
	}

	public void setAU(String aU) {
		AU = aU;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}




	public void setCourseRegistered(List<String> courses) {
		courseRegistered.addAll(courses);
	}
	
	public void addCourse(String... courses ) {
		for(String s: courses)
		{
			courseRegistered.add(s);
		}
	}
	
	public void removeCourse(String course)
	{
		courseRegistered.remove(course);
	}


}