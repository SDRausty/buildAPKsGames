package eu.veldsoft.russian.triple.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.veldsoft.russian.triple.model.Card.Suit;
import eu.veldsoft.russian.triple.model.computer.ComputerBidder;
import eu.veldsoft.russian.triple.model.computer.ComputerContractor;

/**
 * Computer player class.
 * 
 * @author Todor Balabanov
 */
public class ComputerPlayer extends Player implements ComputerBidder,
		ComputerContractor {
	/**
	 * Reference to board talon object.
	 */
	private Talon talon = null;

	/**
	 * Constructor with parameters.
	 * 
	 * @param name
	 *            Player's name.
	 */
	public ComputerPlayer(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bid doBid(int currentValue) {
		Bid bid = null;

		// TODO Implement better AI.
		if (Util.PRNG.nextDouble() < 0.55) {
			if (currentValue < 100) {
				bid = new Bid(100, this);
			} else {
				bid = new Bid(currentValue + 1, this);
			}
		} else {
			/*
			 * Pass.
			 */
			bid = new Bid(0, this);
		}

		return bid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endBidding() {
		stopBidding();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canDoBid(int currentValue) {
		return isBidding();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Suit trumpSelection() {
		// TODO Implement something stronger than random search.
		return getHand().getCards()
				.get(Util.PRNG.nextInt(getHand().getCards().size())).getSuit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card[] giveCards() {
		// TODO Array can be replaced with list.
		Card gifts[] = { null, null };

		List<Card> cards = new ArrayList<Card>();
		cards.addAll(getHand().getCards());
		cards.addAll(talon.getCards());

		/*
		 * Random selection, but two different cards.
		 */
		do {
			gifts[0] = cards.get(Util.PRNG.nextInt(cards.size()));
			gifts[1] = cards.get(Util.PRNG.nextInt(cards.size()));
		} while (gifts[0] == gifts[1]);

		/*
		 * Remove cards from your own hand.
		 */
		talon.getCards().removeAll(Arrays.asList(gifts));
		getHand().getCards().removeAll(Arrays.asList(gifts));

		return gifts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void talonLink(Talon talon) {
		this.talon = talon;
	}
}
