package stars;

import java.io.IOException;

/**
 * Database class that implements FlatFileDatabase that handles stores and handles Student_details objects.
 * This class can be instantiated with a file that contains Student_details data in flat file format,
 * and is able to write its contents back to a file in the same format.
 * Students in this database are indexed by their matriculation number.  
 * @author Lai Ming Hui
 * @since 11/11/2020
 * @version 1.0.0
 */
public class StudentDatabase extends FlatFileDatabase<Student_details>{

	/**
	 * Initializes this database with the flat file specified in fileName
	 * @param fileName A file containing student details in flat file format.
	 * @throws IOException Thrown when file with fileName is cannot be found.
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
