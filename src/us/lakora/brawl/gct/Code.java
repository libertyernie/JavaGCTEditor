package us.lakora.brawl.gct;

public abstract class Code {
	
	public String toString() {
		String s = getClass().getName();
		s = s.substring(s.lastIndexOf('.')+1);
		return s;
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
