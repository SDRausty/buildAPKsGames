package org.scoutant.cc;

import java.util.List;

import org.scoutant.Command;
import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Peg;
import org.scoutant.cc.model.Pixel;
import org.scoutant.cc.model.Point;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class PegUI extends ImageView {
	public static int[] icons = { R.drawable.red, R.drawable.green, R.drawable.pink, R.drawable.blue, R.drawable.violet, R.drawable.orange};
	public static int[] ins = { R.anim.red_in, R.anim.push_left_in,R.anim.push_left_in, R.anim.blue_in, R.anim.push_left_in, R.anim.push_left_in}; 
	protected Peg peg;
	private int diameter;
	public GameAware game;

	public PegUI(Context context) {
		super(context);
	}
	
	public PegUI(Context context, Peg peg, GameAware game) {
		this(context);
		this.peg = peg;
		this.game = game;
		this.diameter = game.diameter();
		setImageBitmap( BitmapFactory.decodeResource(context.getResources(), icons[peg.color]));
		doLayout();
		}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		doLayout();
	}
	
	private void doLayout() {
		LayoutParams params = new LayoutParams(diameter, diameter, Gravity.TOP);
		Pixel pixel = game.pixel( peg.point);
		Rect rect = toSquare(pixel, diameter);
		params.setMargins(rect.left-1, rect.top-1, rect.right, rect.bottom);
		setLayoutParams(params);
		setAlpha( peg.equals( game.selected()) ? 95 : 255);
	}
	
	@Override
	public String toString() {
		return peg.toString();
	}
	/**
	 * @return a Rect instance representing a square centered on (@param x, @param y) with @param length  
	 */
	public static Rect toSquare(Pixel l, int length) {
		return new Rect( l.x-length/2, l.y-length/2, l.x+length/2, l.y+length/2);
	}

	/**
	 * Animate the Peg so as to move along the path corresponding to provided
	 * @param move
	 */
	public void animate(Move move, Command whenDone) {
		if (move==null) return;
		List<Point> points = move.points;
		if (points==null || points.size()< 2) return;
		MoveAnimation animation = new MoveAnimation( this, move);
		game.addAnimation(animation);
		animation.start( whenDone);
	}

	/**
	 * Adjust the pegs position according to played
	 * @param move 
	 */
	public void move(Move move) {
		peg.point = move.last();
		invalidate();
	}
}

