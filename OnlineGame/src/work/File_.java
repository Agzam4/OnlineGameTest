package work;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class File_ {
	
	
	public String ReadFile(String name) throws IOException {
		String string = "";
		
		byte[] all = Files.readAllBytes(Paths.get(name));
		string = new String(all);
		return string;
	}
	
	public boolean CanReadFile(String name) {
		String string = "";
		try(FileReader reader = new FileReader(name)) {
            int c;
            while((c=reader.read())!=-1){
            	string = string + (char)c;
            }
            return true;
        }
        catch(IOException ex){
        	return false;
        }
		
	}
	
	public void WriteFile(String filename, String text) throws IOException {
		
		try (FileWriter writer = new FileWriter(new File(filename))) {
			writer.write(text);
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public File[] GetDir(String dir) {
		File f = new File(dir);
		return (f.isDirectory()) ? f.listFiles() : new File[] {f};
	}
	
	public String[] GetStringNamesDir(String dir) {
		File[] f = GetDir(dir);
		String[] s = new String[f.length];
		for (int i = 0; i < s.length; i++) {
			String fn = f[i].getName();
			s[i] = f[i].isDirectory() ? fn : fn.substring(0, fn.lastIndexOf("."));
		}
		return s;
	}
	
	public int CountOfFiles() {
		int i = 0;
		boolean can = true;
		while (can) {
			try {
				can = CanReadFile(System.getProperty("user.dir") + "\\files\\names\\" + i + ".name");
			} catch (Exception e) {
				can = false;
			}
			i++;
			
		}
		return i-2;
	}
	
	public void SaveImage(String p, BufferedImage bufferedImage2) throws IOException {
		BufferedImage bufferedImage = bufferedImage2;
		byte[] currentImage = null;
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			baos.flush();
			currentImage = baos.toByteArray();
			baos.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}   
		
		FileOutputStream newFile = new FileOutputStream(p);
				newFile.write(currentImage);
				newFile.close();
	}
	
}
