package Models;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nickname;
	private String message;

	public Message(String nick, String msg) {
		nickname = nick;
		message = msg;
//		id = iD;
	}
	
//	public Message(String msg) {
//		message = msg;
//	}
	
	public String getMessage() {
		return message;
	}

	public String getNickname() {
		return nickname;
	}
//	public int getID() {
//		return id;
//	}
}
