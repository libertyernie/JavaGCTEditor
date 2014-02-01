package us.lakora.brawl.gct;

public abstract class DynamicCode extends Code {
	
	public abstract Line[] getLineArray();
	
	/**
	 * The description used for the code in a text file.
	 * @return
	 */
	public abstract String description();

}
