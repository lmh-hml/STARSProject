package stars;
public class Admin_details implements FlatFileObject {
	public String name;
	public String course;
	public String gender;
	public String nationality;
	
	public Admin_details (String name, String course, String gender, String nationality)
	{	
		this.name = name;
		this.course = course;
		this.gender = gender;
		this.nationality = nationality;	
	}

	@Override
	public String toFlatFileString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void fromFlatFileString(String s) {
		// TODO Auto-generated method stub
		
	}
}
