package eu.veldsoft.russian.triple.model;

import java.util.Map;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;

/**
 * Game board class. The most common object in the object model.
 * 
 * @author Todor Balabanov
 */
public class Board {
	/**
	 * Index of the human player in the array with the players.
	 */
	public static final int HUMAN_PLAYER_INDEX = 1;

	/**
	 * Game state.
	 */
	private State state = State.NOT_STARTED;

	/**
	 * Each round different player is first.
	 */
	private int firstInRoundIndex = HUMAN_PLAYER_INDEX;

	/**
	 * Bidding process object.
	 */
	private Bidding bidding = null;

	/**
	 * Keep reference to the player who won announce.
	 */
	private Player announceWinner = null;

	/**
	 * Talone object.
	 */
	private Talon talon = new Talon();

	/**
	 * Tricks played.
	 */
	private Map<Player, Card> trick = new HashMap<Player, Card>();

	/**
	 * All players on the board.
	 */
	private Player players[] = { new ComputerPlayer("Player 1"),
			new HumanPlayer("Player 2"), new ComputerPlayer("Player 3") };

	/**
	 * State getter.
	 * 
	 * @return Board state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * State setter.
	 * 
	 * @param state
	 *            Next board state.
	 */
	public void setState(State state) {
		this.state = state;

		if (state == State.STARTING) {
			resetGame();
		}

		if (state == State.DEALING) {
			resetRound();
			deal();
		}

		if (state == State.CONTRACTING) {
			revealTalon();
		}
	}

	/**
	 * Bidding object getter.
	 * 
	 * @return Bidding object.
	 */
	public Bidding getBidding() {
		// TODO Consider to do a deep copy of this object.
		return bidding;
	}

	/**
	 * Talon getter.
	 * 
	 * @return Talon object.
	 */
	Talon getTalon() {
		// TODO Consider to do a deep copy of this object.
		return talon;
	}

	/**
	 * Talon setter.
	 * 
	 * @param talon
	 *            New talon.
	 */
	void setTalon(Talon talon) {
		// TODO Consider to do a deep copy of this object.
		this.talon = talon;
	}

	/**
	 * Trick getter.
	 * 
	 * @return Last trick.
	 */
	Vector<Card> getTrick() {
		Vector<Card> trick = new Vector<Card>();

		for (Player player : players) {
			trick.add(this.trick.get(player));
		}

		return trick;
	}

	/**
	 * Trick setter.
	 * 
	 * @param trick
	 *            New trick.
	 */
	void setTrick(Vector<Card> trick) {
		// TODO Check for number of cards in the trick.

		this.trick.clear();

		for (int i = 0; i < trick.size() && i < players.length; i++) {
			this.trick.put(players[i], trick.elementAt(i));
		}
	}

	/**
	 * Players getter.
	 * 
	 * @return All players on the board.
	 */
	Player[] getPlayers() {
		return players;
	}

	/**
	 * Players setter.
	 * 
	 * @param players
	 *            All players who should be on the board.
	 */
	void setPlayers(Player[] players) {
		this.players = players;
	}

	/**
	 * Player internal information.
	 * 
	 * @return Service information for the players.
	 */
	public String[] getPlayersInfo() {
		String info[] = new String[players.length];

		for (int p = 0; p < players.length; p++) {
			info[p] = players[p].getName() + " ~ " + players[p].getScore();
		}

		return info;
	}

	/**
	 * Announce winner getter.
	 * 
	 * @return Reference to the player won the announce stage.
	 */
	public Player getAnnounceWinner() {
		return announceWinner;
	}

	/**
	 * Get all cards which are on the board.
	 * 
	 * @return All visible cards on the board even if they should be face down.
	 */
	public Card[][] getCardsOnTheBoard() {
		Card cards[][] = { { null, null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null, null },
				{ null, null, null }, { null, null, null } };

		int c = 0;
		for (int p = 0; p < players.length; p++, c = 0) {
			for (Card card : players[p].getHand().getCards()) {
				cards[p][c++] = card;
			}
		}

		c = 0;
		for (Card card : talon.getCards()) {
			cards[3][c++] = card;
		}

		c = 0;
		for (Player player : players) {
			cards[4][c++] = trick.get(player);
		}

		return cards;
	}

	/**
	 * Reset full game.
	 */
	public void resetGame() {
		state = State.STARTING;
		firstInRoundIndex = Util.PRNG.nextInt(players.length);
		resetRound();
	}

	/**
	 * Reset the current round.
	 */
	public void resetRound() {
		Card.Suit.removeTrump();
		bidding = new Bidding(players, firstInRoundIndex);
		talon = new Talon();
		trick.clear();
		for (Player player : players) {
			player.resetRound();
		}
		for (int p = 0; p < players.length; p++) {
			if (firstInRoundIndex == p) {
				firstInRoundIndex = (p + 1) % players.length;
				break;
			}
		}
	}

	/**
	 * Deal cards in the beginning of each round.
	 */
	public void deal() {
		state = State.DEALING;

		Deck.reset();
		Deck.shuffle();

		int index = 0;

		/*
		 * Deal talon.
		 */
		for (int i = 0; i < 3; i++) {
			Deck.cardAtPosition(index).faceDown();
			talon.recieve(Deck.cardAtPosition(index));
			index++;
		}
		talon.sort();

		for (int p = 0; p < players.length; p++) {
			for (int i = 0; i < 7; i++) {
				Card card = Deck.cardAtPosition(index);
				if (p == HUMAN_PLAYER_INDEX) {
					card.faceUp();
				}
				players[p].recieve(card);
				index++;
			}
			players[p].prepare();
		}

		state = State.BIDDING;
	}

