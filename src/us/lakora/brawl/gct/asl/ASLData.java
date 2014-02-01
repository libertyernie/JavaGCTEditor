package us.lakora.brawl.gct.asl;

import java.util.ArrayList;
import java.util.List;

import us.lakora.brawl.gct.Line;
import us.lakora.brawl.gct.DynamicCode;

public class ASLData extends DynamicCode {
	
	private static class StageData {
		private String name;
		private int buttonActivatorCount;
		private int randomCount;
		private List<ButtonActivator> buttonActivators;
		
		public StageData(Line l) {
			byte[] sub = new byte[4];
			System.arraycopy(l.data, 0, sub, 0, 4);
			name = new String(sub);
			buttonActivatorCount = l.data[4];
			randomCount = l.data[4];
			buttonActivators = new ArrayList<ButtonActivator>(buttonActivatorCount);
		}
		
		public String toString() {
			return name + ": " + buttonActivatorCount + " button activators, " + randomCount + " random";
		}
	}
	
	private static class ButtonActivator {
		private int buttons;
		private char letter;
		
		public ButtonActivator(Line l) {
			buttons = l.data[0] * 0x100 + l.data[1];
			letter = (char)(l.data[3] + 'A');
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("_" + letter + ": ");
			if (buttons == 0) {
				sb.append("no buttons+");
			} else {
				if ((buttons & 0x0001) != 0) sb.append("left+");
				if ((buttons & 0x0002) != 0) sb.append("right+");
				if ((buttons & 0x0004) != 0) sb.append("down+");
				if ((buttons & 0x0008) != 0) sb.append("up+");
				if ((buttons & 0x0010) != 0) sb.append("Z+");
				if ((buttons & 0x0020) != 0) sb.append("R+");
				if ((buttons & 0x0040) != 0) sb.append("L+");
				if ((buttons & 0x0100) != 0) sb.append("A+");
				if ((buttons & 0x0200) != 0) sb.append("B+");
				if ((buttons & 0x0400) != 0) sb.append("X+");
				if ((buttons & 0x0800) != 0) sb.append("Y+");
				if ((buttons & 0x1000) != 0) sb.append("Start+");
				if ((buttons & 0x2000) != 0) sb.append("ZL+");
				if ((buttons & 0x4000) != 0) sb.append("ZR+");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
	}

	private Line[] lines;
	private List<StageData> stageData;
	
	public Line[] getLineArray() {
		return lines;
	}
	
	public ASLData(List<Line> code) {
		lines = code.toArray(new Line[0]);
		stageData = new ArrayList<StageData>();
		for (int i=3; i<lines.length-2; i++) {
			StageData sd = new StageData(lines[i]);
			for (int j=0; j<sd.buttonActivatorCount; j++) {
				i++;
				ButtonActivator ba = new ButtonActivator(lines[i]);
				sd.buttonActivators.add(ba);
			}
			stageData.add(sd);
		}
	}
	
	public String toString() {
		return description();
	}
	
	public String description() {
		return "Alternate Stage Loader Data v1.1";
	}
	
	public String longform() {
		StringBuilder sb = new StringBuilder();
		sb.append(description() + "\n");
		for (StageData sd : stageData) {
			sb.append("* " + sd + "\n");
			for (ButtonActivator ba : sd.buttonActivators) {
				sb.append("  * " + ba + "\n");
			}
		}
		return sb.toString();
	}

}
