package work;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TooManyListenersException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import JFrames.JMain;

public class DropFile {

	private static final Color BACKGROUND = new Color(13,17,23);
	private static final Color FOREGROUND = new Color(201,209,217);
	BufferedImage textures[] = new BufferedImage[8];
	double[] px = new double[8];
	double[] py = new double[8];
	boolean[] init = new boolean[8];
	boolean isReady = false;
	private int w =1;
	private int h =1;
	int totalid = 0;
	
	String dir = ""; 
	
	public void AddFileDropper(JPanel p) {
		totalid = 0;
//		DropPane dropPane = new DropPane();
//		p.add(dropPane);
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			px[i] = 0;
			init[i] = false;
		}
		JPanel dndJPanel = new JPanel();
		dndJPanel.setLayout(new BorderLayout(0, 0));
		dndJPanel.setBackground(BACKGROUND);
		p.add(dndJPanel, BorderLayout.CENTER);
		
		JLabel dnd = new JLabel("") {
			
			private static final long serialVersionUID = 1L;
			private final Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
			private final Stroke basic = new BasicStroke(3);
			
			@Override
			public void paint(Graphics gg) {
				super.paint(gg);
				 w = getWidth();
				 h = getHeight();
				Graphics2D g = (Graphics2D) gg;
				
				for (int i = 0; i < textures.length; i++) {
					if(isReady) {
					}else{
						px[i] = (getWidth()/textures.length)*i;
						py[i] = 3;
					}
					drawImage(g, i);
					
//					g.drawImage(textures[i], i*(getWidth()/textures.length), 0, getWidth()/textures.length, (getWidth()/textures.length), null);
//					g.setColor(Color.WHITE);
//					g.drawRect(i*(getWidth()/textures.length), 0, getWidth()/textures.length, getWidth()/textures.length);
				}
				
				g.dispose();
			}
			
			private void drawImage(Graphics2D g, int id) {
				g.setStroke(init[id] ? basic : dashed);
				if(init[id]) {
					g.setColor(Color.GRAY);
					g.drawRect((int)px[id], (int)py[id] -3 , getWidth()/textures.length, getWidth()/textures.length);
				}
				g.drawImage(textures[id], (int)px[id], (int)py[id], getWidth()/textures.length, (getWidth()/textures.length), null);

				g.setColor(FOREGROUND);
				g.drawRect((int)px[id], (int)py[id], getWidth()/textures.length, getWidth()/textures.length);
			}
		};
		DropTarget target = new DropTarget();
		try {
			target.addDropTargetListener(new DropTargetAdapter() {
			        public void drop(DropTargetDropEvent dtde) {
			            Transferable t = dtde.getTransferable();
			            try {
			                DataFlavor[] dataFlavors = t.getTransferDataFlavors();
			                dtde.acceptDrop(DnDConstants.ACTION_COPY);

			                for (int i = 0; i < dataFlavors.length; i++) {
			                    File file = new File(t.getTransferData(dataFlavors[i]).toString().replace("[", "").replace("]", ""));
//			                    dnd.setText(file.getName());
			                    if(file.isDirectory()) {
			                    	dir = file.getName();
			                    	for (File f : file.listFiles()) {
		                    			if(totalid < textures.length+1) {
				                    		try {
					                    		textures[totalid] = ImageIO.read(f);
					                    		init[totalid] = true;
					                    		totalid++;
											} catch (IOException |ArrayIndexOutOfBoundsException e) {
											}
		                    			}
									}
			                    	dnd.repaint();
			                    }else {
			                    	if(dir.equals("")) {
			                    		dir = JOptionPane.showInputDialog("Name:");
			                    	}
			                    	if(dir == null || dir.equals("")) {
			                    		dir = "unnamed";
			                    	}
			                    	try {
			                    		textures[totalid] = ImageIO.read(file);
			                    		init[totalid] = true;
			                    		totalid++;
									} catch (IOException |ArrayIndexOutOfBoundsException e) {
									}
			                    }
//			                    Desktop.getDesktop().open(new File(file.getCanonicalPath()));
			                }
			                dtde.dropComplete(true);

			            } catch (Exception ex) {
			                ex.printStackTrace();
			            }
                    	dnd.repaint();
			        }
			    });
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
		//		dnd.setDragEnabled(true);
		
		
		dnd.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() > 1) {
					boolean cans = true;
					for (int j = 0; j < init.length; j++) {
						if(!init[j]) cans = false;
					}
					if(cans) {
						if(!isReady) {
							isReady = true;
							Thread an = new Thread() {
								public void run() {
									boolean aic = true;
									while(aic) {
										int centerX = w/2 - (w/textures.length)/2;
										int centerY = h/2 - (h/textures.length)/2;
										boolean aic1 = false;
										for (int j = 0; j <px.length; j++) {
											if((!((int)((py[j]-j*15)/3) == (int)(centerY/3))) || 
													(!((int)(px[j]/3) == (int)(centerX/3)))) {
												aic1 = true;
											}
											px[j] = ((px[j]-centerX)*0.8)+centerX;
											py[j] = ((py[j]-centerY-j*15)*0.8)+centerY+j*15;
										}



										aic = aic1;
										try {Thread.sleep(50);
										} catch (InterruptedException e) {}
										dnd.repaint();
									}

									//								aic = true;
									//								while(aic) {
									//									int centerX = w/2 - (w/textures.length)/2;
									//									int centerY = w + w/textures.length + 100;
									//									boolean aic1 = false;
									//									for (int j = 0; j <px.length; j++) {
									//										if(py[j] > w + w/textures.length) {
									//											aic1 = true;
									//										}
									//										px[j] = ((px[j]-centerX)*0.98)+centerX;
									//										py[j] = ((py[j]-centerY-j*15)*0.98)+centerY+j*15;
									//									}
									//									
									//									aic = aic1;
									//									try {Thread.sleep(50);
									//									} catch (InterruptedException e) {}
									//									dnd.repaint();
									//								}

									boolean ss = true;
									File nd = new File(System.getProperty("user.dir") + "\\data\\datapacks\\" + dir);
									if(nd.mkdir()) {
										for (int i = 0; i < textures.length; i++) {
											try {
												ImageIO.write(textures[i], "png", new File(nd + "\\" + i + ".png"));
											} catch (IOException e) {
												JOptionPane.showMessageDialog(null, "Err " + e.getMessage());
												ss = false;
											}
										}
										if(ss)
											JOptionPane.showMessageDialog(null, "Successful");

									}else {
										JOptionPane.showMessageDialog(null, "Err");
									}
									p.removeAll();
									JMain.showMenu();
									p.repaint();
								};
							};
							an.start();
						}
					}else {
						p.removeAll();
						JMain.showMenu();
						p.repaint();
					}
				}
			}
		});
		dnd.setDropTarget(target);
		dndJPanel.add(dnd, BorderLayout.CENTER);
		p.repaint();
		dndJPanel.setVisible(false);
		p.repaint();
		dndJPanel.setVisible(true);
