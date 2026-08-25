import java.util.ArrayList;
import java.util.List;

public class Team implements Cloneable {

    private String name;
    private List<Person> members;

    public static void main(String [] args) throws CloneNotSupportedException {
        Person p = new Person();
        p.setFirstName("Original First Name");
        p.setLastName("Original Last Name");

        Team t = new Team("Original Team", List.of(p));
        System.out.println("Original Team:\n\t" + t);

        Team t2 = t.clone();
        t2.setName("Cloned Team");
        List<Person> cloneMembers = t2.getMembers();
        cloneMembers.get(0).setFirstName("Changed First Name");
        cloneMembers.get(0).setLastName("Changed Last Name");

        System.out.println("Original Team after making and changing clone:\n\t" + t);
        System.out.println("Cloned Team:\n\t" + t2);
    }

    public Team(String name, List<Person> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void addMember(Person person) {
        members.add(person);
    }

    public void removeMember(Person person) {
        members.remove(person);
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", members=" + members +
                '}';
    }

    @Override
    public Team clone() {
        try {
            Team clone = (Team) super.clone();

            // Comment these lines out to see what happens with a shallow copy that contains a mutable instance variable
            List<Person> cloneMembers = new ArrayList<>();
            for(Person person : members) {
                Person personClone = person.clone();
                cloneMembers.add(personClone);
            }

            clone.members = cloneMembers;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
