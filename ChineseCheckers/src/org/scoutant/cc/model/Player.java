package org.scoutant.cc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

	/** Each player has a set of ten balls */ 
	private List<Peg> pegs = new ArrayList<Peg>();
	private PegComparator comparator=null;
	
	public List<Peg> pegs() {
		Collections.sort(pegs, comparator);
		return pegs;
	}
	
	public Point goal;
	public int color;
	public int over=0;
	
	private Player(Point goal) {
		this.goal = goal;
	}
	private Player(int i, int j) {
		this(new Point(i,j));
	}
	public Player(int i, int j, int color) {
		this(i,j);
		this.color = color;
		comparator = new PegComparator(color);
	}
	
	public Player add(Peg peg) {
		pegs.add(peg);
		peg.color = this.color;
		return this;
	}
	public void clear() {
		pegs.clear();
	}
	
	public Peg peg(int index) {
		return pegs.get(index);
	}
	
	public Peg peg(Point p) {
		for(Peg peg : pegs) {
			if(peg.point.equals(p)) return peg;
		}
		return null;
	}
	
	public Player add(int i, int j) {
		return add( new Peg(i,j, color));
	}

	public String toString() {
		return toString(0, Board.sizeJ-1);
	}

	/** A graphical display for visual console checks */
	public String toString(int jmin, int jmax) {
		boolean ji[][] = new boolean [Board.sizeJ][Board.sizeI];
		for (Peg p : pegs) {
			ji[p.point.j][p.point.i] = true;
		}
		String msg = "";
		msg += "----------------player--------------------\n";
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
	 * @return true if @param point is occupied by one of the 10 pegs of present player 
	 */
	public boolean has(Point point) {
		for (Peg peg : pegs) {
			if (peg.point.equals(point)) return true;
		}
		return false;
	}
	
}
