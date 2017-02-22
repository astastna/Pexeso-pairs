package pexeso;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class clickProcessor implements MouseListener{
	JPexesoCard currentCard; //card, which has been clicked on
	PexesoContainer game; //container with game parameters
	
	public clickProcessor(PexesoContainer game, JPexesoCard currentCard){
		this.currentCard = currentCard;
		this.game = game;
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		JPexesoCard[] turned = game.getTurnedCards(); //cards, which had been turned during previous click
		boolean ignore = false;  //signalizes that this click should be ignored, because it is duplicit (the card clicked on is already turned and not matched)
		boolean alreadyFoundCard = false;  //signalizes that we have clicked on already matched card
		JPexesoCard[] newTurned = new JPexesoCard[2]; //the cards which will be turned during the next click
		
		//current card is in already found tuple
		if (currentCard.tupleFound){
			//we don't want to do anything
			newTurned = turned;
			alreadyFoundCard = true;
		}
		
		//dealing with duplicit clicks
		//already matched card (the first one) and current card are the same 
		if( turned[0] != null && turned[0].tupleNr == currentCard.tupleNr && turned[0].id == currentCard.id){
			ignore = true; // click on already turned card
		}
		
		//already matched card (the second one) and current card are the same - second card
		if( turned[1] != null && turned[1].tupleNr == currentCard.tupleNr && turned[1].id == currentCard.id){
			ignore = true;
		}
		
		//no card is turned - we are turning up the current card as the first one turned
		if (turned[0] == null && turned[1] == null && !ignore && !alreadyFoundCard){
			newTurned[0] = currentCard;
			newTurned[1] = null;
			currentCard.turnToFrontSide();	
		}
		
		//one card is turned
		if (turned[0] != null && turned[1] == null && !ignore && !alreadyFoundCard){
			
			//current card matches with the already turned, game is over (successfully)
			if (turned[0].getTupleNr() == currentCard.getTupleNr()){
				//a good match, finding of this tuple will be processed in the next round
				//but we will set the buttons yellow to let the player know, that he did it right
				//(the images might not be the same in pairs)
				turned[0].butt.setBackground(new Color(255, 255, 146));
				currentCard.butt.setBackground(new Color(255, 255, 146));
				
				//and if there's only one pair left, show the current card immediately
				//and announce the end of game
				if (game.lastPairRemains()){
					//adding the current step to the number of steps needed
					game.addStep();
					currentCard.turnToFrontSide();
				
					//showing the winning dialog
					JOptionPane.showMessageDialog(game,
							"You have just won this game! \n"+
								"Number of steps needed: "+ ((Integer) game.getStepsNum()).toString(),
								"Congratulations",
								JOptionPane.INFORMATION_MESSAGE,
								game.getWinningIcon());
				}
			}
			
			//already turned and current card aren't the same
			newTurned[0] = turned[0];
			// nevertheless, we turn the current card
			newTurned[1] = currentCard;
			currentCard.turnToFrontSide();
			//else turned card and clicked card are the same - do nothing (duplicit click)
		}
		
		//two cards are turned
		if(turned[0] != null && turned[1] != null && !ignore && !alreadyFoundCard){
			//another two cards were turned, increment number of needed steps
			// (it doesn't matter if the turning was or wasn't successful)
			game.addStep();
			
			//tuple found	
			if (turned[0].getTupleNr() == turned[1].getTupleNr()){
				
				assert(turned[0].equals(turned[1]));
				assert(turned[0].id == turned[1].id);
				
				//already turned and current card are the same
				//set cards as found - NOTICE: we are not turning them back
				turned[0].setFound();
				turned[1].setFound();
				game.decreaseNotFound();
				
			}
			//tuple wasn't found
			else{
				//we turn the cards to the back side
				turned[0].turnToBackSide();
				turned[1].turnToBackSide();
			}	
			
			//behave like no cards were turned before 
			//so we can't turn them back / because we already have turned them back
			newTurned[0] = currentCard;
			currentCard.turnToFrontSide();
			newTurned[1] = null;
		}
		
		assert(turned[0] == null && turned[1] != null);
		
		//save the changes to the game environment
		game.setTurnedCards(newTurned);
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Shouldn't do anything
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Shouldn't do anything
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Shouldn't do anything
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Shouldn't do anything
		
	}

}
