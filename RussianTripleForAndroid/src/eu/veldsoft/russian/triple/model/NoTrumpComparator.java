package eu.veldsoft.russian.triple.model;

import java.util.Comparator;

/**
 * No trump comparator class.
 * 
 * @author Todor Balabanov
 */
class NoTrumpComparator implements Comparator<Card> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Card lhs, Card rhs) {
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
