package us.lakora.brawl.gct.staticcodes;

import java.util.Collection;
import java.util.ArrayList;

import us.lakora.brawl.gct.Line;

public class StaticCodeOccurrence implements Comparable<StaticCodeOccurrence> {
	
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
	
	/*
	 * Makes a copy of this object's line list, containing the same instances of the Line class.
	 */
	public ArrayList<Line> getLines() {
		return new ArrayList<Line>(lines);
	}

	@Override
	public int compareTo(StaticCodeOccurrence o) {
		return foundAt - o.foundAt;
	}

}
