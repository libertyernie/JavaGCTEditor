package us.lakora.brawl.gct;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Converter {

	public static void convert(String[] args) {
		
		if (args.length != 2) {
			System.err.println("Usage: java -jar [jarfile] file.gct file.txt");
			System.exit(1);
		} else try {
			GCT gct = new GCT(args[0]);
			BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
			bw.write(gct.toString());
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}
