package us.lakora.brawl.gct.sss;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import us.lakora.DisplayLicense;
import us.lakora.brawl.gct.Code;
import us.lakora.brawl.gct.Editor;
import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.Line;

public class CustomSSSPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public final static String[] SKELETON = {
		"046B8F5C 7C802378", // 0
		"046B8F64 7C6300AE",
		"040AF618 5460083C",
		"040AF68C 38840002",
		"040AF6AC 5463083C",
		"040AF6C0 88030001",
		"040AF6E8 3860FFFF",
		"040AF59C 3860000C",
		"060B91C8 00000018",
		"BFA10014 7CDF3378",
		"7CBE2B78 7C7D1B78",
		"2D05FFFF 418A0014",
		"006B929C 000000",
		"066B99D8 000000",
		"006B92A4 000000", // 14
		"066B9A58 000000",
		"06407AAC 000000", // 16
	};
	
	private GCT gct;
	private boolean[] edited;
	private SSS sss;
	private File gctFile;
	
	private JPanel buttonPanel, centerPanel;
	
	public CustomSSSPanel(final GCT gct, final boolean[] edited, final File gctFile) {
		this.gct = gct;
		this.edited = edited;
		this.gctFile = gctFile;

		setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		this.add(buttonPanel, BorderLayout.EAST);
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		this.add(centerPanel, BorderLayout.CENTER);
		if (findSSSInstance()) {
			centerPanel.add(new JLabel(sss.getBrawl() + " stages on 1st screen\n"));
			centerPanel.add(new JLabel(sss.getMelee() + " stages on 2nd screen\n"));
			centerPanel.add(new JLabel(sss.getStages() + " stage pairs defined"));
			
			JButton showCode = new JButton("Show code");
			showCode.setAlignmentX(JButton.RIGHT_ALIGNMENT);
			buttonPanel.add(showCode);
			showCode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					DisplayLicense.readString(null, Code.codeLinesToString(sss.getLineArray()));
				}
			});
			JButton removeCode = new JButton("Remove code");
			removeCode.setAlignmentX(JButton.RIGHT_ALIGNMENT);
			buttonPanel.add(removeCode);
			removeCode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this code?",
							Editor.TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						gct.deleteDynamicCode(sss);
						edited[0] = true;
						removeAll();
						add(new JLabel("Custom SSS code removed."));
						repaint();
					}
				}
			});
			if (new File("SSSEditor.exe").isFile()) {
				JButton sssEditor = new JButton("Open SSS Editor");
				sssEditor.setAlignmentX(JButton.RIGHT_ALIGNMENT);
				buttonPanel.add(sssEditor);
				sssEditor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						launchSSSEditor();
					}
				});
			}
		} else {
			add(new JLabel("No custom SSS code found."));
		}
	}
	
	public void launchSSSEditor() {
		if (new File("SSSEditor.exe").isFile()) {
			String gctpath = gctFile.getAbsolutePath();
			File dir = gctFile.getParentFile();
			// try to find dir
			while (dir != null) {
				try {
					if (new File(dir + File.separator + "private").isDirectory()
							|| new File(dir + File.separator + "projectm").isDirectory()
							|| new File(dir + File.separator + "minsuery").isDirectory()) {
						break;
					}
					dir = dir.getParentFile();
				} catch (Exception e) {
					dir = null;
				}
			}
			// warning
			int r = edited[0] ? JOptionPane.showConfirmDialog(null,
					"Close GCT Editor and open SSS Editor?\nAny unsaved changes you have made in GCT Editor will be lost.",
					"Confirm", JOptionPane.OK_CANCEL_OPTION)
					: JOptionPane.OK_OPTION;
			if (r == JOptionPane.OK_OPTION) {
				// launch program
				ProcessBuilder pb = new ProcessBuilder("SSSEditor.exe", gctpath);
				if (dir != null) pb.directory(dir);
				try {
					pb.start();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean findSSSInstance() {
		if (sss == null) {
			ListIterator<Line> it = gct.getCodeLines().listIterator();
			ArrayList<Line> code = new ArrayList<Line>();
			ArrayList<Integer> skipCounts = new ArrayList<Integer>(3);
			int pointInSkeleton = 0;
			boolean keepLooking = true;
			while (it.hasNext() && keepLooking) {
				Line l = it.next();
				if (!l.startsWith(SKELETON[pointInSkeleton])) {
					pointInSkeleton = 0;
					code.clear();
				} else {
					code.add(l);
					if (pointInSkeleton == 13 || pointInSkeleton == 15 || pointInSkeleton == 16) {
						int skipCount = l.data[7] & 0xff;
						skipCounts.add(skipCount);
						int skipLines = (skipCount+7)/8;
						
						for (int j=0; j<skipLines; j++) {
							l = it.next();
							code.add(l);
						}
					}
					pointInSkeleton++;
					if (pointInSkeleton == SKELETON.length) {
						keepLooking = false;
						sss = new SSS(code, skipCounts);
						gct.recordDynamicCode(sss);
					}
				}
			}
		}
		return (sss != null);
	}

}
