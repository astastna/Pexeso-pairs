package pexeso;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

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
	int gameWidth;
	int gameHeight;
	JFrame original;
	ChoosePictureForm currentWindow;
	Container pane;
	PexesoContainerGL chosenPexesoPane;
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
	
	private String[][] defaultImages(){
	int pairs = gameWidth*gameHeight/2;
		
		String[][] paths = new String[pairs][2];
		
		//get path in order to make pictures working
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		for (int i = 0; i < pairs; i++){
			paths[i][0] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			paths[i][1] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			//System.out.println("Saving path "+ paths[i][0] );
		}
		
		return paths;
	}
	
	private PexesoContainerGL prepareNewGame(String[][] images){
		//TODO change the back side of card to be absolute and not so hard-coded
		PexesoContainerGL pexesoPane = new PexesoContainerGL(pane, gameWidth, gameHeight, "zadni-strana-pexesa.png", images);
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
			    		System.out.println("Adding file "+ file.getAbsolutePath() +" to String["+((Integer)(j/2)).toString() +"]["+ ((Integer)(j%2)).toString()+"]");
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
			    		System.out.println("Adding file "+ file.getAbsolutePath() +" to String["+((Integer)(j/2)).toString() +"]["+ ((Integer)(j%2)).toString()+"]");
			    		//adding the image twice for both cards of the pair
			   			paths[j/2][0] = file.getAbsolutePath();
			   			paths[j/2][1] = file.getAbsolutePath();
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
				String[][] images = defaultImages();
				PexesoContainerGL newGame = prepareNewGame(images);
				chosenPexesoPane = newGame;
				//won't be done here
				//original.setContentPane(newGame);
				//currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		pane.add(defaultImg);
		
		//adding boxes enabling to show choosers on the right places
		final Box boxForFiles = Box.createVerticalBox();
		final Box boxForFiles2 = Box.createVerticalBox();
		
		//custom radio button - same pairs
		JRadioButton customImgPairs = new JRadioButton("Use custom images - pairs contain same images");
		customImgPairs.setActionCommand("customPairs");
		customImgPairs.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				//remove existing buttons for choosing of images from second button
				boxForFiles2.removeAll();
				//add buttons for choosing of the images
				addButtonsForPairs(boxForFiles);
			}
			
		});
		
		//customImgPairs.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(customImgPairs);
		pane.add(boxForFiles);
		
		//custom radio button - different pairs
		JRadioButton customImgDiff = new JRadioButton("Use custom images - pairs contain different images");
		customImgDiff.setActionCommand("customDiff");
		customImgDiff.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				//add buttons for choosing of the images
				boxForFiles.removeAll();
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
				//TODO check file paths and then flush in a text format into a file
			}
		});
		
		//Finish button
		JButton finish = new JButton("Finish");
		//finish.setAlignmentX(Component.RIGHT_ALIGNMENT);
		//finish.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		finish.addActionListener(new ActionListener() {
			
			//TODO check that enough files were written
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
						PexesoContainerGL customPairs = prepareNewGame(paths);
						chosenPexesoPane = customPairs;
					}
					else{
						showImageError();
					}
					break;
					
				case "customDiff":
					//choose-file fields already created
					//and take the paths from them and use them to create new game
					if (pathNamesExist()){
						PexesoContainerGL customDiff = prepareNewGame(paths);
						chosenPexesoPane = customDiff;
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
		
		//Button box
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(save);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(finish);
		pane.add(buttonBox);
		
		this.pack();
	}

}
