package it.ecosw.dudo.settings;

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
import it.ecosw.dudo.games.PlayerInfo;
import it.ecosw.dudo.media.BackgroundStatus;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Helper for interfacing with settings options
 * @author Enrico Strocchi
 *
 */
public class SettingsHelper {
	
	private Activity mContext;
	
	public static final String SIXDICE_SETTING = "sixdice_setting";
	
	public static final String PLAYERNAME_SETTING = "playername_setting";
	
	public static final String PLAYERSAVE_SETTING = "playersave_setting";

	public static final String SOUND_SETTING = "sound_setting";
	
	public static final String ANIMATION_SETTING = "animation_setting";
	
	public static final String SORTING_SETTING = "sorting_setting";
	
	public static final String VIBRATION_SETTING = "vibration_setting";
	
	public static final String STYLE_SETTING = "style_setting";
	
	public static final String ASKDELETE_SETTING = "askdelete_setting";
	
	public static final String BACKGROUNDTYPE_SETTING = "backgroundtype_setting";
	public static final String BACKGROUND_SOLIDCOLOR_SETTING = "background_solidcolor_setting";
	public static final String BACKGROUD_TEXTCOLOR_SETTING = "background_textcolor_setting";
	public static final String BACKGROUND_IMAGE_SETTING = "background_image_setting";
	
	public static final String CHRONO_SETTING = "chrono_setting";
	
	/**
	 * Constructor
	 * @param context
	 */
	public SettingsHelper(Activity context) {
		mContext = context;
	}
	
	/**
	 * Return true if the sixth die is activated
	 * @return true if sixth die is activated
	 */
	public boolean isSixthDieActivated(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(SIXDICE_SETTING, false);
	}
	
	/**
	 * Return true if sound are activated
	 * @return true if sound activated
	 */
	public boolean isSoundActivated(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(SOUND_SETTING, true);
	}
	
	/**
	 * return true if animation are activated
	 * @return true if animation activated
	 */
	public boolean isAnimationActivated(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(ANIMATION_SETTING, true);
	}
	
	/**
	 * Return true if sorting is activated
	 * @return true if sorting activated
	 */
	public boolean isSortingActivated(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(SORTING_SETTING, false);
	}
	
	/**
	 * Return true if vibration is activated
	 * @return true if vibration is activated
	 */
	public boolean isVibrationActivated(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(VIBRATION_SETTING, true);
	}
	
	/**
	 * Return true if the software shall ask before to delete a die
	 * @return true ask to delete
	 */
	public boolean askDeletingDie(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(ASKDELETE_SETTING, false);
	}
	
	/**
	 * Return the style for die
	 * @return description of style for dice
	 */
	public String getStyle(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getString(STYLE_SETTING, "CLASSIC");
	}
	
	/**
	 * Return the background status
	 * @return background status
	 */
	public BackgroundStatus getBackgroundStatus(){
		boolean isimage = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(BACKGROUNDTYPE_SETTING, true);
		int textcolor = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(BACKGROUD_TEXTCOLOR_SETTING,Color.BLACK);
		if(!isimage) {
			int backcolor = PreferenceManager.getDefaultSharedPreferences(mContext).getInt(BACKGROUND_SOLIDCOLOR_SETTING,0);
			return new BackgroundStatus(backcolor,textcolor);
		} else {
			String image = PreferenceManager.getDefaultSharedPreferences(mContext).getString(BACKGROUND_IMAGE_SETTING, "GREENCARPET");
			return new BackgroundStatus(image,textcolor);
		}
	}
	
	
	/**
	 * Return the information about one player
	 * @return Player info
	 */
	public PlayerInfo getPlayerStatus(){
		String name, saved;
		if(isSixthDieActivated()) {
			name = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PLAYERNAME_SETTING,mContext.getText(R.string.text_player).toString());
			saved = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PLAYERSAVE_SETTING,"000000");
			if(saved.length() == 5) saved = "000000";
			return new PlayerInfo(name, saved);
		}
		name = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PLAYERNAME_SETTING,mContext.getText(R.string.text_player).toString());
		saved = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PLAYERSAVE_SETTING,"00000");
		if(saved.length() == 6) saved = "00000";
		return new PlayerInfo(name, saved);
	}
	
	/**
	 * Save information about one player
	 * @param info Player Info
	 * @return 0 if write successfully
	 */
	public int setPlayerStatus(PlayerInfo info){
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		spe.putString(PLAYERNAME_SETTING+0, info.getName());
		spe.putString(PLAYERSAVE_SETTING+0, info.getSave());
		spe.commit();
		return 0;
	}	

	
	/**
	 * Return last chrono save
	 * @return last chrono save
	 */
	public long getChronoTime(){
		return PreferenceManager.getDefaultSharedPreferences(mContext).getLong(CHRONO_SETTING,0);
	}
	
	/**
	 * Save chrono time
	 * @param version software version
	 * @return 0 if writing was correct
	 */
	public int setChronoTime(Long time){
		SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		spe.putLong(CHRONO_SETTING, time);
		spe.commit();
		return 0;
	}

}
