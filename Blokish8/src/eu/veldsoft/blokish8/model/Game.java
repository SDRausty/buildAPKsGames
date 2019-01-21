/*
 * Copyright (C) 2011- stephane coutant
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

package eu.veldsoft.blokish8.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Game {
	public static final String tag = "sc";
	public List<Board> boards = new ArrayList<Board>();
	public int size = 20;

	public Game() {
		reset();
	}

	public void reset() {
		boards.clear();
		for (int k = 0; k < 8; k++) {
			boards.add(new Board(k));
		}
	}

	public List<Move> moves = new ArrayList<Move>();

	public void historize(Move move) {
		moves.add(move);
	}

	/**
	 * @return true if game is over
	 */
	public boolean over() {
		boolean result = true;
		for (int k = 0; k < 8; k++) {
			result = result && boards.get(k).over;
		}
		
		return result;
	}

	// TODO adapt message when equal score?
	/**
	 * on equal score : winner is the last to play.
	 * 
	 */
	public int winner() {
		int highscore = 0;
		for (int p = 0; p < 8; p++)
			highscore = Math.max(highscore, boards.get(p).score);
		for (int p = 7; p >= 0; p--) {
			if (boards.get(p).score == highscore)
				return p;
		}
		return -1;
	}

	/**
	 * to be called onto a fresh Game...
	 */
	public boolean replay(List<Move> moves) {
		for (Move move : moves) {
			Piece piece = move.piece;
			int color = piece.color;
			Piece p = boards.get(color).findPieceByType(piece.type);
			p.reset(piece);
			move.piece = p;

			if (play(move) == false)
				return false;
		}

		return true;
	}

	protected void add(Piece piece, int i, int j) {
		for (int k = 0; k < 8; k++) {
			boards.get(k).add(piece, i, j);
		}
	}

	public boolean valid(Move move) {
		return valid(move.piece, move.i, move.j);
	}

	public boolean valid(Piece piece, int i, int j) {
		return fits(piece, i, j) && boards.get(piece.color).onseed(piece, i, j);
	}

	public boolean fits(Piece p, int i, int j) {
		boolean result = true;
		for (int k = 0; k < 8; k++) {
			result = result && boards.get(k).fits(k, p, i, j);
		}
		
		return result;
	}

	public boolean play(Move move) {
		if (!valid(move)) {
			Log.e(tag, "not valid! " + move);
			Log.e(tag, "not valid! " + move.piece);
			return false;
		}
		add(move.piece, move.i, move.j);
		Log.d(tag, "played move : " + move);
		historize(move);
		return true;
	}

	public String toString() {
		String msg = "# moves : " + moves.size();
		for (Move move : moves) {
			msg += "\n" + Move.serialize(move);
		}
		return msg;
	}

	public List<Move> deserialize(String msg) {
		List<Move> list = new ArrayList<Move>();
		return list;
	}

	int[][] ab = new int[20][20];

	/**
	 * @return # of seeds if actually adding enemy @param piece at @param i, @param
	 *         j on board @param board.
	 */
	private int scoreEnemySeedsIfAdding(Board board, Piece piece, int i, int j) {
		// how many of the board's seeds happen to be under piece?
		int result = 0;
		for (int b = 0; b < 20; b++)
			for (int a = 0; a < 20; a++)
				ab[a][b] = 0;
		for (Square s : board.seeds()) {
			try {
				ab[s.i][s.j] = 1;
			} catch (Exception e) {
			}
		}
		for (Square s : piece.squares()) {
			try {
				ab[i + s.i][j + s.j] = 0;
			} catch (Exception e) {
			}
		}
		for (int b = 0; b < 20; b++)
			for (int a = 0; a < 20; a++)
				if (ab[a][b] == 1)
					result++;
		// Log.d(tag, "scoreEnemySeedsIfAdding : " + result + ". color : " +
		// board.color);
		return result;
	}

	public int scoreEnemySeedsIfAdding(int color, Piece piece, int i, int j) {
		int result = 0;
		//TODO try consider only Red as enemy, for machine to compete with human!
		result += scoreEnemySeedsIfAdding(boards.get(0), piece, i, j);
		return result;
	}
}
