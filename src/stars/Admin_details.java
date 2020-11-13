package stars;
public class Admin_details {
	public String name;
	public String course;
	public String gender;
	public String nationality;
	
	public Admin_details(String name, String course, String gender, String nationality)
	{
		
		this.name = name;
		this.course = course;
		this.gender = gender;
		this.nationality = nationality;	
	}
	public String getname() {
		return name;
	}
	public String getcourse() {
		return course;
	}
	public String getgender() {
		return gender;
	}
	public String getnationality() {
		return nationality;
	}
}
