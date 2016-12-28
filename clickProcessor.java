package pexeso;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class clickProcessor implements MouseListener{
	JPexesoCard currentCard; //card, which has been clicked on
	
	PexesoContainer game; //container with game parameters
	//game.getTurned
	//cards, which are already turned over
		//null if no card is turned
	
	public clickProcessor(PexesoContainer game, JPexesoCard currentCard){
		this.currentCard = currentCard;
		this.game = game;
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		JPexesoCard[] turned = game.getTurnedCards();
		boolean ignore = false;
		JPexesoCard[] newTurned = new JPexesoCard[2];
		
		// dealing with duplicit clicks
		if (currentCard.tupleFound){
			ignore = true; // click on already found tuple
			System.out.println("Current card is in already found tuple.");
		}
		//if (turned[0] != null && turned[0].equals(currentCard)){
		if( turned[0] != null && turned[0].tupleNr == currentCard.tupleNr && turned[0].id == currentCard.id){
			ignore = true; // click on already turned card
			System.out.println("Already turned and current card are the same - ignoring.");
		}
		//if (turned[1] != null && turned[1].equals(currentCard)){
		if( turned[1] != null && turned[1].tupleNr == currentCard.tupleNr && turned[1].id == currentCard.id){
			ignore = true;
			System.out.println("Already turned and current card are the same - ignoring.");
		}
		
		//no card is turned
		if (turned[0] == null && turned[1] == null && !ignore){
			System.out.println("No card turned now.");
			newTurned[0] = currentCard;
			newTurned[1] = null;
			currentCard.turnToFrontSide();	
			System.out.println("Turning front card "+((Integer) currentCard.tupleNr).toString()+"-"+ ((Integer)currentCard.id ).toString() );
		}
		
		//one card is turned
		if (turned[0] != null && turned[1] == null && !ignore){
			System.out.println("Already turned is card "+ ((Integer) turned[0].tupleNr).toString()+"-"+((Integer) turned[0].id).toString()+", current card is "+((Integer) currentCard.getTupleNr()).toString()+"-"+((Integer) currentCard.id).toString() );
			//already turned and current card aren't the same
			newTurned[0] = turned[0];
			newTurned[1] = currentCard;
			currentCard.turnToFrontSide();
			System.out.println("Turning front card "+((Integer) currentCard.tupleNr).toString()+"-"+ ((Integer)currentCard.id ).toString() );
			//else turned card and clicked card are the same - do nothing
		}
		
		//two cards are turned
		if(turned[0] != null && turned[1] != null && !ignore){
			System.out.println("Already turned are cards "+ ((Integer) turned[0].tupleNr).toString()+"-"+((Integer) turned[0].id).toString() +" and "+((Integer) turned[1].tupleNr).toString()+"-"+((Integer) turned[1].id).toString()+", current card is "+((Integer) currentCard.getTupleNr()).toString()+"-"+((Integer) currentCard.id).toString() );
			
			//tuple found	
			if (turned[0].getTupleNr() == turned[1].getTupleNr()){
				
				assert(turned[0].equals(turned[1]));
				assert(turned[0].id == turned[1].id);
				
				//already turned and current card aren't the same
				//set cards as found
				System.out.println("Tuple found! Cards should stay as they are.");
				turned[0].setFound();
				turned[1].setFound();
				//TODO ring a bell, make something to make the person know that tuple was found
			}
			//tuple wasn't found
			else{
				System.out.println("Tuple not found! Both cards are turned back.");
				turned[0].turnToBackSide();
				turned[1].turnToBackSide();
					
			}	
			
			// behave like no cards were turned before 
			//so we can't turn them back / because we already turned them back
			newTurned[0] = currentCard;
			currentCard.turnToFrontSide();
			System.out.println("Turning front card "+((Integer) currentCard.tupleNr).toString()+"-"+ ((Integer) currentCard.id ).toString() );
			newTurned[1] = null;
		}
		
		assert(turned[0] == null && turned[1] != null);
		
		//save the changes to the game environment
		game.setTurnedCards(newTurned);
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Shouldn't do anything - for now
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Shouldn't do anything - for now
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Shouldn't do anything - for now
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Shouldn't do anything - for now
		
	}

}
