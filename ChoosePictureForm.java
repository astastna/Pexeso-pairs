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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;



public class ChoosePictureForm extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//TODO enable user to choose own back side image
	private final static String backSideImage = "/home/anet/MFF/Java/workspace/Pexeso/src/pexeso/back-side.png";
	
	int gameWidth;
	int gameHeight;
	JFrame original;
	ChoosePictureForm currentWindow;
	Container pane;
	PexesoContainer chosenPexesoPane;
	String[][] paths;
	
	public ChoosePictureForm(int width, int height, JFrame orig){
		this.gameWidth = width;
		this.gameHeight = height;
		this.pane = this.getContentPane();
		this.original = orig;
		this.currentWindow = this;
		
		this.paths = new String[gameWidth*gameHeight/2][2];
		for (int i=0; i < gameWidth*gameHeight/2; i++){
			paths[i][0] = "";
			paths[i][1] = "";
		}
		
		initializeChoosePictureForm();
	}
	
	private JDialog showImageError(){
		JOptionPane.showMessageDialog(currentWindow,
			    "Not enough images loaded. \n Please, use all the buttons for choosing the images. \n If you want to change the size of game, use the \"Back\" button.",
			    "Absence of image error",
			    JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	private JDialog showInvalidImageError(){
		JOptionPane.showMessageDialog(currentWindow,
			    "This image seems to be invalid. \n Please, use another one. \n Remember, that supported format are only jpg, png and gif.",
			    "Invalid image error",
			    JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
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
				System.out.println("Output should be succesfully rewritten.");
			}
		}
		else{
			//file doesn't exist
			writeDataToFile(file);
			System.out.println("Output should be succesfully written.");
		}
	}
	
	private void writeDataToFile(File file){
		try{
			Charset utf8 = StandardCharsets.UTF_8;
			List<String> lines = getDataIntoList();
			Files.write(Paths.get(file.getAbsolutePath()), lines, utf8);
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
	
	private List<String> getDataIntoList(){
		ArrayList<String> list = new ArrayList<String>();
		
		//add width and height to the file
		String dimen = ((Integer) gameWidth).toString() + ";" + ((Integer) gameHeight).toString();
		list.add(dimen);
		
		//add path to back side image to the file
		list.add(backSideImage);
		
		//create string with matching pathnames
		for (int k = 0; k < gameWidth*gameHeight/2; k++){
			list.add(paths[k][0] + ";" + paths[k][1]);
		}
		
		return list;
	}
	
	/**
	 * This function creates a two-dimensional string array with paths
	 * to default files. The size of the array depends on the size of the game.
	 * 
	 * @return	two-dimensional string array with paths to default files
	 */
	private void defaultImages(){
	int pairs = gameWidth*gameHeight/2;
		
		//get path in order to make pictures working
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		for (int i = 0; i < pairs; i++){
			paths[i][0] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			paths[i][1] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			//System.out.println("Saving path "+ paths[i][0] );
		}
		
	}
	
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
	 * @return	string {@link actionCommand} from the selected button
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
			System.out.println("paths["+ ((Integer)k).toString() + "][0]:" + paths[k][0] + 
					"; paths["+ ((Integer)k).toString() + "][1]:" + paths[k][1]);
			if (paths[k][0] == "" || paths[k][1] == ""){
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Function for adding {@link JFileChoosers}. This function creates two file choosers 
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
			    		System.out.println("DiffPairs: Adding file "+ file.getAbsolutePath() +" to paths["+((Integer)(j/2)).toString() +"]["+ ((Integer)(j%2)).toString()+"]");
			   			paths[j/2][j%2] = file.getAbsolutePath();
			   		}
				}
			
			});
		}
		
	}
	
	/**
	 * Function for adding {@link JFileChoosers}. This function creates a file chooser 
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
			System.out.println("Adding button for image pair "+ ((Integer)(j/2)).toString());
			//pane.add(chooserBox[j/2]);
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
		//defaultImg.setAlignmentX(Component.CENTER_ALIGNMENT);
		defaultImg.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				defaultImages();
				PexesoContainer newGame = prepareNewGame(paths);
				chosenPexesoPane = newGame;
				//won't be done here
				//original.setContentPane(newGame);
				//currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
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
		
		//customImgPairs.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		
		//customImgDiff.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(customImgDiff);
		pane.add(boxForFiles2);
		
		//make it possible to choose only one button
		final ButtonGroup buttGroup = new ButtonGroup();
		buttGroup.add(defaultImg);
		buttGroup.add(customImgPairs);
		buttGroup.add(customImgDiff);
		
		//Back button
		JButton back = new JButton("Back");
		//back.setAlignmentX(Component.LEFT_ALIGNMENT);
		//back.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		back.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//System.out.println("Size form calling: "+ ((Integer) gameWidth).toString() + " , " + ((Integer) gameHeight).toString() ) ;
				SizeForm changeSize = new SizeForm(gameWidth, gameHeight, original);
				changeSize.setVisible(true);
				currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		//Save button
		JButton save = new JButton("Save this new game");
		//save.setAlignmentX(Component.LEFT_ALIGNMENT);
		//save.setAlignmentY(Component.BOTTOM_ALIGNMENT);
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
		//finish.setAlignmentX(Component.RIGHT_ALIGNMENT);
		//finish.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		finish.addActionListener(new ActionListener() {
			
			public void actionPerformed (ActionEvent e){
				
				String actionCommand = getSelectedButton(buttGroup);
				System.out.println("Action command: " + actionCommand);
				
				switch (actionCommand){
				
				case "default":
					//all work already done when pressing the radio button
					System.out.println("Default game chosen.");
					break;
					
				case "customPairs":
					//choose-file fields already created
					//and take the paths from them and use them to create new game
					if (pathNamesExist()){
						PexesoContainer customPairs = prepareNewGame(paths);
						chosenPexesoPane = customPairs;
						if (customPairs == null) showInvalidImageError();
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
					}
					else{
						showImageError();
					}
					break;
					
				default:
					System.out.println("It doesn't work this way.");
				}
				
				//view created game
				original.setContentPane(chosenPexesoPane);
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
