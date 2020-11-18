package stars;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import stars.FlatFileDatabase;
import stars.FlatFileObject;

public class CourseManager{

	public static final String SEPARATOR = "|";

	final String courseFile = "src\\studentCourse\\courses.txt";
	final String indexFile = "src\\studentCourse\\namelist";
	
	private CourseDatabase courses = new CourseDatabase();
	private IndexDatabase  indexes = new IndexDatabase();
	public CourseManager() {
		
		try {
			courses.openFile(courseFile);
			indexes.openFile(indexFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveCourse(String filename)
	{
		try {
			courses.writeFile(courseFile);
			indexes.writeFile(indexFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void addIndex(Index_details index, String courseCode)
	{
		this.indexes.add(index);
		this.courses.get(courseCode).addIndexName(index.getIndexCode());
	}
	
	public void removeIndex( String indexCode)
	{
		Index_details index = this.indexes.get(indexCode);
		Course c = this.courses.get(index.getCourseCode());
		if(c != null)
		{
			c.removeIndexname(indexCode);
		}
		indexes.remove(indexCode);	

	}
	public Index_details getIndex(String indexCode)
	{
		return this.indexes.get(indexCode);
	}	
	public Collection<Index_details> getAllIndexes() 
	{
		return this.indexes.getContents();
	}
	public boolean checkIndexExists(String indexCode)
	{
		return this.indexes.getKeys().contains(indexCode);
	}
//	
//			
//		public ArrayList<Course> printcourse() throws IOException {
//			ArrayList al = readCourse(courseFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//				Course c=(Course)al.get(i);
//	
//				System.out.print(c.getindex()+" ");
//				
//				System.out.print(c.getcoursecode()+" ");
//				System.out.print(c.getcourseName()+" ");
//				System.out.print(c.getcourseGroup()+" ");
//				System.out.print(c.getAU()+" ");
//				System.out.print(c.getlec()+" ");
//				System.out.print(c.getlab()+" ");
//				System.out.print(c.gettut()+"\n");
//				
//			}
//			return al;
//	
//	            	
//	}
		public void addCourse(Course course)
		{
			this.courses.add(course);
		}
		public void removeCourse(String courseCode){
			this.courses.remove(courseCode);
		}
		public Collection<Course> getAllCourses()
		{
			return this.courses.getContents();
		}
		public Course getCourse(String courseCode)
		{
			return this.courses.get(courseCode);
		}
		
//		public String getau(String index) throws IOException{
//			ArrayList al = readCourse(courseFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//			
//				Course c=(Course)al.get(i);
//				if(c.getindex().matches(index)) {
//				AU= c.getAU();
//			
//				System.out.println(c.getAU());
//				}
//			
//			}		
//			return AU;
//			
//		}
//		public String getlec(String index) throws IOException{
//			ArrayList al = readCourse(courseFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//			
//				Course c=(Course)al.get(i);
//				if(c.getindex().matches(index)) {
//				lec= c.getlec();
//				System.out.println(c.getlec());
//				}
//			
//			}		
//			return lec;
//			
//		}
//		public String getlab(String index) throws IOException{
//			ArrayList al = readCourse(courseFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//			
//				Course c=(Course)al.get(i);
//				if(c.getindex().matches(index)) {
//				lab= c.getlab();
//				System.out.println(c.getlab());
//				}
//			
//			}		
//			return lab;
//			
//		}
//		public String gettut(String index) throws IOException{
//			ArrayList al = readCourse(courseFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//			
//				Course c=(Course)al.get(i);
//				if(c.getindex().matches(index)) {
//				tut= c.getAU();
//				System.out.println(c.gettut());
//				}
//			
//			}		
//			return tut;
//			
//		}
//		public String getslot(String index) throws IOException{
//			ArrayList al = readCourse(courseFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//			
//				Course c=(Course)al.get(i);
//				if(c.getindex().matches(index)) {
//				AvailableSlot= c.getslot();
//				System.out.println(c.getslot());
//				}
//			
//			}		
//			return AvailableSlot;
//			
//		}
//		public String setslot(String index,String num_slot) throws IOException{
//			ArrayList al = readCourse(courseFile);
//			//int in=Integer.parseInt(index);
//			for (int i = 0 ; i < al.size() ; i++) {
//			
//				Course c=(Course)al.get(i);
//				if(c.getindex().matches(index)) {
//					//AvailableSlot=c.getslot();
//					//AvailableSlot=num_slot;
//					AvailableSlot=c.setslot(num_slot);
//					
//					//this.slot=Integer.parseInt(slot);
//				
//				System.out.println(AvailableSlot);
//				}
//			
//			}	
//			saveCourse(courseFile, al);
//			return AvailableSlot;
//		
//			
//		}
//		public void printNamebyCourse(String index) throws IOException {
//			ArrayList al =readindex(indexFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//				index name=(index)al.get(i);
//				if(name.GetIndex().matches(index)) {
//					name.PrintStudentList();
//					
//					}
//			}		
//			
//		}
//		public String getName(String index) throws IOException{
//			ArrayList al =readindex(indexFile);
//			for (int i = 0 ; i < al.size() ; i++) {
//				index name=(index)al.get(i);
//				if(name.GetIndex().matches(index)) {
//					Name=name.getName();
//					System.out.println(Name);
//				}
//			}
//			
//			return Name;
//			
//		}
//		public ArrayList<index>addName(String index,String name)  throws IOException{
//			ArrayList al =readindex(indexFile);
//			
//			
//			String nName = null;
//			for (int i = 0 ; i < al.size() ; i++) {
//				index n=(index)al.get(i);
//				if(n.GetIndex().matches(index)) {
//					Name=n.getName();
//					Name=String.join(" ", Name,name);
//					nName=n.addName(Name);
//					}
//				
//			}
//					
//			writeindex(indexFile, al);
//			return al;
//			
//		}
//		public ArrayList<index>removeName(String index,String name)  throws IOException{
//			
//			ArrayList al =this.readindex(indexFile);
//			String nName = null;
//			
//			ArrayList<String> ns = null;
//			for (int i = 0 ; i < al.size() ; i++) {
//				index n=(index)al.get(i);
//				if(n.GetIndex().matches(index)) {
//					Name=n.getName();
//					ns = new ArrayList<String>(Arrays.asList(Name.split(" ")));
//					for (int z=0;z<=ns.size();z++) {
//						if (ns.contains(name));{
//							ns.remove(name);}
//					}
//					Name=String.join(" ", ns);
//					nName=n.addName(Name);
//					}
//					
//	
//					}
//			//System.out.println(ns);
//			//System.out.println(nName);	
//			writeindex(indexFile, al);
//			return al;
//		}
//		
//	
//		public static List read(String fileName) throws IOException {
//					List data = new ArrayList() ;
//				    Scanner scanner = new Scanner(new FileInputStream(fileName));
//				    try {
//				      while (scanner.hasNextLine()){
//				        data.add(scanner.nextLine());
//				      }
//				    }
//				    finally{
//				      scanner.close();
//				    }
//				    return data;
//				  }
//		public static void write(String fileName, List data) throws IOException  
//			{
//				    PrintWriter out = new PrintWriter(new FileWriter(fileName));
//	
//				    try {
//						for (int i =0; i < data.size() ; i++) {
//				      		out.println((String)data.get(i));
//						}
//				    }
//				    finally {
//				      out.close();
//				    }
//				  }
//			
//		public static ArrayList readCourse(String filename) throws IOException
//			{
//				ArrayList stringArray = (ArrayList)read(filename);
//				ArrayList alr = new ArrayList() ;
//				for (int i = 0 ; i < stringArray.size() ; i++) {
//					String st = (String)stringArray.get(i);
//					// get individual 'fields' of the string separated by SEPARATOR
//					StringTokenizer courseStar = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","
//	
//					String courseId = courseStar.nextToken().trim();
//					String coursecode = courseStar.nextToken().trim();
//					String coursename = courseStar.nextToken().trim();
//					String coursegroup = courseStar.nextToken().trim();
//					String Au = courseStar.nextToken().trim();
//					String  lec= courseStar.nextToken().trim();
//					String lab = courseStar.nextToken().trim();
//					String tut = courseStar.nextToken().trim();
//					String slot =courseStar.nextToken().trim(); 
//					// create Professor object from file data
//					Course info = new Course(courseId, coursecode,coursename,coursegroup,Au,lec,lab,tut,slot);
//					// add to Professors list
//					alr.add(info) ;
//				}
//				return alr;
//			}
//	
//	
//		    
//		    
//		public static ArrayList readindex(String filename) throws IOException
//			{
//				ArrayList stringArray = (ArrayList)read(filename);
//				ArrayList alr = new ArrayList() ;
//				for (int i = 0 ; i < stringArray.size() ; i++) {
//					String st = (String)stringArray.get(i);
//					// get individual 'fields' of the string separated by SEPARATOR
//					StringTokenizer courseStar = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","
//	
//					String courseId = courseStar.nextToken().trim();
//					String name= courseStar.nextToken().trim();
//	
//					index info = new index(courseId, name);
//					
//					alr.add(info) ;
//				}
//				return alr;
//			}
//			
//			
//		public static void writeindex(String filename,List al) throws IOException{
//				List alw = new ArrayList() ;
//				for (int i = 0 ; i < al.size() ; i++) {
//					index name=(index)al.get(i);
//					StringBuilder st =  new StringBuilder() ;
//					st.append(name.GetIndex().trim());
//					st.append(SEPARATOR);
//					st.append(name.getName().trim());
//					alw.add(st.toString()) ;
//					
//				}
//				
//				write(filename,alw);
//				
//			}
//	
}	  