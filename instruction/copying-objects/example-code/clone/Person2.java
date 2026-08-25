import java.util.Date;

public class Person2 implements Cloneable {

    private String firstName;
    private String lastName;
    private Date birthdate;

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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public Person2 clone() {
        try {
            Person2 clone = (Person2) super.clone();

            // Clone / copy the birthdate. Not safe to share because it's mutable.
            Date clonedBirthdate = (Date) getBirthdate().clone();
            clone.setBirthdate(clonedBirthdate);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
