package school;

public class Student extends Person {

	private YearInSchool year;
	
	public Student(int id, String name, YearInSchool year) {
		super(id, name);
		setYear(year);
	}

	public Student(Student other) {

		super(other);

		setYear(other.year);
	}

	public Student clone() {
		return new Student(this);
	}

	public YearInSchool getYear() {
		return year;
	}

	public void setYear(YearInSchool year) {
		this.year = year;
	}

}
