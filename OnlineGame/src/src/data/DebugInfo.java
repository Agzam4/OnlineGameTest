package data;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import work.File_;

public class DebugInfo {
	
	/**
	 * Write all Exception to file
	 */
	public static ArrayList<String> debugMsg = new ArrayList<String>();
	
	public static void createLogOfErrs() {
		File_ file = new File_();
		String txt = "";
//		try {
//			txt = file.ReadFile("debug.debug");
//		} catch (IOException e1) {
			txt = "Welcome to Debug File,"
					+ " here info about starting programm, Exception, Err, and other";
//		}
		for (String string : debugMsg) {
			txt += string;
		}
		
		try {
			file.WriteFile("debug.debug", txt);
		} catch (IOException e) {
		}
	}
	public static String getAllInfo(Exception e) {
		String s = "[" + getDate() + "]\n"  + e.getClass() + ": " + e.getMessage() + "\n"
				+ e.getStackTrace()[2];
		
		for (StackTraceElement string : e.getStackTrace()) {
			s += string;
			s += "\n";
		}
		System.err.println();
		e.printStackTrace();
		return s;
	}
	
	public static String getDate() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
}
