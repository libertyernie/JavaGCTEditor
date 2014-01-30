package us.lakora.brawl.gct;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TXTExportOptions extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JCheckBox separateCodes, sdslAfter;
	private JButton ok, cancel;
	public boolean dialogResult;

	public boolean getSeparateCodes() {
		return separateCodes.isSelected();
	}
	public boolean getSdslAfter() {
		return sdslAfter.isEnabled() && sdslAfter.isSelected();
	}
	
	public void setCanCancel(boolean value) {
		cancel.setEnabled(value);
		setDefaultCloseOperation(value ? JFrame.DISPOSE_ON_CLOSE : JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public TXTExportOptions() {
		dialogResult = false;
		setLayout(new GridLayout(3,1));
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		separateCodes = new JCheckBox("Separate known codes from main block", true);
		separateCodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sdslAfter.setEnabled(separateCodes.isSelected());
			}
		});
		add(separateCodes);
		sdslAfter = new JCheckBox("Put SDSL codes at end of file", false);
		add(sdslAfter);
		
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
		
		pack();
	}

}
