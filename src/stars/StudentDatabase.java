package stars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentDatabase extends FlatFileDatabase<Student_details>{

	/**
	 * Initializes this database with the flat file specified in fileName
	 * @param fileName A file containing student details in flat file format.
	 * @throws IOException
	 */
	public StudentDatabase(String fileName) throws IOException {
		openFile(fileName);
	}

	/**
	 * Default contructor of this object.
	 */
	public StudentDatabase() {
	}


	@Override
	public Student_details parseLine(String line) {
		// TODO Auto-generated method stub
		Student_details student = new Student_details();
		student.fromFlatFileString(line);
		return student;
	}
	
	
}
