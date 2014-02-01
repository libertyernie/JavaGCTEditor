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
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import us.lakora.brawl.gct.Editor;
import us.lakora.brawl.gct.GCT;

public class StaticCodePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private GCT gct;
	private boolean[] edited;
	private final static String FILENAME = "static_codes.txt";
	
	public StaticCodePanel(GCT g, boolean[] b) {
		gct = g;
		edited = b;
		
		List<StaticCode> codes;
		try {
			File file = new File(FILENAME);
			codes = file.exists()
					? StaticCode.readFile(file)
					: StaticCode.readDefaultCodesFile();
			File file2 = new File("additional_codes.txt");
			if (file2.exists()) {
				codes.addAll(StaticCode.readFile(file2));
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
			codes = new ArrayList<StaticCode>(0);
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

}
