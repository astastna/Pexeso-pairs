package pexeso;


import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;


public class PexesoContainer extends Container {
	public static final String pathToWinImage = "res/win.png";
	
	int fieldsCnt; //number of fields
	int notFoundPairsCnt; //counter for pairs, which weren't matched yet
	int stepNr; //variable for number of tuple of cards shown needed to complete the game
	int rows;
	int columns;
	JPexesoCard[] turned; //buffer of pressed pexeso cards
	ImageIcon backPicture; //back side of the cards
	ImageIcon winningIcon; //winning icon
	ImageIcon[][] frontPictureTuples;
	JPexesoCard[] cards;
	
	private static final long serialVersionUID = 1L;
	
	public PexesoContainer(Container container, int rows, int columns, ImageIcon backSidePicture, ImageIcon[][] iconsWithFrontPictures){
		this.fieldsCnt = rows*columns;
		this.rows = rows;
		this.columns = columns;
		this.notFoundPairsCnt = fieldsCnt/2;
		this.stepNr = 0;
		this.turned = new JPexesoCard[2];
		URL winImageURL = PexesoContainer.class.getClassLoader().getResource(pathToWinImage);
		
		Image big = (new ImageIcon(winImageURL).getImage());
		Image smaller = big.getScaledInstance(100, 100, 100);
		this.winningIcon = new ImageIcon(smaller);
		 
		backPicture = backSidePicture;
		
		//processing the paths to pictures
		frontPictureTuples = new ImageIcon[rows*columns/2][2];
		for (int i=0; i < rows*columns/2; i++){
			for (int j=0; j < 2; j++){
				if (picturePathOk(iconsWithFrontPictures[i][j])){
					frontPictureTuples[i][j] = iconsWithFrontPictures[i][j];
				}
				else System.out.println("Picture path not ok: "+ iconsWithFrontPictures[i][j]);
			}
		}
	}

	/**
	 * Prepares the {@link PexesoContainer} for starting of new game. 
	 * Places shuffled cards on the game board and adds the click processing logic to them. 
	 * 
	 */
	public void createNewGame(){

		//Setup layout
		GridLayout layout = new GridLayout(rows, columns, 0, 0);
		this.setLayout(layout);
		
		//initializing cards and placing them on the game board
		cards = new JPexesoCard[fieldsCnt];
		String[] description = new String[fieldsCnt];
		
		//prepare shuffled field
		ArrayList<Integer> shuffled = new ArrayList<Integer>();
		for (int k = 0; k < fieldsCnt; k++){
			shuffled.add(k);
		}
		Collections.shuffle(shuffled);
		
		for (int k = 0; k < fieldsCnt; k++){
			//use k from shuffled array for the card
			int randK = shuffled.get(k);
			
			//back-picture handling
			cards[k] = new JPexesoCard(frontPictureTuples[randK/2][randK%2], randK/2 , this, randK%2);
			//version without shuffling for testing
			//cards[k] = new JPexesoCard(frontPictureTuples[k/2][k%2], k/2 , this, k%2);
			
			//adds the logic - what happens after clicking
			MouseListener onClick = new clickProcessor(this, cards[k]);
			cards[k].getButton().addMouseListener(onClick);
			
			description[k] = "("+((Integer)columns).toString()+";"+((Integer)rows).toString()+")";
			layout.addLayoutComponent(description[k], cards[k]);
			
			this.add(cards[k].getButton());
		}
	}
	
	public void updateAllCards(){
		for (int k = 0; k < fieldsCnt; k++){
			cards[k].turnToBackSide();
		}
		
		for (int k = 0; k < 2; k++){
			if (turned[k] != null) turned[k].turnToFrontSide();
		}
	}
	
	/**
	 * Sets cards from the field to be turned up.
	 * 
	 * @param cards	Array with cards which should be turned up.
	 */
	public void setTurnedCards(JPexesoCard[] cards){
		turned = cards;
	}
	
	/**
	 * Gives the information about cards which are turned up.
	 * 
	 * @return turned	Array of cards which are currently turned up.
	 */
	public JPexesoCard[] getTurnedCards(){
		return turned;
	}
	
	/**
	 * Gives the information if all but one pairs are already found.
	 * 
	 * @return true if there is only one pair left
	 */
	public boolean lastPairRemains(){
		if (notFoundPairsCnt == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Decreases the notFoundPairs attribute of {@link PexesoContainer}.
	 */
	public void decreaseNotFound(){
		notFoundPairsCnt -= 1;
	}
	
	/**
	 * Increments the stepNr attribute of {@link PexesoContainer}.
	 */
	public void addStep(){
		stepNr++;
	}
	

	/**
	 * Returns the stepNr attribute of {@link PexesoContainer}.
	 *
	 *@return stepNr	Number of doubles of images needed to be shown till now in the game.
	 */
	public int getStepsNum(){
		return stepNr;
	}
	
	/**
	 * Returns icon to be used in the winning dialog.
	 * 
	 * @return	winningIcon	Returns the icon to show to the winning player.
	 */
	public ImageIcon getWinningIcon(){
		return winningIcon;
	}
	
	/**
	 * This function gives information whether the path ends with .jpg, .gif or .png,
	 * which are the supported image formats. It DOESN'T CHECK that the file contains
	 * data in the specified format.
	 * 
	 * @param icon	ImageIcon with image.
	 * @return	True if the path ends with .jpg, .gif or .png.
	 */
	private boolean picturePathOk(ImageIcon icon){
		if (icon == null) return false;
		
		String fileString = icon.getDescription();
		if (fileString.equals(null)) return false;
		
		String[] fileFormat = fileString.split("\\.");
		int last = fileFormat.length - 1;
		
		switch(fileFormat[last]){
		//lower case
		case "png":
			return true;
		case "gif":
			return true;
		case "jpeg":
			return true;
		case "jpg":
			return true;
		
		//upper case
		case "JPG":
			return true;
		case "JPEG":
			return true;
		case "GIF":
			return true;
		case "PNG":
			return true;
		default:
			return false;
		}
	}
	

}
