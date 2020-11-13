package stars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentDatabase extends FlatFileDatabase<String, Student_details>{

	public StudentDatabase(String fileName) throws IOException {
		openFile(fileName);
	}

	public StudentDatabase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(Student_details obj) {
		// TODO Auto-generated method stub
		this.hashmap.put(obj.getName(), obj);
	}

	@Override
	public Student_details parseLine(String line) {
		// TODO Auto-generated method stub
		Student_details student = new Student_details();
		student.fromFlatFileString(line);
		this.add(student);
		return student;
	}

	public static void main(String args[])
	{
		try {
			StudentDatabase students = new StudentDatabase();
			
			String[] names = { "Melvin", "Thrish", "John", "Tim", "Jim", "Nekosamachan", "Jimmy"};
	
			
			for (String n : names)
			{
				Student_details student  = new Student_details();
				student.setName(n);
				student.setAU("0");
				student.setGender("M/F");
				student.setMatric_num(String.valueOf(n.length() * 123456 ) );
				student.setNationality("Signaporean");
				student.setCourseRegistered( Arrays.asList(names));
				student.setId(n+ String.valueOf(n.length()) );

				students.add(student);
				

				System.out.println(students.get(n).toFlatFileString());
			}
			
			students.writeFile("D:/Eclipse/STARS/src/stars/Users2.txt");

			StudentDatabase student2 = new StudentDatabase("D:/Eclipse/STARS/src/stars/Users2.txt");
			for( Student_details s : student2.getContents())
			{
				System.out.println(s.toFlatFileString());
			}			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
