package eu.veldsoft.russian.triple.model;

import java.util.Comparator;

/**
 * Comparator use for cards order when there is a trupm.
 * 
 * @author Todor Balabanov
 */
class TrumpComparator implements Comparator<Card> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Card lhs, Card rhs) {
		if (lhs.getSuit().isTrump() == true && rhs.getSuit().isTrump() == false) {
			return -1;
		}

		if (lhs.getSuit().isTrump() == false && rhs.getSuit().isTrump() == true) {
			return +1;
		}

		if (lhs.getSuit().getOrder() > rhs.getSuit().getOrder()) {
			return -1;
		} else if (lhs.getSuit().getOrder() < rhs.getSuit().getOrder()) {
			return +1;
		} else {
			if (lhs.getRank().getPoints() > rhs.getRank().getPoints()) {
				return -1;
			} else if (lhs.getRank().getPoints() < rhs.getRank().getPoints()) {
				return +1;
			}
		}

		return 0;
	}
}
