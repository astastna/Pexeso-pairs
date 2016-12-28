package pexeso;


import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PexesoContainer extends Container {
	int fieldsCnt;
	int rows;
	int columns;
	JPexesoCard[] turned; //buffer of pressed pexeso cards
	ImageIcon backPicture; //back side of the cards
	ImageIcon[][] frontPictureTuples;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PexesoContainer(Container container, int rows, int columns, String pathToPicture, String[][] pathsToFrontPictures){
		this.fieldsCnt = rows*columns;
		this.rows = rows;
		this.columns = columns;
		this.turned = new JPexesoCard[2];
		
		if (picturePathOk(pathToPicture)){
			System.out.println("calling ImageIcon constructor on "+pathToPicture);
			backPicture = new ImageIcon(pathToPicture);
		}
		
		//processing the paths to pictures
		frontPictureTuples = new ImageIcon[rows*columns/2][2];
		for (int i=0; i < rows*columns/2; i++){
			for (int j=0; j < 2; j++){
				System.out.println("calling ImageIcon constructor on "+pathsToFrontPictures[i][j]);
				if (picturePathOk(pathsToFrontPictures[i][j])){
					frontPictureTuples[i][j] = new ImageIcon(pathsToFrontPictures[i][j]);
				}
			}
		}
		

		//TODO copy all properties from container
		
	}

	/**
	 * @param pathToBackPicture - path to picture, which will be shown as a back side of the pexeso cards
	 * 
	 */
	public void createNewGame(){

		//Setup layout and it's constraints
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints constraints[] = new GridBagConstraints[fieldsCnt];
		
		//initializing cards and placing them on the game board
		JPexesoCard card[] = new JPexesoCard[fieldsCnt];
		for (int k = 0; k < fieldsCnt; k++){
			
			//back-picture handling
			card[k] = new JPexesoCard(frontPictureTuples[k/2][k%2], k/2 , this, k%2);
			
			MouseListener onClick = new clickProcessor(this, card[k]);
			card[k].getButton().addMouseListener(onClick);
			
			//property setup for given card
			constraints[k] = new GridBagConstraints();
			setConstraints(constraints[k], k);
			layout.setConstraints(card[k].getButton(), constraints[k]);
			
			this.add(card[k].getButton());
		}
	}
	
	public void setTurnedCards(JPexesoCard[] cards){
		turned = cards;
	}
	
	public JPexesoCard[] getTurnedCards(){
		return turned;
	}
	
	private void setConstraints(GridBagConstraints c, int k){
		c.fill = GridBagConstraints.BOTH;
		c.gridx = k%columns;
		c.gridy = k/columns;
		c.weightx = 1;
		c.weighty = 1;
	}
	
	private boolean picturePathOk(String path){
		String[] fileFormat = path.split("\\.");
		//System.out.println(path);
		//System.out.print(fileFormat[0]+" a "+fileFormat[1]);
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
