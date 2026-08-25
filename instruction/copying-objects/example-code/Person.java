package school;

public class Person {

	private int id;
	private String name;

	public Person(int id, String name) {
		setId(id);
		setName(name);
	}

	public Person(Person other) {
		setId(other.id);
		setName(other.name);
	}

	@Override
	public Person clone() {
		return new Person(this);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		
		if (o == null) {
			return false;
		}
		
		if (getClass() != o.getClass()) {
			return false;
		}
		
		Person other = (Person)o;
		
		return (id == other.id && name.equals(other.name));
	}
	
}
