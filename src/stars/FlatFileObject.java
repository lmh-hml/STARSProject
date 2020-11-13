package stars;
import java.util.List;
import java.util.Objects;

public interface FlatFileObject {
	
	public final static String delimiter = "|";
	
	public abstract String toFlatFileString();
	public abstract void fromFlatFileString(String s);
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
