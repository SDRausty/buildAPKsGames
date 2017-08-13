package org.scoutant.cc.model;

public class TurnMgr {

	private int turn = 0;
	
	public void update() {
		turn++;
		turn = turn%6;
	}

	public int player() {
		return turn;
	}
	
	public boolean allowed(Peg peg){
		return ( peg.color == turn);
	}

}
