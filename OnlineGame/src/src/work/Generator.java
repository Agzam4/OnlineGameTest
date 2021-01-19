package work;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.DebugInfo;

public class Generator {
	public static int W;
	public static int H;
	

	public int[][] Generate(int id) {
		File dir = new File(System.getProperty("user.dir") + "\\data\\maps");
		File_ file = new File_();
		String s;
		try {
			s = file.ReadFile(dir.listFiles()[id] + "");
		} catch (IOException e) {
			DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			s = " ";
		}
		
		String[] lines = s.split("\n");

		int w = lines[0].length()-1;
		int h = lines.length;
		
		W = w;
		H = h;
		
		int[][] map = new int[w][h];
		ArrayList<Character> characters = new ArrayList<Character>();
		characters.add(' ');  // ID: 0
		characters.add('#');  // ID: 1
		characters.add('_');  // ID: 2
		characters.add('/');  // ID: 3
		characters.add('\\'); // ID: 4
		characters.add('Y');  // ID: 5
		characters.add('|');  // ID: 6
		characters.add('\''); // ID: 7

		for (int y = 0; y < h; y++) {
			char[] cs = lines[y].toCharArray();
			for (int x = 0; x < cs.length-1; x++) {
				int io = characters.indexOf(cs[x]);
				map[x][y] = (io == -1 || io > 7) ? 0 : io;
			}
		}
		return map;
	}
	
	public void SetSize(int w, int h) {
		W = w;
		H = h;
	}
}
