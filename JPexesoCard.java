package pexeso;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class JPexesoCard extends JButton{
	
	private static final long serialVersionUID = 1L;
	
	JButton butt; //basic button
	ImageIcon front; //only front picture is interesting, back will be the same for all fields
	PexesoContainer cont; //the game board, to which this card belongs
	
	int tupleNr; //number, which defines tuple, in which this card is
	int id; //id in the tuple
	boolean tupleFound; //signalizes that the card has been already matched with the second one
	
	public JPexesoCard(ImageIcon img, int nr, PexesoContainer c, int id){
		this.cont = c;
		
		//creating new card - default is not turned
		ImageIcon scaled = transformImageIcon(cont, cont.backPicture, true);
		this.butt = new JButton(scaled);
		butt.setBackground(Color.WHITE);

		this.front = img; //saving front image for future use
		c.frontPictureTuples[nr][id] = front; //saving the image to the game board
		this.tupleNr = nr; 
		this.tupleFound = false;
		this.cont = c;
		this.id = id;
	}
	
	/**
	 * Returns tuple number/id of this card.
	 * Can be used to decide whether cards match or not.
	 * 
	 * @return	Id of tuple in which the card belongs.
	 */
	public int getTupleNr(){
		return tupleNr;
	}
	
	/**
	 * Sets the card found.
	 */
	public void setFound(){
		tupleFound = true;
	}
	
	/**
	 * Turns the card to it's back side by changing the {@link ImageIcon} of the JButton.
	 */
	public void turnToBackSide(){
		this.getButton().setIcon(this.transformImageIcon(cont, cont.backPicture, true));
	}
	
	/**
	 * Turns the card to it's front side by changing the {@link ImageIcon} of the JButton.
	 */
	public void turnToFrontSide(){
		this.getButton().setIcon(this.transformImageIcon(cont, front, false));	
	}
	
	/**
	 * 
	 * @return button	The button which is included in {@link JPexesoCard}.
	 */
	public JButton getButton(){
		return butt;
	}
	
	
	/**
	 * Transforms the Image of the card to be the right size,
	 * so it fits the card properly. The scaling is always proportional. 
	 * <p>
	 * The transformation is different for front and back-side images.
	 * The front side image is transformed such that the whole image is visible.
	 * On the other hand, back-side image is transformed such that it fills the whole card 
	 * (the background shouldn't be visible underneath). The result is that only a part of 
	 * the original image is visible. 
	 * 
	 * 
	 * @param cont	The game board on which the card lays.
	 * @param orig	The original ImageIcon - included because the container might not have it initialized. (Enables to call it in a constructor of the container.)
	 * @param back	Signalizes if the {@link ImageIcon} is or isn't the back side Image. Transformation behaves differently for them.
	 * @return	The resized ImageIcon
	 */
	private ImageIcon transformImageIcon(PexesoContainer cont, ImageIcon orig, boolean back){
		
		// finding out the actual size of the grid cell
		int imgWidth = cont.getSize().width / cont.columns;
		int imgHeight = cont.getSize().height / cont.rows;
		
		// get image from icon
		Image originalIcon = orig.getImage();
		
		//scale the image with respect to the ratio
		Image resizedIcon;
		if (!back){
			//front image - we want the whole image to be visible
			//so scale it according to the smaller size
			if (imgHeight > imgWidth) {
				resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
			}
			else {
				resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE);
			}
		}
		else{
			//back image - we want the whole button to be under the image
			//so we scale according to the greater size
			if (imgWidth > imgHeight) {
				resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
			}
			else {
				resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE);
			}
		}
		//return the resized image
		return new ImageIcon( resizedIcon );
	}

}
