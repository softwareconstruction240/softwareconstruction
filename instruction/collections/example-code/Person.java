public class Person implements Cloneable {

    private String firstName;
    private String lastName;

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

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    /**
     * Overridden to make it public. Always call super.clone(). Makes a shallow copy. OK in this case because all
     * instance variables are immutable.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
