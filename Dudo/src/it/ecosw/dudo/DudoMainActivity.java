package it.ecosw.dudo;

/**
 * This file is part of Dudo for Android software.
 *
 *  Dudo is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Dudo is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Dudo.  If not, see <http://www.gnu.org/licenses/>.
 */

import it.ecosw.dudo.games.PlayerSet;
import it.ecosw.dudo.gui.GraphicsElement;
import it.ecosw.dudo.gui.InterfaceAdapter;
import it.ecosw.dudo.gui.HtmlViewerWindow;
import it.ecosw.dudo.media.Background;
import it.ecosw.dudo.media.GenDiceImage;
import it.ecosw.dudo.media.PlayFX;
import it.ecosw.dudo.settings.SettingsActivity;
import it.ecosw.dudo.settings.SettingsHelper;
import it.ecosw.dudo.R;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Toast;

/**
 * Main Activity of Dudo Software
 * @author Enrico Strocchi
 *
 */
public class DudoMainActivity extends Activity {
	
	protected static final int SUB_ACTIVITY_NEW_MATCH = 100;
	
	private InterfaceAdapter d = null;
	
	private SettingsHelper settings;
	
	private View parentLayout;
	
	private Background background;
	
	private Chronometer chrono;
	
	private GenDiceImage gdi;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        // Avoid Standby Mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        // Load setting object
        settings = new SettingsHelper(this);
        
        // Load game layout
        setContentView(R.layout.layout_dudo_game);
        
        // Sound Management
        PlayFX fx = new PlayFX(this,settings);
        
        // Create new Dice Set
        gdi = new GenDiceImage(this, settings.getStyle());
        
        // Acquire all graphics element of gui
        GraphicsElement ge = new GraphicsElement(this, gdi, settings);
        
        // Set initial background
        parentLayout = (View) findViewById(R.id.parentLayout);
        background = new Background(this, parentLayout,ge);
        background.setBackground(settings.getBackgroundStatus());  
        
        // Set Chrono
        chrono = ge.getChrono();
        
		d = new InterfaceAdapter(this,ge,fx,settings);
        d.setAnimEnabled(settings.isAnimationActivated());
		d.setPlayerStatus(new PlayerSet(settings.getPlayerStatus(),settings.isSixthDieActivated()));
		
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.dudo_menu, menu);
        return true;
    }
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		chrono.stop();
		settings.setChronoTime(calculateElapsedTime(chrono));
		settings.setPlayerStatus(d.getPlayerInfo());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		chrono.setBase(SystemClock.elapsedRealtime()-settings.getChronoTime());
		chrono.start();
		background.setBackground(settings.getBackgroundStatus());
		d.setAnimEnabled(settings.isAnimationActivated());
		d.setSorting(settings.isSortingActivated());
		d.setSixDice(settings.isSixthDieActivated());
		d.setAskDelete(settings.askDeletingDie());
		d.setPlayerName(settings.getPlayerStatus().getName());
		boolean stylechg = !gdi.getCurrentStyle().equals(settings.getStyle());
		if(stylechg) {
			gdi.setStyle(settings.getStyle());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
    	switch (item.getItemId()){
    	
    	case R.id.menu_new:
    		d.newMatch();
    		return true;
    	
    	case R.id.menu_settings:
    		Intent intent = new Intent(getBaseContext(),SettingsActivity.class);
    		startActivity(intent);
    		return true;
    		
    	case R.id.menu_help:
    		// Convert HTML file in raw resource to string
    	    String help = new java.util.Scanner(getResources().openRawResource(R.raw.help)).useDelimiter("\\A").next();
    		HtmlViewerWindow.showWindow(this, help,getString(R.string.alert_help_label),R.drawable.ic_help);
    		return true;
    		
    	case R.id.menu_changelog:
    		// Convert HTML file in raw resource to string
    	    String changelog = new java.util.Scanner(getResources().openRawResource(R.raw.changelog)).useDelimiter("\\A").next();
    		HtmlViewerWindow.showWindow(this,changelog,getString(R.string.alert_changelog_label),R.drawable.ic_launcher);
    		return true;
    		
    	case R.id.menu_about:
    		String about = new java.util.Scanner(getResources().openRawResource(R.raw.about)).useDelimiter("\\A").next();
            String version = "not defined";
            try {
            	version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    		} catch (NameNotFoundException e) {
    			// TODO Auto-generated catch block
    			Toast.makeText(DudoMainActivity.this,getText(R.string.package_not_found),Toast.LENGTH_SHORT).show();
    		}
    		HtmlViewerWindow.showWindow(this, about, getString(R.string.alert_about_label)+" - "+getString(R.string.version)+" "+version, R.drawable.ic_launcher);
    		return true;
    		
    	default:
    		return super.onOptionsItemSelected(item);
    	}
	}
	
	private long calculateElapsedTime(Chronometer mChronometer) {
	    long stoppedMilliseconds = 0;
	    String chronoText = mChronometer.getText().toString();
	    String array[] = chronoText.split(":");
	    if (array.length == 2) {
	        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
	                + Integer.parseInt(array[1]) * 1000;
	    } else if (array.length == 3) {
	        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
	                + Integer.parseInt(array[1]) * 60 * 1000
	                + Integer.parseInt(array[2]) * 1000;
	    }
	    return stoppedMilliseconds;
	}
	
}
