package com.seavenois.tetris;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class HighScores extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		
		//TextViews
		TextView hs1, hs2, hs3, hs1d, hs2d, hs3d;
		
		//Saved values
		SharedPreferences highScores;
		
    	//Assign layouts
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.highscores);
        
        //Assign textViews
        hs1 = (TextView) findViewById(R.id.textViewHighScore1);
        hs2 = (TextView) findViewById(R.id.textViewHighScore2);
        hs3 = (TextView) findViewById(R.id.textViewHighScore3);
        hs1d = (TextView) findViewById(R.id.textViewHighScore1Date);
        hs2d = (TextView) findViewById(R.id.textViewHighScore2Date);
        hs3d = (TextView) findViewById(R.id.textViewHighScore3Date);
        
        //Load high scores
        highScores = getSharedPreferences("highScores", 0);
		if (highScores.getInt("hScore1", 0) > 0){
			hs1.setText(getResources().getString(R.string.first) + ": " + Integer.toString(highScores.getInt("hScore1", 0)));
			hs1d.setText(highScores.getString("hScore1Date", " "));
		}
		if (highScores.getInt("hScore2", 0) > 0){
			hs2.setText(getResources().getString(R.string.second) + ": " + Integer.toString(highScores.getInt("hScore2", 0)));
			hs2d.setText(highScores.getString("hScore2Date", " "));
		}
		if (highScores.getInt("hScore3", 0) > 0){
			hs3.setText(getResources().getString(R.string.third) + ": " + Integer.toString(highScores.getInt("hScore3", 0)));
			hs3d.setText(highScores.getString("hScore3Date", " "));
		}		
	}
}
