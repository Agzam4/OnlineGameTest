package JFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import Graphics.ButtonsGraphics;
import Server.Server;
import data.Filters;
import work.File_;
import work.Game;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.CompoundBorder;

public class JMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField socket;
	private static final Color BACKGROUND = new Color(13,17,23);
	private static final Color FOREGROUND = new Color(201,209,217);
	private static String MY_HOST;
	private static final String LOCAL_HOST = "127.0.0.1";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMain frame = new JMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JMain() {
		
		try {
			MY_HOST = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e2) {
			MY_HOST = "";
		}
		
		ButtonsGraphics buttonsGraphics = new ButtonsGraphics();
		
        setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(BACKGROUND);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel iv = new JPanel();
		iv.setBackground(BACKGROUND);
		iv.setVisible(false);
//		contentPane.add(iv, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBackground(BACKGROUND);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(BACKGROUND);
		panel.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setForeground(FOREGROUND);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		panel_1.add(lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(BACKGROUND);
		panel.add(panel_2);
		
		JButton join = new JButton("Join");
		panel_2.add(join);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(BACKGROUND);
		panel.add(panel_3);

		JButton createRoom = new JButton("Create Room");
		panel_3.add(createRoom);
		
		JButton go = new JButton("Go");

		createRoom.setForeground(FOREGROUND);
		join.setForeground(FOREGROUND);
		go.setForeground(FOREGROUND);
		buttonsGraphics.Convert(createRoom, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		buttonsGraphics.Convert(join, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		buttonsGraphics.Convert(go, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(BACKGROUND);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		
		JPanel jcrp = new JPanel();
		jcrp.setBackground(BACKGROUND);
		panel_4.add(jcrp);
		
		JLabel jcr = new JLabel();
		jcr.setFont(new Font("Tahoma", Font.PLAIN, 25));
		jcr.setForeground(FOREGROUND);
		jcrp.add(jcr);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(BACKGROUND);
		panel_4.add(panel_5);
		
		JLabel lblNewLabel_1 = new JLabel("IP: ");
		lblNewLabel_1.setForeground(FOREGROUND);
		panel_5.add(lblNewLabel_1);
		
		JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(
				new String[] {LOCAL_HOST, MY_HOST}));
		
//		comboBox_1.setFocusable(false);
		comboBox_1.setEditable(true);
		panel_5.add(comboBox_1);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(BACKGROUND);
		panel_4.add(panel_6);
		
		JLabel socketlabel = new JLabel("Socket:");
		socketlabel.setForeground(FOREGROUND);
		panel_6.add(socketlabel);
		
		socket = new JTextField("4242");
		panel_6.add(socket);
		socket.setColumns(10);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(BACKGROUND);
		panel_4.add(panel_7);

		JPanel mapJPanel = new JPanel();
		mapJPanel.setBackground(BACKGROUND);
		panel_4.add(mapJPanel);

		JPanel texturesJPanel = new JPanel();
		texturesJPanel.setBackground(BACKGROUND);
		panel_4.add(texturesJPanel);
		
		JLabel lblNewLabel_3 = new JLabel("Name: ");
		lblNewLabel_3.setForeground(FOREGROUND);
		panel_7.add(lblNewLabel_3);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(BACKGROUND);
		panel_4.add(panel_8);
		panel_8.add(go);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] {System.getProperty("user.name")}));
		comboBox.setEditable(true);
//		comboBox.setColumns(10);
		panel_7.add(comboBox);

		File_ file = new File_();

		JLabel mapJLabel = new JLabel("Map: ");
		mapJLabel.setForeground(FOREGROUND);
		mapJPanel.add(mapJLabel);
		
		JComboBox<String> mapsBox = new JComboBox<String>();
		mapsBox.setModel(new DefaultComboBoxModel<String>(
				file.GetStringNamesDir(System.getProperty("user.dir") + "\\data\\maps\\")));
		mapsBox.setFocusable(false);
		mapJPanel.add(mapsBox);

		JLabel texturesJLabel = new JLabel("Textures: ");
		texturesJLabel.setForeground(FOREGROUND);
		texturesJPanel.add(texturesJLabel);
		
		JComboBox<String> texturesBox = new JComboBox<String>();
		texturesBox.setModel(new DefaultComboBoxModel<String>(
				file.GetStringNamesDir(System.getProperty("user.dir") + "\\data\\texturePacks\\")));
		texturesBox.setFocusable(false);
		texturesJPanel.add(texturesBox);
		
		Filters filters = new Filters();
		filters.AddServerSocketFilter(socket);
		
		JLabel hts = new JLabel("");
		hts.setForeground(new Color(227,180,65));
		hts.setOpaque(true);
		hts.setBackground(new Color(31,28,22));
		panel_6.add(hts);
		socket.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(!socket.getText().equals("")) {
					if(Integer.valueOf(socket.getText()) < 1250) {
						hts.setText("It is better to use at least 1250");
						hts.setBorder(new CompoundBorder(new LineBorder(new Color(93,68,17), 1, true),
								new EmptyBorder(5, 5, 5, 5)));
					}else {
						hts.setText("");
						hts.setBorder(null);
					}
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!socket.getText().equals("")) {
					if(Integer.valueOf(socket.getText()) < 1250) {
						hts.setText("It is better to use at least 1250");
						hts.setBorder(new CompoundBorder(new LineBorder(new Color(93,68,17), 1, true),
								new EmptyBorder(5, 5, 5, 5)));
					}else {
						hts.setText("");
						hts.setBorder(null);
					}
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		

		JLabel ipil = new JLabel("124");
		ipil.setForeground(new Color(65,180,227));
		ipil.setOpaque(true);
		ipil.setBackground(new Color(22,28,31));
		ipil.setText("Local Host (Only for you PC)");
		ipil.setBorder(new CompoundBorder(new LineBorder(new Color(17,68,93), 1, true),
				new EmptyBorder(5, 5, 5, 5)));
		panel_5.add(ipil);
		
		comboBox_1.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(LOCAL_HOST.equals(comboBox_1.getEditor().getItem() + "")) {
					ipil.setText("Local Host (Only for you PC)");
					ipil.setBorder(new CompoundBorder(new LineBorder(new Color(17,68,93), 1, true),
							new EmptyBorder(5, 5, 5, 5)));
				}else if(comboBox_1.getEditor().getItem().equals(MY_HOST)) {
					ipil.setText("Local Host (Only for LAN)");
					ipil.setBorder(new CompoundBorder(new LineBorder(new Color(17,68,93), 1, true),
							new EmptyBorder(5, 5, 5, 5)));
				}else {
					ipil.setText("");
					ipil.setBorder(null);
				}
//				if(!socket.getText().equals("")) {
//					if(Integer.valueOf(socket.getText()) < 1250) {
//						ipil.setText("It is better to use at least 1250");
//						ipil.setBorder(new CompoundBorder(new LineBorder(new Color(93,68,17), 1, true),
//								new EmptyBorder(5, 5, 5, 5)));
//					}else {
//						ipil.setText("12");
//						ipil.setBorder(null);
//					}
//				}
			}
		});
		
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowDataInput(panel_4, panel);		
				jcr.setText("Joining");	
				contentPane.repaint();			
			}
		});	
		
		createRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowDataInput(panel_4, panel);			
				jcr.setText("Creating Room");
				contentPane.repaint();
			}
		});
		
		go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				Go(panel_4, iv);
