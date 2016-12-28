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
	JPexesoButton pressed; //buffer of pressed pexeso cards
	//? if I find out, that I am pressing a different card, how to turn it down?
	// -> simply change it's imageicon
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public PexesoContainer(Container container, int rows, int columns){
		this.fieldsCnt = rows*columns;
		this.rows = rows;
		this.columns = columns;
		this.pressed = null;
		
		//TODO copy all properties from container
		
	}

	/**
	 * @param pathToBackPicture - path to picture, which will be shown as a back side of the pexeso cards
	 * 
	 */
	public void createNewGame(String pathToBackPicture){

		//Setup layout and it's constraints
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints constraints[] = new GridBagConstraints[fieldsCnt];
		
		//initializing cards and placing them on the game board
		JButton card[] = new JButton[fieldsCnt];
		for (int k = 0; k < fieldsCnt; k++){
			
			//back-picture handling
			card[k] = new JButton(new ImageIcon(pathToBackPicture));
			this.transformImageIcon(card[k]);
			
			MouseListener onClick = null;
			card[k].addMouseListener(onClick);
			
			//property setup for given card
			constraints[k] = new GridBagConstraints();
			setConstraints(constraints[k], k);
			layout.setConstraints(card[k], constraints[k]);
			
			this.add(card[k]);
		}
	}

	//TODO run every time when replacing image-icon on a card
	private void transformImageIcon(JButton card){
		int imgWidth = this.getSize().width / columns;
		int imgHeight = this.getSize().height / rows;	
		Image originalIcon = ((ImageIcon) card.getIcon()).getImage(); // get image from icon
		Image resizedIcon;
		if (imgHeight > imgWidth) resizedIcon = originalIcon.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE); //scale it
		else resizedIcon = originalIcon.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
		ImageIcon newIcon = new ImageIcon( resizedIcon ); //substitute the old one by the scaled one
		card.setIcon(newIcon);
	}
	
	private void setConstraints(GridBagConstraints c, int k){
		c.fill = GridBagConstraints.BOTH;
		c.gridx = k%columns;
		c.gridy = k/columns;
		c.weightx = 1;
		c.weighty = 1;
	}

}
