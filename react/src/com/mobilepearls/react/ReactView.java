package com.mobilepearls.react;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class ReactView extends View {

	private static final int STATE_START = 1;
	private static final int STATE_WAITING = 2;
	private static final int STATE_RED = 3;
	private static final int STATE_AFTER_CHEAT = 4;

	public static final int NUMBER_OF_CLICKS = 10;

	private int state = STATE_START;
	private int clicks;
	private int totalTime;
	private long startTime = -1;
	private long lastTime = -1;
	private final Paint textPaint = new Paint();
	private Timer timer = new Timer(true);
	private final Random random = new Random();
	private long lastClick;

	public ReactView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stateChange();
			}
		});

		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int smallest = Math.min(metrics.widthPixels, metrics.heightPixels);
		int textSize;
		if (smallest <= 300) {
			textSize = 18;
		} else if (smallest <= 400) {
			textSize = 24;
		} else {
			textSize = 30;
		}

		textPaint.setTextSize(textSize);
		begin();
	}

	private void startTurnRedTimer() {
		long delay = 1200 + random.nextInt(5000);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				post(new Runnable() {
					@Override
					public void run() {
						state = STATE_RED;
						ReactSoundManager.playCorrect();
						invalidate();
						startTime = System.currentTimeMillis();
					}
				});
			}
		}, delay);
	}

	private void begin() {
		clicks = totalTime = 0;
		startTime = -1;
	}

	private void stateChange() {
		switch (state) {
		case STATE_START:
			lastClick = System.currentTimeMillis();
			begin();
			startTurnRedTimer();
			state = STATE_WAITING;
			break;
		case STATE_WAITING:
			long timeSinceLast = System.currentTimeMillis() - lastClick;
			if (timeSinceLast < 800) {
				// a quick click - ignore
				return;
			}
			timer.cancel();
			timer = new Timer(true);
			ReactSoundManager.playIncorrect();
			state = STATE_AFTER_CHEAT;
			break;
		case STATE_RED:
			lastClick = System.currentTimeMillis();
			lastTime = lastClick - startTime;
			totalTime += lastTime;
			clicks++;
			startTime = -1;
			if (clicks == NUMBER_OF_CLICKS) {
				ReactSoundManager.playGameDone();
				gameOver();
			} else {
				startTurnRedTimer();
				state = STATE_WAITING;
			}
			break;
		case STATE_AFTER_CHEAT:
			restart();
			return;
		}
		invalidate();
	}

	void restart() {
		state = STATE_START;
		clicks = 0;
		totalTime = 0;
		timer.cancel();
		timer = new Timer();
		invalidate();
	}

	private void gameOver() {
		final int totalTimeCopy = totalTime;
		restart();

		final ReactHighScoreDatabase db = ReactHighScoreDatabase.getDatabase(getContext());
		int position = db.getPositionForScore(totalTimeCopy);

		final String LAST_NAME_KEY = "last_name";
		SharedPreferences prefs = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE);
		String initialName = prefs.getString(LAST_NAME_KEY, "");

		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		alert.setCancelable(false);
		if (position >= ReactHighScoreDatabase.MAX_ENTRIES) {
			alert.setMessage("Time: " + totalTimeCopy + " ms.\n\nYou did not attain high score!");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getContext().startActivity(new Intent(getContext(), ReactGameActivity.class));
				}
			});
		} else {
			alert.setMessage("Time: " + totalTimeCopy + " ms.\nHigh score position: " + position
					+ "\n\nPlease enter name:");
			final EditText textInput = new EditText(getContext());
			textInput.setText(initialName);
			textInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
			alert.setView(textInput);
			alert.setCancelable(true);
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = textInput.getText().toString();

					Editor editor = ((Activity) getContext()).getPreferences(Context.MODE_PRIVATE).edit();
					editor.putString(LAST_NAME_KEY, value);
					editor.commit();

					db.addEntry(value, totalTimeCopy);
					Intent intent = new Intent();
					intent.setClass(getContext(), ReactHighScoresActivity.class);
					intent.putExtra(ReactHighScoresActivity.JUST_STORED, true);
					getContext().startActivity(intent);
				}
			});
		}

		alert.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return false;
		stateChange();
		return true;
	}

	private void drawCenteredText(Canvas canvas, String... text) {
		int linePadding = 2;
		int totalHeight = text.length * (int) textPaint.getTextSize() + linePadding;
		int startY = getHeight() / 2 - totalHeight / 2;
		int x = getWidth() / 2;
		for (int i = 0; i < text.length; i++) {
			String s = text[i];
			int y = startY + i * ((int) textPaint.getTextSize());
			canvas.drawText(s, x, y, textPaint);
		}

	}

	@Override
	public void draw(Canvas canvas) {
		switch (state) {
		case STATE_START:
			canvas.drawColor(Color.BLACK);
			drawCenteredText(canvas, "Touch the screen", "or press trackball", "as quick as possible",
					"when the screen turns red.", "", "Touch screen to start!");
			break;
		case STATE_WAITING:
			canvas.drawColor(Color.BLACK);
			String lastString = (clicks == 0) ? "" : "Last: " + lastTime + " ms";
			drawCenteredText(canvas, "Reactions: " + clicks + "/" + NUMBER_OF_CLICKS, "", "Average: "
					+ (clicks == 0 ? 0 : totalTime / clicks) + " ms", "", lastString);
			break;
		case STATE_RED:
			canvas.drawColor(Color.RED);
			drawCenteredText(canvas, "React!");
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			}
			break;
		case STATE_AFTER_CHEAT:
			canvas.drawColor(Color.BLACK);
			drawCenteredText(canvas, "Head start!", "", "Touch to continue.");
			break;

		}
	}

}
