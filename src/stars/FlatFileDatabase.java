package stars;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * A class that reads, stores and write data from from to a flat file.
 * This class only stores and performs operations with objects that implements 
 * the FlatFileObject Interface.
 * Users can initialize a database with a flat file, retrieve and modify its contents, and
 * write the modified state back to a flat file.
 * 
 * Since different data types have different ways of being read, written and indexed, each of those types
 * should have their own database class that extends this class and overrides its methods to the 
 * specification of those types.
 * @author Lai Ming Hui
 *
 * @param <K> The type of key used to retrieve items from this database.
 * @param <T> A class that implements FlatFileObject
 */
public abstract class  FlatFileDatabase <K, T extends FlatFileObject>{
	
	protected HashMap<K,T> hashmap = new HashMap<K,T>();
	protected List<String> flatFileFormat;

	
	/**
	 * Adds an object of type T into the database.
	 * @param obj The object to be added into database
	 */
	public abstract void add( T obj);
	
	/**
	 * Removes an object indexed by the key from the database
	 * @param key Key of object to be removed.
	 */
	public void remove(K key) 
	{
		hashmap.remove(key);
	};
	
	/**
	 * Retrieves the object indexed by key.
	 * @param key Key of object to be retrieved.
	 * @return The object indexed by the key, or null if it is not in the database.
	 */
	public T get(K key) 
	{
		return hashmap.get(key);
	};
	
	/**
	 * Returns the a Collection view of values stored in this database.
	 * Changes to this collection view will be reflected in the database.
	 * @return The collection view of the values in this database.
	 */
	public Collection<T> getContents()
	{
		return hashmap.values();
	}

	/**
	 * Returns a list of strings, each string being a flat file representation
	 * of a value stored in the database.
	 * @return A list of string representations of values stored in the database.
	 */
	public List<String> printContents()
	{
		List<String> s = new ArrayList<String>();
		for( FlatFileObject obj : this.getContents() )
		{
			s.add(obj.toFlatFileString());
		}
		return s;
	}
	
	
	
	/**
	 * Opens a file that contains data in flat file format, converts the text data into 
	 * actual objects of this database's target type and stores them into this databse.
	 * The file should be a text file containing data in flat file format
	 * @param fileName
	 * @throws IOException
	 */
	public void openFile(String fileName) throws IOException
	{
		Path p = Paths.get(fileName);
		Files.lines(p).forEach( line -> {
			
			T obj = parseLine(line);
			add(obj);
		});	
	}
	
	/**
	 * Parses a flat file string and initializes a flat file object with the data.
	 * @param line A flat file string
	 * @return The initialized object.
	 */
	public abstract T parseLine(String line);
	
	
	/**
	 * Write the contents of the database to the specified file in flat file format.
	 * @param fileName The file to be written to
	 * @throws IOException 
	 */
	public  void writeFile(String fileName) throws IOException
	{
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		for( FlatFileObject item : hashmap.values() )
			{
				String s;
				s = item.toFlatFileString();
				out.println(s);
			}
		
		out.close();
	}


}
