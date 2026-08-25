package javaclasses;

public class Student extends Person {

    private YearinSchool year;
    private float gpa;


    public Student(String name, int age, YearinSchool year, float gpa) {
        super(name, age);

        setYear(year);
        setGpa(4.0f);
    }

    public YearinSchool getYear() {
        return year;
    }

    public void setYear(YearinSchool year) {
        this.year = year;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return String.format("%s, year: %s, gpa; %f", super.toString(), year, gpa);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        Student s = (Student)o;

        return (year.equals(s.year) && gpa == s.gpa);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        return (hash ^ year.hashCode() * (int)gpa);
    }

    @Override
    protected int agePriority() {
        return getAge() / 10;
    }

    @Override
    protected int categoryPriority() {
        return 5;
    }
}
