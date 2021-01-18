package data;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class Filters {

	public void AddServerSocketFilter(JTextField socket) {
		PlainDocument doc = (PlainDocument) socket.getDocument();
		doc.setDocumentFilter(new ServerSocketFilter(socket));
	}
	
	public static String IPFilter(String str) {
		String ns = ""; 
		System.out.println(str);
		String[] strings = str.split("\\.");
		for (int i = 0; i < 4; i++) {
			try {
				int intt = Integer.valueOf(strings[i]);
				if(intt > 255)intt = 255;
				if(intt < 0)intt = 0;
				ns += intt;
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				ns += "0";
			}
			if(i < 3)
				ns += ".";
		}
		return ns;
	}
	
	class ServerSocketFilter extends DocumentFilter {
		
	    private static final String DIGITS = "\\d+";
	    public JTextField serSok;
	    
	    public ServerSocketFilter(JTextField socket) {
	    	serSok = socket;
		}
	    
	    @Override
	    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
	    	if (string.matches(DIGITS) && serSok.getText().length() < 4) {
	    		super.insertString(fb, offset, String.valueOf(Integer.valueOf(string)), attr);
	    	}
	    }
	    
	    @Override
	    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
	    	if (string.matches(DIGITS) && serSok.getText().length() < 4) {
	    		super.replace(fb, offset, length, string, attrs);
	    	}
	    }
	}
}
