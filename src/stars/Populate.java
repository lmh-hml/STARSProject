package stars;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Populate {
	
	public static HashMap<String, Student_details> readStudents(String filename)
	{
		HashMap<String, Student_details> map = new HashMap<>();
		Random random = new Random();
		
		BufferedReader bfr;
		try {
			bfr = new BufferedReader(new FileReader(filename));
			String line = "";
			int count = 0;
			while ( (line = bfr.readLine())!= null)
			{
				count++;
				if(count == 1)continue;
				String[] array = line.split("\\,");
				Student_details student = new Student_details();
				student.setName(array[0]);
				student.setGender(array[1]);
				student.setNationality(array[2]);
				student.setStudyYear(Integer.parseInt(array[3]));
				long num = (long)(Math.random() * (2000000 - 1900000 + 1) + 1900000);
				student.setMatric_num("U"+num+(char) ('A' + random.nextInt(26)));
				student.setAU(0);
				String username = student.getName().split("\\_")[0] + (long)(Math.random() * (100 - 200 + 1) + 100);
				student.setUserName(username);
				map.put(username, student);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String args[])
	{
		StarsDatabase sd = new StarsDatabase();
	

		
		
		
//		HashMap<String, Student_details> map = readStudents("Users - Students.csv");
//		for(Student_details s : map.values())
//		{
//			//System.out.println(s.toFlatFileString());
//			sd.addStudent(s);
//			User_details u = new User_details();
//			u.setUsername(s.getUserName());
//			u.setEmail(s.getUserName()+"@e.ntu.edu.sg");
//			u.setAccountType("Student");
//			u.setPassword(PasswordMaker.generatePasswordHash(s.getUserName()));
//			sd.addUser(u);
//		}
//		for(User_details u : sd.getAllUsers())
//		{
//			System.out.println(u.toFlatFileString());
//			Student_details s = sd.getStudent(u);
//			if(s!=null)System.out.println(s.toFlatFileString());
//
//		}
//		User_details admin = new User_details();
//		admin.setUsername("Admin123");
//		admin.setEmail("Admin123"+"@e.ntu.edu.sg");
//		admin.setAccountType("Admin");
//		admin.setPassword(PasswordMaker.generatePasswordHash("admin"));
//		sd.addUser(admin); 
//		try {
//			sd.saveStudents();
//			sd.saveUsers();
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		
//		
//		for(Student_details u : sd.getAllStudents())
//		{
//			System.out.println(u.toFlatFileString());
//		}
//		
//		
		
		

	}

}
