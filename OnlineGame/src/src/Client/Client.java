package Client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Interfaces.Publisher;
import Interfaces.Subscriber;
import JFrames.JMain;
import Models.Message;
import data.DebugInfo;
import work.Game;

public class Client implements Publisher {
	
	ObjectInputStream reader;
	ObjectOutputStream writer;
	Socket socket;
	String nickname;

	public static int players = 0;
	private ArrayList<Subscriber> observers = new ArrayList<Subscriber>();
	
	@Override
	public void addObserver(Subscriber o) {
		observers.add(o);
		players = observers.size();
	}

	@Override
	public void removeObserver(Subscriber o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers(Message m) {
		for (Subscriber o : observers) {
			o.update(m);
		}
	}
	
	public Client(String nick) {
		nickname = nick;
	}
	
	public String setUpNetwork(String ip, int ss) {
		try {
			socket = new Socket(ip, ss);
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());
			IncomingReader ir = new IncomingReader();
			Thread t = new Thread(ir);
			t.start();
			return "Successful connection";
		}catch (ConnectException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ConnectException");
			return e.getMessage();
		} catch (Exception e) {
			DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			return e.getMessage();
		}
	}
	
	public boolean sendMessage(String message) {
		Message msg = new Message(nickname, message); // TODO: maybe add id to players
		try {
			writer.writeObject(msg);
			writer.flush();
			return true;
		} catch (SocketException e) {
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static int errs = 0;
	
	private class IncomingReader implements Runnable {	

		@Override
		public void run() {
			Message message;
			try {
				while (true) {
					Object o = reader.readObject();
					if(o.equals("")) {
						errs++;
					} else {
						message = (Message) o;
						notifyObservers(message);
					}
				} 
			}catch (StreamCorruptedException | SocketException e) {
				// Client crash
				DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
				Game.StopGame();
				JMain.showMenu();
			} catch (ClassNotFoundException | IOException e) {
				DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			}
		}
	}
}
