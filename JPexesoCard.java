package pexeso;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class JPexesoCard extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JButton butt;
	ImageIcon front; //only front picture is interesting, back will be the same for all fields
	PexesoContainer cont;
	
	int tupleNr; //number, which defines tuple, in which this card is
	int id; //id in the tuple
	boolean tupleFound;
	
	public JPexesoCard(ImageIcon img, int nr, PexesoContainer c, int id){
		System.out.println("Adding back icon to card nr "+((Integer) nr).toString());
		this.butt = new JButton(c.backPicture);
		ImageIcon newBackIcon = transformImageIcon2(c, c.backPicture);
		butt.setIcon(newBackIcon);
		//butt.setText(((Integer) nr).toString());
		this.front = transformImageIcon2(c, img);
		c.frontPictureTuples[nr][id] = front;
		this.tupleNr = nr;
		this.tupleFound = false;
		this.cont = c;
		this.id = id;
	}
	
	public int getTupleNr(){
		return tupleNr;
	}
	
	public void setFound(){
		tupleFound = true;
	}
	
	public void turnToBackSide(){
		//System.out.println("Function turnToBackSide card "+((Integer) tupleNr).toString()+"-"+((Integer) id).toString());
		butt.setIcon(cont.backPicture);
		this.transformImageIcon(cont);
		
	}
	
	public void turnToFrontSide(){
		//System.out.println("Function turnToFrontSide card "+((Integer) tupleNr).toString()+"-"+((Integer) id).toString());
		butt.setIcon(front);
		this.transformImageIcon(cont);	
	}
	
	public JButton getButton(){
		return butt;
	}
	
	private void transformImageIcon(PexesoContainer cont2){
		
		System.out.println("Transforming an image icon in transformImageIcon.");
		int imgWidth = cont2.getSize().width / cont2.columns;
		int imgHeight = cont2.getSize().height / cont2.rows;	
		
		// get image from icon
		Image originalIcon = ((ImageIcon) this.getButton().getIcon()).getImage(); 
		
		//scale the image with respect to the ratio
		Image resizedIcon;
		if (imgHeight > imgWidth) {
			resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE); 
		}
		else {
			resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
		}
		
		//replace the image
		ImageIcon newIcon = new ImageIcon( resizedIcon ); //substitute the old one by the scaled one
		this.getButton().setIcon(newIcon);
	}
	
	public ImageIcon transformImageIcon2(PexesoContainer cont, ImageIcon orig){
		
		System.out.println("Transforming an image icon in transformImageIcon2.");
		int imgWidth = cont.getSize().width / cont.columns;
		int imgHeight = cont.getSize().height / cont.rows;	
		
		// get image from icon
		Image originalIcon = orig.getImage(); 
		
		//scale the image with respect to the ratio
		Image resizedIcon;// = originalIcon.getScaledInstance(imgWidth, imgHeight, java.awt.Image.SCALE_REPLICATE);
		if (imgHeight > imgWidth) {
			resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE); 
		}
		else {
			resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
		}
		
		return new ImageIcon( resizedIcon ); //substitute the old one by the scaled one
		
	}
	
	private void viewError(String errorMsg){
		//TODO create pop-up window
	}
	
	
	
	/*ImageIcon back = new ImageIcon("/home/anet/MFF/Java/Zapocet/zadni-strana-pexesa.png");
	
	if (picturePathOk(frontPicture)){
		ImageIcon front = new ImageIcon(frontPicture);
	}
	else {
		System.out.println("Wrong image file format.");
	}*/

}
