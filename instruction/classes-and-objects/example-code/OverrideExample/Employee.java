package OverrideExample;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Employee extends Person {

    private Date hireDate;

    public static void main(String [] args) {
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(1968, Month.JULY.ordinal(), 8);

        Employee emp = new Employee("Jerod", "Wilkerson", birthDate.getTime(), new Date());
        System.out.println(emp);
    }

    public Employee(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public Employee(String firstName, String lastName, Date birthDate) {
        this(firstName, lastName, birthDate, null);
    }

    public Employee(String firstName, String lastName, Date birthDate, Date hireDate) {
        // What happens if you comment out the super call? Why?
        // What happens if you comment out the this calls in the other constructors?
        super(firstName, lastName, birthDate);
        this.hireDate = hireDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        String personString = super.toString();

        personString = personString.replace("Person", "Employee");
        personString = personString + "\b";

        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/YYYY");
        String formattedHireDate = dateFormat.format(hireDate);

        return personString +
                ", hireDate=" + formattedHireDate +
                '}';
    }
}
