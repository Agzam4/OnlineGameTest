package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Models.Message;
import data.DebugInfo;
import work.Game;

public class Server {

	public void main(int ss) {
		Server s = new Server();
		s.go(ss);
	}

	private ArrayList<ObjectOutputStream> clientWriters = new ArrayList<ObjectOutputStream>();

	private void go(int ss) {
		try {
			ServerSocket serverSock = new ServerSocket(ss);
			boolean truee = true;
			System.err.println("\n\nServer was started\n\n");
			while (truee) {
				Socket clientSocket = serverSock.accept();
				ServerClient client = new ServerClient(clientSocket);
				ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
				clientWriters.add(writer);
				Thread t = new Thread(client);
				t.start();
			}
			serverSock.close();

		} catch (BindException e) {
			JOptionPane.showMessageDialog(null,
					"The server with this port and IP is already.\nConnecting");
			DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
		} catch (IOException e) {
			e.printStackTrace();
			DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			JOptionPane.showMessageDialog(null, "Server was falled:\n" + e.getMessage());
		}
	} 
	public static int players = 0;
	
	private class ServerClient implements Runnable {

		ObjectInputStream reader;
		Socket socket;

		public ServerClient(Socket clientSocket) {
			try {
				socket = clientSocket;
				reader = new ObjectInputStream(socket.getInputStream());
			} catch (Exception e) {
				DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			}
		}

		private void sendToOthers(Message message) {
			players = clientWriters.size();
			if(players > 2) {
				try {
					clientWriters.get(2).writeObject("FULL");
					clientWriters.get(2).flush();
					clientWriters.remove(2);
				} catch (IOException e) {
					DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
				}
				players = 2;
			}
			for (int i = 0; i < players; i++) {
				try {
					clientWriters.get(i).writeObject(message);
					clientWriters.get(i).flush();
				} catch (IOException e) {
					DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
					Game.messegessList.add("Player left game");
					clientWriters.remove(i);
				}
			}
		}

		@Override
		public void run() {
			Message message;
			try {
				while (true) {
					try {
						message = (Message)reader.readObject();
						sendToOthers(message);
					} catch (SocketException e) {
					}
				}
			} catch (Exception e) {
				DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e));
			}
		}
	}
}
