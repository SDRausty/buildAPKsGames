package eu.veldsoft.russian.triple.model;

/**
 * Player who can establish playing contract after announce win.
 * 
 * @author Todor Balabanov
 */
public interface Contractor {
	/**
	 * Selection of trump.
	 * 
	 * @return Suit appointed for trump.
	 */
	Card.Suit trumpSelection();

	/**
	 * Give two cards to the other players one card for each opponent. There is
	 * no order in the cards.
	 * 
	 * @return Array of to cards as references in random order.
	 */
	Card[] giveCards();
}
