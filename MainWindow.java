package pexeso;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;

public class MainWindow {	
	/**
	 * 
	 */
	
	private final static int defaultGameWidth = 4;
	private final static int defaultGameHeight = 4;
	private static String backSideImage = "/home/anet/MFF/Java/workspace/Pexeso/src/pexeso/back-side.png";
	
	
	private static void createAndShowGUI() {
		
		// basic window setup
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Pexeso");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ResizeListener());
		
		//create the menu's
		JMenuBar menuBar = createMenus(frame);
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

	/**
	 * This function creates the main menu's for the whole game.
	 * 
	 * @param frame	The main window of application. 
	 * @return menuBar	The resulting menu bar.
	 */
	public static JMenuBar createMenus(final JFrame frame){
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem newGameItem, existingGameItem, backSideSetup, quitItem;

		menuBar = new JMenuBar();

		//The main menu
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "Menu for game options.");
		menuBar.add(menu);
		
		
		//New game item
		newGameItem = new JMenuItem("Create new game");
		//Set the keyboard shortcut
		newGameItem.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_N, 
		        java.awt.Event.CTRL_MASK));
		newGameItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a form for new game.");
		newGameItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//Run the input forms for creating of a new game
				SizeForm newGameForm = new SizeForm(defaultGameWidth, defaultGameHeight, frame, backSideImage);
				newGameForm.setVisible(true);
			}
		});
		menu.add(newGameItem);
		
		//Play existing game item
		existingGameItem = new JMenuItem("Load an existing game");
		//Set the keyboard shortcut
		existingGameItem.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_L, 
		        java.awt.Event.CTRL_MASK));
		existingGameItem.getAccessibleContext().setAccessibleDescription(
		        "Choose an existing game from file.");
		existingGameItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//Show a file chooser
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				File file;
				
				//Process the given file
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		file = fc.getSelectedFile();
		    		PexesoContainer pc = loadGame(frame, file);
		    		if (pc != null) {
		    			frame.setContentPane(pc);
		    		}
		    		else {
		    			showLoadError(frame);
		    		}
		    		
		    		//update the frame
		    		JMenuBar menuBar = createMenus(frame);
		    		frame.setJMenuBar(menuBar);
		    		frame.validate();
		    		frame.pack();
		   		}
		    	else{
		    		JOptionPane.showMessageDialog(frame,
		    			    "The game has not been uploaded because no name for the input file has been given.",
		    			    "No input file warining",
		    			    JOptionPane.WARNING_MESSAGE);
		    	}
			}
		});
		menu.add(existingGameItem);
		
		//Back-side image setup
		backSideSetup = new JMenuItem("Set back-side image");
		//Set the keyboard shortcut
		backSideSetup.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_B, 
		        java.awt.Event.CTRL_MASK));
		backSideSetup.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//Show a file chooser
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				File file;
				
				//Process the given image
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		file = fc.getSelectedFile();
		    		String path = file.getAbsolutePath();
		    		
		    		//Check, that the image is correct
		    		if (imageOk(path)) {
		    			backSideImage = path;
		    		}
		    		else {
		    			System.out.println("Back-side image is wrong.");
		    			showImageError(frame);
		    		}
		   		}
		    	else{
		    		//Show a warning
		    		JOptionPane.showMessageDialog(frame,
		    			    "The image chosen isn't correct.",
		    			    "Wrong input image warining",
		    			    JOptionPane.WARNING_MESSAGE);
		    	}
			}
		});
		menu.add(backSideSetup);
		
		
		//Quit
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//close frame
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		menu.add(quitItem);
		
		// About menu
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);
		
		//Author item
		JMenuItem authorItem = new JMenuItem("Author");
		authorItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//show dialog with information about the author
				JOptionPane.showMessageDialog(frame,
					    "The author of this application is Aneta Šťastná. It has been done as a school project \n " +
					    "on the Faculty of Mathematics and Physics, Charles University, \n" +
					    "and is available on https://github.com/astastna." +
					    "This aplication can be used under the Creative Commons licence.");

			}
		});
		aboutMenu.add(authorItem);
		
		//Default pictures
		JMenuItem picturesItem = new JMenuItem("Pictures");
		picturesItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//show dialog with information about the author
				JOptionPane.showMessageDialog(frame,
					    "The default pictures in this game come from pixabay.com. \n");

			}
		});
		aboutMenu.add(picturesItem);
		
		return menuBar;
	}
	
	/** Shows an error dialog with message about wrong format of uploaded file.
	 * 
	 * @param f	The main window of application. 
	 */
	private static void showLoadError(JFrame f){
		JOptionPane.showMessageDialog(f,
			    "The game has not been uploaded because there is something wrong with the file." + "Use file created when saving the game or create it in a correct format.",
			    "Unsaved game warning",
			    JOptionPane.WARNING_MESSAGE);
	}
	
	/** Shows an error dialog with message about wrong path to image given.
	 * 
	 * @param f	The main window of application. 
	 */
	private static void showImageError(JFrame f){
		JOptionPane.showMessageDialog(f,
			    "There is a wrong path to image in the game being loaded. \n Check that that the image path is correct and \n that it contains a valid image. \n"+
			    " Suported image types are JPG, GIF and PNG.",
			    "Image error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Creates new {@link PexesoContainer} with playable game according to given parameters of the game.
	 * 
	 * @param pane	{@link Container}, in which the game will be shown. 
	 * @param paths	{@code String[number of pairs][2]} with paths to images to play with. Images, which 
	 * are together in the array of size 2 form pairs.
	 * @param width	The width of the game board, which is number of cards in rows.
	 * @param height	The height of the game board, which is number of cards in columns.
	 * @param backSideImage	The image that will be shown on the back side of the cards.
	 * @return pexesoPane	{@link PexesoContainer} with new game.
	 */
	private static PexesoContainer createNewPexeso(Container pane, String[][] paths, int width, int height, String backSideImage){
		
		//create new PexesoContainer
		PexesoContainer pexesoPane = new PexesoContainer(pane, height, width, backSideImage, paths);
		//set the size of Container
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		
		return pexesoPane;
		
	}
	
	/**
	 * Creates {@link PexesoContainer} with playable game. The parameters of the game are taken from 
	 * the file. The file has to be in text format with following structure:
	 * width;height
	 * path-to-back-side-picture
	 * path-to-first-image-of-pair1;path-to-second-image-of-pair1
	 * path-to-first-image-of-pair2;path-to-second-image-of-pair2
	 * ...
	 * path-to-first-image-of-pair(width*height/2);path-to-second-image-of-pair(width*height/2)
	 * 
	 * 
	 * @param frame The main window of application. 
	 * @param file	Input text file with parameters of game.
	 * @return pexesoPane	{@link PexesoContainer} with new game or null if there has been an error.
	 */
	private static PexesoContainer loadGame(JFrame frame, File file){
		int width = 0;
		int height = 0;
		String line;
		String[] split;
		String backsideImage = null;
		int lineNum = 1;
		String[][] paths = new String[width*height/2][2];
		boolean everythingOk = true;
		
		try{
			//open the file and get buffered reader
			Charset utf8 = StandardCharsets.UTF_8;
			BufferedReader bufr = Files.newBufferedReader(file.toPath(), utf8);
			
			//read and parse the information from file
			while((line = bufr.readLine()) != null){
				
				switch(lineNum){
				case 1: 
					//parsing width and height on the first line
					split = line.split(";");
					if (split.length != 2){
						everythingOk = false;
						return null;
					}
					width = Integer.parseInt(split[0]);
					height = Integer.parseInt(split[1]);
					paths = new String[(width*height+1)/2][2];
					split[0] = "";
					split[1] = "";
					lineNum++;
					break;
				
				
				case 2:
					//parsing and checking the path to back-side image
					if (imageOk(line)) backsideImage = line;
					else {
						everythingOk = false;
						showImageError(frame);
					}
					
					lineNum++;
					break;
				
				default:
					//parsing and checking the paths to the image pairs
					split = line.split(";");
					if (split.length != 2){
						return null;
					}
					if (imageOk(split[0])) paths[lineNum-3][0] = split[0];
					else {
						everythingOk = false;
						showImageError(frame);
					}
					
					if (imageOk(split[1])) paths[lineNum-3][1] = split[1];
					else {
						everythingOk = false;
						showImageError(frame);
					}
					
					lineNum++;
					split[0] = "";
					split[1] = "";
				}
			}
			
			//creating the PexesoContainer from the parsed parameters
			if (everythingOk) {
				PexesoContainer pc = createNewPexeso(frame.getContentPane(), paths, width, height, backsideImage);
				pc.createNewGame();
				return pc;
			}
			else {
				return null;
			}
		}
		catch(IOException io){
			System.out.println("An IO Exception occured.");
		}
		return null; 
	}
	
	
	/**
	 * 
	 * Checks if the String is valid path to a file.
	 * 
	 * @param path String which should be path to the image file.
	 * @return true if exist a file with given path
	 */
    protected static boolean imageOk(String path) {
    	Path filePath = Paths.get(path);
    	return Files.exists(filePath, LinkOption.NOFOLLOW_LINKS);
    }
	
}
