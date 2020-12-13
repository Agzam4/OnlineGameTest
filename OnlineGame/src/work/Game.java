package work;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Client.Client;
import Interfaces.Subscriber;
import Models.Message;

public class Game {

	// Game data
	private static final int millis = 50;
	private int[][] map;

	// Keys data
	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;

	// Move data
	private double speed = 7;
	private double goX = 0;
	private double goY = 0;
	private double px = 0;
	private double py = 0;
	private double px2 = -100;
	private double py2 = -100;

	// Drawing data
	private BufferedImage textures[] = new BufferedImage[8];
	private int size = 25;
	private double sx = 0;
	private double sy = 0;

	// Online data
	private String myName;
	private String notMyName = "";
	boolean admin = false;
	boolean initMap = false;
	
	final Client client = new Client("");
	
	public void Go(JPanel ivv, String name, String ip, int ss, int id, String tp, boolean isAdmin) {
		myName = name;
		admin = isAdmin;
		
		String s = client.setUpNetwork(ip, ss);
		JOptionPane.showMessageDialog(null, s);
		if(!s.equals("Successful connection")) {
			return;
		}
		client.addObserver(new MessageHandler());
		
		if(admin) {
			initMap = true;
			Generator generator = new Generator();
			map = generator.Generate(id);
			System.out.println("ADMIN | " + Generator.W + "x" + Generator.H);
		}else {
			initMap = false;
			map = new int[5][5];
			for (int i = 0; i < 5; i++) {
				for (int i1 = 0; i1 < 5; i1++) {
					map[i][i1] = 1;
				}
			}
			Send(client, myName + "&!GETMAP");
		}
		
		
		JLabel iv = new JLabel();
		ivv.add(iv);
		
		for (int i = 0; i < textures.length; i++) {
			try {
				textures[i] =  ImageIO.read(new File(System.getProperty("user.dir")
						+ "\\data\\texturePacks\\" + tp + "\\" + i + ".png"));
			} catch (IOException e) {
				textures[i] = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
				System.err.println("ERR LOADING TEXTURE: " + i);
			}
		}
		
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				
				String newName = name.replaceAll("&!", "&:");

				double pw = 15;
				double ph = 15;
				
				boolean game = true;
				BufferedImage main = 
						new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
				iv.setIcon(new ImageIcon(main));

				int idY = -1;
				int idX = -1;

//				int iw2 = ivv.getWidth()*iw;
//				int ih2 = ivv.getHeight()*ih;

				size = 50;
				
//				if(iw2 > ih2) {
//					size = ivv.getWidth()/iw;
//					System.out.println(size);
//				}else {
//					size = ivv.getHeight()/ih;
//					System.out.println(size);
//				}
				main = new BufferedImage(ivv.getWidth(), ivv.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				iv.setIcon(new ImageIcon(main));

				double sx2 = px*size;
				double sy2 = py*size;
				
				px = size*4;//(Math.random()*iw*size);
				py = size*4;//(Math.random()*ih*size);
				
				while (!initMap) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
					}
				}
				
				int iw = Generator.W;
				int ih = Generator.H;
				
