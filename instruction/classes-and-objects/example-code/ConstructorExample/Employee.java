package ConstructorExamples;

import java.util.Date;

public class Employee extends Person {

    private Date hireDate;

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
}
