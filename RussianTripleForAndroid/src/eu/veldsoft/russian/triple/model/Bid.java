package eu.veldsoft.russian.triple.model;

import java.io.Serializable;
import java.util.Vector;

import eu.veldsoft.russian.triple.model.Card.Rank;

/**
 * Bid description class.
 * 
 * @author Todor Balabanov
 */
@SuppressWarnings("serial")
public class Bid implements Serializable {
	/**
	 * Minimum valid bid value according game rules.
	 */
	public final static int MIN_VALID_BID_VALUE = 100;

	/**
	 * Score to be achieved if the bidding process is won.
	 */
	private int score;

	/**
	 * Player who did the bid.
	 */
	private Player player;

	/**
	 * Copy constructor.
	 * 
	 * @param bid
	 *            Original object.
	 */
	public Bid(Bid bid) {
		super();
		this.score = bid.score;
		this.player = bid.player;
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param score
	 *            Score to achieve.
	 * @param player
	 *            Player who is doing the bid.
	 */
	public Bid(int score, Player player) {
		super();
		this.score = score;
		this.player = player;
	}

	/**
	 * Score getter.
	 * 
	 * @return Score value.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Player getter.
	 * 
	 * @return Player who did the bid.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Maximum score available as bid value.
	 * 
	 * @return Maximum valid bid value.
	 */
	public int maximum() {
		int numberOfMarriages = 0;

		Vector<Card> cards = player.getHand().getCards();

		for (Card a : cards) {
			if (a.getRank() != Rank.QUEEN) {
				continue;
			}

			for (Card b : cards) {
				if (b.getRank() != Rank.KING) {
					continue;
				}

				if (a.getSuit() == b.getSuit()) {
					numberOfMarriages++;
				}
			}
		}

		if (numberOfMarriages < 1) {
			return 120;
		}

		if (numberOfMarriages < 2) {
			return 160;
		}

		if (numberOfMarriages < 3) {
			return 180;
		}

		if (numberOfMarriages < 4) {
			return 200;
		}

		return 0;
	}

	/**
	 * Check for valid bid.
	 * 
	 * @return True if the bid is valid, false otherwise.
	 */
	public boolean valid() {
		if (score > maximum()) {
			return false;
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + score;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bid other = (Bid) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (score != other.score)
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Bid [score=" + score + ", player=" + player + "]";
	}
}
