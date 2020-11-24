package stars;

import java.io.IOException;

import stars.FlatFileDatabase;
import stars.FlatFileObject;

/**
 * A class that reads from and writes to a file containing course information in flat file format and stores them in memory 
 * for access. 
 * @author Lai Ming Hui
 * @since 11/11/2020
 * @version 1.0.0
 *
 */
public class CourseDatabase extends FlatFileDatabase<Course>{

	/**Default constructor of this class**/
	public CourseDatabase() {
	}

	@Override
	public Course parseLine(String line) {
		Course course = new Course();
		course.fromFlatFileString(line);
		return course;
	}

}
