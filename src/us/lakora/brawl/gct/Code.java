package us.lakora.brawl.gct;

public abstract class Code {
	
	public String toString() {
		String s = getClass().getName();
		s = s.substring(s.lastIndexOf('.')+1);
		return s;
	}

}
