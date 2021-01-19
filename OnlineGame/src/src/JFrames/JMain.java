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
import data.DebugInfo;
import data.Filters;
import work.DropFile;
import work.File_;
import work.Game;
import work.Sounds;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.CompoundBorder;

public class JMain extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// JPanels
	private static JPanel contentPane;
	private static JPanel panel;
	// Socket input
	private JTextField socket;
	
	// Colors
	private static final Color BACKGROUND = new Color(13,17,23);
	private static final Color FOREGROUND = new Color(201,209,217);
	
	// IP:
	private static String LOOPBACK_HOST;
	private static String LOCAL_HOST;
	private static String PUBLIC_HOST;
	
	/** 
	 * @author Agzam4
	 **/
	private static final String BYAGZAM4 = "by Agzam4";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DebugInfo.debugMsg.add("\n\n\n[" + DebugInfo.getDate() + "]\nStart of the program\n\n");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMain frame = new JMain();
					frame.setVisible(true);
				} catch (Exception e) {
					System.err.println(e.getStackTrace()[0]);
					JOptionPane.showMessageDialog(null, DebugInfo.getAllInfo(e));
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JMain() {

		// Set IP <defaults>;
		try {
			LOCAL_HOST = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e3) {
			LOCAL_HOST = "";
			DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e3));
		}
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
	        PUBLIC_HOST = inetAddress.getHostAddress();
		} catch (UnknownHostException e3) {
			PUBLIC_HOST = "";
			DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e3));
		}
		LOOPBACK_HOST = Inet4Address.getLoopbackAddress().getHostAddress();
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					DebugInfo.createLogOfErrs();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		
        setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle(BYAGZAM4);
		
		contentPane = new JPanel();
		contentPane.setBackground(BACKGROUND);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel iv = new JPanel();
		iv.setBackground(BACKGROUND);
		iv.setVisible(false);
		
		panel = new JPanel();
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

		JPanel panel_31 = new JPanel();
		panel_31.setBackground(BACKGROUND);
		panel.add(panel_31);

		JButton addTPButton = new JButton("Add Textures");
		panel_31.add(addTPButton);

		createRoom.setForeground(FOREGROUND);
		join.setForeground(FOREGROUND);
		go.setForeground(FOREGROUND);
		addTPButton.setForeground(FOREGROUND);
		
		ButtonsGraphics buttonsGraphics = new ButtonsGraphics();
		buttonsGraphics.Convert(createRoom, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		buttonsGraphics.Convert(join, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		buttonsGraphics.Convert(go, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		buttonsGraphics.Convert(addTPButton, new Color(35,134,54), new Color(27,92,42), new Color(18,47,32));
		
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
				new String[] {LOOPBACK_HOST, LOCAL_HOST}));
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
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(
				new String[] {System.getProperty("user.name")}));
		comboBox.setEditable(true);
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
				file.GetStringNamesDir(System.getProperty("user.dir") + "\\data\\datapacks\\")));
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
		

		JLabel ipil = new JLabel("LoopBack Host (Only for you PC)");
		ipil.setForeground(new Color(65,180,227));
		ipil.setOpaque(true);
		ipil.setBackground(new Color(22,28,31));
		ipil.setBorder(new CompoundBorder(new LineBorder(new Color(17,68,93), 1, true),
				new EmptyBorder(5, 5, 5, 5)));
		panel_5.add(ipil);
		comboBox_1.getEditor().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				comboBox_1.getEditor().setItem(Filters.IPFilter(comboBox_1.getEditor().getItem() + ""));
			}
		});
		
		comboBox_1.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboBox_1.requestFocus();
				if(LOOPBACK_HOST.equals(comboBox_1.getEditor().getItem() + "")) {
					ipil.setText("LoopBack Host (Only for you PC)");
					ipil.setForeground(new Color(65,180,227));
					ipil.setBackground(new Color(22,28,31));
					ipil.setBorder(new CompoundBorder(new LineBorder(new Color(17,68,93), 1, true),
							new EmptyBorder(5, 5, 5, 5)));
				}else if(comboBox_1.getEditor().getItem().equals(LOCAL_HOST)) {
					ipil.setText("Local Host (Only for LAN)");
					ipil.setForeground(new Color(65,180,227));
					ipil.setBackground(new Color(22,28,31));
					ipil.setBorder(new CompoundBorder(new LineBorder(new Color(17,68,93), 1, true),
							new EmptyBorder(5, 5, 5, 5)));
				}else {
					ipil.setText("");
					ipil.setBorder(null);
				}
				
				if(!jcr.getText().equals("Creating Room")) {
					try {
						if(!InetAddress.getByName(comboBox_1.getEditor().getItem() + "").isReachable(100)) {
							ipil.setForeground(new Color(227,65,65));
							ipil.setBackground(new Color(31,22,22));
							ipil.setBorder(new CompoundBorder(new LineBorder(new Color(93,17,17), 1, true),
									new EmptyBorder(5, 5, 5, 5)));
							ipil.setText(comboBox_1.getEditor().getItem() + " - isn't Reachable");
						}
					} catch (IOException e1) {
					}
				}
			}
		});
		
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowDataInput(panel_4, panel);		
				jcr.setText("Joining");	
				/** Search for available hosts **/
