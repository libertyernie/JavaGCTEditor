package us.lakora.brawl.gct;

import java.util.Arrays;

import javax.swing.JOptionPane;

/**
 * Describes a line of code in a GCT file.
 * @author libertyernie
 */
public class Line implements Comparable<Line> {

	public byte[] data;
	private Code code;
	
	/**
	 * Creates a new Line object with the given data.
	 * @param data A byte array with the data. An internal copy will be made of this array with a length of 8, using Arrays.copyOf.
	 */
	public Line(byte[] data) {
		this.data = Arrays.copyOf(data, 8);
	}
	
	/**
	 * Creates a new Line object with the given data.
	 * @param data A String array with the data. All spaces will be removed, then the first 8 characters will be used.
	 */
	public Line(String s) throws IndexOutOfBoundsException {
		s = s.replace(" ", "");
		this.data = new byte[8];
		for (int i=0; i<8; i++) {
			String number = s.substring(2*i, 2*i+2);
			this.data[i] = (byte)Integer.parseInt(number, 16);
		}
	}
	
	public Code getAssignedCode() {
		return code;
	}
	
	/**
	 * Records which code this line is part of. If it's already marked as part of a code, an exception will be thrown.
	 * @throws AlreadyAssignedException 
	 */
	public void assign(Code from) {
		if (code != null) {
			String s = "Line " + this + " is already part of the code " + code.description() + ". This reflects a programming error in the application.";
			new Exception(s).printStackTrace();
			JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		code = from;
	}
	
	public boolean equals(Object o) {
		return this == o;
	}

	public boolean startsWith(String s) {
		s = s.replace(" ", "");
		byte[] b = new byte[s.length()/2];
		for (int i=0; i<b.length && i<8; i++) {
			String number = s.substring(2*i, 2*i+2);
			b[i] = (byte)Integer.parseInt(number, 16);
		}
		return startsWith(b);
	}
	
	public boolean startsWith(byte[] b) {
		boolean ret = true;
		int i = 0;
		while (ret && i < b.length && i < 8) {
			ret = (b[i] == data[i]);
			i++;
		}
		return ret;
	}
	
	public int compareTo(Line l) {
		int i = 0;
		int difference = 0;
		while (difference == 0 && i < 8) {
			difference = data[i] - l.data[i];
			i++;
		}
		return difference;
	}
	
	/**
	 * Returns the code line as a string in the standard format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(9);
		for (int i=0; i<4; i++) {
			String s = Integer.toString((data[i]+256)%256, 16);
			if (s.length() == 1) {
				sb.append('0');
			}
			sb.append(s);
		}
		sb.append(' ');
		for (int i=4; i<8; i++) {
			String s = Integer.toString((data[i]+256)%256, 16);
			if (s.length() == 1) {
				sb.append('0');
			}
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static boolean isCodeLine(String s) {
		if (s.length() < 17) return false;
		s = s.substring(0, 17);
		for (int i=0; i<8; i++) {
			if (!isHexDigitChar(s.charAt(i))) return false;
		}
		if (s.charAt(8) != ' ') return false;
		for (int i=9; i<17; i++) {
			if (!isHexDigitChar(s.charAt(i))) return false;
		}
		return true;
	}
	
	public static boolean isHexDigitChar(char c) {
		return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
	}

}