//				contentPane.repaint();
				contentPane.removeAll();
				contentPane.repaint();
				contentPane.add(iv, BorderLayout.CENTER);
				iv.setVisible(true);
//				System.out.println(contentPane.getComponents().getClass().getMethods());
				contentPane.repaint();
				if(jcr.getText().equals("Creating Room")) {

					Thread T = new Thread() {
						@Override
						public void run() {
							Server server = new Server();
							int ss = Integer.valueOf(socket.getText());
							server.main(ss);
						}
					};
					T.start();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
				}
				
				Game game = new Game();
				try {
					game.Go(iv, comboBox.getEditor().getItem() + "",
							comboBox_1.getEditor().getItem() + "",
							Integer.valueOf(socket.getText()), mapsBox.getSelectedIndex(),
							texturesBox.getSelectedItem() + "",
							jcr.getText().equals("Creating Room"));
				} catch (Exception e2) {
//					if(e.getModifiers() == )
					e2.printStackTrace();
					contentPane.removeAll();
					contentPane.add(panel, BorderLayout.CENTER);
					contentPane.repaint();
				}
			}
		});
		comboBox.setEditor(new ItemEditor());
	}

	private void ShowDataInput(JPanel panel_4, JPanel panel) {
		contentPane.remove(panel);
		contentPane.add(panel_4, BorderLayout.CENTER);
	}

	class ItemEditor extends BasicComboBoxEditor {
	    public void setItem(Object anObject) {
	        editor.setText(anObject + "");
	        
	    }
	}

}
