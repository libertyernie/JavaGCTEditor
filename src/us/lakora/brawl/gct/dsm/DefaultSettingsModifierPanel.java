package us.lakora.brawl.gct.dsm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListIterator;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.Line;

public class DefaultSettingsModifierPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GCT gct;
	private boolean[] edited;
	private JCheckBox useCode;
	private DSM dsm;
	
	private JComboBox gameType;
	private SpinnerNumberModel stock;
	private SpinnerNumberModel time;
	private SpinnerNumberModel stocktime;
	private JComboBox handicap;
	private SpinnerNumberModel damageRatio;
	private JComboBox stageMethod;
	private JCheckBox teamAttack, pause, scoreDisplay, damageGauge;
	
	private JPanel container;

	public DefaultSettingsModifierPanel(GCT gctarg, boolean[] editedarg) {
		gct = gctarg;
		edited = editedarg;

		setLayout(new BorderLayout());

		useCode = new JCheckBox("Include Default Settings Modifier", findDSMInstance());
		useCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				edited[0] = true;
				if (dsm != null) {
					gct.deleteDynamicCode(dsm);
					dsm = null;
				} else {
					dsm = new DSM();
					gct.addDynamicCode(dsm);
				}
				initialize();
			}
		});

		add(useCode, BorderLayout.NORTH);
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JPanel[] rows = {new JPanel(), new JPanel(), new JPanel()};
		for (JPanel p : rows) {
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			container.add(p);
		}
		add(container, BorderLayout.CENTER);

		gameType = new JComboBox();
		gameType.addItem("Time");
		gameType.addItem("Stock");
		gameType.addItem("Coin");
		gameType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setGameType((byte)gameType.getSelectedIndex());
			}
		});
		rows[0].add(new JLabel("Game Type: "));
		rows[0].add(gameType);

		time = new SpinnerNumberModel(
				4,
				0,
				99,
				1);
		time.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				byte b = time.getNumber().byteValue();
				dsm.setTimeLimit(b);
				edited[0] = true;
			}
		});
		rows[0].add(new JLabel("Time (min): "));
		rows[0].add(new JSpinner(time));
		
		stock = new SpinnerNumberModel(
				4,
				1,
				99,
				1);
		stock.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				byte b = stock.getNumber().byteValue();
				dsm.setStock(b);
				edited[0] = true;
			}
		});
		rows[0].add(new JLabel("Stock: "));
		rows[0].add(new JSpinner(stock));
		
		stocktime = new SpinnerNumberModel(
				8,
				0,
				99,
				1);
		stocktime.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				byte b = stocktime.getNumber().byteValue();
				dsm.setStockTimeLimit(b);
				edited[0] = true;
			}
		});
		rows[0].add(new JLabel("Stock-mode time: "));
		rows[0].add(new JSpinner(stocktime));
		
		handicap = new JComboBox();
		handicap.addItem("Off");
		handicap.addItem("Auto");
		handicap.addItem("On");
		handicap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setHandicap((byte)handicap.getSelectedIndex());
				edited[0] = true;
			}
		});
		rows[1].add(new JLabel("Handicap: "));
		rows[1].add(handicap);
		
		damageRatio = new SpinnerNumberModel(
				1.0,
				0.5,
				2.0,
				.1);
		damageRatio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				double d = 10*damageRatio.getNumber().doubleValue();
				dsm.setDamageRatio(d);
				edited[0] = true;
			}
		});
		rows[1].add(new JLabel("Damage ratio: "));
		rows[1].add(new JSpinner(damageRatio));
		
		stageMethod = new JComboBox();
		stageMethod.addItem("Choose");
		stageMethod.addItem("Random");
		stageMethod.addItem("Take turns");
		stageMethod.addItem("Ordered");
		stageMethod.addItem("Loser's pick");
		stageMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setStageMethod((byte)stageMethod.getSelectedIndex());
				edited[0] = true;
			}
		});
		rows[1].add(new JLabel("Stage method: "));
		rows[1].add(stageMethod);
		
		teamAttack = new JCheckBox("Team attack");
		teamAttack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setTeamAttack(teamAttack.isSelected());
				edited[0] = true;
			}
		});
		rows[2].add(teamAttack);
		
		pause = new JCheckBox("Pause");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setPause(pause.isSelected());
				edited[0] = true;
			}
		});
		rows[2].add(pause);
		
		scoreDisplay = new JCheckBox("scoreDisplay");
		scoreDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setScoreDisplay(scoreDisplay.isSelected());
				edited[0] = true;
			}
		});
		rows[2].add(scoreDisplay);
		
		damageGauge = new JCheckBox("damageGauge");
		damageGauge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dsm.setDamageGauge(damageGauge.isSelected());
				edited[0] = true;
			}
		});
		rows[2].add(damageGauge);

		initialize();
	}
	
	private void initialize() {
		if (useCode.isSelected()) {
			if (dsm == null) {
				dsm = new DSM();
			}
		} else {
			dsm = null;
		}
		
		boolean dsmCodeExists = (dsm != null);
		if (dsmCodeExists) {
			gameType.setSelectedIndex(dsm.getGameType());
			time.setValue(dsm.getTimeLimit());
			stock.setValue(dsm.getStock());
			stocktime.setValue(dsm.getStockTimeLimit());
			handicap.setSelectedIndex(dsm.getHandicap());
			damageRatio.setValue(dsm.getDamageRatio()/10.0);
			stageMethod.setSelectedIndex(dsm.getStageMethod());
			teamAttack.setSelected(dsm.getTeamAttack());
			pause.setSelected(dsm.getPause());
			scoreDisplay.setSelected(dsm.getScoreDisplay());
			damageGauge.setSelected(dsm.getDamageGauge());
		}
		for (Component c : container.getComponents()) {
			JPanel row = (JPanel)c;
			for (Component component : row.getComponents()) {
				component.setEnabled(dsmCodeExists);
			}
		}
	}

	private boolean findDSMInstance() {
		int index;
		if (dsm == null) {
			index = -1;
			int i = 0;
			ListIterator<Line> it = gct.getCodeLines().listIterator();
			Line[] finalCode = new Line[9];
			int pointInCode = 0;
			while (it.hasNext()) {
				Line l = it.next();
				if (!l.startsWith(DSM.SEARCH[pointInCode])) {
					pointInCode = 0;
				} else {
					finalCode[pointInCode] = l;
					pointInCode++;
					if (pointInCode == DSM.SEARCH.length) {
						if (index == -1) {
							index = new Integer(i - DSM.SEARCH.length + 1);
							dsm = new DSM(finalCode);
							gct.recordDynamicCode(dsm);
						} else {
							System.err.println("Error: the Default Settings Modifier code was found more than once in the GCT. The second instance will be ignored.");
						}
						pointInCode = 0; // reset counter
					}
				}
				i++;
			}
		}
		return (dsm != null);
	}
	
	public String toString() {
		return "Default Settings Modifier Panel\n" + dsm;
	}

}
