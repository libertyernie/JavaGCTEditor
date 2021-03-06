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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import us.lakora.brawl.gct.sdsl.SDSL;
import us.lakora.brawl.gct.staticcodes.StaticCode;
import us.lakora.brawl.gct.staticcodes.StaticCodeOccurrence;

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
	private static final byte[] FOOTER1 = {
			(byte)0xf0,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00};
	private static final byte[] FOOTER2 = {
			(byte)0xff,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00,
			(byte)0x00};

	private LinkedList<Line> allLines;

	private LinkedList<Code> knownCodes;
	
	private String gamecode;
	
	/**
	 * Reads the data from an InputStream and then closes it.
	 * @param is
	 * @throws IOException Passed on from the InputStream.
	 * @throws GCTFormatException If the header or footer of the GCT file is invalid.
	 */
	private GCT(InputStream is) throws IOException, GCTFormatException, InterruptedException {
		allLines = new LinkedList<Line>();
		knownCodes = new LinkedList<Code>();
		byte[] header = new byte[8];
		is.read(header);
		if (Arrays.equals(header, HEADER)) {
			byte[] nextLine = new byte[8];
			while (is.read(nextLine) == 8) {
				allLines.add(new Line(nextLine));
			}
			Line last = allLines.getLast();
			if (last.startsWith(FOOTER1) || last.startsWith(FOOTER2)) {
				allLines.removeLast();
			} else {
				throw new GCTFormatException("The GCT footer is invalid. (Was this file made by BrawlBox? It might have metadata appended to it - if so, just use BrawlBox to edit it.)");
			}
		} else {
			// assume text file
			int res = JOptionPane.showConfirmDialog(null, "Skip codes that are turned off (no \"*\" at start of line)?",
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
		gamecode = (f.getName() + "      ").substring(0, 6);
		if (!Pattern.matches("^[A-Za-z0-9][A-Za-z0-9][A-Za-z0-9][A-Za-z0-9][0-9][0-9]$", gamecode)) {
			gamecode = "RSBE01";
		}
	}
	
	/**
	 * Exports to TXT in original order.
	 * @param separate Whether to separate the known codes (true) or export everything as one block (false).
	 */
	public String exportSameOrder(boolean separate) {
		StringBuilder sb = new StringBuilder();
		if (gamecode.equals("RSBE01")) {
			sb.append("\r\nRSBE01\r\nSuper Smash Bros. Brawl (US)\r\n");
		} else {
			sb.append("\r\n");
			sb.append(gamecode);
			sb.append("\r\nUnknown\r\n");
		}
		Code currentCode = null;
		if (allLines.get(0).getAssignedCode() == null) {
			sb.append("\r\nUnknown Code(s)\r\n");
		}
		for (Line l : allLines) {
			if (separate && currentCode != l.getAssignedCode()) {
				String comments = currentCode == null ? null : currentCode.getComments();
				if (comments != null) sb.append(comments);
				
				sb.append("\r\n");
				
				currentCode = l.getAssignedCode();
				String desc = currentCode == null ? "Unknown Code(s)" : currentCode.description();
				sb.append(desc + "\r\n");
			}
			sb.append("* " + l.toString() + "\r\n");
		}
		return sb.toString();
	}
	
	/**
	 * Exports to TXT: known codes first; unknown codes later.
	 * @param sortByOriginalOrder Whether to export the known codes in the same order they were in the original file. Unknown codes will be in a separate block.
	 * @param sdslAfterOtherCodes Whether to move the SDSL codes to the end, after the unknown code block.
	 */
	public String exportReorder(boolean sortByOriginalOrder, boolean sdslAfterOtherCodes) {
		LinkedList<SDSL> sdsls = new LinkedList<SDSL>();
		
		HashSet<Line> toSkip = new HashSet<Line>();
		StringBuilder sb = new StringBuilder();
		if (gamecode.equals("RSBE01")) {
			sb.append("\r\nRSBE01\r\nSuper Smash Bros. Brawl (US)\r\n");
		} else {
			sb.append("\r\n");
			sb.append(gamecode);
			sb.append("\r\nUnknown\r\n");
		}
		sb.append("\r\n");
		
		/*ArrayList<StaticCodeOccurrence> sorted = new ArrayList<StaticCodeOccurrence>(knownStaticCodes);
		if (sortByOriginalOrder) Collections.sort(sorted);*/
		
		for (Code code : knownCodes) {
			sb.append(code.description()+"\r\n");
			
			Line[] codeLines = code.getLineArray();
			sb.append(Code.codeLinesToString(codeLines));
			for (Line l : codeLines) toSkip.add(l);
			
			String comments = code.getComments();
			if (comments != null) {
				sb.append(comments);
			}
			sb.append("\r\n");
		}
		if (toSkip.size() != allLines.size()) {
			sb.append("Remainder of Codeset\r\n");
			for (Line l : allLines) {
				if (!toSkip.contains(l)) {
					sb.append("* " + l.toString() + "\r\n");
				}
			}
		}
		sb.append("\r\n");
		if (!sdsls.isEmpty()) {
			sb.append("Stage-Dependent Song Loaders [Oshtoby]\r\n\r\n");
			for (SDSL sdsl : sdsls) {
				List<Line> codeLines = Arrays.asList(sdsl.getLineArray());
				sb.append(sdsl.description().replace(" [Oshtoby]", "") + "\r\n");
				sb.append(Code.codeLinesToString(codeLines) + "\r\n");
			}
		}
		return sb.toString();
	}
	
	public String lineAudit() {
		StringBuilder sb = new StringBuilder();
		for (Line l : allLines) {
			sb.append(l + " ");
			sb.append(l.getAssignedCode() != null ? l.getAssignedCode().description() : "(none)");
			sb.append("\r\n");
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
		os.write(FOOTER1);
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
		int i = 0, foundAt = -1;
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
						foundAt = i-pointInCode;
					} else {
						System.err.println("Warning: the same code (" + sn + ") was found more than once in the GCT. The second instance will be ignored.");
					}
					pointInCode = 0; // reset counter
				}
			}
			i++;
		}
		if (found) {
			List<Line> code = Arrays.asList(newCode);
			knownCodes.add(new StaticCodeOccurrence(sn, code, foundAt));
			System.out.println("Code found: " + sn.toString());
			return true;
		} else {
			return false;
		}
	}
	
	public LinkedList<Line> getCodeLines() {
		return allLines;
	}
	
	public synchronized void deleteStaticCode(StaticCode sn) {
		Iterator<Code> it = knownCodes.iterator();
		while (it.hasNext()) {
			Code code = it.next();
			if (code instanceof StaticCodeOccurrence) {
				StaticCodeOccurrence sco = (StaticCodeOccurrence)code;
				if (sco.getCode().equals(sn)) {
					for (Line l : sco.getLineArray()) allLines.remove(l);
					it.remove();
				}
			}
		}
	}
	
	public synchronized void addStaticCode(StaticCode sn) {
		String[] stringArray = sn.getStringArray();
		ArrayList<Line> toAdd = new ArrayList<Line>(stringArray.length);
		for (String s : stringArray) {
			toAdd.add(new Line(s));
		}
		allLines.addAll(toAdd);
		knownCodes.add(new StaticCodeOccurrence(sn, toAdd));
	}
	
	/**
	 * Adds a code to the GCT and records it in the internal list.
	 */
	public synchronized void addDynamicCode(DynamicCode c) {
		knownCodes.add(c);
		allLines.addAll(Arrays.asList(c.getLineArray()));
	}
	
	/**
	 * Records an already existing code in the internal list.
	 * Should be used by initialization functions that look for existing codes in the GCT.
	 */
	public synchronized void recordDynamicCode(DynamicCode c) {
		knownCodes.add(c);
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
		knownCodes.remove(c);
		if (!done) {
			System.err.println("Removal warning: Not all code lines found in internal GCT");
		}
	}

}
