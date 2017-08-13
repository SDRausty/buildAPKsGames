/*
 * Copyright (C) 2011- stephane coutant
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package eu.veldsoft.blokish8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.veldsoft.blokish8.model.AI;
import eu.veldsoft.blokish8.model.Board;
import eu.veldsoft.blokish8.model.Game;
import eu.veldsoft.blokish8.model.Move;
import eu.veldsoft.blokish8.model.Piece;
import eu.veldsoft.blokish8.model.Square;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * For DnD approach, refer to
 * http://blog.scoutant.org/index.php?post/2011/02/Approche
 * -naturelle-de-Drag-and-Drop-en-Android
 */
public class GameView extends FrameLayout {
	private static String tag = "activity";
	private Paint paint = new Paint();

	int size;
	ButtonsView buttons;
	PieceUI selected;
	int selectedColor;
	int swipe = 0;
	int gone = 0;

	Game game = new Game();

	AI ai = new AI(game);

	private Drawable[] dots = new Drawable[8];

	TextView[] tabs = new TextView[8];

	UI ui;

	/** true if red has acknowlegde no more moves for her */
	boolean redOver = false;
	SharedPreferences prefs;
	boolean thinking = false;
	boolean singleline = false;
	BusyIndicator indicator;
	PieceUI lasts[] = new PieceUI[8];

