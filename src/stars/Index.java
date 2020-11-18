package stars;

import java.util.ArrayList;

public class index {
	private String index;
	private String Name;
	private int Vacancy;

	private ArrayList<String> StudentsRegistered;
	
	public index(String index,String name) {
		this.index=index;		
		this.Name=name;
	}
	public String getName() {return Name;}
	
	public void  PrintStudentList() {
		String[] n = Name.split(" ");
        for (String a : n) 
            System.out.println(a);
		
	}
	
	public Boolean CheckVacancy() {
		if (Vacancy>0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void SetIndex(String newindex) {
		Name = newindex;
	}
	
	public String GetIndex() {
		return index;
	}
	
	public String addName(String name) {
		Name=name;
		return Name;}

}
