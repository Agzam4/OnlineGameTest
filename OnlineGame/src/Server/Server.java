package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Models.Message;

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
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server was falled:\n" + e.getMessage());
		}
		//		} catch (Exception e) {
		//			System.out.println(e.getLocalizedMessage());
		//			System.out.println();
		//			System.out.println(e.hashCode());
		//			
		//			e.printStackTrace();
		//			JOptionPane.showMessageDialog(null, "Server was falled:\n" + e.getMessage());
	} 

	private class ServerClient implements Runnable {

		ObjectInputStream reader;
		Socket socket;

		public ServerClient(Socket clientSocket) {
			try {
				socket = clientSocket;
				reader = new ObjectInputStream(socket.getInputStream());
			} catch (Exception e) {
				System.err.println("");
			}
		}

		private void sendToOthers(Message message) throws IOException {
			for (int i = 0; i < clientWriters.size(); i++) {
				clientWriters.get(i).writeObject(message);
				clientWriters.get(i).flush();
			}
		}

		@Override
		public void run() {
			Message message;
			try {
				while ((message = (Message)reader.readObject()) != null) {
					sendToOthers(message);
				}
			} catch (Exception e) {
			}
		}
	}
}
