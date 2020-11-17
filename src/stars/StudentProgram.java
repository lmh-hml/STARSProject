package stars;
import java.util.*;

import course2.CourseManager;
public class StudentProgram{
	private Student_details currentUser;
	private CourseManager courseApp;
	private StarsDatabase starsDatabase;
	/**StudentProgram has attributes of Student_details, CourseManager and StarsDatabase*/
	public StudentProgram(Student_details currentUser, CourseManager courseApp, StarsDatabase starsDatabase) {
		this.currentUser=currentUser;
		this.courseApp=courseApp;
		this.starsDatabase=starsDatabase;
	}
	
	//Method to register a course for the student
	public void Add(String course) {
		//if student name already in course
		//print{} else if
		if (Integer.parseInt(currentUser.getAU())+Integer.parseInt(courseApp.getau(course))>20) {
			System.out.println("Amount of AU exceeds maximum AU");
		}
		// else if course does not exist{print}
		else {
			currentUser.setAU(String.valueOf(Integer.parseInt(currentUser.getAU())+Integer.parseInt(courseApp.getau(course))));
			int vacancy = Integer.parseInt(courseApp.getslot(course));
			if (vacancy==0) {
				currentUser.getCourseWaitlist().add(course); //not using setcoursewaitlist
				//add student to waitlist in course
				System.out.println("Course added to waitlist");
			}
			else {
				courseApp.addName(course,currentUser.getName());
				courseApp.setslot(course, String.valueOf(Integer.parseInt(courseApp.getslot(course))-1));
			}
			starsDatabase.writeDatabaseFiles();
		}
		

	}
	
	//Method to drop a course for the student
	public void Drop(String course) {
		currentUser.removeCourse(course);
		currentUser.setAU(String.valueOf(Integer.parseInt(currentUser.getAU())-Integer.parseInt(courseApp.getau(course))));
		courseApp.removeName(course, currentUser.getName());
		//if there is student in waitlist of course
			Student_details newstudent;
			newstudent = starsDatabase.getStudentByUsername(" ")//waitlist[0];
			//remove first out student from course waitlist
			//remove first out course from student waitlist
			courseApp.addName(course, newstudent.getName()); //add student name to course
			newstudent.addCourse(course);
			//write back to student file
		//else
			courseApp.setslot(course,String.valueOf(Integer.parseInt(courseApp.getslot(course))+1)); //vacancy of course ++
		starsDatabase.writeDatabaseFiles();


	}
	
	//Method to print all registered courses for the student
	public void CheckAndPrintRegistered() {
		for (int i=0;i<currentUser.getCourseRegistered().size();i++) {
			System.out.print(currentUser.getCourseRegistered().get(i)+" ");
			System.out.print(courseApp.getau(currentUser.getCourseRegistered().get(i))+" ");
			System.out.print(courseApp.getlab(currentUser.getCourseRegistered().get(i))+" ");
			System.out.print(courseApp.getlec(currentUser.getCourseRegistered().get(i))+" ");
			System.out.println(courseApp.gettut(currentUser.getCourseRegistered().get(i)));  //print using coursemanager for each i
			continue;
		}
	}
	
	//Method to check the vacancies of a course index
	public void CheckVacancies(String course) {
		//if course does not exist{print}
		//else
			System.out.println("Number of vacancy : "+courseApp.getslot(course));
	}
	
	//Method to change the course index for the student
	public void changeCourseIndex(String oldindex, String newindex) {
		//if student already in newindexcourse {print}
		//else if student not in oldindex {print}
		//else if any index does not exist{print}
		//else 
		this.Drop(oldindex);
		this.Add(newindex);
		System.out.println("Index Number "+oldindex+" has been changed to "+newindex);
	}
	
	//Method to swap index of the student and another student
	public void SwapIndex(String userindex, Student_details newstudent, String stuindex) {
		//check if indexes in same course
		//if student not in userindex{print}
		//else if newstudent not in stuindex {print}
		//else
		currentUser.removeCourse(userindex);
		currentUser.addCourse(stuindex);
		newstudent.removeCourse(stuindex);
		newstudent.addCourse(userindex);
		courseApp.removeName(userindex, currentUser.getName());  //swap names of students from both courses
		courseApp.addName(stuindex, currentUser.getName());
		courseApp.removeName(stuindex, newstudent.getName());
		courseApp.addName(userindex, newstudent.getName());     //swap names of student from both courses

		starsDatabase.writeDatabaseFiles();
		System.out.println(currentUser.getMatric_num()+"-Index Number "+userindex+" swapped with "+ newstudent.getMatric_num()+"-Index Number "+stuindex);
	}
	
}
