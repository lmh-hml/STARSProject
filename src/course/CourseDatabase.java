package course2;

import stars.FlatFileDatabase;
import stars.FlatFileObject;

public class CourseDatabase extends FlatFileDatabase<Course>{

	public CourseDatabase() {
	}

	@Override
	public Course parseLine(String line) {
		Course course = new Course();
		course.fromFlatFileString(line);
		return course;
	}

}
