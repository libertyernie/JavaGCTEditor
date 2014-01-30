package us.lakora.brawl.gct;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JOptionPane;

import us.lakora.brawl.gct.gui.Editor;
import us.lakora.brawl.gct.sdsl.SDSL;
import us.lakora.brawl.gct.staticcodes.StaticCode;

public class GCT {
	
	private static final byte[] HEADER = {
			(byte)0x00,
			(byte)0xd0,
			(byte)0xc0,
			(byte)0xde,
			(byte)0x00,
			(byte)0xd0,
			(byte)0xc0,
			(byte)0xde};
	private static final byte[] FOOTER = {
			(byte)0xf0,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00};

	private LinkedList<Line> allLines;

	/**
	 * Maps a class representing a static code to a list of Line objects representing that code in memory (if the code is enabled.)
	 */
	private HashMap<StaticCode, List<Line>> knownStaticCodes;
	
	/**
	 * Keeps track of the known dynamic codes embedded in the codeset.
	 */
	private LinkedList<DynamicCode> knownDynamicCodes;
	
	/**
	 * Reads the data from an InputStream and then closes it.
	 * @param is
	 * @throws IOException Passed on from the InputStream.
	 * @throws GCTFormatException If the header or footer of the GCT file is invalid.
	 */
	public GCT(InputStream is) throws IOException, GCTFormatException, InterruptedException {
		allLines = new LinkedList<Line>();
		knownStaticCodes = new HashMap<StaticCode, List<Line>>();
		knownDynamicCodes = new LinkedList<DynamicCode>();
		byte[] header = new byte[8];
		is.read(header);
		if (Arrays.equals(header, HEADER)) {
			byte[] nextLine = new byte[8];
			while (is.read(nextLine) == 8) {
				allLines.add(new Line(nextLine));
			}
			Line last = allLines.getLast();
			if (last.startsWith(FOOTER)) {
				allLines.removeLast();
			} else {
				throw new GCTFormatException("The GCT footer is invalid");
			}
		} else {
			// assume text file
			// I know using GUI code here makes it less portable, but this way the user is only asked if the file turns out to be a text file
			int res = JOptionPane.showConfirmDialog(null, "Skip codes that are turned off (no \"*\" in the file)?",
					Editor.TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.CANCEL_OPTION) {
				throw new InterruptedException("Opening file cancelled");
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				for (String line = br.readLine(); line != null; line = br.readLine()) {
					if (line.startsWith("* ")) {
						line = line.substring(2);
					} else {
						if (res == JOptionPane.YES_OPTION) {
							// "Yes" selected, but code is not enabled (no *)
							line = "";
						}
					}
					if (Line.isCodeLine(line)) {
						allLines.add(new Line(line));
					}
				}
			}
		}
		
	}
	
	public GCT(File f) throws FileNotFoundException, IOException, GCTFormatException, InterruptedException {
		this(new BufferedInputStream(new FileInputStream(f)));
	}
	
	public GCT(String s) throws FileNotFoundException, IOException, GCTFormatException, InterruptedException {
		this(new BufferedInputStream(new FileInputStream(s)));
	}
	
	/**
	 * Returns the code lines for this code, with a trailing newline.
	 */
	public static String codeLinesToString(List<Line> lines) {
		StringBuilder sb = new StringBuilder(10*lines.size());
		for (Line l : lines) {
			sb.append("* "+l.toString()+"\n");
		}
		return sb.toString();
	}
	
	/**
	 * Exports to TXT. Known static codes separated if separate == true.
	 */
	public String splitExport(boolean separate, boolean sdslAfterOtherCodes) {
		LinkedList<SDSL> sdsls = new LinkedList<SDSL>();
		
		HashSet<Line> toSkip = new HashSet<Line>();
		StringBuilder sb = new StringBuilder("\nRSBE01\nSuper Smash Bros. Brawl (US)\n\n");
		if (separate) {
			for (StaticCode sc : knownStaticCodes.keySet()) {
				sb.append(sc.toString()+"\n");
				List<Line> codeLines = knownStaticCodes.get(sc);
				sb.append(codeLinesToString(codeLines));
				toSkip.addAll(codeLines);
				String comments = sc.getComments();
				if (comments != null) {
					sb.append(comments.replaceAll("</?html>", "").replaceAll("<br?/>", "\n"));
				}
				sb.append('\n');
			}
			for (DynamicCode dc : knownDynamicCodes) {
				List<Line> codeLines = Arrays.asList(dc.getLineArray());
				if (sdslAfterOtherCodes && (dc instanceof SDSL)) {
					sdsls.add((SDSL)dc);
				} else {
					sb.append(dc.description() + "\n");
					sb.append(codeLinesToString(codeLines) + "\n");
				}
				toSkip.addAll(codeLines);
			}
		}
		sb.append("Remainder of Codeset\n");
		for (Line l : allLines) {
			if (!toSkip.contains(l)) {
				sb.append("* " + l.toString() + "\n");
			}
		}
		sb.append('\n');
		for (SDSL sdsl : sdsls) {
			List<Line> codeLines = Arrays.asList(sdsl.getLineArray());
			sb.append(sdsl.description() + "\n");
			sb.append(codeLinesToString(codeLines) + "\n");
		}
		return sb.toString();
	}
	
