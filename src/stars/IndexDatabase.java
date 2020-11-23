
package stars;

import java.io.IOException;

import stars.FlatFileDatabase;

public class IndexDatabase extends FlatFileDatabase<Index_details>{

	public IndexDatabase() {
	}

	@Override
	public Index_details parseLine(String line) {

		Index_details index = new Index_details();
		index.fromFlatFileString(line);
		return index;
	}

}
