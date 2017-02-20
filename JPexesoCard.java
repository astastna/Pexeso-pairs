package pexeso;

//import java.awt.Dimension;
import java.awt.Color;
import java.awt.Image;
//import java.awt.Rectangle;

//import javax.swing.Box;
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
		//version with JButton directly
		ImageIcon scaledBackPict = this.transformImageIcon(c, c.backPicture, true);
		this.butt = new JButton(scaledBackPict);
		butt.setBackground(Color.WHITE);
		
		//version with additional container for better viewing properties
		
		//ImageIcon newBackIcon = transformImageIcon2(c, butt, c.backPicture);
		//butt.setIcon(newBackIcon);
		//butt.setText(((Integer) nr).toString());
		//this.front = transformImageIcon2(c, butt, img);
		this.front = img;
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
		this.getButton().setIcon(this.transformImageIcon(cont, cont.backPicture, true));
		
	}
	
	public void turnToFrontSide(){
		//System.out.println("Function turnToFrontSide card "+((Integer) tupleNr).toString()+"-"+((Integer) id).toString());
		butt.setIcon(front);
		this.getButton().setIcon(this.transformImageIcon(cont, front, false));	
	}
	
	public JButton getButton(){
		return butt;
	}
	
	private ImageIcon transformImageIcon(PexesoContainer cont2, ImageIcon orig, boolean back){
		
		//System.out.println("Transforming an image icon in transformImageIcon.");
		// finding out the actual size of the grid cell
		//System.out.println("Container width is " + ((Integer)cont2.getSize().width).toString() + " and height is "+((Integer)cont2.getSize().height).toString());
		//System.out.println("Number of columns is "+ ((Integer)cont2.columns).toString() + " and number of rows is " + ((Integer)cont2.rows).toString());
		int imgWidth = cont2.getSize().width / cont2.columns;
		//System.out.println("Img width is "+ ((Integer)imgWidth).toString());
		int imgHeight = cont2.getSize().height / cont2.rows;
		//System.out.println("Img height is "+ ((Integer)imgHeight).toString());
		
		// get image from icon
		Image originalIcon = orig.getImage(); 
		//((ImageIcon) this.getButton().getIcon()).getImage();
		
		//scale the image with respect to the ratio
		Image resizedIcon;
		if (!back){
			//front image - we want the whole image to be visible
			//so scale it according to the smaller size
			if (imgHeight > imgWidth) {
				//System.out.println("Height is greater than width.");
				resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
			}
			else {
				//System.out.println("Width is greater than height.");
				resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE);
			}
		}
		else{
			//back image - we want the whole button to be under the image
			//so we scale according to the greater size
			if (imgWidth > imgHeight) {
				//System.out.println("Height is greater than width.");
				resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
			}
			else {
				//System.out.println("Width is greater than height.");
				resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE);
			}
		}
		
		//replace the image
		return new ImageIcon( resizedIcon ); //substitute the old one by the scaled one
		//this.getButton().setIcon(newIcon);
	}
	
	/*public ImageIcon transformImageIcon2(PexesoContainer cont, ImageIcon orig){
		
		System.out.println("Transforming an image icon in transformImageIcon2.");
		//counting the size of button
		int imgWidth = cont.getSize().width / cont.columns;
		System.out.println("Img width is "+ ((Integer)imgWidth).toString());
		int imgHeight = cont.getSize().height / cont.rows;
		System.out.println("Img height is "+ ((Integer)imgHeight).toString());	
		
		// get image from icon
		Image originalIcon = orig.getImage(); 
		
		//scale the image with respect to the ratio
		Image resizedIcon;
		if (imgHeight > imgWidth) {
			//System.out.println("Height is greater than width.");
			resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
		}
		else {
			//System.out.println("Width is greater than height.");
			resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE);
		}				
		
		return new ImageIcon( resizedIcon ); //substitute the old one by the scaled one
		
	}*/
	
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
