package org.scoutant.cc.model;

import java.util.Comparator;

/**
 * Compare 2 moves against their length, according to player direction.
 * So that a Collections.sort(., comparator) puts at first the longest moves.  
 */
public class MoveComparator implements Comparator<Move> {

	private Evaluator<Move> evaluator;
	public MoveComparator(Evaluator<Move> evaluator) {
		this.evaluator = evaluator;
	}
	
	@Override
	public int compare(Move a, Move b) {
		return (evaluator.evaluate(b) - evaluator.evaluate(a));
	}
}