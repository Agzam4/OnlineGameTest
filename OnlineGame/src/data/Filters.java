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
