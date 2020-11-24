
package stars;

import java.io.IOException;

import stars.FlatFileDatabase;

/**
 * Database class that implements FlatFileDatabase that handles stores and handles Index_details objects.
 * This class can be instantiated with a file that contains Index_details data in flat file format,
 * and is able to write its contents back to a file in the same format.
 * Indexes in this database are indexed by their Index code.   
 * @author Lai MingHui
 * @version 1.0.0
 * @since 10/11/2020
 */
public class IndexDatabase extends FlatFileDatabase<Index_details>{

	/**
	 * Default constructor of this class.
	 */
	public IndexDatabase() {
	}

	@Override
	public Index_details parseLine(String line) {

		Index_details index = new Index_details();
		index.fromFlatFileString(line);
		return index;
	}

}
