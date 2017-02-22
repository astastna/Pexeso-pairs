package pexeso;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;



public class ChoosePictureForm extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String backSideImage; //current back side image (for this game)
	int gameWidth;
	int gameHeight;
	JFrame original; //the main window of the application
	ChoosePictureForm currentWindow; //this window
	Container pane;
	PexesoContainer chosenPexesoPane; //game board
	String[][] paths; //paths to the front images
	
	public ChoosePictureForm(int width, int height, JFrame orig, String backSideImage){
		this.gameWidth = width;
		this.gameHeight = height;
		this.pane = this.getContentPane();
		this.original = orig;
		this.currentWindow = this;
		this.backSideImage = backSideImage;
		
		this.paths = new String[gameWidth*gameHeight/2][2];
		for (int i=0; i < gameWidth*gameHeight/2; i++){
			paths[i][0] = "";
			paths[i][1] = "";
		}
		
		initializeChoosePictureForm();
	}
	
	/**
	 * Shows an error dialog with message about lack of given images.
	 */
	private void showImageError(){
		JOptionPane.showMessageDialog(currentWindow,
			    "Not enough images loaded. \n Please, use all the buttons for choosing the images. \n If you want to change the size of game, use the \"Back\" button.",
			    "Absence of image error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Shows an error dialog with message about wrong picture.
	 */
	private void showInvalidImageError(){
		JOptionPane.showMessageDialog(currentWindow,
			    "This image seems to be invalid. \n Please, use another one. \n Remember, that supported format are only jpg, png and gif.",
			    "Invalid image error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Flushes the data from user to a file, which is given as the parameter.
	 * The format of the file is human readable/editable and can be used to
	 * quickly load the same game again.
	 * <p>
	 * This function is rather a shell with a GUI and uses a different 
	 * function to do the file and data manipulation.
	 * 
	 * 
	 * @param file	File to which the data should be given.
	 */
	private void flushNewGameDataIntoFile(File file){
		//open file
		if (file.exists()){
			//ask user whether to overwrite the file
			int userOutput = JOptionPane.showConfirmDialog(currentWindow,
				    "The specified file already exists.\n"
				    + "Are you sure that you want to overwrite it's contents?",
				    "File already exists",
				    JOptionPane.YES_NO_OPTION); 
			//overwrite it if user agrees
			if (userOutput == JOptionPane.YES_OPTION){
				writeDataToFile(file);
			}//else don't do anything, because user don't want to override the file
		}
		else{
			//file doesn't exist
			writeDataToFile(file);
		}
	}
	
	/**
	 * Writes the game parameters to a specified file.
	 * This function really handles with writing to the file.
	 * 
	 * @param file	File to which the data should be given.
	 */
	private void writeDataToFile(File file){
		try{
			Charset utf8 = StandardCharsets.UTF_8;
			List<String> lines = getDataIntoList(); //here the data about the game are processed into the right format
			Files.write(Paths.get(file.getAbsolutePath()), lines, utf8); //writing the list with strings into a file
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(currentWindow,
				    "An IO Exception occured. \n"+
				    "Trying again with different file might help. \n"+
				    "Please note that the game has not been saved.",
				    "IO Exception warning",
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * This function collects the game parameters specified by the user 
	 * and flushes them into the file.
	 * 
	 * @return {@link ArrayList} with lines of the output file.
	 */
	private List<String> getDataIntoList(){
		ArrayList<String> list = new ArrayList<String>();
		
		//add width and height to the file
		String dimen = ((Integer) gameWidth).toString() + ";" + ((Integer) gameHeight).toString();
		list.add(dimen);
		
		//add path to back side image to the file
		list.add(backSideImage);
		
		//create string with matching pathnames
		if (pathNamesExist()){
			for (int k = 0; k < gameWidth*gameHeight/2; k++){
				list.add(paths[k][0] + ";" + paths[k][1]);
			}
		}
		else{
			showImageError();
		}
		
		return list;
	}
	
	/**
	 * This function creates a two-dimensional string array with paths
	 * to default files. The size of the array depends on the size of the game.
	 * The resulting two-dimensional string array with paths to default files 
	 * is saved as the attribute.
	 */
	private void defaultImages(){
	int pairs = gameWidth*gameHeight/2;
		
		//get path in order to make pictures working
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		//save the paths to files
		for (int i = 0; i < pairs; i++){
			paths[i][0] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			paths[i][1] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
		}
		
	}
	
	/**
	 * Creating a new {@link PexesoContainer} from parameters.
	 * Almost all needed parameters are attributes of this object, only paths
	 * to the front images are specified.
	 * 
	 * @param images	Paths (Strings) to the front images.
	 * @return	pexesoPane	Returns new {@link PexesoContainer}, which is playable.
	 */
	private PexesoContainer prepareNewGame(String[][] images){
		PexesoContainer pexesoPane = new PexesoContainer(pane, gameWidth, gameHeight, backSideImage, images);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		pexesoPane.createNewGame();
		
		return pexesoPane;
	}
	
	/**
	 * Returns action command from currently selected {@link AbstractButton}. 
	 * 
	 * @param buttonGroup {@link ButtonGroup} in which the selected button is searched
	 * @return	string {@code actionCommand} from the selected button
	 */
	public String getSelectedButton(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getActionCommand();
            }
        }

        return null;
    }
	
	/**
	 * Function to check that all paths to files have been filled in.
	 * 
	 * @return	all file paths are/aren't filled in
	 */
	private boolean pathNamesExist(){
		
		for (int k = 0; k < gameWidth*gameHeight/2; k++){
			if (paths[k][0] == "" || paths[k][1] == ""){
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Function for adding {@link JFileChooser}. This function creates two file choosers 
	 * for every pair of images, because two of them are needed, as the images 
	 * in pairs aren't the same.
	 * <p>
	 * File choosers are added to the box specified in the parameter. 
	 * This way the final position of the file chooser can be specified by the 
	 * caller of this function.
	 *
	 * @param box	box to which created file choosers will be added
	 */
	private void addButtonsForAllImages(Box box){
		
		JButton[] fileChoosers = new JButton[gameWidth*gameHeight];
		final JFileChooser[] fc = new JFileChooser[gameWidth*gameHeight];
		Box[] chooserBox = new Box[gameWidth*gameHeight/2];
		
		for (int j=0; j<gameHeight*gameWidth; j+=2){
			//create buttons with file choosers
			fileChoosers[j] = new JButton("Image 1 of pair "+ ((Integer)((j+1)/2)).toString() );
			fileChoosers[j].setActionCommand(((Integer) j).toString());
			fileChoosers[j+1] = new JButton("Image 2 of pair "+ ((Integer)((j+1)/2)).toString() );
			fileChoosers[j+1].setActionCommand( ((Integer) (j+1)).toString());
			
			//group buttons for a pair together on one line
			chooserBox[j/2] = Box.createHorizontalBox();
			chooserBox[j/2].add(Box.createHorizontalGlue());
			chooserBox[j/2].add(fileChoosers[j]);
			chooserBox[j/2].add(Box.createHorizontalGlue());
			chooserBox[j/2].add(fileChoosers[j+1]);
			chooserBox[j/2].add(Box.createHorizontalGlue());
			box.add(chooserBox[j/2]);
			currentWindow.pack();
		}
		
		for (int j = 0; j < gameHeight*gameWidth; j++){
			fc[j] = new JFileChooser();
			
			fileChoosers[j].addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int j = Integer.parseInt(e.getActionCommand());
			    	int returnVal = fc[j].showOpenDialog(currentWindow);

			    	if (returnVal == JFileChooser.APPROVE_OPTION) {
			    		File file = fc[j].getSelectedFile();
			   			paths[j/2][j%2] = file.getAbsolutePath();
			   		}
				}
			
			});
		}
		
	}
	
	/**
	 * Function for adding {@link JFileChooser}. This function creates a file chooser 
	 * for every pair of images, because only one is needed, as 
	 * the images in pair are the same.
	 * <p>
	 * File choosers are added to the box specified in the parameter. 
	 * This way the final position of the file chooser can be specified by the 
	 * caller of this function.
	 *
	 * @param box	box to which created file choosers will be added
	 */
	private void addButtonsForPairs(Box box){
		JButton[] fileChoosers = new JButton[gameWidth*gameHeight/2];
		final JFileChooser[] fc = new JFileChooser[gameWidth*gameHeight/2];
		Box[] chooserBox = new Box[gameWidth*gameHeight/2];
		
		for (int j=0; j < gameHeight*gameWidth; j+=2){
			//create buttons with file choosers
			fileChoosers[j/2] = new JButton("Image pair "+ ((Integer)(j/2)).toString() );
			fileChoosers[j/2].setActionCommand(((Integer) (j/2)).toString());
			
			//give the button into a box to get it into center
			chooserBox[j/2] = Box.createHorizontalBox();
			chooserBox[j/2].add(Box.createHorizontalGlue());
			chooserBox[j/2].add(fileChoosers[j/2]);
			chooserBox[j/2].add(Box.createHorizontalGlue());
			box.add(chooserBox[j/2]);
			currentWindow.pack();
		}
		
		for (int j = 0; j < gameHeight*gameWidth/2; j++){
			fc[j] = new JFileChooser();
			
			//add action - save path to image file after choosing
			fileChoosers[j].addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int j = Integer.parseInt(e.getActionCommand());
			    	int returnVal = fc[j].showOpenDialog(currentWindow);

			    	if (returnVal == JFileChooser.APPROVE_OPTION) {
			    		File file = fc[j].getSelectedFile();
			    		System.out.println("SamePairs: Adding file "+ file.getAbsolutePath() +" to paths["+((Integer)(j)).toString() +"][0] "+ 
			    				"and paths["+((Integer)(j/2)).toString() +"][1].");
			    		//adding the image twice for both cards of the pair
			   			paths[j][0] = file.getAbsolutePath();
			   			paths[j][1] = file.getAbsolutePath();
			   		}
				}
			
			});
		}
		
	}
	
	/**
	 * Sets all the labels, text fields, texts and buttons to the form.
	 */
	private void initializeChoosePictureForm(){
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		
		//header
		JLabel choose = new JLabel("Images");
		choose.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(choose);
		
		//description
		JLabel chooseDescr = new JLabel("Choose whether you want to play with default images or with your own images.");
		pane.add(chooseDescr);
		
		JLabel chooseDescr2 = new JLabel(" 0nly pictures in format png, gif and jpg are allowed.");
		pane.add(chooseDescr2);
		
		//default radio button
		JRadioButton defaultImg = new JRadioButton("Use default images");
		defaultImg.setActionCommand("default");
		defaultImg.setSelected(true);
		defaultImg.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//everything will be done after pressing Finish button
			}
		});
		
		pane.add(defaultImg);
		
		//Vertical space
		Component strut1 = Box.createVerticalStrut(10);
		pane.add(strut1);
		
		//adding boxes enabling to show choosers on the right places
		final Box boxForFiles = Box.createVerticalBox();
		final Box boxForFiles2 = Box.createVerticalBox();
		
		//custom radio button - same pairs
		JRadioButton customImgPairs = new JRadioButton("Use custom images - pairs contain same images");
		customImgPairs.setActionCommand("customPairs");
		customImgPairs.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				//remove existing buttons for choosing of images from second button
				boxForFiles.removeAll();
				boxForFiles2.removeAll();
				//add buttons for choosing of the images
				addButtonsForPairs(boxForFiles);
			}
			
		});
		
		pane.add(customImgPairs);
		pane.add(boxForFiles);
		
		//Vertical space
		Component strut2 = Box.createVerticalStrut(10);
		pane.add(strut2);
		
		//custom radio button - different pairs
		JRadioButton customImgDiff = new JRadioButton("Use custom images - pairs contain different images");
		customImgDiff.setActionCommand("customDiff");
		customImgDiff.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				//add buttons for choosing of the images
				boxForFiles.removeAll();
				boxForFiles2.removeAll();
				addButtonsForAllImages(boxForFiles2);
			}
			
		});
		
		pane.add(customImgDiff);
		pane.add(boxForFiles2);
		
		//make it possible to choose only one button
		final ButtonGroup buttGroup = new ButtonGroup();
		buttGroup.add(defaultImg);
		buttGroup.add(customImgPairs);
		buttGroup.add(customImgDiff);
		
		
		//Back button
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				SizeForm changeSize = new SizeForm(gameWidth, gameHeight, original, backSideImage);
				changeSize.setVisible(true);
				currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		//Save button
		JButton save = new JButton("Save this new game");
		save.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e){
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(currentWindow);
				File file;
				
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		file = fc.getSelectedFile();
		    		if (pathNamesExist()){
						flushNewGameDataIntoFile(file);
					}
					else{
						showImageError();
					}
		   		}
		    	else{
		    		JOptionPane.showMessageDialog(currentWindow,
		    			    "The game has not been saved because no name for the output file has been given.",
		    			    "Unsaved game warning",
		    			    JOptionPane.WARNING_MESSAGE);
		    	}
			}
		});
		
		//Finish button
		JButton finish = new JButton("Finish");
		finish.addActionListener(new ActionListener() {
			
			public void actionPerformed (ActionEvent e){
				
				String actionCommand = getSelectedButton(buttGroup);
				System.out.println("Action command: " + actionCommand);
				
				switch (actionCommand){
				
				case "default":
					defaultImages();
					PexesoContainer newGame = prepareNewGame(paths);
					if (newGame == null){
						System.out.println("prepareNewGame returned null.");
						JOptionPane.showMessageDialog(currentWindow,
		    			    "Something went wrong with creation of the new game.",
		    			    "Unknown error",
		    			    JOptionPane.ERROR_MESSAGE);}
					else {
						chosenPexesoPane = newGame;
					}
					
					//view created game
					original.setContentPane(chosenPexesoPane);
					
					break;
					
				case "customPairs":
					//choose-file fields already created
					//and take the paths from them and use them to create new game
					if (pathNamesExist()){
						PexesoContainer customPairs = prepareNewGame(paths);
						chosenPexesoPane = customPairs;
						if (customPairs == null) showInvalidImageError();
						else {//view created game
							original.setContentPane(chosenPexesoPane);
						}
					}
					else{
						showImageError();
					}
					break;
					
				case "customDiff":
					//choose-file fields already created
					//and take the paths from them and use them to create new game
					if (pathNamesExist()){
						PexesoContainer customDiff = prepareNewGame(paths);
						chosenPexesoPane = customDiff;
						if (customDiff == null) showInvalidImageError();
						else {//view created game
							original.setContentPane(chosenPexesoPane);
						}
					}
					else{
						showImageError();
					}
					break;
					
				default:
					System.out.println("It doesn't work this way.");
				}
				
				//update the main frame
	    		JMenuBar menuBar = MainWindow.createMenus(original);
	    		original.setJMenuBar(menuBar);
	    		original.validate();
	    		original.pack();
				
				//close window
				currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		//Vertical space
		Component strut3 = Box.createVerticalStrut(10);
		pane.add(strut3);
		
		//Button box
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(save);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(finish);
		buttonBox.add(Box.createHorizontalGlue());
		pane.add(buttonBox);
		
		this.pack();
	}

}
