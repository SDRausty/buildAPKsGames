/*
* Copyright (C) 2012- stephane coutant
*
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Players in this order :
 *<p>    3 
 *<p> 2     4
 *<p> 1     5
 *<p>    0
 *
 *<p>Directions in the board : 
 *<p>  0  1
 *<p> 5    2
 *<p>  4  3
 *
 */
public class AI {
	private static String tag = "ai";
	/** Consider as first directions pointing to opposite triangle. */
	public static final int[][] dirs = {
		{0, 1, 5, 2, 4, 3}, // optimal directions for player : 0  
		{1, 2, 0, 3, 5, 4}, // 1  
		{2, 3, 1, 4, 0, 5}, // 2 
		{3, 4, 2, 5, 1, 0}, // 3 
		{4, 5, 3, 0, 2, 1}, // 4 
		{5, 0, 4, 1, 3, 2}, // 5 
	};

	private Game game;
	private Board board;
	private int player;
	private List<Peg> pegs;
	private Board track;
	private List<Move> moves = new ArrayList<Move>();
	private Comparator<Move> comparator; 
	private Comparator<Move> endgameComparator;
	private Random random = new Random();
	
	public AI(Game game, int player) {
		this.game = game;
		this.player = player;
		board = game.board;
		comparator = new MoveComparator( new MoveEvaluator(player));
		endgameComparator = new MoveComparator( new EndgameMoveEvaluator(player));
	}
	
	protected void init() {
		// TODO performance exclude peg that has reached target!
		pegs = game.player(player).pegs();
		moves.clear();
	}
	
	public Move think() {
		init();
		thinkJumps();
		int nb = nbPositiveJumps();
		think012PegHops();
		Collections.sort(moves, comparator);
		if (Game.LOG) Log.d(tag, "# of jumps : " + moves.size() +", and strictly positive : " + nb);
		if (isBeginning()) {
			considerBeginning();
		}
		if (threatening()) {
			boolean done = consider0PegMoves();
			if (done) return moves.get(0); 
		}
		if (nb<=4) {
			thinkHops();
			Collections.sort(moves, endgameComparator);
			Log.d(tag, "# of moves including hops : " + moves.size());
		}
		if (moves.size()<=4) { // trapped in the 9-peg endgame issue?
			consider9PegEndgamePosition();
		}
		log();
		if (moves.size()==0) return null; // game over
		Move move = randomAmongBest();
		return move ;
	}

	private boolean consider0PegMoves() {
		thinkHops();
		if (! ableToPlay0Peg()) return false; // we are not going to promote 0-peg if it cannot be moved anyway! 
		// We have 0-peg moves, now keep only those...
		for (int i=moves.size()-1; i>=0; i--) {
			Move move = moves.get(i);
			if ( !move.first().equals( Board.origins[player])) {
				moves.remove(i);
			}
		}
		Collections.sort(moves, comparator);
		Log.d(tag, "escaping threatening situation : ");
		if (moves.size()==0) return false; // game over ?
		return true ;
		
	}
	private boolean ableToPlay0Peg() {
		for (Move move : moves) {
			if (move.first().equals(Board.origins[player])) return true;
		}
		return false;
	}

	protected void thinkHops() {
		for (Peg peg : pegs) {
			for (int i=0; i<4; i++) {
				int dir = dirs[player][i];
				Point p = board.hop(peg.point, dir);
				if (p!=null && !board.is(p)) {
					// target is a hole not occupied by a peg
					Move move = new Move( peg.point);
					move.add(p);
					moves.add( move);
				}
			}
		}
	}
	
	/**
	 * find hops for the potential 0-peg and the 2 15-length pegs... 
	 */
	protected void think012PegHops() {
		for (int k=0; k<=2; k++) {
			Peg peg = pegs.get(k);
			if (Board.length(player, peg.point)>=15) { 
				for (int i=0; i<2; i++) {
					int dir = dirs[player][i];
					Point p = board.hop(peg.point, dir);
					if (p!=null && !board.is(p)) {
						// target is a hole not occupied by a peg
						Move move = new Move( peg.point);
						move.add(p);
						moves.add( move);
					}
				}
			}
		}
	}

	/**
	 * @return the list of moves for given play. Considering only jumps.
	 */
	protected void thinkJumps() {
		pegs = game.player(player).pegs();
		for (Peg peg : pegs) {
			Move move = new Move(peg.point);
			track = new Board();
			visite( move);
		}
		Collections.sort(moves, comparator);
	}
	
	private void visite(Move move) {
		for (int dir:dirs[player] ) {
			visite(move, dir);
		}
	}
	
	private void visite(Move move, int dir) {
		Point p = board.jump(move.last(), dir);
		if (p==null) return;
		Point middle = Move.middle(p, move.last());
		if (move.point(0).equals(middle)) {
			Log.d(tag, "excluding jump over origin peg.");
			return;
		}
		if (track.is(p)) {
			return;
		}
		track.set(p);
		Move found = move.clone();
		found.add(p);
		if (found.length( player)>=0) {
			moves.add(found);
		} 
		visite( found.clone());
	}

