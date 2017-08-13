package eu.veldsoft.russian.triple.model;

import java.io.Serializable;
import java.util.Vector;

import eu.veldsoft.russian.triple.model.computer.ComputerBidder;

/**
 * Each round there is a different bidding. Create bidding object each time.
 * 
 * @author Todor Balabanov
 */
@SuppressWarnings("serial")
public class Bidding implements Serializable {
	// TODO Implement methods for reading and writing in the serialization
	// process.

	/**
	 * Keep track of the players from the board.
	 */
	private Player players[] = null;

	/**
	 * Index of the current bidder.
	 */
	private int currentBidderIndex = -1;

	// TODO Create BidHistory class.
	/**
	 * Bid history record.
	 */
	private Vector<Bid> bidHistory = new Vector<Bid>();

	/**
	 * Constructor with parameters.
	 * 
	 * @param players
	 *            Set of players.
	 * @param firstBidderIndex
	 *            Who is going to bid first.
	 */
	public Bidding(Player players[], int firstBidderIndex) {
		this.players = players;
		currentBidderIndex = firstBidderIndex;
	}

	/**
	 * Current bidding player getter.
	 * 
	 * @return Player who is bidding at the moment.
	 */
	public Player getCurrentBidder() {
		return (players[currentBidderIndex]);
	}

	/**
	 * Do bid by a human.
	 * 
	 * @param value
	 *            Value of the bid.
	 * 
	 * @return True if the bid was done, false otherwise.
	 */
	public boolean doBid(int value) {
		// TODO Handle human bidder too.

		if (players[currentBidderIndex] instanceof HumanPlayer == false) {
			return false;
		}

		Bid bid = new Bid(value, players[currentBidderIndex]);

		if (bid.valid() == true) {
			bidHistory.add(bid);
			return true;
		} else {
			/*
			 * Pass.
			 */
			players[currentBidderIndex].stopBidding();
		}

		return false;
	}

	/**
	 * Do bid by an artificial intelligence.
	 * 
	 * @return True if the bid was done, false otherwise.
	 */
	public boolean doBid() {
		if (players[currentBidderIndex] instanceof ComputerBidder == false) {
			return false;
		}

		ComputerBidder bidder = (ComputerBidder) players[currentBidderIndex];

		int score = 0;
		if (hasLast() == true) {
			score = last().getScore();
		}

		if (bidder.canDoBid(score) == true) {
			Bid bid = bidder.doBid(score);

			if (bid.getScore() == 0) {
				/*
				 * Pass.
				 */
				bidder.endBidding();
			} else if (bid.valid() == true) {
				bidHistory.add(bid);
				return true;
			} else {
				/*
				 * Pass.
				 */
				bidder.endBidding();
			}
		} else {
			/*
			 * Pass.
			 */
			bidder.endBidding();
		}

		return false;
	}

	/**
	 * Has last bid ckeck.
	 * 
	 * @return True if there is a last bid, false otherwise.
	 */
	public boolean hasLast() {
		return !bidHistory.isEmpty();
	}

	/**
	 * Obtain last bid.
	 * 
	 * @return Last bid done or null pointer of there is no last bid.
	 */
	public Bid last() {
		if (bidHistory.isEmpty() == false) {
			return bidHistory.lastElement();
		}

		return null;
	}

	/**
	 * Has winner of the bidding process.
	 * 
	 * @return True if there is a winner in the bidding process, false
	 *         otherwise.
	 */
	public boolean hasWinner() {
		return !bidHistory.isEmpty();
	}

	/**
	 * Obtain winner of the bidding process.
	 * 
	 * @return Winning bid or null pointer if there is no winner.
	 */
	public Bid announceWinner() {
		if (hasWinner() == true) {
			return bidHistory.lastElement();
		}

		return null;
	}

	/**
	 * Obtain bidding end.
	 * 
	 * @return True if the bidding process finished, false otherwise.
	 */
	public boolean finished() {
		int counter = 0;

		for (Player player : players) {
			if (player.isBidding() == true) {
				counter++;
			}
		}

		if (counter > 1) {
			return false;
		}

		return true;
	}

	/**
	 * Move to the next player who can bid.
	 * 
	 * @return True if next player will bid, false otherwise.
	 */
	public boolean nextBidder() {
		boolean biddingInProgress = false;
		for (Player player : players) {
			if (player.isBidding() == true) {
				biddingInProgress = true;
				break;
			}
		}

		if (biddingInProgress == false) {
			return false;
		}

		do {
			currentBidderIndex = (currentBidderIndex + 1) % players.length;
		} while (players[currentBidderIndex].isBidding() == false);

		return true;
	}
}
