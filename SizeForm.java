package pexeso;

import java.awt.Component;
import java.awt.Container;
//import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SizeForm extends JFrame{
	private static final long serialVersionUID = 1L;
	
	//Warning! When getting this one greater, you must also add enough default images accordingly
	private final static int maxSize = 8; 
	
	int defaultWidth;
	int defaultHeight;
	JFrame originalFrame;
	SizeForm currentSizeForm;
	
	
	public SizeForm(int defaultWidth, int defaultHeight, JFrame orig){
		//System.out.println("Size form constructor: "+ ((Integer) defaultWidth).toString() + " , " + ((Integer) defaultHeight).toString() ) ; 
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.currentSizeForm = this;
		this.originalFrame = orig;
		
		this.initializeSizeForm();
	}
	
	private JDialog showRangeError(){
		JOptionPane.showMessageDialog(currentSizeForm,
			    "Range or value error. \n Please, insert natural numbers between 1 and "+((Integer) maxSize).toString() + "\n At least one of the numbers have to be odd.",
			    "Number value error",
			    JOptionPane.ERROR_MESSAGE);
		return null;
		
	}
	
	private JDialog showNumberFormatError(){
		JOptionPane.showMessageDialog(currentSizeForm,
			    "Wrong number format. \n Please insert numbers in a valid format.",
			    "Number format error",
			    JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	private boolean numOk(int num){
		if (num > 0 && num <= maxSize){
			return true;
		} 
		else{
			return false;
		}
	}
	
	private void initializeSizeForm(){
		
		Container pane = this.getContentPane();
		BoxLayout box = new BoxLayout(pane, BoxLayout.Y_AXIS);
		pane.setLayout(box);
		//TODO unable to close window
		
		
		//Title text
		JLabel size = new JLabel("Size");
		//size.setFont(new Font("Serif", Font.BOLD, 14));
		size.setAlignmentX(Component.CENTER_ALIGNMENT);
		size.setAlignmentY(Component.TOP_ALIGNMENT);
		pane.add(size);
		
		//Description text
		JLabel sizeDescr = new JLabel("Choose the size of your game board. Only numbers between 1 and "+ ((Integer) maxSize).toString() +" are allowed.");
		//sizeDescr.setFont(new Font("Serif", Font.PLAIN, 10));
		sizeDescr.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(sizeDescr);
		
		
		//Glue
		pane.add(Box.createRigidArea(getPreferredSize()));
		
		//Width-text
		JLabel widthText = new JLabel("Width");
		//widthText.setFont(new Font("Serif", Font.PLAIN, 10));
		widthText.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Height-text
		JLabel heightText = new JLabel("Height");
		//heightText.setFont(new Font("Serif", Font.PLAIN, 10));
		heightText.setAlignmentX(Component.RIGHT_ALIGNMENT);

		//Grouping together
		Box whLableBox = Box.createHorizontalBox();
		whLableBox.add(Box.createHorizontalGlue());
		whLableBox.add(widthText);
		whLableBox.add(Box.createHorizontalGlue());
		whLableBox.add(heightText);
		whLableBox.add(Box.createHorizontalGlue());
		pane.add(whLableBox);
		
		
		//Width-field
		final JTextField widthField = new JTextField(((Integer)defaultWidth ).toString());
		widthField.setAlignmentX(Component.LEFT_ALIGNMENT);
				
		//x
		JLabel x = new JLabel(" x ");
		x.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Height-field
		final JTextField heightField = new JTextField(((Integer)defaultHeight ).toString());
		heightField.setAlignmentX(Component.RIGHT_ALIGNMENT);	
		
		//Grouping together
		Box whFieldBox = Box.createHorizontalBox();
		whFieldBox.add(Box.createHorizontalGlue());
		whFieldBox.add(widthField);
		whFieldBox.add(Box.createHorizontalGlue());
		whFieldBox.add(x);
		whFieldBox.add(Box.createHorizontalGlue());
		whFieldBox.add(heightField);
		whFieldBox.add(Box.createHorizontalGlue());
		pane.add(whFieldBox);
		
		//Glue
		pane.add(Box.createRigidArea(getPreferredSize()));
		
		//Next button
		JButton next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
				//get information from text fields
				try {
					String widthText = widthField.getText();
					String heightText = heightField.getText();
					int widthData = Integer.parseInt(widthText);
					int heightData = Integer.parseInt(heightText);
				
					//check it
					if (numOk(widthData) && numOk(heightData) && (widthData * heightData)%2 == 0 ){
						//correct data -> next step
						ChoosePictureForm cpf = new ChoosePictureForm(widthData, heightData, originalFrame);
						cpf.setVisible(true);
						currentSizeForm.dispatchEvent(new WindowEvent(currentSizeForm, WindowEvent.WINDOW_CLOSING));
					}	
					else {	
						//wrong data -> warning
						showRangeError();
					}
				}
				catch (NumberFormatException nfe){
					showNumberFormatError();
				}
			}
		});
		
		Box nextBox = Box.createHorizontalBox();
		nextBox.add(Box.createHorizontalGlue());
		nextBox.add(next);
		nextBox.add(Box.createHorizontalGlue());
		
		pane.add(nextBox);
		
		//Vertical space
		Component strut = Box.createVerticalStrut(10);
		pane.add(strut);
		
		this.pack();
		
	}

}