	public String toString() {
		return "GCT with " + size() + " lines";
	}
	
	public int size() {
		return allLines.size();
	}
	
	/**
	 * Writes the GCT to an OutputStream and then closes it.
	 * @param os
	 * @throws IOException
	 */
	public synchronized void write(OutputStream os) throws IOException {
		os.write(HEADER);
		for (Line l : allLines) {
			os.write(l.data);
		}
		os.write(FOOTER);
		os.close();
	}
	
	/**
	 * Writes the GCT to the given file.
	 * @param s
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void write(File f) throws FileNotFoundException, IOException {
		OutputStream os = new FileOutputStream(f);
		write(os);
	}

	public synchronized boolean findStaticCode(StaticCode sn) {
		String[] stringArray = sn.getStringArray();
		boolean found = false;
//		int i = 0;
		ListIterator<Line> it = allLines.listIterator();
		int pointInCode = 0;
		Line[] newCode = new Line[stringArray.length];
		while (it.hasNext()) {
			Line l = it.next();
			if (!l.startsWith(stringArray[pointInCode])) {
				pointInCode = 0;
			} else {
				if (!found) newCode[pointInCode] = l;
				pointInCode++;
				if (pointInCode == stringArray.length) {
					if (!found) {
						found = true;
					} else {
						System.err.println("Warning: the same code (" + sn + ") was found more than once in the GCT. The second instance will be ignored.");
					}
					pointInCode = 0; // reset counter
				}
			}
//			i++;
		}
		if (found) {
			List<Line> code = Arrays.asList(newCode);
			knownStaticCodes.put(sn, code);
			return true;
		} else {
			knownStaticCodes.remove(sn); // this case might not be used
			return false;
		}
	}
	
	public LinkedList<Line> getCodeLines() {
		return allLines;
	}
	
	public synchronized void deleteStaticCode(StaticCode sn) {
		List<Line> codeList = knownStaticCodes.get(sn);
		if (codeList != null) {
			allLines.removeAll(codeList);
			knownStaticCodes.remove(sn);
		}
	}
	
	public synchronized void addStaticCode(StaticCode sn) {
		String[] stringArray = sn.getStringArray();
		if (!knownStaticCodes.containsKey(sn)) {
			ArrayList<Line> toAdd = new ArrayList<Line>(stringArray.length);
			for (String s : stringArray) {
				toAdd.add(new Line(s));
			}
			allLines.addAll(toAdd);
			knownStaticCodes.put(sn, toAdd);
		}
	}
	
	/**
	 * Adds a code to the GCT and records it in the internal list.
	 */
	public synchronized void addDynamicCode(DynamicCode c) {
		knownDynamicCodes.add(c);
		allLines.addAll(Arrays.asList(c.getLineArray()));
	}
	
	/**
	 * Records an already existing code in the internal list.
	 * Should be used by initialization functions that look for existing codes in the GCT.
	 */
	public synchronized void recordDynamicCode(DynamicCode c) {
		knownDynamicCodes.add(c);
	}
	
	public synchronized void deleteDynamicCode(DynamicCode c) {
		ListIterator<Line> it = allLines.listIterator();
		List<Line> subCode = Arrays.asList(c.getLineArray());
		int linesRemoved = 0;
		boolean done = false;
		while (!done && it.hasNext()) {
			Line l = it.next();
			if (subCode.contains(l)) {
				it.remove();
				linesRemoved++;
				if (linesRemoved == subCode.size()) {
					done = true;
				}
			}
		}
		knownDynamicCodes.remove(c);
		if (!done) {
			System.err.println("Removal warning: Not all code lines found in internal GCT");
		}
	}

}
