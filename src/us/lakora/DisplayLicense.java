package us.lakora;

/**
 * DisplayLicense.java - displays a text file within the JAR, with the option of saving
 * by Isaac Schemm - last revised Feb. 15, 2012
 * 
 * This class is licensed under CC0: see http://creativecommons.org/publicdomain/zero/1.0/
 * To the extent possible under law, I have waived all copyright and related or
 * neighboring rights to DisplayLicense.java. This work is published from the United States.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DisplayLicense extends JScrollPane {
	private static final long serialVersionUID = 2L;
	
	private JTextArea text;
	
	/**
	 * Make a new DisplayLicense panel with the stack trace of the given exception, in red.
	 * @param e The exception to get the stack trace of.
	 */
	public DisplayLicense(Exception e) {
		initialize(e);
	}
	
	/**
	 * A shortcut to make a new DisplayLicense that reads from a file while handling any possible exceptions.
	 * @param filename The path to the file to read from.
	 * @return an instance of DisplayLicense
	 */
	public static DisplayLicense newInstanceFromFile(String filename) {
		try {
			FileReader fr = new FileReader(filename);
			return new DisplayLicense(fr);
		} catch (FileNotFoundException e) {
			return new DisplayLicense(e);
		}
	}
	
	/**
	 * Make a new DisplayLicense from a Reader while handling any IOException that may occur.
	 * @param r The Reader to read from.
	 */
	public DisplayLicense(Reader r) {
		try {
			initialize(r);
		} catch (IOException e) {
			initialize(e);
		}
	}
	
	private void initialize(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String s = sw.toString();

		initialize(s);
		text.setForeground(Color.red);
	}
	
	private void initialize(Reader r) throws IOException {
		// Load in the text file
		String s = null;
		BufferedReader br = new BufferedReader(r);
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			sb.append('\n');
			line = br.readLine();
		}
		br.close();
		s = sb.toString();
		
		initialize(s);
	}
	
	private void initialize(String s) {
		text = new JTextArea();
		text.setFont(new Font("Monospaced", Font.PLAIN, 12));
		setViewportView(text);
		text.setText(s);
		
		setPreferredSize(new Dimension(600, 400));
		text.getCaret().setDot(0);
	}
	
	public void setText(String s) {
		text.setText(s);
	}
	
	public String getText() {
		return text.getText();
	}
	
//	public static void main(String[] args) {
//		example();
//	}
//	
//	public static void example() {
//		JDialog parent = new JDialog();
//		parent.setTitle("Example");
//		parent.add(new javax.swing.JLabel("<html>This window is the parent of each dialog in this example.<br/>" +
//				"<br/>" +
//				"These will be loaded, in order:<br/>" +
//				"String<br/>" +
//				"URL (downloaded from the Internet)<br/>" +
//				"C:\\Windows\\system.ini (will only be found on Windows)<br/>" +
//				"/etc/group (will only be found on GNU)</html>"));
//		parent.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
//		parent.pack();
//		parent.setVisible(true);
//		
//		String s = "This is a string passed from within the program.\n" +
//				"It has a line break in it.";
//		DisplayLicense.readString(parent, s);
//		
//		try {
//			URL url = new URL("http://creativecommons.org/publicdomain/zero/1.0/legalcode.txt");
//			DisplayLicense.read(parent, url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		DisplayLicense.readFileFromFS(parent, "C:\\Windows\\system.ini");
//		DisplayLicense.readFileFromFS(parent, "/etc/group");
//		
//		parent.dispose();
//	}
	
	/**
	 * Displays a dialog with the contents of the given string.
	 * @param parent The parent component; can be null, but is usually a JFrame.
	 * @param s The string to display.
	 */
	public static void readString(Component parent, String s) {
		read(null, new StringReader(s));
	}
	
	/**
	 * Displays a dialog with the contents of the given plain text file.
	 * @param parent The parent component; can be null, but is usually a JFrame.
	 * @param s The path to the file.
	 */
	public static void readFileFromFS(Component parent, String s) {
		read(parent, new File(s));
	}
	
	/**
	 * Displays a dialog with the contents of the given plain text file.
	 * @param parent The parent component; can be null, but is usually a JFrame.
	 * @param f The File object representing the file.
	 */
	public static void read(Component parent, File f) {
		try {
			read(parent, new FileReader(f));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(parent, "Could not find the file " + f, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Displays a dialog with the contents of the given plain text file.
	 * @param parent The parent component; can be null, but is usually a JFrame.
	 * @param s The path to the file inside the JAR archive.
	 */
	public static void readFileFromJAR(Component parent, String s) {
		read(parent, DisplayLicense.class.getClassLoader().getResource(s));
	}
	
	public static void read(Component parent, URL url) {
		if (url != null) {
			try {
				Reader r = new InputStreamReader(url.openStream());
				read(parent, r);
				r.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(parent, "I/O error reading file " + url, "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(parent, "Could not read the file " + url, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void read(final Component parent, final Reader r) {
		final JDialog d = new JDialog();
		d.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		d.setLayout(new BorderLayout());
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		final DisplayLicense dl = new DisplayLicense(r);
		d.add(dl, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel();
		d.add(buttons, BorderLayout.SOUTH);
		
		JButton close = new JButton("Close");
		buttons.add(close);
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				d.setVisible(false);
			}
		});
		JButton save = new JButton("Save");
		buttons.add(save);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveFile(parent, dl.getText());
			}
		});

		d.pack();
		d.setVisible(true);
		d.dispose();
	}
	
	/**
	 * Shows a Save dialog to the user and writes the text to the file they choose. Does not require Java 7.
	 */
	private static void saveFile(Component parent, String s) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter ff = new FileNameExtensionFilter("Plain text file", "txt");
			fileChooser.addChoosableFileFilter(ff); fileChooser.setFileFilter(ff);
			fileChooser.setAcceptAllFileFilterUsed(false);
			int result = fileChooser.showSaveDialog(parent);
			if (result == JFileChooser.APPROVE_OPTION) {
				File out = fileChooser.getSelectedFile();
				int option;
				if (out.exists()) {
					option = JOptionPane.showConfirmDialog(parent, "Overwrite " + out.getName() + "?", "Option", JOptionPane.OK_CANCEL_OPTION);
				} else {
					option = JOptionPane.OK_OPTION;
				}
				if (option == JOptionPane.OK_OPTION) {
					String outname = out.getPath();
					if (!outname.endsWith(".txt")) {
						outname += ".txt";
						out = new File(outname);
					}
					// copy file - too bad this isn't Java 7. We do know it's plaintext, so we can use BufferedWriter
					BufferedWriter bw = new BufferedWriter(new FileWriter(out));
					bw.write(s);
					bw.close();
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(parent, "Could not write to the file (IO error.)", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