	public GameView(Context context) {
		super(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		ui = (UI) context;
		setWillNotDraw(false);
		setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, Gravity.TOP));
		paint.setStrokeWidth(1.3f);
		paint.setColor(Color.WHITE);
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		size = display.getWidth() / 20;
		Log.d(tag, "size " + size);
		if (display.getHeight() / 32 < display.getWidth() / 20)
			singleline = true;
		for (Board board : game.boards) {
			int i = 2;
			for (Piece piece : board.pieces) {
				addView(new PieceUI(context, piece, i, 20 + 2));
				i += 4;
			}
			reorderPieces(board.color);
		}
		buttons = new ButtonsView(context);
		addView(buttons);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tabs, this);

		showPieces(0);
		for (int color = 0; color < 8; color++) {
			dots[color] = context.getResources().getDrawable(Common.BALL_ICONS[color]);
			dots[color].setAlpha(191);
			tabs[color] = (TextView) findViewById(Common.LABELS[color]);
			/* 
			 * Let put the listener on the parent view group.
			 */
			ViewGroup tab = (ViewGroup) tabs[color].getParent();
			if (tab != null)
				tab.setOnClickListener(new ShowPiecesListener(color));
		}

		/* 
		 * Progress indicator.
		 */
		View iView = new View(context);
		iView.setLayoutParams(new FrameLayout.LayoutParams(150, 150,
				Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL));
		addView(iView);
		indicator = new BusyIndicator(context, iView);
	}

	private class ShowPiecesListener implements OnClickListener {
		private int color;

		protected ShowPiecesListener(int color) {
			this.color = color;
		}

		public void onClick(View v) {
			GameView.this.showPieces(color);
			GameView.this.invalidate();
		}
	}

	public float downX;
	public float downY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (selected != null)
			return false;
		
		doTouch(event);
		
		return true;
	}

	public void doTouch(MotionEvent event) {
		int action = event.getAction();
		
		if (action == MotionEvent.ACTION_DOWN) {
			downX = event.getRawX();
			downY = event.getRawY();
		}
		
		if (action == MotionEvent.ACTION_MOVE) {
			swipePieces(
					selectedColor,
					-(swipe + Float.valueOf(event.getRawX() - downX).intValue()));
		}
		
		if (action == MotionEvent.ACTION_UP) {
			swipe += Float.valueOf(event.getRawX() - downX).intValue();
			downX = 0;
			swipePieces(selectedColor, -swipe);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for (int i = 0; i < 20; i++) {
			canvas.drawLine(i * size, 0, i * size, 20 * size, paint);
		}
		
		canvas.drawLine(20 * size - 1, 0, 20 * size - 1, 20 * size, paint);
		
		for (int j = 0; j <= 20; j++) {
			canvas.drawLine(0, j * size + 1, 20 * size, j * size + 1, paint);
		}
		
		if (prefs.getBoolean("displaySeeds", true)) {
			for (Square s : game.boards.get(selectedColor).seeds()) {
				dots[selectedColor].setBounds(s.i * size + size / 4, s.j * size
						+ size / 4, s.i * size + 3 * size / 4, s.j * size + 3
						* size / 4);
				dots[selectedColor].draw(canvas);
			}
		}
	}

	public PieceUI findPiece(int color, String type) {
		PieceUI found = null;
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (v instanceof PieceUI) {
				found = (PieceUI) v;
				if (found.piece.color == color && found.piece.type == type)
					return found;
			}
		}
		return null;
	}

	public PieceUI findPiece(Piece piece) {
		return findPiece(piece.color, piece.type);
	}

	public void play(Move move, boolean animate) {
		if (move == null)
			return;
		
		PieceUI ui = findPiece(move.piece);
		
		boolean done = game.play(move);
		if (done) {
			lasts[ui.piece.color] = ui;
			ui.place(move.i, move.j, animate);
		}
		
		tabs[move.piece.color].setText(""
				+ game.boards.get(move.piece.color).score);
		
		mayReorderPieces();
		invalidate();
	}

	public void showPieces(int color) {
		selectedColor = color;
		for (PieceUI piece : piecesInStore())
			piece.setVisibility(piece.piece.color == color ? VISIBLE
					: INVISIBLE);
	}

	public void swipePieces(int color, int x) {
		for (PieceUI piece : piecesInStore(color))
			piece.swipe(x);
	}

	public void mayReorderPieces() {
		gone++;
		if (gone >= 8) {
			gone = 0;
			reorderPieces();
		}
	}

	public void reorderPieces() {
		for (int p = 0; p < 8; p++)
			reorderPieces(p);
	}

	public void reorderPieces(int color) {
		List<PieceUI> pieces = piecesInStore(color);
		Collections.sort(pieces);
		Collections.reverse(pieces);
		
		for (int p = 0; p < pieces.size(); p++) {
			PieceUI piece = pieces.get(p);
			
			if (singleline == true) {
				piece.j0 = 22;
				if (p < 1) {
					if (piece.piece.type.equals("I5"))
						piece.i0 = 1;
					else
						piece.i0 = 2;
				} else {
					piece.i0 = pieces.get(p - 1).i0
							+ pieces.get(p - 1).piece.size + 1;
				}
			} else {
				piece.j0 = 22 + ((p % 2) > 0 ? 5 : 0);
				if (p < 2) {
					if (piece.piece.type.equals("I5"))
						piece.i0 = 1;
					else
						piece.i0 = 2;
				} else {
					piece.i0 = pieces.get(p - 2).i0
							+ pieces.get(p - 2).piece.size + 1;
				}
			}
			piece.replace();
		}
	}

	private List<PieceUI> piecesInStore() {
		List<PieceUI> list = new ArrayList<PieceUI>();
		
		for (int k = 0; k < this.getChildCount(); k++) {
			if (this.getChildAt(k) instanceof PieceUI) {
				PieceUI piece = (PieceUI) this.getChildAt(k);
				if (piece != null && piece.movable) {
					list.add(piece);
				}
			}
		}
		
		return list;
	}

	private List<PieceUI> piecesInStore(int color) {
		List<PieceUI> list = new ArrayList<PieceUI>();
		
		for (PieceUI piece : piecesInStore()) {
			if (piece.piece.color == color)
				list.add(piece);
		}
		
		return list;
	}

	public boolean replay(List<Move> moves) {
		for (Move move : moves) {
			Piece piece = move.piece;
			PieceUI ui = findPiece(piece);
			ui.piece.reset(piece);
			play(move, false);
		}
		
		return true;
	}
}
