package us.lakora.brawl.gct.asl;

import java.awt.BorderLayout;
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
import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.Line;
import us.lakora.brawl.gct.gui.Editor;

public class ASLDataPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public final static String HEADER1 = "46000010 00000000",
			HEADER2 = "44000000 005A7D00",
			HEADER3 = "6620",
			FOOTER1 = "00000DED",
			FOOTER2 = "E0000000 80008000";
	
	private GCT gct;
	private boolean[] edited;
	private ASLData aslData;
	
	private JPanel buttonPanel, centerPanel;
	
	public ASLDataPanel(GCT gctarg, boolean[] editedarg) {
		gct = gctarg;
		edited = editedarg;

		setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		this.add(buttonPanel, BorderLayout.EAST);
		centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		this.add(centerPanel, BorderLayout.CENTER);
		if (findASLInstance()) {
			centerPanel.add(new JLabel("found: " + aslData.getLineArray().length + " lines"));
			
			JButton showCode = new JButton("Show code");
			showCode.setAlignmentX(JButton.RIGHT_ALIGNMENT);
			buttonPanel.add(showCode);
			showCode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					DisplayLicense.readString(null, aslData.lineArrayToString());
				}
			});
			JButton removeCode = new JButton("Remove code");
			removeCode.setAlignmentX(JButton.RIGHT_ALIGNMENT);
			buttonPanel.add(removeCode);
			removeCode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this code?",
							Editor.TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						gct.deleteDynamicCode(aslData);
						edited[0] = true;
						removeAll();
						add(new JLabel("Custom SSS code removed."));
						repaint();
					}
				}
			});
		} else {
			add(new JLabel("No custom SSS code found."));
		}
	}
	
	public void addSSSEditorButton(ActionListener al) {
		JButton sssEditor = new JButton("Open SSS Editor");
		sssEditor.setAlignmentX(JButton.RIGHT_ALIGNMENT);
		buttonPanel.add(sssEditor);
		sssEditor.addActionListener(al);
	}
	
	private static enum STATE {
		LOOKFOR_H1,
		LOOKFOR_H2,
		LOOKFOR_H3,
		LOOKFOR_F1,
		LOOKFOR_F2
	}
	
	private boolean findASLInstance() {
		if (aslData == null) {
			ListIterator<Line> it = gct.getCodeLines().listIterator();
			ArrayList<Line> code = new ArrayList<Line>();
			STATE state = STATE.LOOKFOR_H1;
			while (it.hasNext()) {
				Line l = it.next();
				if (state == STATE.LOOKFOR_H1) {
					if (l.startsWith(HEADER1)) {
						code.add(l);
						state = STATE.LOOKFOR_H2;
					}
				} else if (state == STATE.LOOKFOR_H2) {
					if (l.startsWith(HEADER2)) {
						code.add(l);
						state = STATE.LOOKFOR_H3;
					} else {
						// mismatch
						code.clear();
						state = STATE.LOOKFOR_H1;
					}
				} else if (state == STATE.LOOKFOR_H3) {
					if (l.startsWith(HEADER3)) {
						int dataLinesIncludingThis = l.data[2] * 0x100 + l.data[3];
						code.add(l);
						for (int i=1; i<dataLinesIncludingThis; i++) {
							if (!it.hasNext()) {
								// end - abort
								break;
							}
							l = it.next();
							code.add(l);
						}
						state = STATE.LOOKFOR_F1;
					} else {
						// mismatch
						code.clear();
						state = STATE.LOOKFOR_H1;
					}
				} else if (state == STATE.LOOKFOR_F1) {
					if (l.startsWith(FOOTER1)) {
						code.add(l);
						state = STATE.LOOKFOR_F2;
					} else {
						// mismatch
						code.clear();
						state = STATE.LOOKFOR_H1;
					}
				} else if (state == STATE.LOOKFOR_F2) {
					if (l.startsWith(FOOTER2)) {
						code.add(l);
						aslData = new ASLData(code);
						gct.recordDynamicCode(aslData);
						break;
					} else {
						// mismatch
						code.clear();
						state = STATE.LOOKFOR_H1;
					}
				}
			}
		}
		return (aslData != null);
	}

}
