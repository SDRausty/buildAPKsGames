package com.mobilepearls.memory;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Graphics fetched with something like:
 * 
 * <pre>
 * # Resize:
 * i=1; for f in `ls`; do if [ $f = "favorites" ]; then cp $f ../converted64/unknown_64.png; else cp $f ../converted64/tile_${i}_64.png; (( i = i+1 )); fi; done;
 * # Move exported images:
 * ( cd ../converted64/ && rm -Rf /home/fornwall/Documents/workspace-android/net.fornwall.memory/res/drawable-port/* && cp * $HOME/src/workspace-android/com.mobilepearls.memory/res/drawable-port/ )
 * </pre>
 */
public class MemoryView extends View {

	static class GameMetrics {
		int boardHeight;

		int boardWidth;
		int offsetX;
		int offsetY;

		int paddingBetweenTiles;
		int tileSize;
	}

	private Bitmap clearBitmap;
	final MemoryGame game;
	private GameMetrics metrics;
	private Bitmap[] tiles;
	final Timer timer = new Timer(true);
	private Bitmap unknownBitmap;

	public MemoryView(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.game = ((MemoryActivity) context).game;
		// setOnTouchListener(this);
	}

	private GameMetrics computeMetrics() {
		GameMetrics m = new GameMetrics();

		int numberOfHorizontalPaddings = game.width + 1;
		int numberOfVerticalPaddings = game.height + 1;
		final int WANTED_PADDING_PX = 2;
		int totalVerticalPadding = numberOfVerticalPaddings * WANTED_PADDING_PX;
		int totalHorizontalPadding = numberOfHorizontalPaddings * WANTED_PADDING_PX;

		m.tileSize = Math.min((getHeight() - totalVerticalPadding) / game.height, (getWidth() - totalHorizontalPadding)
				/ game.width);
		int neededWidth = m.tileSize * game.width;
		int neededHeight = m.tileSize * game.height;

		int verticalSpaceOver = getHeight() - neededHeight;
		int horizontalSpaceOver = getWidth() - neededWidth;
		m.paddingBetweenTiles = Math.min(verticalSpaceOver / numberOfVerticalPaddings, horizontalSpaceOver
				/ numberOfHorizontalPaddings);

		m.boardWidth = game.width * m.tileSize + (game.width - 1) * m.paddingBetweenTiles;
		m.boardHeight = game.height * m.tileSize + (game.height - 1) * m.paddingBetweenTiles;

		m.offsetX = (getWidth() - m.boardWidth) / 2;
		m.offsetY = (getHeight() - m.boardHeight) / 2;

		return m;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		canvas.setDensity(Bitmap.DENSITY_NONE);

		for (int x = 0; x < game.width; x++) {
			for (int y = 0; y < game.height; y++) {
				int left = metrics.offsetX + (metrics.tileSize + metrics.paddingBetweenTiles) * x;
				int top = metrics.offsetY + (metrics.tileSize + metrics.paddingBetweenTiles) * y;
				Integer bitmapIndex = game.displayedBoard[x][y];
				Bitmap bitmap = getBitmapFromIndex(bitmapIndex);
				canvas.drawBitmap(bitmap, left, top, null);
			}
		}
	}

	void gameOver() {
		SoundManager.playGameDone();
		final int usedSeconds = (int) (game.getUsedTimeMs() / 1000);

		final String LAST_NAME_KEY = "last_name";
		SharedPreferences prefs = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
		String initialName = prefs.getString(LAST_NAME_KEY, "");

		final HighScoreDatabase db = HighScoreDatabase.getDatabase(getContext());
		int position = db.getPositionForScore(usedSeconds);

		game.restart();
		invalidate();

		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		alert.setCancelable(false);
		if (position >= HighScoreDatabase.MAX_ENTRIES) {
			alert.setMessage("Time: " + usedSeconds + " seconds.\n\nYou did not attain high score!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					((Activity) getContext()).finish();
				}
			});
		} else {
			alert.setMessage("Congratulations!\n\nHigh score position: " + position + "\nTime: " + usedSeconds
					+ " seconds\n\nEnter your name:");
			final EditText textInput = new EditText(getContext());
			textInput.setText(initialName);
			textInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
			alert.setView(textInput);
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = textInput.getText().toString();

					Editor editor = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE).edit();
					editor.putString(LAST_NAME_KEY, value);
					editor.commit();

					db.addEntry(value, usedSeconds);
					Intent intent = new Intent();
					intent.setClass(getContext(), ListHighScoresActivity.class);
					intent.putExtra(ListHighScoresActivity.JUST_STORED, true);
					getContext().startActivity(intent);
				}
			});
		}
		alert.show();
	}

	private Bitmap getBitmapFromIndex(int index) {
		switch (index) {
		case MemoryGame.INT_CLEAR:
			return clearBitmap;
		case MemoryGame.INT_UNKNOWN:
			return unknownBitmap;
		default:
			return tiles[index];
		}
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);

		metrics = computeMetrics();

		Resources resources = getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

		final int IMAGE_SIZE = 64;
		float scaleFactor = metrics.tileSize / (float) IMAGE_SIZE;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleFactor, scaleFactor);

		unknownBitmap = BitmapFactory.decodeResource(resources, R.drawable.unknown_64, options);
		unknownBitmap = Bitmap.createBitmap(unknownBitmap, 0, 0, IMAGE_SIZE, IMAGE_SIZE, matrix, true);

		clearBitmap = Bitmap.createBitmap(metrics.tileSize, metrics.tileSize, Config.ARGB_8888);
		clearBitmap.eraseColor(Color.BLACK);

		int[] tilesIndices = new int[] { R.drawable.tile_1_64, R.drawable.tile_2_64, R.drawable.tile_3_64,
				R.drawable.tile_4_64, R.drawable.tile_5_64, R.drawable.tile_6_64, R.drawable.tile_7_64,
				R.drawable.tile_8_64, R.drawable.tile_9_64, R.drawable.tile_10_64, R.drawable.tile_11_64,
				R.drawable.tile_12_64, R.drawable.tile_13_64, R.drawable.tile_14_64, R.drawable.tile_15_64,
				R.drawable.tile_16_64, R.drawable.tile_17_64, R.drawable.tile_18_64, };

		tiles = new Bitmap[tilesIndices.length];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = BitmapFactory.decodeResource(resources, tilesIndices[i], options);
			tiles[i] = Bitmap.createBitmap(tiles[i], 0, 0, IMAGE_SIZE, IMAGE_SIZE, matrix, true);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actionMasked = event.getAction() & MotionEvent.ACTION_MASK;
		if (actionMasked != MotionEvent.ACTION_DOWN && actionMasked != MotionEvent.ACTION_POINTER_DOWN)
			return false;
		if (game.isWaitingForTimeout())
			return false;

		int x = ((int) event.getX(event.getActionIndex())) - metrics.offsetX + metrics.paddingBetweenTiles;
		int y = ((int) event.getY(event.getActionIndex())) - metrics.offsetY + metrics.paddingBetweenTiles;
		if (x < 0 || y < 0)
			return false;
		int xTile = x / (metrics.tileSize + metrics.paddingBetweenTiles);
		int yTile = y / (metrics.tileSize + metrics.paddingBetweenTiles);
		if (xTile >= game.width || yTile >= game.height)
			return false;

		if (!game.isStarted()) {
			game.start();
		}

		if (game.click(xTile, yTile)) {
			performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING/* =1=VIRTUAL_KEY */);
			if (game.isWaitingForTimeout()) {
				startTimeoutCountdown();
				if (game.wasLastClickIncorrect()) {
					SoundManager.playIncorrect();
				} else {
					SoundManager.playCorrect();
				}
			} else {
				SoundManager.playClick();
			}
		}
		invalidate();
		return true;
	}

	public void startTimeoutCountdown() {
		final long delay = 400;

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				post(new Runnable() {
					@Override
					public void run() {
						game.afterTimeout();
						invalidate();
						if (game.isDone())
							gameOver();
					}
				});
			}
		}, delay);
	}

}
