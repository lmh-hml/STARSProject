
package stars;
import java.util.*;
public class StudentProgram extends coursemanager{
	private Student_details currentUser;

	
	public StudentProgram(Student currentUser) {
		this.currentUser=currentUser;
	}
	
	public void Add(String course) {
		currentUser.getCourseRegistered().add(course);

		//vacancy of course --
		currentUser.setAU(currentUser.getAU()+coursemanager.getAU(course))

	}
	
	public void Drop(String course) {
		currentUser.getCourseRegistered().remove(course);

		//vacancy of course --
		//student AU -


	}
	
	public void CheckAndPrintRegistered() {
		for (int i=0;i<currentUser.getCourseRegistered().length;i++) {
			//use course id to print course info
			continue;
		}
	}
	
	public void CheckVacancies(int index) {
		// use course index to print vacancy
		continue;
	}
	
	public void changeCourseIndex(int oldindex, int newindex) {
		currentUser.getCourseRegistered().remove(oldindex);

		//vacancy of course ++
		//remove student name from course
		currentUser.getCourseRegistered().add(newindex);
		//vacancy of course --
		System.out.println("Index Number ",oldindex," has been changed to ",newindex);
	}
	
	public void SwapIndex(int userindex, Student_details newstudent, int stuindex) {
		currentUser.getCourseRegistered().remove(userindex);
		currentUser.getCourseRegistered().add(stuindex);
		//swap names of student from both courses
		newstudent.getCourseRegistered().remove(stuindex);
		newstudent.getCourseRegistered().add(userindex);
		System.out.println(currentUser.getmatric_num(),"-Index Number ",userindex," swapped with ", newstudent.getmatric_num(),"-Index Number ",stuindex);;
	}
}
		currentUser.getCourseRegistered().add(newindex);
		continue;
	}
	
	public void SwapIndex(int index, Student student) {
		continue;
	}
}
