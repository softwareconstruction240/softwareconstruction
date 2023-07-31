package EnumExamples;

public class Person2 {
    private String firstName;
    private String lastName;
    private Gender gender;

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

    public Gender getGender() {
        return gender;
    }

    // Problem solved. Now gender can only be set to Gender.Male or Gender.Female
    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
