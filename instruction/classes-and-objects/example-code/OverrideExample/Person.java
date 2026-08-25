package OverrideExample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {

    private String firstName;
    private String lastName;
    private Date birthDate;

    public Person(String firstName, String lastName) {
        this(firstName, lastName, null);
    }

    public Person(String firstName, String lastName, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/YYYY");
        String formattedBirthDate = dateFormat.format(birthDate);

        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + formattedBirthDate +
                '}';
    }
}
