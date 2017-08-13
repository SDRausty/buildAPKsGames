package eu.veldsoft.russian.triple.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

/**
 * Player's hand class.
 * 
 * @author Todor Balabanov
 */
@SuppressWarnings("serial")
class Hand implements Serializable {
	/**
	 * Number of cards in the hand during announce stage.
	 */
	static int NUMBER_OF_CARDS_DURING_ANNOUNCE = 7;

	/**
	 * Number of cards in the hand start playing stage.
	 */
	static int NUMBER_OF_CARDS_FOR_START_PLAYING = 8;

	/**
	 * Set of cards.
	 */
	private Vector<Card> cards = new Vector<Card>();

	/**
	 * Cards getter.
	 * 
	 * @return Set of cards in the player's hand.
	 */
	Vector<Card> getCards() {
		// TODO May be it is better to do a deep copy.
		return cards;
	}

	/**
	 * Cards setter.
	 * 
	 * @param cards
	 *            Set of cards in the player's hand.
	 */
	void setCards(Vector<Card> cards) {
		// TODO May be it is better to do a deep copy.
		this.cards = cards;
	}

	/**
	 * Reset player's hand by removing all cards.
	 */
	void reset() {
		cards.clear();
	}

	/**
	 * Add card in the player's hand.
	 * 
	 * @param card
	 *            Card to be added.
	 */
	void recieve(Card card) {
		cards.add(card);
	}

	/**
	 * Arrange class according card arrangement policy.
	 */
	void sort() {
		Collections.sort(cards, Util.noTrumpComparator);
	}
}
