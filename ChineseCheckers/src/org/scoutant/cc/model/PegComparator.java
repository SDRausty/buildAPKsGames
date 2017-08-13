package org.scoutant.cc.model;

import java.util.Comparator;

/**
 * A Peg is 'preferred' if distance to target if larger
 */
public class PegComparator implements Comparator<Peg>{
	private int player;
	private PegEvaluator evaluator;

	public PegComparator(int color) {
		this.player = color;
		this.evaluator = new PegEvaluator();
	}
	
	@Override
	public int compare(Peg a, Peg b) {
		return evaluator.evaluate(b) - evaluator.evaluate(a);
	}
	
	private class PegEvaluator implements Evaluator<Peg> {
		@Override
		public int evaluate(Peg peg) {
			return Board.length(player, peg.point);
		}
	}
}
