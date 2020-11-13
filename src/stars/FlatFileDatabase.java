package stars;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public abstract class  FlatFileDatabase <K, T extends FlatFileObject>{
	
	protected HashMap<K,T> hashmap = new HashMap<K,T>();
	protected List<String> flatFileFormat;

	public abstract void add( T obj);
	public void remove(K key) 
	{
		hashmap.remove(key);
	};
	public T get(K key) 
	{
		return hashmap.get(key);
	};
	public Collection<T> getContents()
	{
		return hashmap.values();
	}
	
	public void openFile(String fileName) throws IOException
	{
		Path p = Paths.get(fileName);
		Files.lines(p).forEach( line -> {
			
			T obj = parseLine(line);
			add(obj);
		});	
	}
	public abstract T parseLine(String line);
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
