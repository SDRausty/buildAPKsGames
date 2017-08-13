package eu.veldsoft.russian.triple.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;

/**
 * Trick after cards play.
 * 
 * @author Todor Balabanov
 */
@SuppressWarnings("serial")
class Trick implements Serializable {
	// TODO Player trick tracking can be useful.
	/**
	 * Cards in the trick.
	 */
	private Vector<Card> cards = new Vector<Card>();

	/**
	 * Constructor with parameters.
	 * 
	 * @param trick
	 *            The taken trick.
	 */
	public Trick(Map<Player, Card> trick) {
		for (Player player : trick.keySet()) {
			cards.add(trick.get(player));
		}
	}

	/**
	 * Cards in the trick getter.
	 * 
	 * @return Set of cards.
	 */
	Vector<Card> getCards() {
		return cards;
	}

	/**
	 * Cards trick setter
	 * 
	 * @param cards
	 *            Set of cards.
	 */
	void setCards(Vector<Card> cards) {
		this.cards = cards;
	}

	/**
	 * Reset the trick.
	 */
	void reset() {
		cards.clear();
	}

	/**
	 * Sort cards in the trick.
	 */
	void sort() {
		Collections.sort(cards, Util.noTrumpComparator);
	}
}
