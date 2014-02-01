package us.lakora.brawl.gct.csv;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class CustomSongVolumePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public final static String[] SKELETON = {
		"C21C744C 00000006",
		"3D80901C 618C3FFC",
		"A7AC0004 2C1D7FFF",
		"41820014 7C1DF000",
		"4082FFF0 A00C0002",
		"48000008 88030014",
		"60000000 00000000",
		"4A000000 90000000",
		"161C4000 000000",
	};
	
	private GCT gct;
	private boolean[] edited;
	private CustomSongVolume csv;
	
	public CustomSongVolumePanel(GCT gctptr, boolean[] editedptr) {
		gct = gctptr;
		edited = editedptr;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (findCSVInstance()) {
			for (VolumeSetting v : csv) {
				add(new JLabel(v.toString()));
			}
			
			JButton showCode = new JButton("Show code");
			add(showCode);
			showCode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					DisplayLicense.readString(null, Code.codeLinesToString(csv.getLineArray()));
				}
			});
			JButton removeCode = new JButton("Remove code");
			add(removeCode);
			removeCode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this code?",
							Editor.TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						gct.deleteDynamicCode(csv);
						edited[0] = true;
						removeAll();
						add(new JLabel("Custom Song Volume code removed."));
						repaint();
					}
				}
			});
		} else {
			add(new JLabel("No Custom Song Volume code found."));
		}
	}
	
	private boolean findCSVInstance() {
		if (csv == null) {
			ListIterator<Line> it = gct.getCodeLines().listIterator();
			ArrayList<Line> code = new ArrayList<Line>();
			int pointInSkeleton = 0;
			boolean keepLooking = true;
			while (it.hasNext() && keepLooking) {
				Line l = it.next();
				if (!l.startsWith(SKELETON[pointInSkeleton])) {
					pointInSkeleton = 0;
					code.clear();
				} else {
					code.add(l);
					if (pointInSkeleton == 8) {
						int skipCount = l.data[7] & 0xff;
						int skipLines = (skipCount+7)/8;
						
						for (int j=0; j<skipLines; j++) {
							l = it.next();
							code.add(l);
						}
					}
					pointInSkeleton++;
					if (pointInSkeleton == SKELETON.length) {
						csv = new CustomSongVolume(code);
						keepLooking = false;
						gct.recordDynamicCode(csv);
					}
				}
			}
		}
		return (csv != null);
	}
}
