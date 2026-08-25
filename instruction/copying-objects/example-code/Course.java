package school;

public class Course {
	
	private String name;

	public Course(String name) {
		setName(name);
	}
	
	public Course(Course other) {
		setName(other.name);
	}

	// This is needed to change "clone" from protected to public

	@Override
	protected Course clone() {
		return new Course(this);
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
		
		Course other = (Course)o;
		
		return name.equals(other.name);
	}

}
