package us.lakora.brawl.gct;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TXTExportOptionsDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public static enum Method {
		RAW,
		SAME_ORDER,
		KNOWN_FIRST,
		NONE_SELECTED,
	}
	
	private JRadioButton optRaw, optSameOrder, optKnownFirst;
	private JCheckBox sortedByOrigOrder, sdslAfter;
	private JButton ok, cancel;
	public boolean dialogResult;
	
	public TXTExportOptionsDialog() {
		dialogResult = false;
		setLayout(new GridLayout(6,1));
		setModalityType(ModalityType.APPLICATION_MODAL);

		optRaw = new JRadioButton("Raw data export (single code)");
		add(optRaw);
		optSameOrder = new JRadioButton("Split codes, keep ordering");
		add(optSameOrder);
		optKnownFirst = new JRadioButton("Static codes 1st; dynamic codes 2nd; remainder 3rd", true);
		add(optKnownFirst);

		ButtonGroup group = new ButtonGroup();
		group.add(optRaw);
		group.add(optSameOrder);
		group.add(optKnownFirst);
		
		sortedByOrigOrder = new JCheckBox("Sort static codes by original GCT order", true);
		add(sortedByOrigOrder);
		sdslAfter = new JCheckBox("Put SDSL codes at end of file", false);
		add(sdslAfter);
		
		ActionListener updateCheckBox = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean b = optKnownFirst.isSelected();
				sortedByOrigOrder.setEnabled(b);
				sdslAfter.setEnabled(b);
			}
		};
		optRaw.addActionListener(updateCheckBox);
		optSameOrder.addActionListener(updateCheckBox);
		optKnownFirst.addActionListener(updateCheckBox);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		add(buttons);
		
		ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogResult = true;
				setVisible(false);
			}
		});
		buttons.add(ok);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		buttons.add(cancel);
		
		this.setTitle("Export");
		pack();
	}
	
	public Method getSelectedMethod() {
		if (optRaw.isSelected()) return Method.RAW;
		if (optSameOrder.isSelected()) return Method.SAME_ORDER;
		if (optKnownFirst.isSelected()) return Method.KNOWN_FIRST;
		return Method.NONE_SELECTED;
	}
	
	public boolean shouldBeSortedByOrigOrder() {
		return sortedByOrigOrder.isSelected();
	}
	
	public boolean shouldBeSDSLAfter() {
		return sdslAfter.isSelected();
	}

}