//		JPanel dnd = new JPanel();
//		  new  FileDrop(dnd, new FileDrop.Listener()
//		  {   public void  filesDropped( java.io.File[] files )
//		      {   
//		          // handle file drop
//		          ...
//		      }   // end filesDropped
//		  }); // end FileDrop.Listener 
	}
	
//	 public class DropPane extends JPanel {
//
//		 	private static final long serialVersionUID = 1L;
//		 	
//			private DropTarget dropTarget;
//	        private DropTargetHandler dropTargetHandler;
//	        private Point dragPoint;
//
//	        private BufferedImage target;
//
//	        private JLabel message;
//
//	        public DropPane() {
//	            try {
//	                target = ImageIO.read(new File("target.png"));
//	            } catch (IOException ex) {
//	                ex.printStackTrace();
//	            }
//
//	            setLayout(new GridBagLayout());
//	            message = new JLabel();
//	            message.setForeground(new Color(75,250,105));
//	            message.setFont(message.getFont().deriveFont(Font.BOLD, 24));
//	            add(message);
//	        }
//
//	        @Override
//	        public Dimension getPreferredSize() {
//	            return new Dimension(400, 400);
//	        }
//
//	        protected DropTarget getMyDropTarget() {
//	            if (dropTarget == null) {
//	                dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
//	            }
//	            return dropTarget;
//	        }
//
//	        protected DropTargetHandler getDropTargetHandler() {
//	            if (dropTargetHandler == null) {
//	                dropTargetHandler = new DropTargetHandler();
//	            }
//	            return dropTargetHandler;
//	        }
//
//	        @Override
//	        public void addNotify() {
//	            super.addNotify();
//	            try {
//	                getMyDropTarget().addDropTargetListener(getDropTargetHandler());
//	            } catch (TooManyListenersException ex) {
//	                ex.printStackTrace();
//	            }
//	        }
//
//	        @Override
//	        public void removeNotify() {
//	            super.removeNotify();
//	            getMyDropTarget().removeDropTargetListener(getDropTargetHandler());
//	        }
//
//	        @Override
//	        protected void paintComponent(Graphics g2) { // TODO
//	            super.paintComponent(g2);
//	            Graphics2D g = (Graphics2D) g2.create();
//				g.setColor(new Color(13,17,23));
//				g.fill(new Rectangle(getWidth(), getHeight()));
//	                Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
//	                g.setStroke(dashed);
//				g.setColor(new Color(201,209,217));
//				g.drawLine(0, 0, getWidth(), getHeight());
//				g.drawRect(5, 5, getWidth()-10, getHeight()-10);
//                message.setText("Drop files here");
//				
//				if (dragPoint != null && target != null) {
//				    int x = dragPoint.x - 12;
//				    int y = dragPoint.y - 12;
//				    g.drawImage(target, x, y, this);
//				}
//				g.dispose();
//	        }
//
//	        protected void importFiles(final List<File> files) {
//	            Runnable run = new Runnable() {
//	                @Override
//	                public void run() {
//	                    message.setText("You dropped " + files.size() + " files");
//	                }
//	            };
//	            SwingUtilities.invokeLater(run);
//	        }
//
//	        protected class DropTargetHandler implements DropTargetListener {
//
//	            protected void processDrag(DropTargetDragEvent dtde) {
//	                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
//	                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
//	                } else {
//	                    dtde.rejectDrag();
//	                }
//	            }
//
//	            @Override
//	            public void dragEnter(DropTargetDragEvent dtde) {
//	                processDrag(dtde);
//	                SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
//	                repaint();
//	            }
//
//	            @Override
//	            public void dragOver(DropTargetDragEvent dtde) {
//	                processDrag(dtde);
//	                SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
//	                repaint();
//	            }
//
//	            @Override
//	            public void dropActionChanged(DropTargetDragEvent dtde) {
//	            }
//
//	            @Override
//	            public void dragExit(DropTargetEvent dte) {
//	                SwingUtilities.invokeLater(new DragUpdate(false, null));
//	                repaint();
//	            }
//
//	            @Override
//	            public void drop(DropTargetDropEvent dtde) {
//
//	                SwingUtilities.invokeLater(new DragUpdate(false, null));
//
//	                Transferable transferable = dtde.getTransferable();
//	                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
//	                    dtde.acceptDrop(dtde.getDropAction());
//	                    try {
//
//	                        @SuppressWarnings("unchecked")
//							List<File> transferData = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
//	                        if (transferData != null && transferData.size() > 0) {
//	                            importFiles(transferData);
//	                            dtde.dropComplete(true);
//	                        }
//
//	                    } catch (Exception ex) {
//	                        ex.printStackTrace();
//	                    }
//	                } else {
//	                    dtde.rejectDrop();
//	                }
//	            }
//	        }
//
//	        public class DragUpdate implements Runnable {
//
//	            private boolean dragOver;
//	            private Point dragPoint;
//
//	            public DragUpdate(boolean dragOver, Point dragPoint) {
//	                this.dragOver = dragOver;
//	                this.dragPoint = dragPoint;
//	            }
//
//	            @Override
//	            public void run() {
//	                DropPane.this.dragOver = dragOver;
//	                DropPane.this.dragPoint = dragPoint;
//	                DropPane.this.repaint();
//	            }
//	        }
//	 }
}