	/** for test purpose */
	protected List<Move> moves() { return moves; }
	
	private void log(Move move) {
		Log.d(tag, "move ! [ " + move.length(player) + " ] < " + new MoveEvaluator(player).evaluate(move) + ", "  + new EndgameMoveEvaluator(player).evaluate(move) + "> " + move);
	}
	private void log() {
		log( moves);
	}
	
	private void log(List<Move> moves) {
		Log.d(tag, "# of moves : " + moves.size());		
		for (Move move :moves) {
			log(move);
		}
	}

	/** 
	 * Among the moves, @return the number of ones which length is strictly positive.
	 */
	private int nbPositiveJumps() {
		int nb=0;
		for (Move move: moves ) {
			if (move.length(player) > 0) nb++;
		}
		return nb;
	}

	/**
	 * @return a nice move among the best ones : taken randomly among the best moves, against 'move length'.
	 * <p>For the very first move we also consider the 2 one-step hop towards pivot point.
	 */
	private Move randomAmongBest() {
		Move best = moves.get(0);
		if (moves.size()==1) return best;
		if (best.length(player)==0) return best; // important to consider the very best when only zero-length moves available.
		int breakthrough = breakthrough();
		if (isBeginning() && moves.get(0).length(player)<=2 ) { // lets randomly get one of the 6 + 2 nive moves
			return moves.get(random.nextInt(breakthrough+2)); 
		}
		int index = random.nextInt( breakthrough < 6 ? breakthrough : 6);
		if (Game.LOG) Log.d(tag, "random index : " + index +", breaktrough was : " + breakthrough);
		return moves.get(index);
	}
	
	/**
	 * @return the first index of move which length is strictly shorter than move # 0. 
	 */
	private int breakthrough() {
		int l0 = moves.get(0).length(player);
		for (int i=1; i<moves.size(); i++) {
			if (l0>moves.get(i).length(player)) return i;
		}
		return moves.size();
	}

	/**
	 * It's the first move if the 4 'last' pegs are still at length more than 13 from target. 
	 */
	private boolean isBeginning(){
		for( int i=6; i<10; i++) {
			if ( Board.length(player, pegs.get(i).point) <= 12) return false;
		}
		return true;
	}
	
	/**
	 * As initial position, a jump is the standard move, but a one-step hop is nice too in order to prepare a 4-length jump the turn.
	 * Here we add 2 interesting moves that will complete the 6 standard other options. Ramdomly choosing one among those 8 moves will provided different
	 * game for the player...
	 */
	protected void considerBeginning(){
		if (!isBeginning()) return;
		if (moves.get(0).length(player)>=4) return; // no competing with long-length moves
		moves.add( Board.beginnings[player][0]);
		moves.add( Board.beginnings[player][1]);
	}


	/** 
	 * <p> In order to prevent this situation :
	 * <li> . . . .
	 * <li>  X . X
	 * <li>   X X
	 * <li>    O
	 * @return true if 3 enemy pegs occupy 3 of the 4 blocking holes...
	 */
	protected boolean threatening() {
		if (! game.player(player).has( Board.origins[player])) return false; // no backward peg, no worry.
		int free=0;
		for (Point p : Board.blockers[player]) {
			if (board.is(p)) {
				if (game.player(player).has(p)) return false;
			} else {
				free++;
			}
		}
		// when at least 2 holes, no worry
		if (free>=2) return false;
		return true;
	}
	
	/**
	 * <p> 9-pegs endgame issue :
	 * <li>. . X . .
	 * <li> . X X X
	 * <li>  X X X
	 * <li>   X X
	 * <li>    X
	 */
	private void consider9PegEndgamePosition() {
		for( int i = 1 ; i<10; i++) {
			Peg peg = pegs.get(i);
			if (!Board.intoTriangle(player, peg.point)) return;
		}
		// the 9 pegs closest to goal are into triangle. 
		Peg peg = pegs.get(0);
		if (Board.length(player, peg.point)!= 4) return; // if length is 3, game actually is over...
		if (! peg.point.equals(Board.pivots[player]) ) return; // if not pivot, AI naturally find move toward pivot.
		Point target = null;
		for (Point point : Board.thirdRow(player)) { // which is the hole not occupied by none of the 9 pegs?
			if ( !board.is(point)) target = point;
		}
		if (target==null) return; // surely occupied by a peg of another player!
		// correct option is to move peg towards target.
		int rigthPlayer = (player+1)%6;
		Move move = new Move(peg.point);
		Log.d(tag, "Considering 9-peg endgame issue! distance of free hole to rigth-player goal : " + Board.length(rigthPlayer, target) +", " + target);
		move.add( Board.length(rigthPlayer, target)>6 ? Board.escapes[player][0] : Board.escapes[player][1]);
		moves.clear();
		moves.add(move);
	}
}