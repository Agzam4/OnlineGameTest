package Graphics;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ButtonsGraphics {

	public void Convert(JComponent button, Color backgroundThenOnButton, Color backgroundThen_No_OnButton,  Color backgroundThenPress) {
		
		if(backgroundThen_No_OnButton == null) {
			backgroundThen_No_OnButton = button.getBackground();
		}

		if(backgroundThenOnButton == null) {
			backgroundThenOnButton = button.getBackground();
		}
		
		if(backgroundThenPress == null) {
			backgroundThenPress = new Color(225,225,225);
		}
		Conver(button, backgroundThenOnButton, backgroundThen_No_OnButton, backgroundThenPress);
		
	}
	
	
	private void Conver(JComponent button, Color backgroundC, Color backgroundNC, Color backgroundBP) {
		
		try {
			((AbstractButton) button).setMargin(new Insets(3, 2, 2, 2));
			((AbstractButton) button).setBorderPainted(false);
			button.setFocusable(false);
		} catch (Exception e) {
		}
		
		Thread Button = new Thread() {
			@Override
			public void run() {
				while (true) {
					if(((AbstractButton) button).getModel().isRollover()) {
						Set_Color(button, backgroundC, ((AbstractButton) button).getModel().isPressed(), backgroundBP,2);
					} else {
						Set_Color(button, backgroundNC, false, backgroundBP,5);
					}

					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Button.start();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}
	
	private void Set_Color(JComponent c, Color newColor, boolean Click, Color backgroundBP, double vale) {
		Color color = c.getBackground();
		int r = newColor.getRed();
		int g = newColor.getGreen();
		int b = newColor.getBlue();
		if(Click) {
			r = backgroundBP.getRed();
			g = backgroundBP.getGreen();
			b = backgroundBP.getBlue();

			int rr = (int) Math.ceil(((color.getRed() - r) / vale) + r);
			int gg = (int) Math.ceil(((color.getGreen() - g) / vale) + g);
			int bb = (int) Math.ceil(((color.getBlue() - b) / vale) + b);
			
			c.setBackground(new Color(rr,gg,bb));
		}else {
			int rr = (int) Math.ceil(((color.getRed() - r) / vale) + r);
			int gg = (int) Math.ceil(((color.getGreen() - g) / vale) + g);
			int bb = (int) Math.ceil(((color.getBlue() - b) / vale) + b);
			
			c.setBackground(new Color(rr,gg,bb));
		}
	}
}
