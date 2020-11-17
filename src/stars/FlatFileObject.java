package stars;
import java.util.List;
import java.util.Objects;

/**
 * An interface implemented by objects that can
 * be written to and read from files in flat file format.
 * @author Lai Minghui
 * @version 1.0
 * @since 2020/11/14
 *
 */
public interface FlatFileObject {
	
	/**The delimter that separate columns in the flat file*/
	public final static String delimiter = "|";
	
	/**Returns the flat file string representation of the object.
	 * @return Returns the flat file representation of the object.
	 */
	public abstract String toFlatFileString();
	
	/**
	 * Initializes this object using a line read from a flat file.
	 * @param s A line from a flat file.
	 */
	public abstract void fromFlatFileString(String s);

	/**
	 * Gets the id of this flat file object used to index it in a flat file database
	 * @return The id of the flat file object 
	 */
	public String getDatabaseId();
	
	
	/**
	 * Combines string representations of objects passed into the method into
	 * a flat file string that can be written to a flat file.
	 * @param args A series of objects to be written into a flat file string
	 * @return A flat file string where each column is the string representation of the objects in args, in the order
	 * that they are passed in.
	 */
	public static String buildFlatFileString(Object... args)
	{
		String s = "";
		for (Object o: args)
		{
			s += o.toString() + delimiter;
		}
		return s;
	}
	
}
