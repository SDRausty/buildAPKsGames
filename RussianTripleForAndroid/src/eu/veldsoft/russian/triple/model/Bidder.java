package eu.veldsoft.russian.triple.model;

/**
 * Player who can bid.
 * 
 * @author Todor Balabanov
 */
public interface Bidder {
	/**
	 * Check is it possible to bid.
	 * 
	 * @param currentValue
	 *            Current bid value.
	 * 
	 * @return True if it can bid, false otherwise.
	 */
	boolean canDoBid(int currentValue);

	/**
	 * Make a bid.
	 * 
	 * @param currentValue
	 *            Current bid value.
	 * 
	 * @return New bid produced.
	 */
	Bid doBid(int currentValue);

	/**
	 * Finish bidding process.
	 */
	void endBidding();
}
