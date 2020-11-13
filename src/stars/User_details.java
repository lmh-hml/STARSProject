package stars;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class User_details implements FlatFileObject 
{
	
	//private final static List<String> fields = Arrays.asList( "username", "password", "email", "accountType", "id" );
	
	private String username = "nil";
	private String password = "nil";
	private String email = "nil";
	private String accountType= "nil";
	private String id = "nil";
	
	public User_details() 
	{
	}

	//Override of FlatFileObjects
	public String toFlatFileString() {
		return FlatFileObject.buildFlatFileString(username, password, email, accountType, id) ;
	}
	@Override
	public void fromFlatFileString(String s) {
		ArrayList<String> array = new ArrayList<String>( Arrays.asList(s.split("\\|")) );
		this.username = array.get(0);
		this.password = array.get(1);
		this.email = array.get(2);
		this.accountType = array.get(3);
		this.id = array.get(4);
	}

	public static List<String> getFields()
	{
		ArrayList<String> list = new ArrayList<String>();
		for(Field f : User_details.class.getDeclaredFields())
		{
			list.add(f.getName());
		}
		return list;
	}
	
	
	//GETTERS AND SETTERS
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}





}
