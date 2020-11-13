package stars;

import java.util.ArrayList;
import java.util.Arrays;

public class Index {
	private String name;
	private int vacancy;
	private ArrayList<String> studentsRegistered;
	
	public Index(String name,int vacancy) {
		this.name=name;
		this.vacancy=vacancy;
		this.studentsRegistered= new ArrayList<String>();
	}
	
	public void PrintStudentList() {
		for (int i=0;i<studentsRegistered.size();i++) {
			System.out.println(studentsRegistered.get(i));
		}
	}

	
	
	public Boolean CheckVacancy() {
		if (Vacancy>0) {
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public void SetIndex(String newindex) {
		this.name = newindex;
	}

	
	
}