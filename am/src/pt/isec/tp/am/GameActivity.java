package pt.isec.tp.am;

import java.io.File;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Toast;
import android.graphics.Bitmap;


public class GameActivity extends Activity {
	
	Timer m_timer;
	Surface gameView;
	Model model;
	Thread tmodel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gameView = new Surface(this.getBaseContext());
        this.setContentView(gameView);
        
        model = new Model(this, gameView);
        tmodel = new Thread(model);
        tmodel.start();
        
   }
    
    public void onResume() {
    	super.onResume();
    	//tmodel = new Thread(model);
    }
    
    
    public void onPause() {
    	super.onPause(); 
    	model.terminate();
    	try {
    		tmodel.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if ( this.isFinishing() )
    	{
        	//Intent intent = new Intent(this, Score.class);
        	//startActivity(intent);
    	}
    	
    }
    
    public void onDestroy() {
    	super.onDestroy();
    }
}