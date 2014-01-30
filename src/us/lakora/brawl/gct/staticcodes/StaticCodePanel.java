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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import us.lakora.brawl.gct.GCT;
import us.lakora.brawl.gct.gui.Editor;

public class StaticCodePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private GCT gct;
	private boolean[] edited;
	private final static String FILENAME = "static_codes.txt";
	
	public StaticCodePanel(GCT g, boolean[] b) {
		gct = g;
		edited = b;
		
		File file = new File(FILENAME);
		List<StaticCode> codes;
		try {
			codes = StaticCode.readFile(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Could not read/write "+FILENAME, Editor.TITLE, JOptionPane.ERROR_MESSAGE);
			createStaticCodesFile(file);
			try {
				codes = StaticCode.read(new BufferedReader(new StringReader(STATIC_CODES_DEFAULT)));
			} catch (IOException e1) {
				e1.printStackTrace();
				codes = new ArrayList<StaticCode>(0);
			}
		}
		StaticCodeToggler[] array = new StaticCodeToggler[codes.size()];
		for (int i=0; i<array.length; i++) {
			StaticCode sc = codes.get(i);
			array[i] = new StaticCodeToggler(sc.toString(), sc);
//			System.out.println(sc);
//			for (String s : sc.getStringArray()) {
//				System.out.println(s);
//			}
//			System.out.println();
		}
//		Arrays.sort(array);
		JPanel main = this;
		main.setLayout(new GridLayout(array.length, 1));
		for (Component c : array) {
			main.add(c);
		}
	}
	
//	private class StaticCodeToggler extends JCheckBox implements Comparable<StaticCodeToggler> {
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
		
//		/**
//		 * Used for compareTo function below.
//		 */
//		public String toString() {
//			return sn.toString();
//		}
//
//		@Override
//		/**
//		 * For sorting the checkboxes automatically.
//		 */
//		public int compareTo(StaticCodeToggler arg0) {
//			return toString().compareTo(arg0.toString());
//		}

	}
	
	private static final String STATIC_CODES_DEFAULT = "\n" +
			"RSBE01\n" +
			"Smash Bros Brawl (US)\n" +
			"\n" +
			"File Patch Code v3.5.1 (NTSC-U) [Phantom Wings]\n" +
			"E0000000 80008000\n" +
			"225664EC 00000000\n" +
			"0401BFE0 4858BE20\n" +
			"065A7E00 00000070\n" +
			"38A00067 38810020\n" +
			"3CE0805A 60E37C18\n" +
			"4BE52531 38A0007F\n" +
			"3883FFE8 38610020\n" +
			"4BE52521 38A00068\n" +
			"60E47C18 38610020\n" +
			"9421FF80 BC410008\n" +
			"38610088 4BA74DB9\n" +
			"7C7C1B78 2C030000\n" +
			"4082000C 38210080\n" +
			"4800001C B8410008\n" +
			"38210080 4BE524E5\n" +
			"38610008 4BA742E1\n" +
			"7C7C1B78 4BA741E8\n" +
			"040223E0 48585BC0\n" +
			"065A7FA0 00000028\n" +
			"80010044 3C608001\n" +
			"6063581C 7C001800\n" +
			"4082000C 7FDDC850\n" +
			"3BDEFFE0 93DB0008\n" +
			"4BA7A424 00000000\n" +
			"0401CD0C 4858B1F4\n" +
			"065A7F00 00000038\n" +
			"2C030000 4182000C\n" +
			"4BA7DD51 4BA74E04\n" +
			"80780008 2C030000\n" +
			"41820014 8118000C\n" +
			"7C634214 7C7B1850\n" +
			"48000008 8078000C\n" +
			"4BA74DE0 00000000\n" +
			"043EE9D8 48000014\n" +
			"043EEBD4 48000014\n" +
			"043D8B9C 48000018\n" +
			"043E9B4C 38600000\n" +
			"043E9D38 38600000\n" +
			"043D8C80 60000000\n" +
			"80000000 80406920\n" +
			"80000001 805A7C00\n" +
			"8A001001 00000000\n" +
			"045A7C10 2F525342\n" +
			"045A7C14 452F7066\n" +
			"80000001 805A7B00\n" +
			"8A001001 00000000\n" +
			"065A7B10 0000000F\n" +
			"2F525342 452F7066\n" +
			"2F736F75 6E642F00\n" +
			"041C6CE0 483E0D20\n" +
			"065A7A00 00000028\n" +
			"9421FF80 BC410008\n" +
			"3C60805A 60637B1F\n" +
			"4BE52931 B8410008\n" +
			"38210080 4BE52995\n" +
			"4BC1F2C4 00000000\n" +
			"065A7900 00000078\n" +
			"9421FF80 7C0802A6\n" +
			"9001000C BC810010\n" +
			"9421FF00 7C872378\n" +
			"54B2BA7E 7CD33378\n" +
			"38800000 9081000C\n" +
			"90810010 90610014\n" +
			"90810018 3880FFFF\n" +
			"9081001C 38610020\n" +
			"90610008 7CE43B78\n" +
			"38A00080 4BE529F5\n" +
			"38610008 4BA752A1\n" +
			"60000000 80210000\n" +
			"B8810010 8001000C\n" +
			"7C0803A6 80210000\n" +
			"4E800020 00000000\n" +
			"043E399C 481C3F04\n" +
			"065A78A0 00000010\n" +
			"80BC0020 7CA59214\n" +
			"3A400000 4BE3C0F4\n" +
			"043DBAEC 481CBDE4\n" +
			"065A78D0 00000018\n" +
			"800302A4 2C130000\n" +
			"41820008 7E609B78\n" +
			"3A600000 4BE3420C\n" +
			"041CDF7C 483D9884\n" +
			"065A7800 00000098\n" +
			"818C0014 9421FF80\n" +
			"BC410008 3D009034\n" +
			"61089D94 7C034000\n" +
			"4082003C 7C882378\n" +
			"3C60804D 60630000\n" +
			"3C80805A 60847B00\n" +
			"7CC53378 38C04200\n" +
			"80E40080 2C070000\n" +
			"40820014 60000000\n" +
			"480000B9 2C030000\n" +
			"41820010 B8410008\n" +
			"80210000 4BC26724\n" +
			"70A501FF 3868FFFF\n" +
			"3C80804C 6084FFFF\n" +
			"7C842A14 38A04001\n" +
			"38A5FFFF 8C040001\n" +
			"9C030001 2C050000\n" +
			"4082FFF0 B8410008\n" +
			"80210000 4BC2670C\n" +
			"141CCF90 483DA770\n" +
			"065A7700 00000048\n" +
			"9421FF80 BC410008\n" +
			"7FE3FB78 3C80805A\n" +
			"60847B00 38A00000\n" +
			"38C04000 480001E5\n" +
			"90640080 807F0008\n" +
			"907A0014 907A005C\n" +
			"907A0074 B8410008\n" +
			"80210000 807F0000\n" +
			"4BC25854 00000000\n" +
			"E0000000 80008000\n" +
			"This code is not necessary if using Riivolution, but it is required for file patching on Gecko OS.\n" +
			"If unsure, leave it on.\n" +
			"\n" +
			"Disable Custom Stages 1.1 [Phantom Wings]\n" +
			"046B841C 48000040\n" +
			"Prevents the Stage Select screen from reading Stage Builder data. If you run hacks through the Stage Builder, this is required.\n" +
			"\n" +
			"Enable Random Expansion Stages v1.1 [Phantom Wings]:\n" +
			"C26B7A6C 00000005\n" +
			"3860001F 2C120000\n" +
			"41820008 3860000A\n" +
			"7C101800 41800008\n" +
			"38000001 7C800379\n" +
			"60000000 00000000\n" +
			"This code causes every stage to be included in the random stage select, regardless of what is selected in the random stage select screen.\n" +
			"Only useful if you're using the stage expansion engine.\n" +
			"\n" +
			"Unrestricted Pause Camera\n" +
			"040A7D60 4E800020\n" +
			"04109D88 38800001\n" +
			"\n" +
			"Unrestricted Replay Camera\n" +
			"0409E934 60000000\n" +
			"0409E93C 60000000\n" +
			"0409E9AC FC201890\n" +
			"0409E9B8 FC600090\n" +
			"0409E9BC FC000890\n" +
			"0409E9C8 FC001890\n" +
			"\n" +
			"Unrestricted Control Editing [spunit262]\n" +
			"211973CC 7CC6F82E\n" +
			"051973CC 38C20060\n" +
			"05197468 38620060\n" +
			"05197574 38820060\n" +
			"05197680 38E20060\n" +
			"051976B4 38620060\n" +
			"051978CC 38E20060\n" +
			"065A9380 0000000A\n" +
			"00010203 04050A0B\n" +
			"0C0D0000 00000000\n" +
			"E0000000 80008000\n" +
			"\n" +
			"Infinite Replays\n" +
			"040EB804 60000000\n" +
			"\n" +
			"Save Tags in Replays\n" +
			"0404B140 38A00000\n" +
			"\n" +
			"Reverse Name Sort (Dantarion)\n" +
			"0469F600 7C002051\n";
	
	private static void createStaticCodesFile(File file) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(STATIC_CODES_DEFAULT);
			bw.close();
			JOptionPane.showMessageDialog(null, "Created file "+FILENAME+" with default codes.", Editor.TITLE, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			try {
				if (bw != null) bw.close();
			} catch (IOException e1) {}
		}
	}

}
