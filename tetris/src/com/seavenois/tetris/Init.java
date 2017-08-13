package com.seavenois.tetris;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class Init extends Activity {
	
	private Button btnNewGame, btnResumeGame, btnHighScores;
	
    /** Called when the activity is first created. */    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	//Assign layouts
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.init);
        
        //Find and assign buttons
        btnNewGame = (Button) findViewById(R.id.buttonNewGame);
        btnNewGame.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Game"));
        		startActivity(intent);
			}
        });
        btnResumeGame = (Button) findViewById(R.id.buttonResumeGame);
        btnResumeGame.setEnabled(false); //Disabled (unimplemented feature)
        btnResumeGame.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Game"));
        		//TODO: Something to load a game. First I need to develop something to load the game
        		startActivity(intent);
			}
        });
        btnHighScores = (Button) findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.HighScores"));
        		startActivity(intent);
			}
        });
        
    }
}