package org.scoutant.cc;

import org.scoutant.cc.model.Peg;
import org.scoutant.cc.model.Pixel;
import org.scoutant.cc.model.Point;

/**
 * Support for piece position and move animation 
 */
public interface GameAware {
	/** @return the diameter of a piece */
	int diameter();

	/** @return the center of the hole identified by provided @param point */
	Pixel pixel(Point p);
	/** */
	Peg selected();
	
	void addAnimation(MoveAnimation anim);
	void pauseAnimations();
	void clearAnimations();
}
