package us.lakora.brawl.gct.sdsl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.Line;

public class SDSLEditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final boolean SAVE_FIELDS_ON_CHANGE = true;
	
	private SDSL sdsl;
	private GCT gct;
	
	private JTextField stageID;
	private JComboBox<String> stageList;
	private JTextField songID;
	private JComboBox<String> songList;
	
	private JComboBox<SDSL> selector;
	private JButton delete;
	private ArrayList<SDSL> knownSDSLInstances;
	
	/**
	 * Stores whether the values shown have changed in the GUI since the file was last opened or saved.
	 */
	private boolean[] edited;
	
	public SDSLEditorPanel(GCT gctarg, boolean[] edited) {
		this.gct = gctarg;
		this.edited = edited;
		
		stageID = new JTextField();
		stageList = new JComboBox<String>();
		songID = new JTextField();
		songID.setPreferredSize(new Dimension(64, 24));
		songList = new JComboBox<String>();
		
		setLayout(new BorderLayout());
		
		/* Create the selectorPanel - a panel with the drop-down menu to select from the codes */
		JPanel selectorPanel = new JPanel();
		JButton up = new JButton(""+(char)0x2c4);
		JButton down = new JButton(""+(char)0x2c5);
		delete = new JButton("Del");
		JButton add = new JButton("Add");
		
		/* Populate the box */
		selector = new JComboBox<SDSL>();
		for (SDSL s : getKnownSDSLInstances()) {
			selector.addItem(s);
		}
		/* Set the sdsl variable, if the gct has sdsl codes in it */
		if (selector.getItemCount() > 0) {
			this.sdsl = selector.getItemAt(0);
		}
		
		/* When the selector is changed, update the other boxes to show the new code */
		selector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (SAVE_FIELDS_ON_CHANGE) {
					update();
				}
				sdsl = selector.getItemAt(selector.getSelectedIndex()); // replace sdsl variable
				initializeFields(); // update the boxes
			}
		});
		
		/* Go up (decrement) when the up button is pressed */
		up.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				int index = selector.getSelectedIndex();
				if (index > 0) {
					selector.setSelectedIndex(index-1);
				}
			}
		});

		/* Go down (increment) when the up button is pressed */
		down.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				int index = selector.getSelectedIndex();
				if (index < selector.getItemCount()-1) {
					selector.setSelectedIndex(index+1);
				}
			}
		});
		
		delete.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				delete(sdsl);
			}
		});
		
		add.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		
		selectorPanel.add(delete);
		selectorPanel.add(up);
		selectorPanel.add(selector);
		selectorPanel.add(down);
		selectorPanel.add(add);
		add(selectorPanel, BorderLayout.NORTH);
		
		/* Create the main panel */
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		add(main, BorderLayout.CENTER);
		
		/* the "stage" row */
		main.add(new StagePanel("Stage: ", stageID, stageList, IDLists.stageIDList, IDLists.stageList));
		
		/* the "song" row */
		main.add(new StagePanel("Song:  ", songID, songList, IDLists.songIDList, IDLists.songList));
		
		JPanel songPanel = new JPanel();
		main.add(songPanel);
		songPanel.setLayout(new BoxLayout(songPanel, BoxLayout.X_AXIS));
		
		initializeFields();
	}
	
	private void delete(SDSL sdsl) {
		selector.removeItem(sdsl);
		deleteSDSLInstance(sdsl);
		if (selector.getItemCount() == 0) {
			delete.setEnabled(false);
		}
	}
	
	private void add() {
		SDSL sdsl = addDefaultSDSLInstance();
		selector.addItem(sdsl);
		selector.setSelectedItem(sdsl);
		delete.setEnabled(true);
	}
	
	/**
	 * Updates the lines of code stored in memory, using the SDSL object.
	 */
	public void update() {
		if (sdsl != null) {
			sdsl.setStageID(stageID.getText());
			sdsl.setSongID(songID.getText());
		}
	}
	
	private void initializeFields() {
		
		JComponent[] temporary = {stageID, stageList, songID, songList};
		if (sdsl == null) {
			for (int i=0; i<temporary.length; i++) {
				temporary[i].setEnabled(false);
			}
		} else {
			stageID.setText(Integer.toString(sdsl.getStageID(), 16));
			int i1 = IDLists.stageIDList.indexOf(sdsl.getStageID());
			if (i1 >= 0) {
				stageList.setSelectedIndex(i1);
			} else {
				stageList.setSelectedIndex(0);
			}
			songID.setText(Integer.toString(sdsl.getSongID(), 16));
			int i2 = IDLists.songIDList.indexOf(sdsl.getSongID());
			if (i2 >= 0) {
				songList.setSelectedIndex(i2);
			} else {
				songList.setSelectedIndex(0);
			}
			for (int i=0; i<temporary.length; i++) {
				temporary[i].setEnabled(true);
			}
		}
//		edited.set(false);
	}
	
	public synchronized boolean findSDSLInstances() {
		knownSDSLInstances = new ArrayList<SDSL>();
		boolean found = false;
		ListIterator<Line> it = gct.getCodeLines().listIterator();
		while (it.hasNext()) {
			Line l = it.next();
			if (l.startsWith("28708ceb 000000")) {
				Line[] code = new Line[4];
				code[0] = l;
				code[1] = it.next();
				if (code[1].startsWith("4A000000 90180F06")) {
					code[2] = it.next();
					if (code[2].startsWith("14000076 FF00")) {
						code[3] = it.next();
						if (code[3].startsWith("E0000000 80008000")) {
							found = true;
							SDSL sdsl = new SDSL(code);
							knownSDSLInstances.add(sdsl);
							gct.recordDynamicCode(sdsl);
						}
					}
				}
			}
		}
		return found;
	}
	
	public ArrayList<SDSL> getKnownSDSLInstances() {
		if (knownSDSLInstances == null) findSDSLInstances();
		return knownSDSLInstances;
	}
	
	public synchronized SDSL addDefaultSDSLInstance() {
		if (knownSDSLInstances == null) findSDSLInstances();
		Line[] code = {new Line("28708CEB 00000001"),
					new Line("4A000000 90180F06"),
					new Line("14000076 FF0026FC"),
					new Line("E0000000 80008000")};
		SDSL sdsl = new SDSL(code);
		gct.addDynamicCode(sdsl);
		knownSDSLInstances.add(sdsl);
		return sdsl;
	}

	public void deleteSDSLInstance(SDSL sdsl) {
		gct.deleteDynamicCode(sdsl);
	}

	private class StagePanel extends JPanel {
		private static final long serialVersionUID = 1L; // eclipse wants this here

		public StagePanel(String label,
				final JTextField stageID, final JComboBox<String> stageList,
				final List<Integer> stageIDSource, final List<String> stageListSource) {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			
			add(new JLabel(label));
			if (sdsl == null) {
				stageID.setEnabled(false);
			} else {
				stageID.setText(Integer.toString(sdsl.getStageID(), 16));
			}
			stageID.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent arg0) {}
				public void focusLost(FocusEvent arg0) {
					edited[0] = true;
					/* When the stage ID box loses focus, update the list */
					int index = stageIDSource.indexOf(Integer.parseInt(stageID.getText(), 16));
					if (index >= 0) {
						stageList.setSelectedIndex(index);
					} else {
						/* not present in the list? show the first element - the empty string */
						stageList.setSelectedIndex(0);
					}
				}
			});
			for (String s : stageListSource) {
				stageList.addItem(s);
			}
			stageList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					edited[0] = true;
					/* When the stage list is changed, update the ID box */
					int index = stageList.getSelectedIndex();
					if (index != 0) stageID.setText( // But don't change the text box if the first item (empty) is selected
							Integer.toString(stageIDSource.get(index), 16));
				}
			});
			add(stageList);
			add(stageID);
		}
	}

}
