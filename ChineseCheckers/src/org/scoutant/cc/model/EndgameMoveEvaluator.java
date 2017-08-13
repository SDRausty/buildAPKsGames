package org.scoutant.cc.model;

/**
 * The AI evaluation function, suitable during end game. ie when the # of positive jumps is low. Considering both jump and hop.<p>
 * @return a scalar representing, in following order : <ul> 
 * <li>player direction, 
 * <li>distance to target point
 * <li>distance to axis as second level
 * <li>bonus for move entering into target triangle (from outsite) 
 * <li>malus for player move with peg already into triangle
 */
public class EndgameMoveEvaluator implements Evaluator<Move> {
	
	private int player;

	public EndgameMoveEvaluator(int player) {
		this.player = player;
	}
	@Override
	public int evaluate (Move m) {
		int target = m.length(player);
		int axis = m.axisLengh(player);
		int intoTriangle = enteringTriangle(player, m); 
		int alreadyIntoTriangle = Board.length(player, m.point(0))<4 ? 1 : 0; 
		return 1*Board.sizeJ*target - axis  + Board.sizeJ*intoTriangle - Board.sizeJ/2*alreadyIntoTriangle;
	}

	private static int enteringTriangle(int player,final Move m) {
		if (m==null || m.point(0)==null || m.last()== null ) throw new IllegalAccessError("Move shall have at least 2 points!");
		return (Board.length(player, m.point(0))>=4 && Board.length(player, m.last()) < 4 ? 1 : 0);
	}
}