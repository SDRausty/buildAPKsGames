package org.scoutant.cc;

import java.util.ArrayList;
import java.util.List;

import org.scoutant.Command;
import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Point;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

public class MoveAnimation {
	@SuppressWarnings("unused")
	private static String tag = "animation";
	private static final int DURATION = 200;

	private PegUI peg;

	private Point first;
	private int Dx;
	private int Dy;
	
	private List<Animator> animators = new ArrayList<Animator>();
	private AnimatorSet set = new AnimatorSet();

	// assuming peg actually is at position corresponding to first point in move...
	public MoveAnimation( PegUI peg, Move move) {
		this.peg = peg;
		this.first = move.first();
		Dx= -dx( move.last(), first);
		Dy= -dy( move.last(), first);
		peg.setTranslationX(-Dx);
		peg.setTranslationY(-Dy);
		
		for (int k=1; k<move.points.size(); k++) {
			Point to = move.point(k);
			this.add( move.point(k-1), to);
		}
	}
	// TODO nicer : parabolic animation
	public void add(Point from, Point to) {
		int dx = dx(first,to);
		int dy = dy(first,to);
		int di = Math.abs(to.i-from.i);
		int dj = Math.abs(to.j-from.j);
		AnimatorSet translateXY = new AnimatorSet();
		translateXY.play(ObjectAnimator.ofFloat(peg, "translationX", -Dx+dx) )
		.with(ObjectAnimator.ofFloat(peg, "translationY", -Dy+dy));
		translateXY.setDuration( Math.max(di, dj)*DURATION);
		animators.add(translateXY);
	}

	public void start(final Command whenDone) {
		set = new AnimatorSet();
		set.playSequentially(animators);
		if (whenDone!=null) {
			set.addListener( new AnimatorListener() {
				@Override public void onAnimationStart(Animator animation) { }
				@Override public void onAnimationRepeat(Animator animation) { }
				@Override public void onAnimationEnd(Animator animation) { whenDone.execute(); }
				@Override public void onAnimationCancel(Animator animation) { }
			});
		}
		set.start();
	}
	public void cancel() {
		peg.clearAnimation();
		set.removeAllListeners();
	}
	
	private int dx(Point from, Point to) {
		return peg.game.pixel(to).x - peg.game.pixel(from).x; 
	}
	private int dy(Point from, Point to) {
		return peg.game.pixel(to).y - peg.game.pixel(from).y; 
	}
}