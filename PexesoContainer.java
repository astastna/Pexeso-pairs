package pexeso;


import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;

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
				System.out.println("i: "+ ((Integer)i).toString() + " , j: " + ((Integer)j).toString());
				System.out.println("calling ImageIcon constructor on "+pathsToFrontPictures[i][j]);
				if (picturePathOk(pathsToFrontPictures[i][j])){
					frontPictureTuples[i][j] = new ImageIcon(pathsToFrontPictures[i][j]);
				}
			}
		}
		//TODO copy all properties from container
	}

	public PexesoContainer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pathToBackPicture - path to picture, which will be shown as a back side of the pexeso cards
	 * 
	 */
	public void createNewGame(){

		//Setup layout and it's constraints
		GridLayout layout = new GridLayout(columns, rows, 0, 0);
		
		this.setLayout(layout);
		//GridBagConstraints constraints[] = new GridBagConstraints[fieldsCnt];
		
		//initializing cards and placing them on the game board
		JPexesoCard card[] = new JPexesoCard[fieldsCnt];
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
			card[k] = new JPexesoCard(frontPictureTuples[randK/2][randK%2], randK/2 , this, randK%2);
			
			MouseListener onClick = new clickProcessor(this, card[k]);
			card[k].getButton().addMouseListener(onClick);
			
			description[k] = "("+((Integer)columns).toString()+";"+((Integer)rows).toString()+")";
			layout.addLayoutComponent(description[k], card[k]);
			
			this.add(card[k].getButton());
		}
	}
	
	public void setTurnedCards(JPexesoCard[] cards){
		turned = cards;
	}
	
	public JPexesoCard[] getTurnedCards(){
		return turned;
	}
	
	
	private boolean picturePathOk(String path){
		if (path == null) return false;
		
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
