package pexeso;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

public class MainWindow {	
	
	private static void createAndShowGUI() {
		
		// basic window setup
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Pexeso");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ResizeListener()); //TODO remake
		//frame.setSize(screenSize.width, screenSize.height);
		
		Container pane = frame.getContentPane();
		
		PexesoContainer pexesoPane = new PexesoContainer(pane, 4, 6);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		System.out.println(pexesoPane.getSize());
		
		pexesoPane.createNewGame("/home/anet/MFF/Java/Zapocet/zadni-strana-pexesa.png");
		
		frame.setContentPane(pexesoPane);
		frame.validate();
		frame.pack();
		
		
		frame.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable()	{
		
		public void run() {
			createAndShowGUI();
			}
		
		});
	}

	
	/*ImageIcon backSide = new ImageIcon("/home/anet/MFF/Zapocet/zadni-strana-pexesa.png");
	JLabel[] backPictures = new JLabel[16];
	GridBagConstraints[] gbc = new GridBagConstraints[16];
	for (int j = 0; j < 16; j++){
		gbc[j] = new GridBagConstraints();
	}*/
	
	
/*	for (int i = 0; i < 16; i++){
		
		backPictures[i] = new JLabel(backSide);
		backPictures[i].setText("Label nr."+((Integer)i).toString());
		gbc[i].gridx = i%4;
		gbc[i].gridy = i/4;
		gbc[i].fill = GridBagConstraints.BOTH;
		//gbc[i].gridwidth = GridBagConstraints.RELATIVE;
		layout.setConstraints(backPictures[i], gbc[i]);
		
		//backPictures[i].setBorderPainted(false);
		//backPictures[i].setFocusPainted(false);
		//backPictures[i].setContentAreaFilled(false)
		System.out.println();
		
		//resize the image icon to fit the button in the grid
		int imgWidth = (int) backPictures[i].getPreferredSize().getWidth();
		int imgHeight = (int) backPictures[i].getPreferredSize().getHeight();
		System.out.printf("Size of the button is %s \n", backPictures[i].getPreferredSize());
		Image backSideImg = backSide.getImage(); // get image from icon
		Image backSideImgScaled = backSideImg.getScaledInstance(imgWidth/2, imgHeight/2, java.awt.Image.SCALE_SMOOTH); //scale it
		backSide = new ImageIcon( backSideImgScaled ); //substitute the old one by the scaled one
		backPictures[i].setIcon(backSide);
		pane.add(backPictures[i]);
		
	}*/
	
	
}
