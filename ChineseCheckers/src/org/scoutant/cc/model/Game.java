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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements org.scoutant.Serializable {
	// TODO set LOG and DEV before publish
	public static final boolean LOG=false;
	public static final boolean DEV=false;
	
	@SuppressWarnings("unused")
	private static String tag = "model";

	public List<Player> players = new ArrayList<Player>();
	public Board board;
	public Player player(int index) {
		return players.get(index);
	}
	public List<Move> moves = new ArrayList<Move>();
	public Move last() { return moves.get(moves.size()-1);}

	public int counter = 1;
	public int[] overs() {
		int [] overs = new int [6];
		for (int i=0; i<6; i++) overs[i] = players.get(i).over;
		return overs;
	}
	public int zeroLengthMoves=0;
	
	public boolean pop() {
		if (moves.size() <= 0) return false;
		moves.remove(moves.size()-1);
		return true;
	}

	public Game( ) {
		this( new boolean[] { true, true, true, true, true, true } );
	}
	
	/**
	 * creating players in this order :
	 *<p>    3 
	 *<p> 2     4
	 *<p> 1     5
	 *<p>    0
	 */
	public Game( boolean[] playings ) {
		board = new Board();
		int color=0;
		players.add( new Player( 6, 0, color++).add( 6,16).add( 5,15).add( 6,15).add( 5,14).add( 6,14).add( 7,14).add( 4,13).add( 5,13).add( 6,13).add( 7,13) ); // 0
		players.add( new Player(12, 4, color++).add( 0,12).add( 1,12).add( 2,12).add( 3,12).add( 0,11).add( 1,11).add( 2,11).add( 1,10).add( 2,10).add( 1, 9) ); // 1
		players.add( new Player(12,12, color++).add( 0, 4).add( 1, 4).add( 2, 4).add( 3, 4).add( 0, 5).add( 1, 5).add( 2, 5).add( 1, 6).add( 2, 6).add( 1, 7) ); // 2
		players.add( new Player( 6,16, color++).add( 6, 0).add( 5, 1).add( 6, 1).add( 5, 2).add( 6, 2).add( 7, 2).add( 4, 3).add( 5, 3).add( 6, 3).add( 7, 3) ); // 3
		players.add( new Player( 0,12, color++).add( 9, 4).add(10, 4).add(11, 4).add(12, 4).add( 9, 5).add(10, 5).add(11, 5).add(10, 6).add(11, 6).add(10, 7) ); // 4
		players.add( new Player( 0, 4, color++).add( 9,12).add(10,12).add(11,12).add(12,12).add( 9,11).add(10,11).add(11,11).add(10,10).add(11,10).add(10, 9) ); // 5
		for (int i=0; i<6; i++) {
			Player player = players.get(i);
			if (!playings[i]) player.clear();
			for (Peg peg : player.pegs()) {
				board.set(peg.point);
			}
		}
	}

	/** return Peg corresponding pointed by provided @param point */
	public Peg peg(Move move) {
		return peg(move.point(0));
	}

	
	/** return Peg corresponding pointed by provided @param point */
	public Peg peg(Point point) {
		for (Player player : players) {
			for (Peg p : player.pegs()) {
				if (p.point.equals(point)) return p;
			}
		}
		return null;
	}
	
	/** Actually move @param peg to a given position @param p. Keeping in synch the pegs list and the board 'ball'. 
	 * Validation is do be done elsewhere.
	 */
	public void move(Peg peg, Point p) {
		board.reset( peg.point);
		peg.point = p;
		board.set(p);
		return;
	}

	/** @return true if each steps of given @param move is valid */
	public boolean valid(Move move) {
		return board.valid(move);
	}
	
	/**
	 * Assuming move was previously validated. Play provided move @param m.
	 * @return true if done.   
	 */
	public void play(Move move) {
		Peg peg = peg( move.point(0));
		if (peg==null) return;
		Point point = move.point( move.points.size()-1);
		move( peg, point);
		moves.add(move);
		registerIfOver( peg.color);
		registerLengthMove(move, peg.color);
		return;
	}

	/**
	 * To avoid game with only AIs to fall in infinite loop in case of blocking position.
	 * Here we count the number of zero-length consecutive moves.
	 * <p>AITask may request this counter and decide to stop the chaining.  
	 */
	private void registerLengthMove(Move move, int player) {
		if (move.length(player)==0) zeroLengthMoves++;
		else zeroLengthMoves=0;
	}

	private void registerIfOver(int player) {
		if ( over(player)) players.get( player).over = counter++;
	}
	
	public String toString() {
		return toString(0, Board.sizeJ-1);
	}

	/** A graphical display for visual console checks */
	public String toString(int jmin, int jmax) {
		boolean ji[][] = new boolean [Board.sizeJ][Board.sizeI];
		for (Player player : players) {
			for (Peg p : player.pegs()) {
				ji[p.point.j][p.point.i] = true;
			}
		}
		String msg = "";
		msg += "----------------game----------------------\n";
		for (int j=jmin; j<=jmax; j++) {
			if (j%2 == 1) msg += "  ";
			for (int i=0; i<Board.sizeI; i++) {
				msg += ji[j][i] ? " X " : "   "; 
			}
			msg += "\n";
		}
		msg += "------------------------------------------\n";
		return msg;
	}

	public boolean over() {
		return over(0) && over(1) && over(2) && over(3) && over(4) && over(5);
	}
	
	public boolean over(int player) {
		List<Peg> pegs = players.get(player).pegs();
		if (pegs.isEmpty()) return true; // player was not playing at all...
		for (Peg peg: pegs) {
			if (Board.outsideTriangle(player, peg)) return false; 
		}
		return true;
	}

	@Override
	public String serialize() {
		String str= "";
		for (Move move : moves) {
			str+= move.serialize() +"\n";
		}
		return str;
	}
	// no direct deserialization : we will replay the list of moves instead. To have the UIs in sync with the game state...

	public static List<Integer> ranking( int[] overs) {
		// let's prune 0...
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Integer> indexes= new ArrayList<Integer>();
		List<Integer> values = new ArrayList<Integer>();
		for (int i=0; i<6; i++) {
			int value = overs[i];
			if (value >0) {
				values.add(value);
				map.put(value, i);
			}
		}
		Collections.sort(values);
		for (int i=0; i<values.size(); i++) {
			indexes.add( map.get(values.get(i)));
		}
		return indexes;
		
	}
	
}