package EqualityExamples;

import SimpleClassExample.Person;

public class EqualsEqualityExample {

    public static void main(String [] args) {
        Person p1 = new Person();
        p1.setFirstName("Jerod");
        p1.setLastName("Wilkerson");

        Person p2 = p1;

        if(p1.equals(p2)){
            // True or False?
        }

        p2 = new Person();
        p2.setFirstName("Jerod");
        p2.setLastName("Wilkerson");

        if(p1.equals(p2)){
            // True or False?
        }
    }
}
