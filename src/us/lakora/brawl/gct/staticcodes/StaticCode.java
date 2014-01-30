package us.lakora.brawl.gct.staticcodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a static Brawl code - that is, one that comes in the same form every time and is not customizable.
 * An instance of this class does not correspond to a specific instance of a code in the GCT, as is the case with MultipleCode.
 * @author libertyernie
 */
public class StaticCode {
	
	private String[] code;
	private String name;
	private String comments;
	
	public StaticCode(String name, String[] code, String comments) {
		this.name = name;
		this.code = code;
		if ((comments == null) || (comments.equals("<html></html>") || comments.length() == 0)) {
			this.comments = null;
		} else {
			this.comments = comments;
		}
	}

	/**
	 * Returns an array of Strings, each representing a line of this code.
	 * This is the same array given to the constructor.
	 */
	public String[] getStringArray() {
		return code;
	}
	
	public String getComments() {
		return comments;
	}
	
	/**
	 * Returns true if and only if both StaticCode instances are of the same class.
	 */
	public boolean equals(Object o) {
		return o.getClass().equals(getClass());
	}
	
	public String toString() {
		return name;
	}
	
	public static List<StaticCode> readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		return read(br);
	}
	public static List<StaticCode> read(BufferedReader br) throws FileNotFoundException, IOException {
		ArrayList<StaticCode> list = new ArrayList<StaticCode>();
		String codeName = null;
		StringBuilder comments = new StringBuilder();
		ArrayList<String> code = new ArrayList<String>();
		String line = br.readLine();
		while (line != null) {
			if (line.startsWith("* ")) {
				line = line.substring(2);
			}
			line.replaceAll("\\s+$", "");
			if (line.length() == 17) {
				code.add(line);
			} else if (line.length() == 0) {
				// new line: store the code and get ready to make a new one
				if (code.size() > 0) {
					StaticCode sn = new StaticCode(codeName, code.toArray(new String[0]), "<html>"+comments.toString()+"</html>");
					list.add(sn);
				}
				comments = new StringBuilder();
				code.clear();
				codeName = null;
			} else {
				if (codeName == null) {
					codeName = line;
				} else {
					// not a code line; it's a comment
					comments.append(line);
					comments.append("<br/>");
				}
			}
			line = br.readLine();
		}
		if (code.size() > 0) {
			StaticCode sn = new StaticCode(codeName, code.toArray(new String[0]), comments.toString());
			list.add(sn);
		}
		return list;
	}

}
