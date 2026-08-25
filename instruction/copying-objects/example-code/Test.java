package school;

public class Test {

	public static void main(String[] args) throws CloneNotSupportedException {
		
		Course physics900 = new Course("Physics 900");
		Course biology350 = new Course("Biology 350");
		
		Student bob = new Student(1, "Bob", YearInSchool.SENIOR);
		Student kathy = new Student(2, "Kathy", YearInSchool.SENIOR);
		
		Faculty prof = new Faculty(3, "Einstein");
		prof.addCourse(physics900);
		prof.addCourse(biology350);
		prof.addStudent(bob);
		prof.addStudent(kathy);
		
		Faculty profCopy = new Faculty(prof);
		
		prof.removeCourse(physics900);
		prof.removeStudent(kathy);

		Faculty profCopy2 = prof.clone();

		prof.removeCourse(biology350);
		prof.removeStudent(bob);

		System.out.println(prof.getCourses().length);
		System.out.println(prof.getStudents().length);
		System.out.println(profCopy.getCourses().length);
		System.out.println(profCopy.getStudents().length);
		System.out.println(profCopy2.getCourses().length);
		System.out.println(profCopy2.getStudents().length);
		
	}

}
