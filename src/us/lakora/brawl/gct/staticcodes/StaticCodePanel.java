package us.lakora.brawl.gct.staticcodes;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.gui.Editor;

public class StaticCodePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private GCT gct;
	private boolean[] edited;
	private final static String FILENAME = "static_codes.txt";
	
	public StaticCodePanel(GCT g, boolean[] b) {
		gct = g;
		edited = b;
		
		File file = new File(FILENAME);
		List<StaticCode> codes;
		try {
			codes = StaticCode.readFile(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Could not read/write "+FILENAME, Editor.TITLE, JOptionPane.ERROR_MESSAGE);
			try {
				createStaticCodesFile(file);
				codes = StaticCode.readFile(file);
			} catch (IOException e1) {
				e1.printStackTrace();
				codes = new ArrayList<StaticCode>(0);
			}
		}
		StaticCodeToggler[] array = new StaticCodeToggler[codes.size()];
		for (int i=0; i<array.length; i++) {
			StaticCode sc = codes.get(i);
			array[i] = new StaticCodeToggler(sc.toString(), sc);
		}
		this.setLayout(new GridLayout(array.length, 1));
		for (Component c : array) {
			this.add(c);
		}
	}
	
	private class StaticCodeToggler extends JCheckBox {
		private static final long serialVersionUID = 1L;
		private StaticCode sn;
		
		public StaticCodeToggler(String title, StaticCode snarg) {
			super(title);
			this.sn = snarg;
			setToolTipText(sn.getComments());
			if (gct != null) {
				setSelected(gct.findStaticCode(sn));
			} else {
				setEnabled(false);
			}
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					edited[0] = true;
					if (gct != null) {
						if (isSelected()) {
							gct.addStaticCode(sn);
						} else {
							gct.deleteStaticCode(sn);
						}
					}
				}
				
			});
		}
	}
	
	private static void createStaticCodesFile(File file) throws IOException {
		InputStream is = StaticCodePanel.class.getClassLoader().getResourceAsStream("us/lakora/brawl/gct/default_codes.txt");
		
		if (is == null) {
			throw new IOException("us/lakora/brawl/gct/default_codes.txt not found in classpath.");
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (int b = is.read(); b >= 0; b = is.read()) {
			bw.write(b);
		}
		bw.close();
		JOptionPane.showMessageDialog(null, "Created file "+FILENAME+" with default codes.", Editor.TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

}
