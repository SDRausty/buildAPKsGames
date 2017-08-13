package eu.veldsoft.russian.triple.model;

import java.util.Collections;
import java.util.Vector;

/**
 * Talon is the cards left face down on the table before the bidding process.
 * 
 * @author Todor Balabanov
 */
public class Talon {
	/**
	 * Set of cards.
	 */
	private Vector<Card> cards = new Vector<Card>();

	/**
	 * Cards getter.
	 * 
	 * @return Set of cards.
	 */
	Vector<Card> getCards() {
		return cards;
	}

	/**
	 * Cards setter.
	 * 
	 * @param cards
	 *            Set of cards.
	 */
	void setCards(Vector<Card> cards) {
		this.cards = cards;
	}

	/**
	 * Put card in the talon.
	 * 
	 * @param card
	 *            Card to keep.
	 */
	void recieve(Card card) {
		cards.add(card);
	}

	/**
	 * Reset talon.
	 */
	void reset() {
		cards.clear();
	}

	/**
	 * Sort set of cards.
	 */
	void sort() {
		Collections.sort(cards, Util.noTrumpComparator);
	}

	/**
	 * Put all cards face up.
	 */
	public void reveal() {
		for (Card card : cards) {
			card.faceUp();
		}
	}
}
