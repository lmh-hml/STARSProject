<<<<<<< HEAD:src/course/IndexDatabase.java
package course;
=======
package stars;
>>>>>>> 11.18.Admin:src/stars/IndexDatabase.java

import stars.FlatFileDatabase;

public class IndexDatabase extends FlatFileDatabase<Index_details>{

	public IndexDatabase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Index_details parseLine(String line) {

		Index_details index = new Index_details();
		index.fromFlatFileString(line);
		return index;
	}

}