//				Thread findHosts = new Thread() {
//					
//					@Override
//					public void run() {
//						int timeout=100;
//						String iiip = LOCAL_HOST.substring(0, LOCAL_HOST.lastIndexOf(".")) + ".";
//						
//						for (int i=1;i<255;i++){
//							String host=  iiip + i;
//							try {
//								if(InetAddress.getByName(host).isReachable(timeout))comboBox_1.addItem(host);
//							} catch (IOException e1) {
//							}
//							System.out.print(i + " ");
//						}
//					}
//				};
//				
//				findHosts.start();
//				comboBox_1.setModel(new DefaultComboBoxModel<String>(
//						new String[] {""}));
				
				mapJPanel.setVisible(false);
				contentPane.repaint();			
			}
		});	
		
		createRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowDataInput(panel_4, panel);			
				jcr.setText("Creating Room");
				mapJPanel.setVisible(true);
				contentPane.repaint();
				
				comboBox_1.setModel(new DefaultComboBoxModel<String>(
						new String[] {LOOPBACK_HOST, LOCAL_HOST, PUBLIC_HOST}));
			}
		});
		
		go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				contentPane.removeAll();
				contentPane.add(iv, BorderLayout.CENTER);
				iv.setVisible(true);
				contentPane.repaint();
				if(jcr.getText().equals("Creating Room")) {

					Thread T = new Thread() {
						@Override
						public void run() {
							// Create server
							Server server = new Server();
							int ss = Integer.valueOf(socket.getText());
							server.main(ss);
						}
					};
					T.start();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e1));
					}
				}
				
				// Start game
				Game game = new Game();
				try {
					game.Go(iv, comboBox.getEditor().getItem() + "",
							comboBox_1.getEditor().getItem() + "",
							Integer.valueOf(socket.getText()), mapsBox.getSelectedIndex(),
							texturesBox.getSelectedItem() + "",
							jcr.getText().equals("Creating Room"));
				} catch (Exception e2) {
					DebugInfo.debugMsg.add(DebugInfo.getAllInfo(e2));
					contentPane.removeAll();
					contentPane.add(panel, BorderLayout.CENTER);
					contentPane.repaint();
				}
			}
		});
		comboBox.setEditor(new ItemEditor());
		panel_4.add(go);

		JPanel addTP = new JPanel();
		addTPButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add textures
				contentPane.removeAll();
				contentPane.add(addTP);
				contentPane.repaint();
				DropFile df = new DropFile();
				df.AddFileDropper(contentPane);
				contentPane.repaint();
			}
		});
		
	}

	private void ShowDataInput(JPanel panel_4, JPanel panel) {
		contentPane.remove(panel);
		contentPane.add(panel_4, BorderLayout.CENTER);
	}

	public static void showMenu() {
		contentPane.removeAll();
		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.repaint();
		Sounds.stop();
	}

	//This is need to editable JComboBox
	class ItemEditor extends BasicComboBoxEditor {
		
	    public void setItem(Object anObject) {
	        editor.setText(anObject + "");
	    }
	}
}
