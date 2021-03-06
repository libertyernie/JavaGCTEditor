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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.Line;

public class SDSLEditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final boolean SAVE_FIELDS_ON_CHANGE = true;
	
	private SDSL sdsl;
	private GCT gct;
	
	/*private JTextField stageID;
	private JComboBox<IDLists.NameAndID> stageList;
	private JTextField songID;
	private JComboBox<IDLists.NameAndID> songList;*/
	private IDRow stageRow, songRow;
	
	private JList<SDSL> selector;
	private DefaultListModel<SDSL> selectorModel;
	private JButton delete;
	private ArrayList<SDSL> knownSDSLInstances;
	
	/**
	 * Stores whether the values shown have changed in the GUI since the file was last opened or saved.
	 */
	private boolean[] edited;
	
	public SDSLEditorPanel(GCT gctarg, boolean[] edited) {
		this.gct = gctarg;
		this.edited = edited;
		
		setLayout(new BorderLayout());
		
		delete = new JButton("Del");
		JButton add = new JButton("Add");
		JButton save = new JButton("Update");
		
		/* Populate the box */
		selectorModel = new DefaultListModel<SDSL>();
		selector = new JList<SDSL>(selectorModel);
		selector.setLayoutOrientation(JList.VERTICAL);
		for (SDSL s : getKnownSDSLInstances()) {
			selectorModel.addElement(s);
		}
		selector.setFixedCellWidth(150);
		/* Set the sdsl variable, if the gct has sdsl codes in it */
		if (selectorModel.size() > 0) {
			this.sdsl = selectorModel.get(0);
			selector.setSelectedIndex(0);
		}
		
		/* When the selector is changed, update the other boxes to show the new code */
		selector.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (SAVE_FIELDS_ON_CHANGE) {
					update();
				}
				sdsl = selector.getSelectedValue();
				initializeFields(); // update the boxes
			}
		});
		
		delete.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				if (sdsl != null) delete(sdsl);
			}
		});
		
		add.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update();
				selector.repaint();
			}
		});
		
		add(new JScrollPane(selector, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.WEST);
		
		/* Create the main panel, split into north/south */
		JPanel main = new JPanel(new BorderLayout());
		add(main, BorderLayout.CENTER);
		
		/* North panel */
		Box north = new Box(BoxLayout.Y_AXIS);
		main.add(north, BorderLayout.NORTH);
		
		/* the "stage" row */
		stageRow = new IDRow("Stage: ", IDLists.stages);
		north.add(stageRow);
		
		/* the "song" row */
		songRow = new IDRow("Song:  ", IDLists.songs);
		north.add(songRow);
		
		/* Create the selectorBox - a box with buttons to work with the selector list */
		Box selectorBox = new Box(BoxLayout.X_AXIS);
		selectorBox.add(delete);
		selectorBox.add(add);
		selectorBox.add(Box.createHorizontalGlue());
		selectorBox.add(save);
		main.add(selectorBox, BorderLayout.SOUTH);
		
		initializeFields();
	}
	
	private void delete(SDSL sdsl) {
		selectorModel.removeElement(sdsl);
		deleteSDSLInstance(sdsl);
		if (selectorModel.size() == 0) {
			delete.setEnabled(false);
		}
	}
	
	private void add() {
		SDSL sdsl = addDefaultSDSLInstance();
		selectorModel.addElement(sdsl);
		selector.setSelectedValue(sdsl, true);
		delete.setEnabled(true);
	}
	
	/**
	 * Updates the lines of code stored in memory, using the SDSL object.
	 */
	public void update() {
		if (sdsl != null) {
			int oldStageID = sdsl.getStageID();
			int oldSongID = sdsl.getSongID();
			sdsl.setStageID(stageRow.getID());
			sdsl.setSongID(songRow.getID());
			if (oldStageID != sdsl.getStageID() || oldSongID != sdsl.getSongID()) {
				edited[0] = true;
			}
		}
	}
	
	private void initializeFields() {
		if (sdsl == null) {
			stageRow.setEnabled(false);
			songRow.setEnabled(false);
		} else {
			stageRow.setID(sdsl.getStageID());
			songRow.setID(sdsl.getSongID());
			stageRow.setEnabled(true);
			songRow.setEnabled(true);
		}
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

	private class IDRow extends JPanel {
		private static final long serialVersionUID = 1L; // eclipse wants this here
		
		private JTextField stageID;
		private JComboBox<IDLists.NameAndID> stageList;
		private List<? extends IDLists.NameAndID> stageSource;

		public IDRow(String label, List<? extends IDLists.NameAndID> ss) {
			stageID = new JTextField();
			stageList = new JComboBox<IDLists.NameAndID>();
			this.stageSource = ss;
			setLayout(new BorderLayout());
			
			if (sdsl == null) {
				stageID.setEnabled(false);
			} else {
				stageID.setText(Integer.toString(sdsl.getStageID(), 16));
			}
			stageID.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent arg0) {}
				public void focusLost(FocusEvent arg0) {
					/* When the stage ID box loses focus, update the list */
					textBoxToList();
				}
			});
			for (IDLists.NameAndID s : stageSource) {
				stageList.addItem(s);
			}
			stageList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					/* When the stage list is changed, update the ID box */
					int index = stageList.getSelectedIndex();
					if (index != 0) { // But don't change the text box if the first item (empty) is selected
						stageID.setText(Integer.toString(stageSource.get(index).id, 16));
					}
				}
			});
			
			Box leftmost = new Box(BoxLayout.X_AXIS);
			leftmost.add(new JLabel(label));
			leftmost.add(stageID);
			leftmost.setPreferredSize(new Dimension(80,16));
			add(leftmost, BorderLayout.WEST);
			add(stageList, BorderLayout.CENTER);
		}

		public int getID() {
			return Integer.parseInt(stageID.getText(), 16);
		}
		public void setID(int id) {
			stageID.setText(Integer.toString(id, 16));
			textBoxToList();
		}
		
		public boolean isValidID() {
			return stageList.getSelectedIndex() > 0;
		}
		
		private void textBoxToList() {
			int id = Integer.parseInt(stageID.getText(), 16);
			stageList.setSelectedIndex(0); // fallback
			for (int i=0; i<stageSource.size(); i++) {
				if (stageSource.get(i).id == id) {
					stageList.setSelectedIndex(i);
					break;
				}
			}
		}
	}

}
