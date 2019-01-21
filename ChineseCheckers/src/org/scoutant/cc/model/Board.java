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
import java.util.List;


public class Board {
	public static final String tag = "model";
	private static final boolean[][] holeJI = { 
		{ false, false, false, false, false, false, true, false, false, false, false, false, false }, // 0

			{ false, false, false, false, false,  true, true, false, false, false, false, false, false }, // 1

		{ false, false, false, false, false,  true, true,  true, false, false, false, false, false }, // 2

			{ false, false, false, false,  true,  true, true,  true, false, false, false, false, false }, // 3
			
		{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true,  true }, // 4
		
			{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 5
			
		{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 6
		
			{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true, false, false }, // 7
			
		{ false, false,  true,  true,  true,  true, true,  true,  true,  true,  true, false, false }, // 8 ----------------------
		
			{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true, false, false }, // 7
			
		{ false,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 6
		
			{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true, false }, // 5
			
		{  true,  true,  true,  true,  true,  true, true,  true,  true,  true,  true,  true,  true }, // 4
		
			{ false, false, false, false,  true,  true, true,  true, false, false, false, false, false }, // 3
			
		{ false, false, false, false, false,  true, true,  true, false, false, false, false, false }, // 2
		
			{ false, false, false, false, false,  true, true, false, false, false, false, false, false }, // 1
			
		{ false, false, false, false, false, false, true, false, false, false, false, false, false }, // 0
		};

	private static final boolean[][] centreJI = { 
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 0
		
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 1
			
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 2
		
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 3
			
		{ false, false, false, false,  true,  true,  true,  true,  true, false, false, false, false }, // 4
		
			{ false, false, false,  true,  true,  true,  true,  true,  true, false, false, false, false }, // 5
			
		{ false, false, false,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 6
		
			{ false, false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 7
			
		{ false, false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 8 ----------------------
		
			{ false, false,  true,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 7
		
		{ false, false, false,  true,  true,  true,  true,  true,  true,  true,  true, false, false }, // 6
		
			{ false, false, false,  true,  true,  true,  true,  true,  true, false, false, false, false }, // 5
		
		{ false, false, false, false,  true,  true,  true,  true,  true, false, false, false, false }, // 4
		
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 3
		
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 2
	
			{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 1
		
		{ false, false, false, false, false, false, false, false, false, false, false, false, false }, // 0
		};

	public static int radiusI = 6;
	public static int radiusJ = 8;
	public static int sizeI = 2*radiusI+1;
	public static int sizeJ = 2*radiusJ+1;
	public static Board center = new Board( centreJI ); 
	public static Board hole = new Board( holeJI ); 
	
	public static boolean hole(Point p) {
		if (p.i<0 || p.i>= sizeI || p.j<0 || p.j>=sizeJ) return false;
		return hole.is(p);
	}

	public static Point oposite(Point p) {
		return new Point (sizeI-1-p.i, sizeJ-1-p.j );
	}
	
	public boolean[][] ji = new boolean [sizeJ][sizeI];
	
	public Board() {
	}
	private Board(final boolean[][]tab) {
		ji = tab;
	}
	
	public boolean is(int i, int j) {
		return ji[j][i];
	}
	
	public boolean is(final Point p) {
		return ji[p.j][p.i];
	}

	public void set(final Point p) {
		ji[p.j][p.i] = true;
	}
	public void set(int i, int j) {
		ji[j][i] = true;
	}
	public void reset(Point p) {
		ji[p.j][p.i] = false;
	}
	public void reset(int i, int j) {
		ji[j][i] = false;
	}
	
	/** 
	 * Hop to neighbor hole in provided direction @param d 
	 *<p>   0  1
	 *<p> 5  *  2
	 *<p>   4  3
	 * @return the new Point identified by a hop in provided direction @param dir.
	 * Or null if hopping out of board.
	 * <p> Does not check if target happens to be filled with a ball  
	 */
	public final Point hop(Point p, int d) {
		if (p==null || d<0 || d> 6) return null;
		Point t = p.clone();
		switch (d) {
			case 0: t.decrement();	t.j--;	break;
			case 1: t.increment();	t.j--;	break;
			case 2: t.i++;	break;
			case 3: t.increment();	t.j++;	break;
			case 4: t.decrement();	t.j++;	break;
			case 5: t.i--;	break;
		}
		return hole(t) ? t : null;
	}
	
	/**
	 * If any @return the point corresponding to the first ball, starting from origin @param o in provided direction @param dir.
	 */
	public Point ball(Point o, int dir) {
		for (Point p : points(o,dir)) {
			if (is(p)) return p;
		}
		return null;		
	}

	/** 
	 * Short jump in provided direction @param d 
	 * @return the new Point or null if :
	 * <li>jumping out of board,
	 * <li>no ball to jump over
	 * <li>target is not free  
	 */
	public final Point jump(Point p, int d) {
		Point ball = ball(p,d);
		if (ball==null) return null;
		// let's checks all points till target actually are free and actually are hole!
		int length = Move.length(p, ball, d);
		// we are jumping out of board if the nb of points below does not reach the length to middle point
		List<Point> points = points(ball,d);
		if (points.size()<length) return null;
		if (points.size()==0) return null;
		Point t=null;
		for (int k=0; k<length; k++) {
//			Log.d(tag, "p : " + p +" d : " + d + ", k : " + k + ", ball : " + ball);
			t = points.get(k);
			if ( is(t)) return null;
		}
		return t;
	}

	/**
	 * @return true if points @param a and @param z are extremities of a valid jump : <ul>
	 * <li> z is a free hole
	 * <li> a and z are in same line
	 * <li> distance between a and z is even
	 * <li> middle point is a ball
	 * <li> but middle point cannot be the move's origin, which is @param origin 
	 * <li> all other intermediate holes are free   
	 */
	public boolean valid(Point a, Point z, Point origin) {
		if (a.equals(z)) return true;
		if(is(z)) return false;
		int dir = Move.direction(a, z);
		if (dir==-1) return false;
		int l = Move.length(a, z, dir);
		if (l<= 1) return true;
		// length must be even
		if (l%2!=0) return false;
		// below only true jumps, we just have to check : 1) a ball in the middle and 2) no other balls in the way.
		Point middle = Move.middle(a, z);
		if(!is(middle)) return false;
		if (middle.equals(origin)) {
			Log.d(tag, "cannot jump over one-self!");
			return false;
		}
		// Checks no other balls in the way (but the one in the middle of course)
		for (int k=1; k<l/2; k++) {
			if (dir==2 || dir==5) {
				if (dir==2 && (is(a.i+k,a.j) || is(z.i-k,z.j))) return false;
				if (dir==5 && (is(a.i-k,a.j) || is(z.i+k,z.j))) return false;
			} else {
				if (is(a,z,dir,k,l) ) return false;
				if (is(z,a,(dir+3)%6,k,l) ) return false;
			}
		}
		return true;
	}
	
	/**
	 * @return true if point at distance @param dist is a ball. Provided points @param a and @param z. Assuming they are in same line
	 * with direction @param dir (possible values 0, 1, 3 or 4). Points are separated by a even length @param l.  
	 */
	protected boolean is(Point a, Point z, int dir, int dist, int l) {
		switch (dir) {
		case 3:
			if (a.isEven()) return is(a.i+dist/2, a.j+dist);
			else return is(a.i+(dist+1)/2, a.j+dist);
		case 4:
			if (a.isEven()) return is(a.i-(dist+1)/2, a.j+dist);
//			else return is(a.i-(dist+1)/2, a.j+dist);
			else return is(a.i-(dist)/2, a.j+dist);
		case 0:
			if (a.isOdd()) return is(a.i-dist/2, a.j-dist);
			else return is(a.i-(dist+1)/2, a.j-dist);
		case 1:
//			if (a.isEven()) return is(a.i+(dist+1)/2, a.j-dist);
			if (a.isEven()) return is(a.i+(dist)/2, a.j-dist);
			else return is(a.i+(dist+1)/2, a.j-dist);
		default:
			throw new IllegalArgumentException("Bad direction provided");
		}
	}

	/**
	 * @return true if move is valid as topography and against present position of balls.
	 */
	public boolean valid(Move move) {
		List<Point> points = move.points;
		if (Move.isHop(points.get(0), points.get(1))) { 
			// move is a hop,
			if (points.size()>2) return false;
			// we need to check that target is free
			return !is(points.get(1));
		}
		// below only multi-step move. Each step is a jump. May be mono jump.
		for (int i=0; i<points.size()-1; i++) {
			Point a = points.get(i);
			Point z = points.get(i+1);
			if (Move.isHop(a,z)) return false;
			boolean isValid = valid(a, z, points.get(0));
//			if ( !valid(a, z, points.get(0))) return false;
			if ( !isValid) return false;
		}
		return true;
	}
	
	public String toString() {
		return toString(0, sizeJ-1);
	}
	public String toString(int jmin, int jmax) {
		String msg = "";
		msg += "----------------board---------------------\n";
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
	
	
	/**
	 * @return the list of points of the board, starting from origin @param o in provided direction @param dir.
	 */
	public List<Point> points(Point o, int dir) {
		List<Point> points = new ArrayList<Point>();
		Point p = o;
		for (int k=1; k<sizeI; k++) {
			p = hop(p, dir);
			if (p==null) return points;
			points.add(p);
		}
		return points;
	}
	
	/**
	 * length to rich end-point of target triangle for player 0 
	 */
	private static final int[][] lengthJI_0 = { 
		{ -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1 }, //   0

		  { -1, -1, -1, -1, -1,  1,  1, -1, -1, -1, -1, -1, -1 }, // 1

		{ -1, -1, -1, -1, -1,  2,  2,  2, -1, -1, -1, -1, -1 }, //   2

		  { -1, -1, -1, -1,  3,  3,  3,  3, -1, -1, -1, -1, -1 }, // 3
			
		{  8,  7,  6,  5,  4,  4,  4,  4,  4,  5,  6,  7,  8 }, //   4
		
		  {  8,  7,  6,  5,  5,  5,  5,  5,  5,  6,  7,  8, -1 }, // 5
			
		{ -1,  8,  7,  6,  6,  6,  6,  6,  6,  6,  7,  8,  -1 }, //  6
		
		  { -1,  8,  7,  7,  7,  7,  7,  7,  7,  7,  8, -1, -1 }, // 7
			
		{ -1, -1,  8,  8,  8,  8,  8,  8,  8,  8,  8, -1,  -1 }, //  8 ----------------

		  { -1,  9,  9,  9,  9,  9,  9,  9,  9,  9,  9, -1, -1 }, // 9
	
		{ -1, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,  -1 }, // 10

		  { 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, -1 }, //11
	
		{ 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 }, //  12
	
		  { -1, -1, -1, -1, 13, 13, 13, 13, -1, -1, -1, -1, -1 }, //13
	
		{ -1, -1, -1, -1, -1, 14, 14, 14, -1, -1, -1, -1, -1 }, //  14

		  { -1, -1, -1, -1, -1, 15, 15, -1, -1, -1, -1, -1, -1 }, //15
	
		{ -1, -1, -1, -1, -1, -1, 16, -1, -1, -1, -1, -1, -1 }, //  16	
	};
	/**
	 * length to rich end-point of target triangle for player 1 
	 */
	private static final int[][] lengthJI_1 = { 
		{ -1, -1, -1, -1, -1, -1,  8, -1, -1, -1, -1, -1, -1 }, //   0

		  { -1, -1, -1, -1, -1,  8,  7, -1, -1, -1, -1, -1, -1 }, // 1

		{ -1, -1, -1, -1, -1,  8,  7,  6, -1, -1, -1, -1, -1 }, //   2

		  { -1, -1, -1, -1,  8,  7,  6,  5, -1, -1, -1, -1, -1 }, // 3
			
		{ 12, 11, 10,  9,  8,  7,  6,  5,  4,  3,  2,  1,  0 }, //   4
		
		  { 12, 11, 10,  9,  8,  7,  6,  5,  4,  3,  2,  1, -1 }, // 5
			
		{ -1, 12, 11, 10,  9,  8,  7,  6,  5,  4,  3,  2,  -1 }, //  6
		
		  { -1, 12, 11, 10,  9,  8,  7,  6,  5,  4,  3, -1, -1 }, // 7
			
		{ -1, -1, 12, 11, 10,  9,  8,  7,  6,  5,  4, -1,  -1 }, //  8 ----------------

		  { -1, 13, 12, 11, 10,  9,  8,  7,  6,  5,  5, -1, -1 }, // 9
	
		{ -1, 14, 13, 12, 11, 10,  9,  8,  7,  6,  6,  6,  -1 }, // 10

		  { 15, 14, 13, 12, 11, 10,  9,  8,  7, 7,  7,  7, -1 }, //11
	
		{ 16, 15, 14, 13, 12, 11, 10,  9,  8,  8, 8,  8,  8 }, //  12
	
		  { -1, -1, -1, -1, 12, 11, 10,  9, -1, -1, -1, -1, -1 }, //13
	
		{ -1, -1, -1, -1, -1, 12, 11, 10, -1, -1, -1, -1, -1 }, //  14

		  { -1, -1, -1, -1, -1, 12, 11, -1, -1, -1, -1, -1, -1 }, //15
	
		{ -1, -1, -1, -1, -1, -1, 12, -1, -1, -1, -1, -1, -1 }, //  16	
	};
	/**
	 * @return length to rich end-point of target triangle for @param player, from provided point @param p 
	 */
	public static int length(int player, Point p) {
		int i;
		switch (player) {
		case 0: return lengthJI_0[p.j][p.i]; 
		case 1: return lengthJI_1[p.j][p.i];
		case 3: return lengthJI_0[16-p.j][p.i]; 
		case 2: return lengthJI_1[16-p.j][p.i]; 
		case 5:
			i = p.isEven() ? 12-p.i : 11 -p.i ; 
			return lengthJI_1[p.j][i]; 
		case 4: 
			i = p.isEven() ? 12-p.i : 11 -p.i ; 
			return lengthJI_1[16-p.j][i]; 
		default: throw new IllegalArgumentException("player ranging from 0 to 5...");
		}
	}
	
	/**
	 * @return true if point @param p is into target triangle for player @param player  
	 */
	public static boolean intoTriangle(int player, Point p) {
		return Board.length(player, p) < 4;
	}
	
	public static boolean outsideTriangle( int player, Peg peg) {
		return !intoTriangle(player, peg.point);
	}
	
	public static Point[] thirdRow(int player) {
		switch (player) {
		case 0: return new Point[] { new Point( 4, 3), new Point( 5, 3), new Point( 6, 3), new Point( 7, 3), };
		case 3: return new Point[] { new Point( 4,13), new Point( 5,13), new Point( 6,13), new Point( 7,13), };
		case 1: return new Point[] { new Point( 9, 4), new Point( 9, 5), new Point(10, 6), new Point(10, 7), };
		case 5: return new Point[] { new Point( 1, 7), new Point( 2, 6), new Point( 2, 5), new Point( 3, 4), };
		case 2: return new Point[] { new Point(10, 9), new Point(10,10), new Point( 9,11), new Point( 9,12), };
		case 4: return new Point[] { new Point( 1, 9), new Point( 2,10), new Point( 2,11), new Point( 3,12), };
		default: throw new IllegalArgumentException("player ranging from 0 to 5...");
		}
	}
	
	public static final Point[] pivots = { new Point(6,4), new Point(9,6), new Point(9,10), new Point(6,12), new Point(3,10), new Point(3,6) };
	public static final Point[][] escapes = { 
			{ new Point( 5, 4), new Point( 7, 4) }, 
			{ new Point( 8, 5), new Point( 9, 7) }, 
			{ new Point( 9, 9), new Point( 8,11) }, 
			{ new Point( 7,12), new Point( 7,10) }, 
			{ new Point( 3,11), new Point( 2, 9) }, 
			{ new Point( 2, 7), new Point( 3, 5) }, 
		};
	public static final Move[][] beginnings = { 
		{ new Move( new Point( 5,13)).add( pivots[3]),  new Move( new Point( 6,13)).add( pivots[3])}, 
		{ new Move( new Point( 2,10)).add( pivots[4]),  new Move( new Point( 2,11)).add( pivots[4])}, 
		{ new Move( new Point( 2, 5)).add( pivots[5]),  new Move( new Point( 2, 6)).add( pivots[5])}, 
		{ new Move( new Point( 5, 3)).add( pivots[0]),  new Move( new Point( 6, 3)).add( pivots[0])}, 
		{ new Move( new Point( 9, 5)).add( pivots[1]),  new Move( new Point(10, 6)).add( pivots[1])}, 
		{ new Move( new Point(10,10)).add( pivots[2]),  new Move( new Point( 9,11)).add( pivots[2])} 
	};
	
	public static final Point[][] blockers = {
		{ new Point( 5,14), new Point( 7,14), new Point( 5,15), new Point( 6,15)},
		{ new Point( 0,11), new Point( 1,10), new Point( 1,12), new Point( 2,12)},
		{ new Point( 1, 4), new Point( 2, 4), new Point( 0, 5), new Point( 1, 6)},
		{ new Point( 5, 2), new Point( 7, 2), new Point( 5, 1), new Point( 6, 1)},
		{ new Point(10, 4), new Point(11, 4), new Point(11, 5), new Point(11, 6)},
		{ new Point(10,12), new Point(11,12), new Point(11,10), new Point(11,11)},
	};
	
	public static final Point[] origins = { new Point(6,16), new Point(0,12), new Point(0,4), new Point(6,0), new Point(12,4), new Point(12,12) };
	public static final Point[][] secondRows = { 
		{ new Point( 5,15), new Point( 6,15) }, 
		{ new Point( 0,11), new Point( 1,12) }, 
		{ new Point( 1, 4), new Point( 0, 5) }, 
		{ new Point( 5, 1), new Point( 6, 1) }, 
		{ new Point(11, 4), new Point(11, 5) }, 
		{ new Point(11,11), new Point(11,12) }, 
	};
}