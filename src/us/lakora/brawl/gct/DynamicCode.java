package us.lakora.brawl.gct;

public abstract class DynamicCode extends Code {
	
	public abstract Line[] getLineArray();
	
	public String lineArrayToString() {
		StringBuilder sb = new StringBuilder();
		for (Line l : getLineArray()) {
			sb.append(l.toString() + "\n");
		}
		return sb.toString();
	}
	
	/**
	 * The description used for the code in a text file.
	 * @return
	 */
	public abstract String description();

}
