package us.lakora.brawl.gct.sdsl;

import java.util.Arrays;

import us.lakora.brawl.gct.Line;
import us.lakora.brawl.gct.DynamicCode;

/**
 * Stage-Dependent Song Loader
 * by Oshtoby
 */
public class SDSL extends DynamicCode {
	
	private Line[] lines;
	
	public Line[] getLineArray() {
		return lines;
	}
	
	public SDSL(Line[] array) {
		super(Arrays.asList(array));
		if (array.length != 4) {
			throw new IndexOutOfBoundsException("Stage-Dependent Song Loader must be 4 lines");
		}
		lines = array;
	}
	
	public int getStageID() {
		return (lines[0].data[7]+256)%256;
	}
	
	public int getSongID() {
		int b1 = (lines[2].data[6]+256)%256;
		int b2 = (lines[2].data[7]+256)%256;
		return 256*b1+b2;
	}
	
	public void setStageID(String s) {
		byte b = (byte)Integer.parseInt(s, 16);
		lines[0].data[7] = b;
	}
	public void setStageID(int i) {
		lines[0].data[7] = (byte)i;
	}
	
	public void setSongID(String s) {
		short sh = (short)Integer.parseInt(s, 16);
		lines[2].data[6] = (byte)(sh / 256);
		lines[2].data[7] = (byte)(sh % 256);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(20);
		sb.append("Stage: ");
		sb.append(Integer.toString(getStageID(), 16));
		sb.append(" Song: ");
		sb.append(Integer.toString(getSongID(), 16));
		return sb.toString();
	}
	
	public String description() {
		String song = IDLists.songList.get(IDLists.songIDList.indexOf(getSongID()));
		String stage = IDLists.stageList.get(IDLists.stageIDList.indexOf(getStageID()));
		return stage + ": always play " + song + " [Oshtoby]";
	}
	
	public Line getStageLinePointer() {
		return lines[0];
	}
	
//	public int hashCode() {
//		byte stageid = stageLinePointer.data[7];
//		byte b1 = songLinePointer.data[6];
//		byte b2 = songLinePointer.data[7];
//		return (stageid*65536 + b1*256 + b2);
//	}
//	
//	public boolean equals(Object o) {
//		SDSL s = (SDSL)o;
//		return (s.hashCode() == hashCode());
//	}

}
