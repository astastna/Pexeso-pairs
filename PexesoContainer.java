package pexeso;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PexesoContainer extends Container {
	int fieldsCnt;
	int rows;
	int columns;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public PexesoContainer(Container container, int rows, int columns){
		this.fieldsCnt = rows*columns;
		this.rows = rows;
		this.columns = columns;
		
		//TODO copy all properties from container
		
	}

	/**
	 * @param pathToBackPicture - path to picture, which will be shown as a back side of the pexeso cards
	 * 
	 */
	public void createNewGame(String pathToBackPicture){

		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		System.out.println(this.getSize());
		GridBagConstraints constraints[] = new GridBagConstraints[fieldsCnt];
		JButton card[] = new JButton[fieldsCnt];
		
		for (int k = 0; k < fieldsCnt; k++){
			
			card[k] = new JButton(new ImageIcon(pathToBackPicture));
			//card[k] = new JButton();
			card[k].setBackground(new Color(130, 54, 52));
			System.out.println(this.getSize());
			int imgWidth = (this.getSize().width / columns);
			int imgHeight = (this.getSize().height / rows);	
			System.out.printf("Size of the lable is %s \n", card[k].getPreferredSize());
			Image backSideImg = ((ImageIcon) card[k].getIcon()).getImage(); // get image from icon
			Image backSideImgScaled;// = backSideImg.getScaledInstance(imgWidth, imgHeight, java.awt.Image.SCALE_REPLICATE);
			if (imgHeight > imgWidth) backSideImgScaled = backSideImg.getScaledInstance(-1, imgHeight, java.awt.Image.SCALE_REPLICATE); //scale it
			else backSideImgScaled = backSideImg.getScaledInstance(imgWidth, -1, java.awt.Image.SCALE_REPLICATE);
			ImageIcon backSideIcon = new ImageIcon( backSideImgScaled ); //substitute the old one by the scaled one
			card[k].setIcon(backSideIcon);
			//pict[k].addMouseListener(mouseListener);
			
			constraints[k] = new GridBagConstraints();
			constraints[k].fill = GridBagConstraints.BOTH;
			constraints[k].gridx = k%columns;
			constraints[k].gridy = k/columns;
			constraints[k].gridwidth = 1;
			constraints[k].gridheight = 1;
			constraints[k].weightx = 1;
			constraints[k].weighty = 1;
			layout.setConstraints(card[k], constraints[k]);
			
			this.add(card[k]);
		}
	}

}
