package pexeso;
import java.nio.file.Path;

import javax.swing.*;

public class JPexesoButton {
	JButton butt;
	ImageIcon back;
	ImageIcon front;
	Integer x_pos;// maybe not needed, because I can remember, which button is pressed right now and 
	Integer y_pos;// then compare current and the one being pressed according to the dictionary? 
	// so there might not be a need to find out where it is
	// maybe when placing the buttons on in their place
	// it is possible to just create them and then randomly place one after another
	
	//tuples? how to represent that?
	// if here, there would be a circle, because I have to create the first one to give link into the second one and vice versa
	//dictionary with with a-b and b-a values?
	
	public JPexesoButton(Integer x, Integer y, String frontPicture){
		JButton butt;
		ImageIcon back = new ImageIcon("/home/anet/MFF/Java/Zapocet/zadni-strana-pexesa.png");
		
		if (picturePathOk(frontPicture)){
			ImageIcon front = new ImageIcon(frontPicture);
		}
		else {
			System.out.println("Wrong image file format.");
		}
		
		Integer x_pos = x;
		Integer y_pos = y;
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
