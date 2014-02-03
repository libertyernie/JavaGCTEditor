package us.lakora.brawl.gct;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import us.lakora.DisplayLicense;
import us.lakora.brawl.gct.asl.ASLDataPanel;
import us.lakora.brawl.gct.csv.CustomSongVolumePanel;
import us.lakora.brawl.gct.dsm.DefaultSettingsModifierPanel;
import us.lakora.brawl.gct.sdsl.SDSLEditorPanel;
import us.lakora.brawl.gct.sss.CustomSSSPanel;
import us.lakora.brawl.gct.staticcodes.StaticCodePanel;

public class Editor extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public final static String TITLE = "In-Place GCT Editor 3.6";
	
	private final static String ABOUT = TITLE + "\n" +
			(char)169 + " 2014 libertyernie\n" +
			"\n" +
			"This program is designed to export GCT codesets for Brawl, along with\n" +
			"the capability to add/remove instances of the Stage-Dependent Song\n" +
			"Loader by Oshtoby, to add/edit/remove the Default Settings Modifier\n" +
			"code, to enable/disable static (non-customizable) codes listed in\n" +
			"static_codes.txt, and to remove the custom SSS code.\n" +
			"\n" +
			"This program is free software: you can redistribute it and/or modify\n" +
			"it under the terms of the GNU General Public License as published by\n" +
			"the Free Software Foundation, either version 3 of the License, or\n" +
			"(at your option) any later version.\n\n" +
			"This program is distributed in the hope that it will be useful,\n" +
			"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
			"GNU General Public License for more details.\n\n" +
			"You should have received a copy of the GNU General Public License\n" +
			"along with this program, available from the main menu (Help>License.)\n" +
			"If not, see <http://www.gnu.org/licenses/>.";

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		String filename = "RSBE01.gct";
		if (args.length > 0) {
			filename = args[0];
		}
		try {
			Editor f = new Editor(new File(filename));
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
		} catch (Exception e) {
			System.out.println("Could not open " + filename + ": " + e.getMessage());
			try {
				Editor f = new Editor();
				f.setVisible(true);
			} catch (Exception e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, e2.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private JPanel main;
	private File currentFile;
	private GCT gct;
	private boolean[] edited;
	
	private StaticCodePanel scp;
	private SDSLEditorPanel ep;
	private DefaultSettingsModifierPanel dsmp;
	private CustomSongVolumePanel csvp;
	private CustomSSSPanel sssp;
	private ASLDataPanel asldp;
	
	private JScrollPane scp_container, csvp_container, sssp_container, asldp_container;
	private JPanel ep_container, dsmp_container;
	
	public Editor() throws FileNotFoundException, IOException, GCTFormatException, InterruptedException {
		this(null);
	}
	
	public Editor(final File filearg) throws FileNotFoundException, IOException, GCTFormatException, InterruptedException {
		final Editor editor = this;
		this.edited = new boolean[1];
		
		setSize(new Dimension(450, 200));
		
		URL iconURL = Editor.class.getClassLoader().getResource("us/lakora/brawl/gct/smallIcon.png");
		if (iconURL != null) {
			setIconImage(new ImageIcon(iconURL).getImage());
		}
		
		if (filearg == null) {
			setTitle(TITLE);
		} else {
			setTitle(TITLE + " - " + filearg.getName());
		}
		main = new JPanel();
		setContentPane(main);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (close()) {
					System.exit(0);
				}
			}
		});
		
		JMenuItem m;
		
		JMenu file = new JMenu("File");
		m = new JMenuItem("Open...");
		file.add(m);
		m.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				open();
			}
		});
		m = new JMenuItem("Export...");
		file.add(m);
		m.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK));
		m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				export();
			}
		});
		m = new JMenuItem("Close");
		file.add(m);
		m.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
		m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});
		file.add(new JMenuItem("Exit")).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (close()) {
					System.exit(0);
				}
			}
		});
		menubar.add(file);
		
		JMenu help = new JMenu("Help");
		m = new JMenuItem("About");
		help.add(m);
		m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icon = null;
				URL iconURL = Editor.class.getClassLoader().getResource("us/lakora/brawl/gct/icon.png");
				if (iconURL != null) {
					icon = new ImageIcon(iconURL);
				}
				JOptionPane.showMessageDialog(editor, ABOUT, TITLE, JOptionPane.PLAIN_MESSAGE, icon);
			}
		});
		m = new JMenuItem("Line-by-line breakdown");
		help.add(m);
		m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (gct != null) DisplayLicense.readString(null, gct.lineAudit());
			}
		});
		m = new JMenuItem("License");
		help.add(m);
		m.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DisplayLicense.readFileFromJAR(editor, "us/lakora/brawl/gct/COPYING");
			}
		});
		menubar.add(help);
		
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		JTabbedPane tabs = new JTabbedPane();
		main.add(tabs);

		Border b = BorderFactory.createLoweredBevelBorder();

		scp_container = new JScrollPane();
		dsmp_container = new JPanel();
		ep_container = new JPanel(new BorderLayout());
		csvp_container = new JScrollPane();
		sssp_container = new JScrollPane();
		asldp_container = new JScrollPane();
		
		JComponent[] allContainers = {
			scp_container, dsmp_container, ep_container, csvp_container, sssp_container, asldp_container
		};
		for (JComponent c : allContainers) c.setBorder(b);

		tabs.addTab("Static Codes", scp_container);
		tabs.addTab("Default Settings Modifier", dsmp_container);
		tabs.addTab("Stage-Dependent Song Loader", ep_container);
		tabs.addTab("Custom Song Volume", csvp_container);
		tabs.addTab("Custom SSS", sssp_container);
		tabs.addTab("ASL Data", asldp_container);
		
		if (filearg != null && filearg.exists()) {
			open(filearg);
		}
		
		edited[0] = false;
	}
	
	/**
	 * Tries to set the JFileChooser's path to the parent directory of the current file, or (if that fails) the directory of the JAR file.
	 * @param jfc
	 */
	private void setPath(JFileChooser jfc) {
		/* First try getting the parent of the currently opened file, if there is one */
		File parent = null;
		if (currentFile != null) {
			parent = currentFile.getParentFile();
		}
		if (parent != null) {
			jfc.setCurrentDirectory(parent);
		} else {
			/* Then try getting the path of the .jar file */
			try {
				String path = Editor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				String decodedPath = URLDecoder.decode(path, "UTF-8");
				decodedPath = decodedPath.substring(0, decodedPath.lastIndexOf('/'));
				File jarDir = new File(decodedPath);
				if (jarDir.isDirectory()) {
					jfc.setCurrentDirectory(jarDir);
				}
			} catch (UnsupportedEncodingException e) {
				/* Do nothing and just let the JFileChooser use its default starting dir */
			}
		}
	}
	
	/**
	 * Shows an "open file" dialog and loads the file chosen.
	 */
	private void open() {
		JFileChooser jfc = new JFileChooser();
		setPath(jfc);
		jfc.setDialogTitle(TITLE);
		FileNameExtensionFilter[] filters = {
				new FileNameExtensionFilter("All supported files", "gct", "txt"),
				new FileNameExtensionFilter("Ocarina code file", "gct"),
				new FileNameExtensionFilter("Text file", "txt")};
		jfc.addChoosableFileFilter(filters[0]);
		jfc.addChoosableFileFilter(filters[1]);
		jfc.addChoosableFileFilter(filters[2]);
		jfc.setFileFilter(filters[0]);
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			open(f);
		}
	}
	
	/**
	 * Open a GCT (or plain text) file.
	 */
	private void open(File f) {
		try {
			GCT g = new GCT(f);
			if (this.ep != null) ep_container.remove(this.ep);
			if (this.dsmp != null) dsmp_container.remove(this.dsmp);
			this.currentFile = f;
			this.gct = g;
			
			this.scp = new StaticCodePanel(g, edited);
			scp_container.setViewportView(scp);
			this.dsmp = new DefaultSettingsModifierPanel(g, edited);
			dsmp_container.add(this.dsmp);
			this.csvp = new CustomSongVolumePanel(g, edited);
			csvp_container.setViewportView(csvp);
			this.sssp = new CustomSSSPanel(g, edited, currentFile);
			sssp_container.setViewportView(this.sssp);
			this.ep = new SDSLEditorPanel(g, edited);
			ep_container.add(this.ep);
			this.asldp = new ASLDataPanel(g, edited, currentFile);
			asldp_container.setViewportView(this.asldp);
			
			edited[0] = false;
			setTitle(TITLE + " - " + f.getName()+ " (" + this.gct.size() + ")");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not find the file " + f + ".", TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not read " + f + ".", TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (GCTFormatException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, f + " does not appear to be a GCT file: " + e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
		} catch (InterruptedException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Closes the current file, if there is one.
	 * @return Whether the closing was successful; "true" if no file is open
	 */
	private boolean close() {
		if (this.ep == null) {
			return true;
		} else {
			int result;
			if (edited[0]) {
				result = JOptionPane.showConfirmDialog(this, "Do you want to save " + currentFile + "?", TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
			} else {
				result = JOptionPane.NO_OPTION;
			}
			boolean cont = true;
			if (result == JOptionPane.YES_OPTION) {
				cont = export();
			} else if (result == JOptionPane.NO_OPTION) {
				cont = true;
			} else if (result == JOptionPane.CANCEL_OPTION) {
				cont = false;
			}
			if (cont) {
				scp_container.setViewportView(null);
				ep_container.remove(this.ep);
				dsmp_container.remove(this.dsmp);
				csvp_container.setViewportView(null);
				sssp_container.setViewportView(null);
				asldp_container.setViewportView(null);
				currentFile = null;
				this.gct = null;
				this.scp = null;
				this.ep = null;
				this.dsmp = null;
				this.csvp = null;
				this.sssp = null;
				this.asldp = null;
				edited[0] = false;
				setTitle(TITLE);
				repaint();
			}
			return cont;
		}
	}
	
	/**
	 * Shows an "export" dialog. Even if the new file is saved, the current file variable will not be updated.
	 */
	private boolean export() {
		if (ep == null) return true;
		ep.update();
		JFileChooser jfc = new JFileChooser();
		setPath(jfc);
		jfc.setDialogTitle(TITLE);
		FileNameExtensionFilter bothfilter = new FileNameExtensionFilter("Compatible formats", "gct", "txt");
		FileNameExtensionFilter txtfilter = new FileNameExtensionFilter("Text format", "txt");
		FileNameExtensionFilter gctfilter = new FileNameExtensionFilter("Ocarina code file", "gct");
		jfc.addChoosableFileFilter(bothfilter);
		jfc.addChoosableFileFilter(txtfilter);
		jfc.addChoosableFileFilter(gctfilter);
		jfc.setFileFilter(bothfilter);
		jfc.setAcceptAllFileFilterUsed(false);
		if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		File newfile = jfc.getSelectedFile();
		if (newfile.exists()) {
			int r = JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite " + newfile.getName() + "?",
					getTitle(), JOptionPane.OK_CANCEL_OPTION);
			if (r != JOptionPane.OK_OPTION) {
				return false;
			}
		}
		try {
			if (newfile.getName().toLowerCase().endsWith(".gct")) {
				gct.write(newfile);
				return true;
			} else {
				if (!newfile.getName().toLowerCase().endsWith(".txt")) {
					newfile = new File(newfile.getPath() + ".txt");
				}
				TXTExportOptionsDialog dialog = new TXTExportOptionsDialog();
				dialog.setVisible(true);
				if (dialog.dialogResult == false) {
					JOptionPane.showMessageDialog(this, "Export cancelled.");
					return false;
				}
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(newfile));

				switch (dialog.getSelectedMethod()) {
				case KNOWN_FIRST:
					bw.write(gct.exportReorder(dialog.shouldBeSortedByOrigOrder(), dialog.shouldBeSDSLAfter()));
					break;
				case RAW:
					bw.write(gct.exportSameOrder(false));
					break;
				case SAME_ORDER:
					bw.write(gct.exportSameOrder(true));
					break;
				default:
					JOptionPane.showMessageDialog(this, "Cannot export - please select an option.", TITLE, JOptionPane.ERROR_MESSAGE);
					bw.close();
					return false;
				}
				bw.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), TITLE, JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