				while (game) {
					
					// Loop Start
					Graphics2D g = (Graphics2D)  main.getGraphics();
					g.setColor(new Color(13,17,23));
					g.fillRect(0, 0, main.getWidth(), main.getHeight());

					sx2 =  (main.getWidth()-pw) / 2;
					sy2 =  main.getHeight()/2;
					final double sxsx = sx + sx2;
					final double sysy = sy + sy2;

					int startX = (int) (-(sx + sx2)/size);
					if(startX < 0)startX = 0;
					int endX = (main.getWidth()/size) + startX + 2;
					if(endX > iw)endX = iw;

					int startY = (int) (-(sy + sy2)/size);
					if(startY < 0)startY = 0;
					int endY = (main.getHeight()/size) + startY + 2;
					if(endY > ih)endY = ih;
					
					for (int x = startX; x < endX; x++) {
						for (int y = startY; y < endY; y++) {
							final int newX = (int) (sxsx + (x*size));
							final int newY = (int) (sysy + (y*size));
								g.drawImage(textures[map[x][y]],
										newX, newY, size, size, null);
								if(map[x][y] == 6) {
									g.drawImage(textures[6], newX, newY+size,
											size, size, null);
								}
						}
					}
					
					g.setColor(Color.GREEN);
					g.fillRect((int)(px + sxsx), (int)(py + sysy), 15, 15);
					g.drawString(myName,
							(int)(px + sxsx + pw/2 - (g.getFontMetrics().stringWidth(myName)/2)),
							(int)(py + sysy) - 5);
					
					if(px2 > 0) {
						g.setColor(Color.RED);
						g.fillRect((int)(px2 + sxsx), (int)(py2 + sysy), 15, 15);
						g.drawString(notMyName,
								(int)((px2 + sxsx) + pw/2 - 
										(g.getFontMetrics().stringWidth(notMyName)/2)),
								(int)(py2+sysy) - 5);
					}
					
					// TODO: MOVE

					if(right) {
						goX += speed;
					}
					if(left) {
						goX -= speed;
					}
					g.setColor(Color.RED);
					if(up) {
						if(GetTile(px+pw, py+ph+2) || GetTile(px, py+ph+2)) {
							goY = -25;
						}
						if(isPlatform(px+pw, py+ph-(size*0.77)+3) ||
								isPlatform(px, py+ph-(size*0.77)+3)) {
							goY = -25;
						}
						if(isAll45PlatformID4(px, py+3, pw, ph)) {
							goY = -25;
						}
						if(isAll45PlatformID3(px, py+3, pw, ph)) {
							goY = -25;
						}
					}
					
					if(down) {
						idY =(int)(py/size);
						idX =(int)(px/size);
					}
					
					boolean fall = idY == (int)(py/size) && idX == (int)(px/size);
					
					if(!fall) {
						idY =-1;
						idX =-1;
					}

					px += goX;
					if(isAll45PlatformID4(px, py, pw, ph) && !fall && goX < 0) {
						goY = goX;
					}
					if(isAll45PlatformID3(px, py, pw, ph) && !fall && goX > 0) {
						goY = -goX;
					}
					if(GetAllTiles(pw, ph)) {
						px -= goX/2;
						if(GetAllTiles(pw, ph)) {
							px -= goX/2;
							goX = 0;
						}
					}
					goX = goX*0.8;
					py += goY;

					g.setColor(Color.CYAN);
					if(TochWallY(px, py, pw, ph, fall)) {
						py -= goY/2;
						if(TochWallY(px, py, pw, ph, fall)) {
							py -= goY/2;
							goY = 0;
						}
					}
					
					goY += 2.5;
					
					sx = (sx + px)*0.9 - px;
					sy = (sy + py)*0.9 - py;
					
					// TODO: send data
					Send(client, newName + "&!#&!" + px + "&!" + py + "&!");
					//((newName.equals(notMyName)) ? newName + "2" : newName));
					g.dispose();
					iv.repaint();
					try{Thread.sleep(millis);
					}catch(InterruptedException e){}
				}
			}

		};
		gameThread.start();
		
		iv.setFocusable(true);
		iv.requestFocus();
		iv.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					right = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					left = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_UP){
					up = false;
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					down = false;
				}
//				key = false;
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					right = true;
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					left = true;
				}
				if(e.getKeyCode() == KeyEvent.VK_UP){
					up = true;
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					down = true;
				}
