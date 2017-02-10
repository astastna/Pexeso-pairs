package pexeso;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MainWindowGL {	
	//private PexesoContainerGL myPexeso;
	private final static int defaultGameWidth = 4;
	private final static int defaultGameHeight = 4;
	
	
	private static void createAndShowGUI() {
		
		// basic window setup
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Pexeso");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ResizeListener()); //TODO redo
		JMenuBar menuBar = createMenus(frame);

		
		//TODO get this from user
		
		//System.out.println(pexesoPane.getSize());
		Container pane = frame.getContentPane();
		
		String[] firstTuple = {"/home/anet/MFF/Java/workspace/Pexeso/src/pexeso/img0.jpg", "img0.jpg"};
		String[] secondTuple = {"/home/anet/MFF/Java/Zapocet/RikiCte.jpg", "/home/anet/MFF/Java/Zapocet/RikiCte.jpg"};
		String[] thirdTuple = {"/home/anet/MFF/Java/Zapocet/spiciRiki.jpg", "/home/anet/MFF/Java/Zapocet/spiciRiki.jpg"};
		String[] forthTuple = {"/home/anet/MFF/Java/Zapocet/produkt-jablko.png", "/home/anet/MFF/Java/Zapocet/produkt-jablko.png"};
		
		String[][] paths = {firstTuple, secondTuple, thirdTuple, forthTuple};
		
		PexesoContainerGL pexesoPane = new PexesoContainerGL(pane, 2, 4, "/home/anet/MFF/Java/Zapocet/zadni-strana-pexesa.png", paths);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		pexesoPane.createNewGame();
		//PexesoContainerGL myPexeso = createNewPexeso();
		frame.setContentPane(pexesoPane);
		frame.setJMenuBar(menuBar);
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

	private static JMenuBar createMenus(final JFrame frame){
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem newGameItem, existingGameItem, quitItem;

		menuBar = new JMenuBar();

		//The main menu
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "Menu for game options.");
		menuBar.add(menu);

		//TODO add sections
		// About - including Author, Licence, Origin of images
		
		//New game item
		newGameItem = new JMenuItem("Create new game",
		                         KeyEvent.VK_T);
		newGameItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK));
		newGameItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a form for new game.");
		newGameItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				SizeForm newGameForm = new SizeForm(defaultGameWidth, defaultGameHeight, frame);
				newGameForm.setVisible(true);
			}
		});
		//TODO add possibility to save game setup - so you don't have to upload the images every time
		menu.add(newGameItem);
		
		//Play existing game item
		existingGameItem = new JMenuItem("Play an existing game", KeyEvent.VK_T);
		existingGameItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK));
		existingGameItem.getAccessibleContext().setAccessibleDescription(
		        "Choose an existing game from file.");
		//TODO add action with pop-up window with only option to upload special file
		menu.add(existingGameItem);
		
		//Quit
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//close frame
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		menu.add(quitItem);
		
		return menuBar;
	}
	
	private static PexesoContainerGL createNewPexeso(JFrame frame, String[][] paths, int width, int height, String backSideImage){
		Container pane = frame.getContentPane();
		
		// manual initialization for testing
		/*String[] firstTuple = {"/home/anet/MFF/Java/Zapocet/RikiJi.jpg", "/home/anet/MFF/Java/Zapocet/RikiJi.jpg"};
		String[] secondTuple = {"/home/anet/MFF/Java/Zapocet/RikiCte.jpg", "/home/anet/MFF/Java/Zapocet/RikiCte.jpg"};
		String[] thirdTuple = {"/home/anet/MFF/Java/Zapocet/spiciRiki.jpg", "/home/anet/MFF/Java/Zapocet/spiciRiki.jpg"};
		String[] forthTuple = {"/home/anet/MFF/Java/Zapocet/produkt-jablko.png", "/home/anet/MFF/Java/Zapocet/produkt-jablko.png"};
		
		String[][] paths = {firstTuple, secondTuple, thirdTuple, forthTuple};*/
		
		PexesoContainerGL pexesoPane = new PexesoContainerGL(pane, 2, 4, "/home/anet/MFF/Java/Zapocet/zadni-strana-pexesa.png", paths);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		
		return pexesoPane;
		
	}
	
	private static PexesoContainerGL loadGame(String pathToObjectFile){
		//TODO load from file
		
		return null; 
	}
	
	
	/* Want to use somewhere!
	 * 
	 * Returns an ImageIcon, or null if the path was invalid.
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = DialogDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }*/
	
	
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
