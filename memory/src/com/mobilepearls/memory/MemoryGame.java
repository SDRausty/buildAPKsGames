package com.mobilepearls.memory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGame implements Serializable {

	public static interface GameListener {
		public void gameOver(MemoryGame game);

		public void gamePaused(MemoryGame game);

		public void gameResumed(MemoryGame game);

		public void gameStarted(MemoryGame game);
	}

	private static final int CLEAR_NOTHING = -3;

	public static final int INT_CLEAR = -2;
	public static final int INT_UNKNOWN = -1;

	private static final long serialVersionUID = 0L;

	private int clickX = -1;
	private int clickY = -1;

	public final int[][] displayedBoard;
	private final int[][] gameBoard;

	public final int height;
	private transient GameListener listener;
	private int objectToClearAfterTimeout = -1;
	private boolean paused = true;
	private boolean started = false;

	private long startTime = -1; // set on start
	private long storedTime = 0;
	private boolean waitingForTimeout = false;

	public final int width;

	public MemoryGame(int width, int height) {
		this.gameBoard = new int[width][height];
		this.displayedBoard = new int[width][height];
		this.width = width;
		this.height = height;

		shuffle();
	}

	public void afterTimeout() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (displayedBoard[i][j] == objectToClearAfterTimeout)
					displayedBoard[i][j] = INT_CLEAR;
				else if (displayedBoard[i][j] != INT_UNKNOWN && displayedBoard[i][j] != INT_CLEAR) {
					displayedBoard[i][j] = INT_UNKNOWN;
				}
			}
		}
		objectToClearAfterTimeout = CLEAR_NOTHING;
		waitingForTimeout = false;
	}

	/** Return wheter something changed (click was "accepted"). */
	public boolean click(int x, int y) {
		if (paused || !started)
			return false;
		int clickedObject = displayedBoard[x][y];
		if (clickedObject == INT_CLEAR)
			return false;
		if (clickedObject != INT_UNKNOWN)
			return false;

		displayedBoard[x][y] = gameBoard[x][y];
		if (clickX == -1) {
			// nothing clicked last
			clickX = x;
			clickY = y;
			return true;
		} else {
			if (x == clickX && y == clickY)
				return false;
			if (gameBoard[x][y] == gameBoard[clickX][clickY]) {
				objectToClearAfterTimeout = gameBoard[x][y];
			} else {
				objectToClearAfterTimeout = CLEAR_NOTHING;
			}

			clickX = clickY = -1;
			waitingForTimeout = true;
			return true;
		}
	}

	public long getUsedTimeMs() {
		if (paused)
			return storedTime;
		return System.currentTimeMillis() - startTime + storedTime;
	}

	public boolean isDone() {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (displayedBoard[i][j] != INT_CLEAR)
					return false;
		return true;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isWaitingForTimeout() {
		return waitingForTimeout;
	}

	/** Pause the game and store the used time. When returning, resume() will be called. */
	public void pause() {
		if (!paused) {
			storedTime += System.currentTimeMillis() - startTime;
			paused = true;
			listener.gamePaused(this);
		}
	}

	public void restart() {
		listener.gameOver(this);
		clickX = clickY = -1;
		started = false;
		paused = true;
		storedTime = 0;
		shuffle();
	}

	public void resume() {
		paused = false;
		startTime = System.currentTimeMillis();
		listener.gameResumed(this);
	}

	public void setListener(GameListener listener) {
		this.listener = listener;
	}

	private void shuffle() {
		int neededPieces = width * height / 2;
		if (neededPieces * 2 != width * height)
			throw new IllegalArgumentException("Illegal size: " + width + "x" + height);

		List<Object> l = new ArrayList<Object>(neededPieces);
		for (int i = 0; i < neededPieces; i++) {
			l.add(i);
			l.add(i);
		}
		Collections.shuffle(l);

		int n = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				gameBoard[i][j] = (Integer) l.get(n++);
				displayedBoard[i][j] = INT_UNKNOWN;
			}
		}
	}

	public void start() {
		started = true;
		paused = false;
		startTime = System.currentTimeMillis();
		listener.gameStarted(this);
	}

	public boolean wasLastClickIncorrect() {
		return objectToClearAfterTimeout == CLEAR_NOTHING;
	}
}