//				if(e.getKeyCode() == KeyEvent.VK_R){
//					kr = true;
//				}
//				if(e.getKeyCode() == KeyEvent.VK_V){
//					viewMode = !viewMode;
//				}
//				if(e.getKeyCode() == KeyEvent.VK_U){
//					upper = !upper;
//				}
//				if(e.getKeyCode() == KeyEvent.VK_M){
//					// FIXME 
//					fullMap = !fullMap;
//				}
//				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
//					game = false;
//				}
//				key = true;
			}
		});
	}
	
	private boolean TochWallY(double x, double y, double w, double h, boolean fall) {
		return GetAllTiles(w, h) 
				|| // Throw Platform
				goY > 0 && !fall 
				&& // Toch Platform
				((isPlatform(x+w, y+h-(size*0.77)) && isPlatform(x+w, y-(size*0.77)+size-h))||
			    (isPlatform(x, y+h-(size*0.77)) && isPlatform(x, y-(size*0.77)+size-h)))
				|| //Toch Platform "\"
				(goY > 0 && !fall && (isAll45PlatformID4(x, y, w, h)))
				|| //Toch Platform "/"
				(goY > 0 && !fall && (isAll45PlatformID3(x, y, w, h)))
				;
				
//		if(is45Platform(px, py) || is45Platform(px+pw, py) ||
//				is45Platform(px, py+ph) || is45Platform(px+pw, py+ph)) {
//			goY += goX;
//		}
	}

	private boolean GetTile(double px2, double py2) {
		// FIXME
		int nx = (int) (px2/size);
		int ny = (int) (py2/size);
		if(nx > -1 && nx < Generator.W && ny > -1 && ny < Generator.H)
			return 1 == map[nx][ny];
		return true;
	}
	
	private boolean GetAllTiles(double pw, double ph) {
		if(GetTile(px, py) || GetTile(px+pw, py+ph) ||
		GetTile(px, py+ph) || GetTile(px+pw, py)) {
			return true;
		}
		return false;
	}
	
	final boolean[] isPlatform = new boolean[] {false,false,true,false,false,true,false};

	private boolean isPlatform(double px2, double py2) {
		// FIXME
		int nx = (int) (px2/size);
		int ny = (int) (py2/size);
		if(nx > -1 && nx < Generator.W && ny > -1 && ny < Generator.H)
			return isPlatform[map[nx][ny]];
		return true;
	}

	private boolean isAll45PlatformID4(double px2, double py2, double pw, double ph) {
		return is45PlatformID4(px2, py2) || is45PlatformID4(px2+ pw, py2) ||
				is45PlatformID4(px2, py2+ph) || is45PlatformID4(px2+pw, py2+ph);
	}
	
	private boolean isAll45PlatformID3(double px2, double py2, double pw, double ph) {
		return is45PlatformID3(px2, py2) || is45PlatformID3(px2+ pw, py2) ||
				is45PlatformID3(px2, py2+ph) || is45PlatformID3(px2+pw, py2+ph);
	}
	

	private boolean is45PlatformID4(double px2, double py2) {
		// FIXME
		int nx = (int) (px2/size);
		int ny = (int) (py2/size);
		if(nx > -1 && nx < Generator.W && ny > -1 && ny < Generator.H) {
			if(map[nx][ny] == 4) {
				return px2%size < py2%size;
			}else{
				return false;
			}
		}
		return true;
	}
	

	private boolean is45PlatformID3(double px2, double py2) {
		// FIXME
		int nx = (int) (px2/size);
		int ny = (int) (py2/size);
		if(nx > -1 && nx < Generator.W && ny > -1 && ny < Generator.H) {
			if(map[nx][ny] == 3) {
				return px2%size > size - py2%size;
			}else{
				return false;
			}
		}
		return true;
	}
	
	
//	private boolean isPlatform(double px2, double py2, int t, Graphics2D g) {
//		// FIXME
//		int nx = (int) (px2/size);
//		int ny = (int) (py2/size);
//		g.drawRect(size*nx, (int) ny*size, size, size);
//		if(nx > -1 && nx < Generator.W && ny > -1 && ny < Generator.W)
//			return t == map[nx][ny];
//		return true;
//	}
	
	private void Send(Client client, String txt) {
		client.sendMessage(txt);
	}
	
	private class MessageHandler implements Subscriber {

		@Override
		public void update(Message m) {
			try {
				String msg = m.getMessage();
				String message[] = msg.split("&!");
				if(!message[0].equals(myName)) {//
				if(message[1].equals("GETMAP")) {
					String txtmap = myName + myName + "&!M&!";
					for (int y = 0; y < Generator.H; y++) {
						for (int x = 0; x < Generator.W; x++) {
							txtmap += (char)(map[x][y]);
						}
						txtmap += "\n";
					}
					Send(client, txtmap);
				}else {
						if(message[1].equals("#")) {
							notMyName = message[0].replaceAll("&:", "&!");
							try {
								px2 = Double.valueOf(message[2]);
								py2 = Double.valueOf(message[3]);
							} catch (NumberFormatException e) {
							}
						}else if(message[1].equals("M")){
							System.out.println("GETTING MAP");
							String[] xp = message[2].split("\n");
							int w = xp[0].length();
							int h = xp.length;
							map = new int[w][h];
							Generator.W = w;
							Generator.H = h;
							for (int y = 0; y < h; y++) {
								char[] cs = xp[y].toCharArray();
								for (int x = 0; x < w; x++) {
									map[x][y] = (int)(cs[x]);
								}
							}
							initMap = true;
						}
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		
		}
	}
}

/*
 * if(GetAllTiles(pw, ph) ||
		goY > 0 && !fall && (
				(isPlatform(px+pw, py+ph-(size*0.77)) &&
				 isPlatform(px+pw, py-(size*0.77)+size-ph))||
				(isPlatform(px, py+ph-(size*0.77)) &&
				 isPlatform(px, py-(size*0.77)+size-ph)))) {
	py -= goY/2;
	if(GetAllTiles(pw, ph) ||
			goY > 0 && !fall && (
					(isPlatform(px+pw, py+ph-(size*0.77)) &&
					 isPlatform(px+pw, py-(size*0.77)+size-ph))||
					(isPlatform(px, py+ph-(size*0.77)) &&
					 isPlatform(px, py-(size*0.77)+size-ph)))) {
		py -= goY/2;
		goY = 0;
	}
}
 */