	/**
	 * Modify board according selected card.
	 * 
	 * @param selected
	 *            Index of the card which was selected.
	 */
	public void click(int selected) {
		/*
		 * Players hands.
		 */
		int index = 0;
		for (int p = 0; p < players.length; p++, index = p * 8) {
			for (Card card : players[p].getHand().getCards()) {
				if (index == selected) {
					if (p == HUMAN_PLAYER_INDEX) {
						/*
						 * Talon spliting action.
						 */
						if (card.isUnhighlighted() == true) {
							Deck.setAllUnhighlighted();
							card.highlight();
							if (state == State.CONTRACTING
									&& bidding.announceWinner().getPlayer() == players[HUMAN_PLAYER_INDEX]) {
								card.getSuit().setTrump();
							}
						} else if (card.isHighlighted() == true) {
							card.unhighlight();
						}
					}

					// TODO Take actions.
					return;
				}

				index++;
			}
		}

		/*
		 * Talon.
		 */
		index = 24;
		for (Card card : talon.getCards()) {
			if (index == selected) {
				if (card.isUnhighlighted() == true) {
					Deck.setAllUnhighlighted();
					card.highlight();
					if (state == State.CONTRACTING
							&& bidding.announceWinner().getPlayer() == players[HUMAN_PLAYER_INDEX]) {
						card.getSuit().setTrump();
					}
				} else if (card.isHighlighted() == true) {
					card.unhighlight();
				}

				// TODO Take actions.
				return;
			}

			index++;
		}

		/*
		 * Trick.
		 */
		index = 27;
		for (Player player : players) {
			if (index == selected) {
				// TODO Take actions.
				return;
			}

			index++;
		}
	}

	/**
	 * Put all talon cards face up.
	 */
	public void revealTalon() {
		talon.reveal();

		// TODO It should be somewhere else, not in this method.
		for (Player player : players) {
			if (player instanceof ComputerPlayer) {
				((ComputerPlayer) player).talonLink(talon);
			}
		}
	}

	/**
	 * Give card during contract stage from announce winner to one of the other
	 * players.
	 * 
	 * @param from
	 *            From player index.
	 * @param to
	 *            To player index.
	 * 
	 * @return True if card transfer was successful, false otherwise.
	 */
	public boolean giveCardDuringContracting(int from, int to) {
		// TODO Check from player index for correctness.

		List<Card> selected = Deck.selected();

		/*
		 * Only one card can be given to each of the opponents during contract
		 * stage.
		 */
		if (selected.size() <= 0 || selected.size() > 1) {
			return false;
		}

		/*
		 * Number of cards after announce should match.
		 */
		if (players[to].getHand().getCards().size() != Hand.NUMBER_OF_CARDS_DURING_ANNOUNCE) {
			return false;
		}

		/*
		 * Give card to the opponent.
		 */
		Card card = selected.get(0);
		players[to].getHand().recieve(card);
		card.unhighlight();
		if (players[to] instanceof ComputerPlayer) {
			card.faceDown();
		} else if (players[to] instanceof HumanPlayer) {
			card.faceUp();
		}

		/*
		 * Remove the card from its previous place.
		 */
		players[from].getHand().getCards().remove(card);
		talon.getCards().remove(card);

		return true;
	}

	/**
	 * Take talon at the end of contracting stage.
	 * 
	 * @return True if talon is taken, false otherwise.
	 */
	public boolean takeTalon() {
		if (talon.getCards().size() == 0) {
			return true;
		}

		/*
		 * Check how many players have enough cards to start playing.
		 */
		int counter = 0;
		Player receiver = null;
		for (Player player : players) {
			if (player.getHand().getCards().size() != Hand.NUMBER_OF_CARDS_FOR_START_PLAYING) {
				receiver = player;
			} else {
				counter++;
			}
		}

		/*
		 * Only one player get all cards from the talon.
		 */
		if (counter == 2) {
			receiver.getHand().getCards().addAll(talon.getCards());
			talon.getCards().removeAllElements();

			/*
			 * Resort players hands.
			 */
			for (Player player : players) {
				player.getHand().sort();
			}

			return true;
		}

		return false;
	}

	/**
	 * Give two cards to the opponents, one card for each.
	 * 
	 * @param gifts
	 *            Cards to be given.
	 * @param opponents
	 *            Players to receive cards.
	 * 
	 * @return True if cards were given correctly, false otherwise.
	 */
	public boolean giveCards(Player[] opponents, Card[] gifts) {
		for (int i = 0; i < opponents.length && i < gifts.length; i++) {
			opponents[i].getHand().recieve(gifts[i]);
			opponents[i].getHand().sort();
		}

		return true;
	}

	/**
	 * Check for playing stage by counting cards in players hands.
	 * 
	 * @return True if playing stage can start, false otherwise.
	 */
	public boolean readyToPlay() {
		for (Player player : players) {
			if (player.readyToPlay() == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Obtain player's opponents.
	 * 
	 * @param player
	 * @return
	 */
	public Player[] opponents(Player me) {
		Player opponents[] = new Player[players.length - 1];

		int p = 0;
		for (Player player : players) {
			if (player == me) {
				continue;
			}
			opponents[p++] = player;
		}

		return opponents;
	}
}
