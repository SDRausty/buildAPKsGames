package org.scoutant.cc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.scoutant.Command;
import org.scoutant.cc.model.Move;

import android.os.Bundle;
import android.util.Log;

public abstract class DemoActivity extends BaseActivity {
	private static final String tag = "activity";
	protected DemoGameView game;
	private int current=0;
	private List<Move> moves = new ArrayList<Move>();
	protected Command afterDemo = new DoNothing();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		load();
	}
	
	public void load() {
		try {
			InputStream fis = getResources().openRawResource(R.raw.game);
			BufferedReader reader = new BufferedReader( new InputStreamReader(fis));
			reader.readLine();
			reader.readLine();
			String line;
			while ((line = reader.readLine()) != null)   {
				Move move = Move.deserialize(line);
				moves.add(move);
			}
		} catch (Exception e) {
			Log.e(tag, "yep error is :", e);
		}
	}
	
	protected class PlayMove implements Command {
		@Override
		public void execute() {
			if (current>= moves.size()) afterDemo.execute();
			else game.play( moves.get(current++), true, this);
		}
	}
	
	public void stopPlaying() {
		afterDemo = new DoNothing();
		moves.clear();
	}
}
