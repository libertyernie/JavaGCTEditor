package us.lakora.brawl.gct;

public abstract class Code {
	
	public Code(Iterable<? extends Line> lines) {
		for (Line l : lines) l.assign(this);
	}
	
	public abstract Line[] getLineArray();
	
	/**
	 * The description used for the code in a text file.
	 * @return
	 */
	public abstract String description();
	
	public String getComments() {
		return null;
	}

	/**
	 * Returns the code lines for a code, with a trailing newline.
	 */
	public static String codeLinesToString(Line[] lines) {
		StringBuilder sb = new StringBuilder(10*lines.length);
		for (Line l : lines) {
			sb.append("* "+l.toString()+"\n");
		}
		return sb.toString();
	}
	public static String codeLinesToString(Iterable<Line> lines) {
		StringBuilder sb = new StringBuilder();
		for (Line l : lines) {
			sb.append("* "+l.toString()+"\n");
		}
		return sb.toString();
	}

}
