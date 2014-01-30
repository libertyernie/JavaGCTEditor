package us.lakora.brawl.gct.sss;

import java.util.List;

import us.lakora.brawl.gct.Line;
import us.lakora.brawl.gct.DynamicCode;

public class SSS extends DynamicCode {

	private Line[] lines;
	
	private int brawl, melee, stages;
	
	public Line[] getLineArray() {
		return lines;
	}
	
	public SSS(List<Line> code, List<Integer> skipCounts) {
		lines = code.toArray(new Line[0]);
		brawl = skipCounts.get(0);
		melee = skipCounts.get(1);
		stages = skipCounts.get(2) / 2;
	}
	
	public int getBrawl() {
		return brawl;
	}
	public int getMelee() {
		return melee;
	}
	public int getStages() {
		return stages;
	}
	
	public String toString() {
		return description();
	}
	
	public String description() {
		return "Custom SSS: " + brawl + "/" + melee + " stages, " + stages + " total stages defined";
	}

}
