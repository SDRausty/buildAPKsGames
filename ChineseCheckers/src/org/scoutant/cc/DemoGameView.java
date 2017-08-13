package org.scoutant.cc;

import org.scoutant.Command;
import org.scoutant.cc.model.Board;
import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Move;
import org.scoutant.cc.model.Peg;
import org.scoutant.cc.model.Pixel;
import org.scoutant.cc.model.Player;
import org.scoutant.cc.model.Point;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class DemoGameView extends FrameLayout implements GameAware {
	private static String tag = "view";
	public static int sizeI = Board.sizeI;
	public static int sizeJ = Board.sizeJ;
	public int dI;
	public int dJ;
	protected int diameter;
	private Bitmap hole ;
	public Game game;
	private Context context;

	public DemoGameView(Context context, int height) {
		super(context);
		this.context = context;
		setWillNotDraw(false);
        setLayoutParams( new LayoutParams(height, height, Gravity.CENTER));
        dJ = height/sizeJ;
        dI = Double.valueOf(dJ / 0.8660254).intValue();
        diameter = Double.valueOf( 0.96*dI).intValue();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        hole = BitmapFactory.decodeResource(context.getResources(), R.drawable.steel);
        init();
		}
	public void init() {
		game = new Game( new boolean[] { true, false, true , false, true , false});
		removeAllViews();
		for (Player player : game.players) {
			for (Peg peg : player.pegs()) {
				addView( new PegUI(context, peg, this));
			}
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBoard(canvas);
	}
	
	private void drawBoard(Canvas canvas){
		for (int j=0; j<sizeJ; j++){
			for (int i=0; i< sizeI; i++) {
				Pixel l = pixel(new Point(i, j));
				if (Board.hole.is(i,j)) {
					canvas.drawBitmap(hole, null, GameView.toSquare(l, diameter), null);
				}
			}
		}
	}

	public void play(Move move, boolean animate, Command whenDone) {
		if (Game.LOG) Log.d(tag, "playing move " + move);
		Peg start = game.peg(move.point(0));
		PegUI peg = findPeg(start);
		game.play(move);
		peg.animate(move, whenDone);
	}
	
	
	@Override
	public int diameter() { return diameter*9/10; }

	@Override
	public Peg selected() { return null; }	
	
	public Pixel pixel(Point p) {
		int oI = (p.j%2==0 ? 0 : dI/2);
		return new Pixel(dI/2 + p.i*dI+oI, dJ/2 +p.j*dJ);
	}
	
	@Override public void addAnimation(MoveAnimation anim) { }
	@Override public void pauseAnimations() { }
	@Override public void clearAnimations() { }
	
	private PegUI findPeg(Peg peg) {
		if (peg==null) return null;
		PegUI found=null;
		for (int i=0; i<getChildCount(); i++) {
			View v = getChildAt(i);
			if (v instanceof PegUI) {
				found = (PegUI) v;
				if (found.peg.equals(peg)) return found;
			}
		}
		return null;
	}
	
}
