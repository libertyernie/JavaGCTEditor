package us.lakora;

public class BooleanContainer {
	
	private boolean value;
	
	public BooleanContainer() {
		
	}
	
	public BooleanContainer(boolean b) {
		value = b;
	}
	
	public boolean get() {
		return value;
	}
	
	public void set(boolean b) {
		value =  b;
	}
	
	public boolean equals(Object o) {
		BooleanContainer b = (BooleanContainer)o;
		return b.value == value;
	}

	public String toString() {
		return Boolean.toString(value);
	}
}
