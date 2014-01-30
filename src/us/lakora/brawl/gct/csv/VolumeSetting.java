package us.lakora.brawl.gct.csv;

import us.lakora.brawl.gct.sdsl.IDLists;

public class VolumeSetting {
	public int songID; // less than 0x8000
	public int volume; // less than 128
	public String song;
	
	public VolumeSetting(byte[] src, int offset) {
		songID = src[offset + 0] * 0x100 + src[offset + 1];
		volume = src[offset + 3];
		
		int i = IDLists.songIDList.indexOf(songID);
		song = i < 0 ? "error" : IDLists.songList.get(i);
	}
	
	public String toString() {
		return volume + " (" + (volume/128.0) + ") - " + song;
	}
}