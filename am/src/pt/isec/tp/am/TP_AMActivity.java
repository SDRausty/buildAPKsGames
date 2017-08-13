package pt.isec.tp.am;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TP_AMActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onJogarPressionado(View v)
    {
    	startActivity(new Intent(TP_AMActivity.this, GameActivity.class));
    }
    

    public void onCliclAbout(View V)
    {
    	Intent intent = new Intent(this,About.class);
    	startActivity(intent);
    	
    }
    
    public void OnClickExit(View v)
    {
    	
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.quitDialogTitle))
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {

            finish();	               
            }

        }
        ).setNegativeButton(android.R.string.no, null).show();
    }

    public void onClickScore(View v)
    {
    	Intent intent = new Intent(this, Score.class);
    	startActivity(intent);
    }
    
    public void OnClickSettings(View v){
    	
    	Intent intent = new Intent(this, MyPreferencesActivity.class);
    	startActivity(intent);

    }
    
    public void onClickHelp(View v){
    	
    	Intent intent = new Intent(this, Help.class);
    	startActivity(intent);

    }


}