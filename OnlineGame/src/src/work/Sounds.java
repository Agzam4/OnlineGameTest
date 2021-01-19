package work;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sounds {
			
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
				clip.loop(-1); // while(true) { play music }
			    clip.start();
			} catch (IOException e) {
			}
		} catch (LineUnavailableException e1) {
		}
	}	
}
