package Client;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import Interfaces.Publisher;
import Interfaces.Subscriber;
import Models.Message;

public class Client implements Publisher {
	
	ObjectInputStream reader;
	ObjectOutputStream writer;
	Socket socket;
	String nickname;
	
	private ArrayList<Subscriber> observers = new ArrayList<Subscriber>();
	
	@Override
	public void addObserver(Subscriber o) {
		observers.add(o);
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
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public boolean sendMessage(String message) {
		Message msg = new Message(nickname, message);
		try {
			writer.writeObject(msg);
			writer.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private class IncomingReader implements Runnable {

		@Override
		public void run() {
			Message message;
			try {
				while ((message = (Message)reader.readObject())!= null) {
					notifyObservers(message);
				}
			}catch (ClassCastException e) {
				try {
					while ((message = new Message(nickname, reader.readObject() + ""))!= null) {
						notifyObservers(message);
					}
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
