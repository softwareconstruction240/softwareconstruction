package EnumExamples;

public class Person {
    private String firstName;
    private String lastName;
    private String gender;

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

    public String getGender() {
        return gender;
    }

    // Problem: Gender can be set to any String, even if we only consider m and f to be valid
    public void setGender(String gender) {
        this.gender = gender;
    }
}
