package us.lakora.brawl.gct.csv;

import us.lakora.brawl.gct.sdsl.IDLists;
import us.lakora.brawl.gct.sdsl.IDLists.Song;

public class VolumeSetting {
	public int songID; // less than 0x8000
	public int volume; // less than 128
	public String song;
	
	public VolumeSetting(byte[] src, int offset) {
		songID = src[offset + 0] * 0x100 + src[offset + 1];
		volume = src[offset + 3];
		
		Song song = IDLists.songFor(songID);
		this.song = song == null
				? ("{Song ID: " + Integer.toString(songID, 16) + "}")
				: song.toString();
	}
	
	public String toString() {
		return volume + " (" + (volume/128.0) + ") - " + song;
	}
}