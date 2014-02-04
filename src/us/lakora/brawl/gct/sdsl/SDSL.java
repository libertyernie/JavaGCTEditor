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

	public void setSongID(int val) {
		short sh = (short)val;
		lines[2].data[6] = (byte)(sh / 256);
		lines[2].data[7] = (byte)(sh % 256);
	}
	public void setSongID(String s) {
		short sh = (short)Integer.parseInt(s, 16);
		lines[2].data[6] = (byte)(sh / 256);
		lines[2].data[7] = (byte)(sh % 256);
	}
	
	public String toString() {
		IDLists.Song song = IDLists.songFor(getSongID());
		String songfilename = song != null ? song.filename : '('+Integer.toString(getSongID(), 16)+')';
		IDLists.Stage stage = IDLists.stageFor(getStageID());
		String stagename = stage != null ? stage.name : '('+Integer.toString(getStageID(), 16)+')';
		
		return stagename + "->" + songfilename;
	}
	
	public String description() {
		IDLists.Song song = IDLists.songFor(getSongID());
		String songdesc = song != null ? song.toString() : '('+Integer.toString(getSongID(), 16)+')';
		IDLists.Stage stage = IDLists.stageFor(getStageID());
		String stagename = stage != null ? stage.name : '('+Integer.toString(getStageID(), 16)+')';
		
		return stagename + ": always play " + songdesc + " [Oshtoby]";
	}
	
	public Line getStageLinePointer() {
		return lines[0];
	}

}
