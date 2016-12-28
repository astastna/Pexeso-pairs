package pexeso;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class JPexesoCard extends JButton{
	JButton butt;
	ImageIcon front; //only front picture is interesting, back will be the same for all fields
	
	int tupleNr; //number, which defines tuple, in which this card is
	
	
	
	public JPexesoCard(String img, int nr){
		super();
		if (picturePathOk(img)) this.front = new ImageIcon(img);
		else viewError("Wrong image format. Image has to be .png, .gif or .jpg.");
		this.tupleNr = nr;
	}
	
	public int getTupleNr(){
		return tupleNr;
	}
	
	private void viewError(String errorMsg){
		//TODO create pop-up window
	}
	
	
	private boolean picturePathOk(String path){
		String[] fileFormat = path.split(".");
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
