package eu.veldsoft.russian.triple.model;

import java.io.Serializable;
import java.util.Vector;

/**
 * Base class for a player.
 * 
 * @author Todor Balabanov
 */
@SuppressWarnings("serial")
abstract public class Player implements Serializable {
	/**
	 * Player's name.
	 */
	private String name = "";

	/**
	 * Player's score for the current game.
	 */
	private int score = 0;

	/**
	 * Set of card in the hand of the player.
	 */
	private Hand hand = new Hand();

	/**
	 * Tricks taken by the player.
	 */
	private Vector<Trick> tricks = new Vector<Trick>();

	// TODO May be it should be in Bidding class.
	/**
	 * Can bid flag.
	 */
	private boolean canBid = false;

	/**
	 * Constructor with parameters.
	 * 
	 * @param name
	 *            Player's name.
	 */
	public Player(String name) {
		this.name = name;
	}

	/**
	 * Name getter.
	 * 
	 * @return Player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name setter.
	 * 
	 * @param name
	 *            Player's new name.
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Score getter.
	 * 
	 * @return Player's score for the current game.
	 */
	int getScore() {
		return score;
	}

	/**
	 * Score setter.
	 * 
	 * @param score
	 *            Player's score for the current game.
	 */
	void setScore(int score) {
		this.score = score;
	}

	/**
	 * Hand getter.
	 * 
	 * @return Player's hand.
	 */
	Hand getHand() {
		// TODO May be it is better to create deep copy.
		return hand;
	}

	/**
	 * Hand setter.
	 * 
	 * @param hand
	 *            New player's hand.
	 */
	void setHand(Hand hand) {
		// TODO May be it is better to create deep copy.
		this.hand = hand;
	}

	/**
	 * Tricks getter.
	 * 
	 * @return Tricks taken by the player in the current round.
	 */
	Vector<Trick> getTricks() {
		// TODO May be it is better to create deep copy.
		return tricks;
	}

	/**
	 * Tricks setter.
	 * 
	 * @param tricks
	 *            Tricks taken by the player in the current round.
	 */
	void setTricks(Vector<Trick> tricks) {
		// TODO May be it is better to create deep copy.
		this.tricks = tricks;
	}

	/**
	 * Bidding conditions getter.
	 * 
	 * @return True if the player still bidding, false otherwise.
	 */
	boolean isBidding() {
		return canBid;
	}

	/**
	 * Stop bidding setter.
	 */
	public void stopBidding() {
		canBid = false;
	}

	/**
	 * Increase player's score.
	 * 
	 * @param difference
	 *            Score difference.
	 */
	void addScore(int difference) {
		score += difference;
	}

	/**
	 * Decrease player's score.
	 * 
	 * @param difference
	 *            Score difference.
	 */
	void subractScore(int difference) {
		score -= difference;
	}

	// TODO Create Round class.
	/**
	 * Reset round conditions for the player..
	 */
	void resetRound() {
		canBid = true;
		hand.reset();
		tricks.clear();
	}

	/**
	 * Add card to the hand.
	 * 
	 * @param card
	 *            Card to be added in the player's hand.
	 */
	void recieve(Card card) {
		hand.recieve(card);
	}

	/**
	 * Add trick to taken tricks.
	 * 
	 * @param trick
	 *            Trick to be added in the taken tricks list.
	 */
	void recieve(Trick trick) {
		tricks.add(trick);
	}

	/**
	 * Prepare for game play.
	 */
	void prepare() {
		hand.sort();
	}

	/**
	 * Check for playing stage by counting cards in the hand.
	 * 
	 * @return True if playing stage can start, false otherwise.
	 */
	public boolean readyToPlay() {
		if (hand.getCards().size() != Hand.NUMBER_OF_CARDS_FOR_START_PLAYING) {
			return false;
		}

		return true;
	}
}
