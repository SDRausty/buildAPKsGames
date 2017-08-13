package eu.veldsoft.russian.triple.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Playing card.
 * 
 * @author Todor Balabanov
 */
@SuppressWarnings("serial")
public class Card implements Serializable {
	/**
	 * Card rank.
	 * 
	 * @author Todor Balabanov
	 */
	public enum Rank {
		NINE(0, "Nine"), JACK(2, "Jack"), QUEEN(3, "Queen"), KING(4, "King"), TEN(
				10, "Ten"), ACE(11, "Ace");

		/**
		 * Initialize static data.
		 */
		static {
			NINE.neighbors(JACK);
			JACK.neighbors(NINE, QUEEN);
			QUEEN.neighbors(JACK, KING);
			KING.neighbors(QUEEN, TEN);
			TEN.neighbors(KING, ACE);
			ACE.neighbors(TEN);
		}

		/**
		 * Points for the rank of the card.
		 */
		private int points;

		/**
		 * Name of the card.
		 */
		private String name;

		/**
		 * Keep track of rank neighbors.
		 */
		private List<Rank> neighbors;

		/**
		 * Constructor with parameters.
		 * 
		 * @param points
		 *            Points for the card according its rank.
		 * @param name
		 *            Name of the card.
		 */
		private Rank(int points, String name) {
			this.points = points;
			this.name = name;
		}

		/**
		 * Setup rank neighbors.
		 * 
		 * @param ranks
		 *            List of ranks.
		 */
		private void neighbors(Rank... ranks) {
			this.neighbors = Arrays.asList(ranks);
		}

		/**
		 * Points getter.
		 * 
		 * @return Points for this rank.
		 */
		int getPoints() {
			return points;
		}

		/**
		 * Name getter.
		 * 
		 * @return Card name.
		 */
		String getName() {
			return name;
		}

		/**
		 * Check for adjacent of the ranks.
		 * 
		 * @param rank
		 *            Rank to compare with.
		 * 
		 * @return True if they are adjacent, false otherwise.
		 */
		boolean isAdjacentTo(Rank rank) {
			return neighbors.contains(rank);
		}
	}

	/**
	 * Cards suit.
	 * 
	 * @author Todor Balabanov
	 */
	public enum Suit {
		DIAMONDS(1, "Diamonds"), CLUBS(2, "Clubs"), HEARTS(3, "Hearts"), SPADES(
				4, "Spades");

		/**
		 * Suit order.
		 */
		private int order = 0;

		/**
		 * Name of the suit.
		 */
		private String name = null;

		/**
		 * Trump flag.
		 */
		private boolean trump = false;

		/**
		 * Remove trump flag from all suits.
		 */
		public static void removeTrump() {
			// TODO May be it is not working!
			for (Suit suit : Suit.values()) {
				suit.trump = false;
			}
		}

		/**
		 * Check for trump selection.
		 * 
		 * @return True if the trump was seletecet, false otherwiese.
		 */
		public static boolean isTrumpSelected() {
			for (Suit suit : Suit.values()) {
				if (suit.isTrump() == true) {
					return true;
				}
			}

			return false;
		}

		/**
		 * Constructor with parameters.
		 * 
		 * @param number
		 *            Suit identifier.
		 * @param name
		 *            Suit name.
		 */
		private Suit(int number, String name) {
			this.order = number;
			this.name = name;
		}

		/**
		 * Suit order getter.
		 * 
		 * @return Number according suits order.
		 */
		int getOrder() {
			return order;
		}

		/**
		 * Suit name getter.
		 * 
		 * @return Suit name.
		 */
		String getName() {
			return name;
		}

		/**
		 * Set particular suit as trump suit.
		 */
		public void setTrump() {
			removeTrump();
			trump = true;
		}

		/**
		 * Is trump getter.
		 * 
		 * @return True if the suit is a trump suit, false otherwise.
		 */
		public boolean isTrump() {
			return (trump);
		}
	}

