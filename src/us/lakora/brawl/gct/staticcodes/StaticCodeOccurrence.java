package us.lakora.brawl.gct.staticcodes;

import java.util.Collection;
import java.util.ArrayList;

import us.lakora.brawl.gct.Code;
import us.lakora.brawl.gct.Line;

public class StaticCodeOccurrence extends Code implements Comparable<StaticCodeOccurrence> {
	
	private StaticCode code;
	private ArrayList<Line> lines;
	private int foundAt; // used for sorting
	
	public StaticCodeOccurrence(StaticCode code, Collection<? extends Line> lines) {
		this.code = code;
		this.lines = new ArrayList<Line>(lines);
		this.foundAt = Integer.MAX_VALUE;
	}
	
	public StaticCodeOccurrence(StaticCode code, Collection<? extends Line> lines, int foundAt) {
		this(code, lines);
		this.foundAt = foundAt;
	}
	
	public StaticCode getCode() {
		return code;
	}

	@Override
	public Line[] getLineArray() {
		return lines.toArray(new Line[0]);
	}

	@Override
	public int compareTo(StaticCodeOccurrence o) {
		return foundAt - o.foundAt;
	}

	@Override
	public String description() {
		return code.toString();
	}

}
