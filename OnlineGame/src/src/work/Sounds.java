package work;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sounds {

//	public static final int BG_MUSIC_1 = 0;
//	public static final int BG_MUSIC_2 = 1;
	
//	private static AudioInputStream[] audioip = InitAIS();
//	
//	private static AudioInputStream[] InitAIS() {
//		File_ file = new File_();
//		
//		File[] files = file.GetDir(System.getProperty("user.dir") + "\\data\\datapacks");
//		AudioInputStream[] audioip = new AudioInputStream[files.length];
//		for (int i = 0; i < audioip.length; i++) {
//			try {
//				audioip[i] = AudioSystem.getAudioInputStream(
//						new File(files[i] + "\\BG" + i + ".wav").getAbsoluteFile());
//			} catch (UnsupportedAudioFileException | IOException e) {
//				System.err.println("ID: " + i);
//				System.err.println(System.getProperty("user.dir") + "\\" + i + ".wav");
//				e.printStackTrace();
//			}
//		}
//		return audioip;
//	}

			
	private static Clip getClip() {
		try {
			return AudioSystem.getClip();
		} catch (LineUnavailableException e) {
		}
		return null;
	}
	private static Clip clip = getClip();
	
	public static void stop() {
		clip.close();
		clip.stop();
		clip.flush();
	}
	
	public static void play(String tp) {
		AudioInputStream ais;
		try {
			ais = AudioSystem.getAudioInputStream(
					new File(System.getProperty("user.dir") + "\\data\\datapacks\\" + tp + "\\BG.wav").getAbsoluteFile());
		} catch (UnsupportedAudioFileException | IOException e2) {
			return;
		}
		if(clip.isOpen())
			return;
		try {
			try {
				clip.open(ais);
				clip.loop(-1);
			    clip.start();
			} catch (IOException e) {
			}
		} catch (LineUnavailableException e1) {
		}
	}	
}
