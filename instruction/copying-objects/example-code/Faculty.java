package school;

import java.util.*;

public class Faculty extends Person {

	private ArrayList<Course> courses;
	private ArrayList<Student> students;
	
	public Faculty(int id, String name) {
		super(id, name);
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
	}

	public Faculty(Faculty other) {
		super(other);

		courses = new ArrayList<Course>();
		for (Course c : other.courses) {
			courses.add(new Course(c));
		}

		students = new ArrayList<Student>();
		for (Student s : other.students) {
			students.add(new Student(s));
		}
	}

	public Faculty clone() {
		return new Faculty(this);
	}

	// Instructor methods
	
	public void addCourse(Course c) {
		courses.add(c);
	}

	public void removeCourse(Course c) {
		courses.remove(c);
	}

	public Course[] getCourses() {
		return courses.toArray(new Course[courses.size()]);
	}
	
	// Advisor methods

	public void addStudent(Student s) {
		students.add(s);
	}

	public void removeStudent(Student s) {
		students.remove(s);
	}

	public Student[] getStudents() {
		return students.toArray(new Student[students.size()]);
	}
	
}
