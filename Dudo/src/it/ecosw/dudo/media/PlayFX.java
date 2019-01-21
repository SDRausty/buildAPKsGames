package it.ecosw.dudo.media;

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

import it.ecosw.dudo.R;
import it.ecosw.dudo.settings.SettingsHelper;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class PlayFX {
	
	private MediaPlayer roll_sound = null;
	
	private MediaPlayer losedice_sound = null;
	
	private MediaPlayer error_sound = null;
	
	private MediaPlayer clickon_sound = null;
	
	private MediaPlayer clickoff_sound = null;
	
	private Vibrator vib = null;
	
	private SettingsHelper settings;
	
	/**
	 * Constructor
	 */
	public PlayFX(Context context, SettingsHelper settings){
		this.settings=settings;
		
        // Load game sound
        roll_sound = MediaPlayer.create(context, R.raw.dice_sound) ;
        losedice_sound = MediaPlayer.create(context, R.raw.lose_dice);
        error_sound = MediaPlayer.create(context, R.raw.error);
        clickon_sound = MediaPlayer.create(context, R.raw.clickon);
        clickoff_sound = MediaPlayer.create(context, R.raw.clickoff);
        
        // Vibration service
        vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
    /**
     * Rolling dice roll_sound
     */
    public void playSoundRoll(){
    	if (settings.isSoundActivated()) {
    		if (roll_sound.isPlaying()) roll_sound.seekTo(0);
    		roll_sound.start();
    	}
    }
    
    /**
     * Rolling lose dice sound fx
     */
    public void playSoundLoseDice(){
    	if (settings.isSoundActivated()) {
    		if (losedice_sound.isPlaying()) losedice_sound.seekTo(0);
    		losedice_sound.start();
    	}
    }
    
    /**
     * Wrong action sound
     */
    public void playErrorSound(){
    	if (settings.isSoundActivated()) {
    		if (error_sound.isPlaying()) error_sound.seekTo(0);
    		error_sound.start();
    	}
    }
    
    /**
     * Clickon sound (used for show action)
     */
    public void playClickonSound(){
    	if (settings.isSoundActivated()) {
    		if (clickon_sound.isPlaying()) clickon_sound.seekTo(0);
    		clickon_sound.start();
    	}
    }
    
    /**
     * Clickoff sound (used for hide action)
     */
    public void playClickoffSound(){
    	if (settings.isSoundActivated()) {
    		if (clickoff_sound.isPlaying()) clickoff_sound.seekTo(0);
    		clickoff_sound.start();
    	}
    }
    
    /**
     * If are enable a vibration of 500ms will be activated
     */
    public void vibration(){
    	if (settings.isVibrationActivated())
    		vib.vibrate(500);
    }
}
