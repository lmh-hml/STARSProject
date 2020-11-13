package stars;

//COMMENT

public class Student_details {

	public String name;
	public String matric_num;
	public String gender;
	public int AU;
	public String nationality;
	public String[] CourseRegistered;
	
	public Student_details(String name, String matric_num, String gender, int AU, String nationality, String[] CourseRegistered)
	{
		this.name = name;
		this.matric_num = matric_num;
		this.gender = gender;
		this.AU = AU;
		this.nationality = nationality;
		this.CourseRegistered = CourseRegistered;
	}


	public String[] getCourseRegistered() {
		return CourseRegistered;
		
		
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

	public int getAU() {
		return AU;
	}

	public void setAU(int aU) {
		AU = aU;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public void setCourseRegistered(String[] courseRegistered) {
		CourseRegistered = courseRegistered;
	}
}
