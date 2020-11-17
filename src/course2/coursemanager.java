package course2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public interface coursemanager {
	public static final String SEPARATOR = "|";
	public static List read(String fileName) throws IOException {
			List data = new ArrayList() ;
		    Scanner scanner = new Scanner(new FileInputStream(fileName));
		    try {
		      while (scanner.hasNextLine()){
		        data.add(scanner.nextLine());
		      }
		    }
		    finally{
		      scanner.close();
		    }
		    return data;
		  }
	public static void write(String fileName, List data) throws IOException  {
		    PrintWriter out = new PrintWriter(new FileWriter(fileName));

		    try {
				for (int i =0; i < data.size() ; i++) {
		      		out.println((String)data.get(i));
				}
		    }
		    finally {
		      out.close();
		    }
		  }
	
	public static ArrayList readCourse(String filename) throws IOException
	{
		ArrayList stringArray = (ArrayList)read(filename);
		ArrayList alr = new ArrayList() ;
		for (int i = 0 ; i < stringArray.size() ; i++) {
			String st = (String)stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer courseStar = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","

			String courseId = courseStar.nextToken().trim();
			String coursecode = courseStar.nextToken().trim();
			String coursename = courseStar.nextToken().trim();
			String coursegroup = courseStar.nextToken().trim();
			String Au = courseStar.nextToken().trim();
			String  lec= courseStar.nextToken().trim();
			String lab = courseStar.nextToken().trim();
			String tut = courseStar.nextToken().trim();
			String slot =courseStar.nextToken().trim(); 
			// create Professor object from file data
			Course info = new Course(courseId, coursecode,coursename,coursegroup,Au,lec,lab,tut,slot);
			// add to Professors list
			alr.add(info) ;
		}
		return alr;
	}

    public static void saveCourse(String filename, List al) throws IOException {
		List alw = new ArrayList() ;

        for (int i = 0 ; i < al.size() ; i++) {
				Course c = (Course)al.get(i);
				StringBuilder st =  new StringBuilder() ;
				st.append(c.getindex().trim());
				st.append(SEPARATOR);
				st.append(c.getcoursecode().trim());
				st.append(SEPARATOR);
				st.append(c.getcourseName().trim());
				st.append(SEPARATOR);
				st.append(c.getcourseGroup().trim());
				st.append(SEPARATOR);
				st.append(c.getAU().trim());
				st.append(SEPARATOR);
				st.append(c.getlec().trim());
				st.append(SEPARATOR);
				st.append(c.getlab().trim());
				st.append(SEPARATOR);
				st.append(c.gettut().trim());
				st.append(SEPARATOR);
				st.append(c.getslot().trim());
	
				alw.add(st.toString()) ;
			}
			write(filename,alw);
	}
	public static ArrayList readindex(String filename) throws IOException
	{
		ArrayList stringArray = (ArrayList)read(filename);
		ArrayList alr = new ArrayList() ;
		for (int i = 0 ; i < stringArray.size() ; i++) {
			String st = (String)stringArray.get(i);
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer courseStar = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","

			String courseId = courseStar.nextToken().trim();
			String name= courseStar.nextToken().trim();

			index info = new index(courseId, name);
			
			alr.add(info) ;
		}
		return alr;}
	public static void writeindex(String filename,List al) throws IOException{
		List alw = new ArrayList() ;
		for (int i = 0 ; i < al.size() ; i++) {
			index name=(index)al.get(i);
			StringBuilder st =  new StringBuilder() ;
			st.append(name.GetIndex().trim());
			st.append(SEPARATOR);
			st.append(name.getName().trim());
			alw.add(st.toString()) ;
			
		}
		
		write(filename,alw);
		
	}
		
	
}
