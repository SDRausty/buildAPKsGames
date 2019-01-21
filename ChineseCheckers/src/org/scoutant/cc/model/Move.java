package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.List;

public class Move implements Comparable<Move>, org.scoutant.Serializable {
	
	public int score;
	public List<Point> points = new ArrayList<Point>();

	public Point point(int index) {
		return points.get(index);
	}

	public Point first(){
		return points.get(0);
	}
	
	public Point last() {
		if (points.isEmpty()) return null;
		return points.get(points.size()-1);
	}
	public Point penultima() {
		if (points.size()<2) return null;
		return points.get(points.size()-2);
	}
	
	public Move() {
	}
	
	public Move(Point p) {
		this();
		add(p);
	}
	public Move(Peg peg) {
		this( peg.point);
	}
	
	public Move clone(){
		Move move = new Move();
		// TODO performance : bunch array cloning? 
		for (Point p : points) move.add(p);
		return move;
	}
	
	public Move add(int i, int j) {
		return add( new Point(i,j));
	}
	public Move add(Point p) {
		points.add(p);
		return this;
	}

	public String toString() {
		String str = "";
		for (Point p : points) {
			str += p + " ";
		}
		return str;
	}
	
	/** valid if points actually are holes and if points are in same line. But does not checks for balls at all! */
	public static boolean valid (Point a, Point z) {
		if (a.equals(z)) return true;
		if ( !Board.hole(a) || !Board.hole(z)) return false;
		int dir = direction(a,z);
		if (dir==-1) return false; // not in same line
		int l = length(a, z, dir);
		if (l==1) return true; // it's a hop
		if (l%2==1) return false; // length cannot be odd!
		return true;
	}

	/** @return true if move is a hop. A hop is a single-step move as opposite to a jump over a ball. 
	 * Do not test if target actually is a hole. 
	 * We suppose a not equal to z
	 */
	public static boolean isHop(Point a, Point z) {
		int di = Math.abs(z.i-a.i);
		int dj = Math.abs(z.j-a.j);
		if (dj==0 && di==1) return true; 
		if (di>=2 || dj>=2) return false;
		// (5,2) and (6,3) are not neighbors. Happens when point with biggest i has an odd j.
		int j = (a.i>z.i? a.j : z.j);
		if (di==1 && j%2==1) return false;
		return true;
	}
	
	/**
	 * @return middle of points @param a and @param z.
	 * <p>Precondition : Points are supposed to be in same line and a even distance; 
	 */
	public static Point middle (Point a, Point z) {
		int o = (a.isOdd()?1:0);
		return new Point( (a.i+z.i+o)/2, (a.j+z.j)/2);
	}

	public Move pop() {
		if (!points.isEmpty()) points.remove( points.size()-1);
		return this;
	}
	
	/**
	 * Points are supposed not to be equals. 
	 * @return -1 if points @param a and @param z are not in same line.
	 * @return direction 0, 1, 2, 3, 4 or 5 if points @param a and @param z are not in same line.
	 *<p>  0  1
	 *<p> 5    2
	 *<p>  4  3
	 */
	public final static int direction(Point a, Point z) {
		int di = z.i-a.i;
		int dj = z.j-a.j;
		if (dj==0)return (di > 0 ? 2 : 5); 
		if (dj>0) { 
			int dir = direction (z,a);
			if (dir==-1) return -1;
			return dir +3;
		}
		// below dj<0 and only 0 or 1 is possible. Or not in line : -1
		int Di = Math.abs(di);
		int Dj = Math.abs(dj);
		// dj has to be more or less twice
		int DI = Dj/2;
		if (Dj%2==0) {
			if ( DI!=Di) return -1;
			return (di<0? 0 : 1);
		} else {
			if (a.isEven()) {
				if (di==DI) return 1;
				if (di==-DI-1) return 0;
			} else {
				if (di==DI+1) return 1;
				if (di==-DI) return 0;
			}
		}
		return -1;
	}

	/**
	 * @return scalar distance between first and last point.
	 * <p>With bonus if distance is positive and start is origin ar second row 
	 */
	public int length(int player) {
		int distance = Board.length(player, this.point(0)) - Board.length(player, last());
		if (distance <= 0 ) return distance;
		Point first = this.first();
		int zeroPegBonus = Board.origins[player].equals( first) ? 4 : 0;
		int secondRowBonus =  Board.secondRows[player][0].equals(first) || Board.secondRows[player][1].equals(first) ? 2 : 0;
		return distance + zeroPegBonus + secondRowBonus;
	}
	
	
	/** Points @param a and @param z are supposed to be in line with @param direction.
	 * @return corresponding length
	 */
	public static int length(Point a, Point z, int direction) {
		if (a.equals(z)) return 0;
		int dir = direction(a, z);
		if (dir==-1) return -1;
		if (dir==2 || dir==5) return Math.abs(a.i-z.i);
		return Math.abs(z.j-a.j);
	}

	@Override
	public int compareTo(Move that) {
		throw new IllegalAccessError( "Use MoveComparator instead! So as to consider player direction");
	}

	public int axisLengh(int player) {
		Point z = last();
		switch (player) {
			case 0: 
			case 3: return Math.abs( z.i-Board.radiusI);
			case 1: 
			case 4: return axisLengh14(z);
			case 2: 
			case 5: return axisLengh25(z);
			default: throw new IllegalArgumentException("player ranging from 0 to 5...");
		}
	}

	/**
	 * Axis is (0,12)<-->(12,4). A representation for even rows is : 2*i+3*j=36. Distance to axis is 2*z.i + 3*z.j-36;
	 */
	protected int axisLengh14(Point z) {
		return Math.abs( 2*z.i + 3*z.j - 36 );
	}
	
	/**
	 * Axis is (0,4)<-->(12,12). A representation for even rows is : 2*i-3*j=-12. Distance to axis is 2*z.i - 3*z.j+12;
	 */
	protected int axisLengh25(Point z) {
		return Math.abs( 2*z.i - 3*z.j +12 );
	}

	/**
	 * @return a string representation : a list of Point, separated by colons.
	 * Example : 5,14:6,12:4,8:
	 */
	@Override
	public String serialize() {
		String str = "";
		for (Point p : points) {
			str += p.serialize()+":";
		}
		return str;
	}
	public static Move deserialize(String str) {
		String[] data = str.split(":");
		// not considering the last...
		Move move = new Move();
		for (String s:data) {
			move.add( Point.desialize(s));
		}
		return move;
	}
	
	// TODO performance refactor with Collections.reserse() ?
	public Move reverse() {
		Move move = new Move();
		for (int i=points.size()-1; i>=0; i--) {
			move.add(points.get(i));
		}
		return move;
	}
}