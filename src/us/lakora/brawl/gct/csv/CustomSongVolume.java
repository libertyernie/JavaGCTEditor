package us.lakora.brawl.gct.csv;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import us.lakora.brawl.gct.DynamicCode;
import us.lakora.brawl.gct.Line;
import us.lakora.brawl.gct.sdsl.IDLists;

public class CustomSongVolume extends DynamicCode implements Iterable<VolumeSetting> {

	private Line[] lines;
	
	private List<VolumeSetting> settings;

	public Line[] getLineArray() {
		return lines;
	}
	
	public CustomSongVolume(List<Line> code) {
		lines = code.toArray(new Line[0]);
		settings = new LinkedList<VolumeSetting>();
		
		for (int i=9; i<lines.length; i++) {
			byte[] data = lines[i].data;
			if (isend(data, 0)) {
				break;
			}
			settings.add(new VolumeSetting(data, 0));
			if (isend(data, 4)) {
				break;
			}
			settings.add(new VolumeSetting(data, 4));
		}
	}
	
	private static boolean isend(byte[] b, int offset) {
		return b[offset + 0] == 127
				&& b[offset + 1] == -1
				&& b[offset + 2] == 0
				&& b[offset + 3] == 0;
	}
	
	public int byteCount() {
		return lines[8].data[7] & 0xFF;
	}
	
	public String toString() {
		return description();
	}

	@Override
	public String description() {
		return "Custom Song Volume [standardtoaster, Magus]: " + settings.toString();
	}

	@Override
	public Iterator<VolumeSetting> iterator() {
		return settings.iterator();
	}

}