	/**
	 * Card suit.
	 */
	private Card.Suit suit;

	/**
	 * Card rank.
	 */
	private Card.Rank rank;

	/**
	 * Face up flag.
	 */
	private boolean faceUp;

	/**
	 * Card selection flag.
	 */
	private boolean highlighted;

	/**
	 * Card visibility flag.
	 */
	private boolean visible;

	/**
	 * Constructor with parameters.
	 * 
	 * @param rank
	 *            Card rank.
	 * @param suit
	 *            Card suit.
	 * @param faceUp
	 *            Face up flag.
	 * @param highlighted
	 *            Selection flag.
	 * @param visible
	 *            Visibility flag.
	 */
	public Card(Rank rank, Suit suit, boolean faceUp, boolean highlighted,
			boolean visible) {
		super();
		this.rank = rank;
		this.suit = suit;
		this.faceUp = faceUp;
		this.highlighted = highlighted;
		this.visible = visible;
	}

	/**
	 * Rank getter.
	 * 
	 * @return Card rank.
	 */
	Card.Rank getRank() {
		return rank;
	}

	/**
	 * Rank setter.
	 * 
	 * @param rank
	 *            Card rank.
	 */
	void setRank(Card.Rank rank) {
		this.rank = rank;
	}

	/**
	 * Suit getter.
	 * 
	 * @return Card suit.
	 */
	Card.Suit getSuit() {
		return suit;
	}

	/**
	 * Suit setter.
	 * 
	 * @param suit
	 *            Card suit.
	 */
	void setSuit(Card.Suit suit) {
		this.suit = suit;
	}

	/**
	 * Face up getter.
	 * 
	 * @return True if the card is in face up condition, false otherwise.
	 */
	public boolean isFaceUp() {
		return faceUp;
	}

	/**
	 * Face down getter.
	 * 
	 * @return True if the card is in face down condition, false otherwise.
	 */
	public boolean isFaceDown() {
		return !faceUp;
	}

	/**
	 * Put the card in face up condition.
	 */
	void faceUp() {
		faceUp = true;
	}

	/**
	 * Put the card in face down condition.
	 */
	void faceDown() {
		faceUp = false;
	}

	/**
	 * Card selection getter.
	 * 
	 * @return True if the card was selected, false otherwise.
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * Card selection getter.
	 * 
	 * @return True if the card was not selected, false otherwise.
	 */
	boolean isUnhighlighted() {
		return !highlighted;
	}

	/**
	 * Select the card.
	 */
	void highlight() {
		highlighted = true;
	}

	/**
	 * Unselect the card.
	 */
	void unhighlight() {
		highlighted = false;
	}

	/**
	 * Mark the card as visible.
	 */
	void visible() {
		visible = true;
	}

	/**
	 * Mark the card as invisible.
	 */
	void invisible() {
		visible = false;
	}

	/**
	 * Card visibility getter.
	 * 
	 * @return True if the card is visible, false otherwise.
	 */
	boolean isVisible() {
		return visible;
	}

	/**
	 * Card visibility getter.
	 * 
	 * @return True if the card is invisible, false otherwise.
	 */
	public boolean isInvisible() {
		return !visible;
	}

	/**
	 * Flip card face up to face down or voice versa.
	 */
	void flip() {
		faceUp = !faceUp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 17;

		result = prime * result + ((rank == null) ? 0 : rank.hashCode());

		result = prime * result + ((suit == null) ? 0 : suit.hashCode());

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Card other = (Card) obj;

		if (rank != other.rank) {
			return false;
		}

		if (suit != other.suit) {
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Card [" + rank.getName() + ", " + suit.getName() + ", "
				+ (faceUp == true ? "Face Up" : "Face Down") + ", "
				+ (highlighted == true ? "Selected" : "Unselected") + ", "
				+ (visible == true ? "Visible" : "Invidible") + "]";
	}
}
