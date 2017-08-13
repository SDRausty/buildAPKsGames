package org.scoutant.cc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Move;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Repository {
	private static String tag = "activity";
	private Context context;
	private GameView game;
	private SharedPreferences prefs;

	public Repository(Context context, GameView game) {
		this.context = context;
		this.game = game;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void save(){
		FileOutputStream fos;
		try {
			fos = context.openFileOutput("moves.txt", Context.MODE_PRIVATE);
			if (fos==null) return;
			String str = "";
			if (game.game.over()) {
				// if game is over we do not save it, so as to open a blank game next time
				prefs.edit().putBoolean( BaseActivity.KEY_GAME_ON, false).commit();
				prefs.edit().putInt( NbPlayersActivity.KEY_NB_PLAYERS, -1).commit();

			} else {
				str += game.game.moves.size()+"\n";
				str += game.turnMgr.player()+"\n";
				str += game.game.serialize();
			}
			fos.write( str.getBytes());
			if (Game.LOG) Log.i(tag, "saving game \n" + game.game.serialize() );
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(tag, "not found...", e);
		} catch (IOException e) {
			Log.e(tag, "io...", e);
		}
	}
	/** 
	 * sources a list of moves like this sample : <ul>
	 * <li>3
	 * <li>3
	 * <li>5,14:6,12:
	 * <li>2,12:4,12:8,12:
	 * <li>1,6:3,6: 
	 */
	public void restore() {
		List<Move> list = new ArrayList<Move>();
		try {
			FileInputStream fis = context.openFileInput("moves.txt");
			BufferedReader reader = new BufferedReader( new InputStreamReader(fis));
			String line;
			reader.readLine(); // first line give the # of moves...
			// second line gives the turn data
			String dataTurn = reader.readLine();
			while ((line = reader.readLine()) != null)   {
				Move move = Move.deserialize(line);
				list.add(move);
			}
			Log.i(tag, "# of moves to replay : " + list.size());
			game.replay( list);
			Log.i(tag, "turn data : " + dataTurn + ", game turn : " + game.turnMgr.player());
		} catch (Exception e) {
			Log.e(tag, "yep error is :", e);
		}
	}

}
