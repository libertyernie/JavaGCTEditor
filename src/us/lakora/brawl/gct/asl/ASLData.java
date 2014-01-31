package us.lakora.brawl.gct.asl;

import java.util.List;

import us.lakora.brawl.gct.Line;
import us.lakora.brawl.gct.DynamicCode;

public class ASLData extends DynamicCode {

	private Line[] lines;
	
	public Line[] getLineArray() {
		return lines;
	}
	
	public ASLData(List<Line> code) {
		lines = code.toArray(new Line[0]);
	}
	
	public String toString() {
		return description();
	}
	
	public String description() {
		return "Alternate Stage Loader Data v1.1";
	}

}
