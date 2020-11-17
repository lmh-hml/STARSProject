package course2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class courseapp implements coursemanager {

	final String courseFile = "src\\studentCourse\\courses.txt";
	final String indexFile = "src\\studentCourse\\namelist";
	private  String courseIndex;
    private String coursecode;
    private String courseGroup;
    private String courseName;
    private String AU;
    private String lec;
    private String lab;
    private String tut;
    private String AvailableSlot;
    private String Name;
    
	public ArrayList<Course> printindex() throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		ArrayList index = new ArrayList();
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			index.add(c.getindex());
			System.out.println(c.getindex());

			}
		return index;
	
		
		}		
		
	public ArrayList<Course> printcourse() throws IOException {
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
			Course c=(Course)al.get(i);

			System.out.print(c.getindex()+" ");
			
			System.out.print(c.getcoursecode()+" ");
			System.out.print(c.getcourseName()+" ");
			System.out.print(c.getcourseGroup()+" ");
			System.out.print(c.getAU()+" ");
			System.out.print(c.getlec()+" ");
			System.out.print(c.getlab()+" ");
			System.out.print(c.gettut()+"\n");
			
		}
		return al;

            	
}
	public ArrayList<Course> addcourse(String index,String c,String name,String group,String au,String lec,String lab,String tut,String slot)throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		Course info = new Course(index, c,name,group,au,lec,lab,tut,slot);
		al.add(info);
		coursemanager.saveCourse(courseFile, al);
		return al;
		
	}
	public ArrayList<Course> removecourse(String index)throws IOException{
		
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
				al.remove(i);
			}
		}
		
		coursemanager.saveCourse(courseFile, al);
		return al;
		
	}
	public String getau(String index) throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
			AU= c.getAU();
		
			System.out.println(c.getAU());
			}
		
		}		
		return AU;
		
	}
	public String getlec(String index) throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
			lec= c.getlec();
			System.out.println(c.getlec());
			}
		
		}		
		return lec;
		
	}
	public String getlab(String index) throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
			lab= c.getlab();
			System.out.println(c.getlab());
			}
		
		}		
		return lab;
		
	}
	public String gettut(String index) throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
			tut= c.getAU();
			System.out.println(c.gettut());
			}
		
		}		
		return tut;
		
	}
	public String getslot(String index) throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
			AvailableSlot= c.getslot();
			System.out.println(c.getslot());
			}
		
		}		
		return AvailableSlot;
		
	}
	public String setslot(String index,String num_slot) throws IOException{
		ArrayList al = coursemanager.readCourse(courseFile);
		//int in=Integer.parseInt(index);
		for (int i = 0 ; i < al.size() ; i++) {
		
			Course c=(Course)al.get(i);
			if(c.getindex().matches(index)) {
				//AvailableSlot=c.getslot();
				//AvailableSlot=num_slot;
				AvailableSlot=c.setslot(num_slot);
				
				//this.slot=Integer.parseInt(slot);
			
			System.out.println(AvailableSlot);
			}
		
		}	
		coursemanager.saveCourse(courseFile, al);
		return AvailableSlot;
	
		
	}
	public void printNamebyCourse(String index) throws IOException {
		ArrayList al =coursemanager.readindex(indexFile);
		for (int i = 0 ; i < al.size() ; i++) {
			index name=(index)al.get(i);
			if(name.GetIndex().matches(index)) {
				name.PrintStudentList();
				
				}
		}		
		
	}
	public String getName(String index) throws IOException{
		ArrayList al =coursemanager.readindex(indexFile);
		for (int i = 0 ; i < al.size() ; i++) {
			index name=(index)al.get(i);
			if(name.GetIndex().matches(index)) {
				Name=name.getName();
				System.out.println(Name);
			}
		}
		
		return Name;
		
	}
	public ArrayList<index>addName(String index,String name)  throws IOException{
		ArrayList al =coursemanager.readindex(indexFile);
		
		
		String nName = null;
		for (int i = 0 ; i < al.size() ; i++) {
			index n=(index)al.get(i);
			if(n.GetIndex().matches(index)) {
				Name=n.getName();
				Name=String.join(" ", Name,name);
				nName=n.addName(Name);
				}
			
		}
				
		coursemanager.writeindex(indexFile, al);
		return al;
		
	}
	public ArrayList<index>removeName(String index,String name)  throws IOException{
		
		ArrayList al =coursemanager.readindex(indexFile);
		String nName = null;
		
		ArrayList<String> ns = null;
		for (int i = 0 ; i < al.size() ; i++) {
			index n=(index)al.get(i);
			if(n.GetIndex().matches(index)) {
				Name=n.getName();
				ns = new ArrayList<String>(Arrays.asList(Name.split(" ")));
				for (int z=0;z<=ns.size();z++) {
					if (ns.contains(name));{
						ns.remove(name);}
				}
				Name=String.join(" ", ns);
				nName=n.addName(Name);
				}
				

				}
	
		
		//System.out.println(ns);
		//System.out.println(nName);	
		coursemanager.writeindex(indexFile, al);
		return al;
		
		
	}
}	  