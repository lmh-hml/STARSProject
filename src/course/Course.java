package course;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Course {
	
	private  String courseIndex;
    private String coursecode;
    private String courseGroup;
    private String courseName;
    
    private String AU;
    private String lec;
    private String lab;
    private String tut;
    private String AvailableSlot;
   
    private static final String courseFile = "src\\studentCourse\\courses.txt";

	public Course(String index,String c,String name,String group,String au,String lec,String lab,String tut,String slot) 
	{
		this.courseIndex=index;
		this.coursecode=c;
		this.courseName=name;
		this.courseGroup=group;
		this.AU=au;
		this.lec=lec;
		this.lab=lab;
		this.tut=tut;
		this.AvailableSlot=slot;
		
	}
	public String getindex() {return courseIndex;}
	public String getcoursecode() {return coursecode;}
	public String getcourseName() {return courseName;}
	public String getcourseGroup() {return courseGroup;}
	public String getAU() {return AU;}
	public String getlec() {return lec;}
	public String getlab() {return lab;}
	public String gettut() {return tut;}
	public String getslot() {return AvailableSlot;}
	public String setslot(String slot) {
		
		AvailableSlot=slot;
		return AvailableSlot;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Course) {
			Course p = (Course)o;
			return (getindex().equals(p.getindex()));
		}
		return false;
	}
	
	
	

}
