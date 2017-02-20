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
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;

import javax.swing.*;

public class MainWindow {	
	//private PexesoContainerGL myPexeso;
	private final static int defaultGameWidth = 4;
	private final static int defaultGameHeight = 4;
	private static String backSideImage = "/home/anet/MFF/Java/workspace/Pexeso/src/pexeso/back-side.png";
	
	
	private static void createAndShowGUI() {
		
		// basic window setup
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Pexeso");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ResizeListener());
		JMenuBar menuBar = createMenus(frame);
		
		//System.out.println(pexesoPane.getSize());
		/*Container pane = frame.getContentPane();
		
		String[] firstTuple = {"/home/anet/MFF/Java/workspace/Pexeso/src/pexeso/img0.jpg", "img0.jpg"};
		String[] secondTuple = {"/home/anet/MFF/Java/Zapocet/RikiCte.jpg", "/home/anet/MFF/Java/Zapocet/RikiCte.jpg"};
		String[] thirdTuple = {"/home/anet/MFF/Java/Zapocet/spiciRiki.jpg", "/home/anet/MFF/Java/Zapocet/spiciRiki.jpg"};
		String[] forthTuple = {"/home/anet/MFF/Java/Zapocet/produkt-jablko.png", "/home/anet/MFF/Java/Zapocet/produkt-jablko.png"};
		
		String[][] paths = {firstTuple, secondTuple, thirdTuple, forthTuple};
		
		PexesoContainer pexesoPane = new PexesoContainer(pane, 2, 4, "/home/anet/MFF/Java/Zapocet/zadni-strana-pexesa.png", paths);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		pexesoPane.createNewGame();
		//PexesoContainerGL myPexeso = createNewPexeso();
		
		frame.setContentPane(pexesoPane);*/
		//File file = new File("/home/anet/Tmp/zasilka.txt");
		//PexesoContainer pc = loadGame(frame.getContentPane(), file);
		//pc.createNewGame();
		//frame.setContentPane(pc);
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
		JMenuItem newGameItem, existingGameItem, backSideSetup, quitItem;

		menuBar = new JMenuBar();

		//The main menu
		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "Menu for game options.");
		menuBar.add(menu);
		
		
		//New game item
		newGameItem = new JMenuItem("Create new game",
		                         KeyEvent.VK_T);
		newGameItem.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_N, 
		        java.awt.Event.CTRL_MASK));
		newGameItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a form for new game.");
		newGameItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				SizeForm newGameForm = new SizeForm(defaultGameWidth, defaultGameHeight, frame, backSideImage);
				newGameForm.setVisible(true);
			}
		});
		menu.add(newGameItem);
		
		//Play existing game item
		existingGameItem = new JMenuItem("Load an existing game", KeyEvent.VK_T);
		existingGameItem.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_L, 
		        java.awt.Event.CTRL_MASK));
		existingGameItem.getAccessibleContext().setAccessibleDescription(
		        "Choose an existing game from file.");
		existingGameItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				File file;
				
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		file = fc.getSelectedFile();
		    		PexesoContainer pc = loadGame(frame, file);
		    		if (pc != null) {
		    			System.out.println("Setting the non-null content pane.");
		    			frame.setContentPane(pc);
		    		}
		    		else {
		    			System.out.println("pc is null");
		    			showLoadError(frame);
		    		}
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
		backSideSetup = new JMenuItem("Setup back-side image");
		backSideSetup.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				File file;
				
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		file = fc.getSelectedFile();
		    		String path = file.getAbsolutePath();
		    		
		    		if (imageOk(path)) {
		    			backSideImage = path;
		    		}
		    		else {
		    			System.out.println("Back-side image is wrong.");
		    			showImageError(frame);
		    		}
		   		}
		    	else{
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
	
	private static void showLoadError(JFrame f){
		JOptionPane.showMessageDialog(f,
			    "The game has not been uploaded because there is something wrong with the file." + "Use file created when saving the game or create it in a correct format.",
			    "Unsaved game warning",
			    JOptionPane.WARNING_MESSAGE);
	}
	
	private static void showImageError(JFrame f){
		JOptionPane.showMessageDialog(f,
			    "There is a wrong path to image in the game being loaded. \n Check that that the image path is correct and \n that it contains a valid image. \n"+
			    " Suported image types are JPG, GIF and PNG.",
			    "Image error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	private static PexesoContainer createNewPexeso(Container pane, String[][] paths, int width, int height, String backSideImage){
		
		// manual initialization for testing
		/*String[] firstTuple = {"/home/anet/MFF/Java/Zapocet/RikiJi.jpg", "/home/anet/MFF/Java/Zapocet/RikiJi.jpg"};
		String[] secondTuple = {"/home/anet/MFF/Java/Zapocet/RikiCte.jpg", "/home/anet/MFF/Java/Zapocet/RikiCte.jpg"};
		String[] thirdTuple = {"/home/anet/MFF/Java/Zapocet/spiciRiki.jpg", "/home/anet/MFF/Java/Zapocet/spiciRiki.jpg"};
		String[] forthTuple = {"/home/anet/MFF/Java/Zapocet/produkt-jablko.png", "/home/anet/MFF/Java/Zapocet/produkt-jablko.png"};
		
		String[][] paths = {firstTuple, secondTuple, thirdTuple, forthTuple};*/
		
		PexesoContainer pexesoPane = new PexesoContainer(pane, height, width, backSideImage, paths);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		
		return pexesoPane;
		
	}
	
	private static PexesoContainer loadGame(JFrame frame, File file){
		int width = 0;
		int height = 0;
		String line;
		String[] split;
		String backsideImage = null;
		int lineNum = 1;
		String[][] paths = new String[width*height/2][2];
		
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
					//parsing the path to back-side image
					backsideImage = line;
					lineNum++;
					break;
				
				default:
					//parsing the paths to the image pairs
					split = line.split(";");
					if (split.length != 2){
						return null;
					}
					if (imageOk(split[0])) paths[lineNum-3][0] = split[0];
					else showImageError(frame);
					
					if (imageOk(split[1])) paths[lineNum-3][1] = split[1];
					else showImageError(frame);
					
					lineNum++;
					split[0] = "";
					split[1] = "";
				}
			}
			
			PexesoContainer pc = createNewPexeso(frame.getContentPane(), paths, width, height, backsideImage);
			pc.createNewGame();
			return pc;
		}
		catch(IOException io){
			System.out.println("An IO Exception occured.");
		}
		return null; 
	}
	
	
	/**
	 * 
	 * Returns an ImageIcon, or null if the path was invalid.
	 */
    protected static boolean imageOk(String path) {
    	Path filePath = Paths.get(path);
    	return Files.exists(filePath, LinkOption.NOFOLLOW_LINKS);
    	/*java.net.URL testURL = MainWindow.getResource(path);
        if (testURL != null) {
            return true;
        } else {
            System.err.println("Couldn't find file: " + path);
            return false;
        }*/
    }
	
}
