package work;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Client.Client;
import Interfaces.Subscriber;
import JFrames.JMain;
import Models.Message;
import Server.Server;
import data.DebugInfo;

public class Game {

	public static ArrayList<String> messegessList = new ArrayList<String>();
	public final int MaxPlayers = 2;
	
	private int msgTime = 255;
	
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
	private boolean showInfo = false;

	// Online data
	private boolean isGame = true;
	private String myName;
	private String notMyName = "";
	private boolean admin = false;
	private boolean initMap = false;
	private boolean sendMap = false;
	
	//Secret features
	String sfc = "";
	private boolean canFly = false;
	private boolean canTwg = false;

	public void Go(JPanel ivv, String name, String ip, int ss, int id, String tp, boolean isAdmin) {
		myName = name;
		admin = isAdmin;
		sendMap = false;
		isGame = true;
		
		// Creating Client
		Client client = new Client(name);
		String s = client.setUpNetwork(ip, ss);
		JOptionPane.showMessageDialog(null, s);
		if(!s.equals("Successful connection")) {
			return;
		}
		client.addObserver(new MessageHandler());
		System.out.println(s);
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
			// Send msg: "GETMAP", in order for the admin to give the map to the client
			Send(client, "GETMAP");
		}
		
		if(Client.players > 2) {
			System.out.println(Client.players);
			JMain.showMenu();
			return;
		}

		JLabel iv = new JLabel();
		ivv.add(iv);

		for (int i = 0; i < textures.length; i++) {
			try {
				textures[i] =  ImageIO.read(new File(System.getProperty("user.dir")
						+ "\\data\\datapacks\\" + tp + "\\" + i + ".png"));
			} catch (IOException e) {
				DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
				textures[i] = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
				System.err.println("ERR LOADING TEXTURE: " + i);
			}
		}

		Thread gameThread = new Thread() {
			@Override
			public void run() {

//				String newName = name.replaceAll("&!", "&:");

				double pw = 15;
				double ph = 15;

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
						DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
					}
				}
				
				Sounds.play(tp);

				int iw = Generator.W;
				int ih = Generator.H;

				while (isGame) {
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
							if(map[x][y] == 7) {
								g.drawImage(textures[7], newX, newY+size,
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
					
					g.setColor(new Color(255,25,25));
					if(showInfo)
					g.drawString("Client errs: " + Client.errs, 5, 12);
					if(admin && showInfo)
					g.drawString("Players: " + Server.players + "/2", 5, 25);

					g.setColor(new Color(255,25,25));
					int mls = messegessList.size();
					if(mls > 0) {
						g.setColor(new Color(255,25,25,msgTime));
						msgTime-=2;
						g.drawString(messegessList.get(0), 5, 50);
						if(msgTime < 0) {
							messegessList.remove(0);
							msgTime = 255;
						}
						for (int i = 1; i < messegessList.size(); i++) {
							String s = messegessList.get(i);
							g.drawString(s, 5, 50 + i*15);
						}
					}
					
					g.dispose();
					iv.repaint();


					// MOVE
					if(right) {
						goX += speed;
					}
					if(left) {
						goX -= speed;
					}
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
						if(canFly) {
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
						if(canTwg) {
							while (GetAllTiles(pw, ph) && px > 0 && px < size * iw) 
								px+=pw*(goX/Math.abs(goX));{
							}
							goX = 0;
						}else {
							px -= goX/2;
							if(GetAllTiles(pw, ph)) {
								px -= goX/2;
								goX = 0;
							}
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

					// Send data
					if(sendMap) {
						// Send map: step2
						String txtmap = "M&!";
						for (int y = 0; y < Generator.H; y++) {
							for (int x = 0; x < Generator.W; x++) {
								txtmap += (char)(map[x][y]);
							}
							txtmap += "\n";
						}
						Send(client, txtmap);
						sendMap = false;
					}else {
						Send(client, "#&!" + (int)(px) + "&!" + (int)(py) + "&!");
					}
					
					try{
						Thread.sleep(millis);
					}catch(InterruptedException e){
						DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
					}
				}
				Sounds.stop();;
			}
		};
		gameThread.start();

		iv.setFocusable(true);
		iv.requestFocus();
		iv.addKeyListener(new KeyListener() {
			
			// Codes: "$" - on, "#" - off
			private final String[] sfcs = new String[] {
					"AGZAM4$FLY", "AGZAM4#FLY",
					"AGZAM4$TWG", "AGZAM4#TWG"
					};

			@Override
			public void keyTyped(KeyEvent e) {
				sfc += e.getKeyChar();
				int start = sfc.length()-10;
				if(start < 0)start = 0;
				sfc = sfc.substring(start);
				if(sfc.equals(sfcs[0]))
					canFly = true;
				if(sfc.equals(sfcs[1]))
					canFly = false;
				if(sfc.equals(sfcs[2]))
					canTwg = true;
				if(sfc.equals(sfcs[3]))
					canTwg = false;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_RIGHT:
					right = false;
					break;
				case KeyEvent.VK_LEFT:
					left = false;
					break;
				case KeyEvent.VK_UP:
					up = false;
					break;
				case KeyEvent.VK_DOWN:
					down = false;
					break;
				default:
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_RIGHT:
					right = true;
					break;
				case KeyEvent.VK_LEFT:
					left = true;
					break;
				case KeyEvent.VK_UP:
					up = true;
					break;
				case KeyEvent.VK_DOWN:
					down = true;
					break;
				case KeyEvent.VK_F1:
					showInfo = !showInfo;
					break;
				default:
					break;
				}
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

	final boolean[] isPlatform = new boolean[] {false,false,true,false,false,true,false,false};

	private boolean isPlatform(double px2, double py2) {
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
				String nick = m.getNickname();
				String msg = m.getMessage();
				if(msg.equals("FULL")) {
					JOptionPane.showMessageDialog(null, "Server Full");
					isGame = false;
				}
				String message[] = msg.split("&!");
				if(!nick.equals(myName)) {
					if(msg.equals("GETMAP") && admin && initMap) {
						// Send map: step1
						sendMap = true;
					}else {
						if(message[0].equals("#")) {
							// Getting position
							notMyName = nick.replaceAll("&:", "&!");
							try {
								px2 = Integer.valueOf(message[1]);
								py2 = Integer.valueOf(message[2]);
							} catch (NumberFormatException e) {
							}
						}else if(message[0].equals("M") && !admin && !initMap){
							// Getting map
							System.out.println("GETTING MAP");
							String[] xp = message[1].split("\n");
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
							System.out.println("CLIENT | " + Generator.W + "x" + Generator.H);
							initMap = true;
						}
					}
				}
			} catch (NullPointerException e) {
				DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			}
		}
	}
	
	public static void StopGame() {
		
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
