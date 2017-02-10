package pexeso;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class ChoosePictureForm extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int gameWidth;
	int gameHeight;
	JFrame original;
	ChoosePictureForm currentWindow;
	Container pane;
	PexesoContainerGL chosenPexesoPane;
	
	public ChoosePictureForm(int width, int height, JFrame orig){
		this.gameWidth = width;
		this.gameHeight = height;
		this.pane = this.getContentPane();
		this.original = orig;
		this.currentWindow = this;
		
		initializeChoosePictureForm();
	}
	
	private PexesoContainerGL gameWithDefaultImages(){
		int pairs = gameWidth*gameHeight/2;
		
		String[][] paths = new String[pairs][2];
		
		//get path in order to make pictures working
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		for (int i = 0; i < pairs; i++){
			paths[i][0] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			paths[i][1] = s + "/src/pexeso/img" + ((Integer) i).toString() + ".jpg";
			//System.out.println("Saving path "+ paths[i][0] );
		}
		PexesoContainerGL pexesoPane = new PexesoContainerGL(pane, gameWidth, gameHeight, "zadni-strana-pexesa.png", paths);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pexesoPane.setSize(screenSize);
		pexesoPane.createNewGame();
		
		return pexesoPane;
	}
	
	private void initializeChoosePictureForm(){
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		
		//header
		JLabel choose = new JLabel("Images");
		choose.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(choose);
		
		//description
		JLabel chooseDescr = new JLabel("Choose whether you want to play with default images or with your own images.");
		pane.add(chooseDescr);
		
		JLabel chooseDescr2 = new JLabel(" 0nly pictures in format png, gif and jpg are allowed.");
		pane.add(chooseDescr2);
		
		//default radio button
		JRadioButton defaultImg = new JRadioButton("Use default images");
		defaultImg.setActionCommand("default");
		//defaultImg.setAlignmentX(Component.CENTER_ALIGNMENT);
		defaultImg.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				PexesoContainerGL newGame = gameWithDefaultImages();
				chosenPexesoPane = newGame;
				//won't be done here
				//original.setContentPane(newGame);
				//currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		pane.add(defaultImg);
		
		//custom radio button - same pairs
		JRadioButton customImgPairs = new JRadioButton("Use custom images - pairs contain same images");
		customImgPairs.setActionCommand("customPairs");
		//TODO action - add appropriate number of choose file items
		//customImgPairs.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(customImgPairs);
		
		//custom radio button - different pairs
		JRadioButton customImgDiff = new JRadioButton("Use custom images - pairs contain different images");
		customImgDiff.setActionCommand("customDiff");
		//TODO action - add appropriate number of choose file items
		//customImgDiff.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(customImgDiff);
		
		//make it possible to choose only one button
		ButtonGroup buttGroup = new ButtonGroup();
		buttGroup.add(defaultImg);
		buttGroup.add(customImgPairs);
		buttGroup.add(customImgDiff);
		
		//Save button
		JButton save = new JButton("Save this new game");
		save.setAlignmentX(Component.LEFT_ALIGNMENT);
		//TODO implement action
		
		//Finish button
		JButton finish = new JButton("Finish");
		finish.setAlignmentX(Component.RIGHT_ALIGNMENT);
		finish.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				
				switch (e.getActionCommand()){
				
				case "default":
					
					break;
					
				case "customPairs":
					
					break;
					
				case "customDiff":
					
					break;
					
				default:
					System.out.println("It does't work this way.");
				}
				
				original.setContentPane(chosenPexesoPane);
				currentWindow.dispatchEvent(new WindowEvent(currentWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		//Button box
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(save);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(finish);
		pane.add(buttonBox);
		
		this.pack();
	}

}
