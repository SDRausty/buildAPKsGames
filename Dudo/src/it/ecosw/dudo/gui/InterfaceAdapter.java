package it.ecosw.dudo.gui;

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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;
import it.ecosw.dudo.R;
import it.ecosw.dudo.games.PlayerInfo;
import it.ecosw.dudo.games.PlayerSet;
import it.ecosw.dudo.media.PlayFX;
import it.ecosw.dudo.settings.SettingsHelper;

/**
 * Adapter for set of die
 * @author Enrico Strocchi
 */
public class InterfaceAdapter implements OnClickListener,OnLongClickListener,AnimationListener {
	
	private static char dicesymbols[] = {'\u2680','\u2681','\u2682','\u2683','\u2684','\u2685'};
	
	private Context context;
	
	private GraphicsElement ge;
	
	private PlayerSet player;
	
	private boolean sorting;
	
	private PlayFX fx;
	
	private boolean diceHide;
	
	private boolean isAnimEnabled;
	
	private boolean askDelete;
	
	public InterfaceAdapter(Context context, GraphicsElement ge, PlayFX fx, SettingsHelper settings) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.ge = ge;
		this.sorting = settings.isSortingActivated();
		this.fx = fx;
		this.askDelete = settings.askDeletingDie();
		
		// Set listener for dice
		ge.getDieLayout().setOnClickListener(this);
		ge.getDieLayout().setId(1);
		
		//  Set Listener for delete
		ge.getDeleteLateral().setOnClickListener(this);
		ge.getDeleteLateral().setId(2);
		
		// Set Listener for roll button
		ge.getRollLateral().setOnClickListener(this);
		ge.getRollLateral().setId(3);
		
		// Set Long listener for playername
		ge.getPlayername().setOnLongClickListener(this);
		ge.getPlayername().setId(11);
		
		diceHide = false;
		isAnimEnabled = true;
		
	}

	/**
	 * Activate or deactivate animation during dice roll
	 * @param anim the anim to set
	 */
	public void setAnimEnabled(boolean anim) {
		this.isAnimEnabled = anim;
	}
	
	public boolean rollAllDie(){
		diceHide = false;
		player.rollSet(sorting);
		fx.playSoundRoll();
		fx.vibration();
		for(int i=0;i<(player.areSixDice()?6:5);i++) {
			if (player.isDieDeleted(i)) ge.getDgos()[i].deleteAnimation(false,null);
			else ge.getDgos()[i].rollAnimation(player.getDieValue(i),isAnimEnabled);
		}
		if(!player.areSixDice()) ge.getDgos()[5].deleteAnimation(false,null); 
		return true;
	}
	
	/**
	 * Delete a dice from current set
	 * @param sort true if dice shall be sorted
	 * @return true if a dice was deleted
	 */
	public boolean delDice() {
		// TODO Auto-generated method stub
		// delete a dice from sets
		int pos = player.delDie();
		if (pos == -1) {
			fx.playErrorSound();
			return false;
		}
		fx.playSoundLoseDice();
		fx.vibration();
		ge.getDgos()[pos].deleteAnimation(isAnimEnabled,this);
		if (player.isEmpty()) {
			Toast.makeText(
					context,
					player.getPlayerName()+" "+context.getResources().getText(R.string.you_lose),
					Toast.LENGTH_SHORT).show();
		}
		//Toast.makeText(context,context.getResources().getText(R.string.text_clickplayername),Toast.LENGTH_SHORT).show();
		
		return true;
	}

	/**
	 * Set Dice Hide
	 * @param true to hide dice
	 */
	public void switchDiceHide(){
		if(player.isEmpty()) return;
		if (!diceHide) {
			diceHide = true;
			fx.playClickoffSound();
			for(int i=0;i<6;i++) {
				if(i==5 && !player.areSixDice()) break;
				if(!player.isDieDeleted(i)) ge.getDgos()[i].hide(isAnimEnabled);
			}
			return;
		} if (diceHide) {
			diceHide = false;
			fx.playClickonSound();
			for(int i=0;i<6;i++) {
				if(i==5 && !player.areSixDice()) break;
				if(!player.isDieDeleted(i)) ge.getDgos()[i].show(player.getDieValue(i) ,isAnimEnabled);
			}
			return;
		}
	}
	
	/**
	 * New match
	 * @param sorting true if the die shall be sort
	 */
	public void newMatch(){
		player.restoreAllDice(sorting);
   		fx.playSoundRoll();
		fx.vibration();
		diceHide = false;
		for(int i=0;i<(player.areSixDice()?6:5);i++) {
			ge.getDgos()[i].rollAnimation(player.getDieValue(i), isAnimEnabled);
		}
		ge.getChrono().setBase(SystemClock.elapsedRealtime());
		ge.getChrono().start();
	}

	/**
	 * Playerinfo for current player
	 * @return PlayerInfo object
	 */
	public PlayerInfo getPlayerInfo(){
		return player.getPlayerInfo();
	}
	
	/**
	 * Set player info
	 * @param sets Array of Player Info
	 * @param numofplayers Number of player
	 */
	public void setPlayerStatus(PlayerSet player){
		this.player = player;
        ge.getPlayername().setText(player.getPlayerName());
        diceHide = false;
		for(int i=0;i<(player.areSixDice()?6:5);i++) {
			if (player.isDieDeleted(i)) ge.getDgos()[i].deleteAnimation(false,null);
			else ge.getDgos()[i].rollAnimation(player.getDieValue(i), isAnimEnabled);
		}
		if(!player.areSixDice()) ge.getDgos()[5].deleteAnimation(false,null);
	}

	
	/**
	 * Set sorting for dice
	 * @param sorting true to sort the die set
	 */
	public void setSorting(boolean sorting) {
		this.sorting = sorting;
	}
	
	/**
	 * Set if shall ask before delete dice or no
	 * @param askDelete true to ask before deleting
	 */
	public void setAskDelete(boolean askDelete){
		this.askDelete = askDelete;
	}
	
	/**
	 * Set six dice on the match
	 * @param sixdice true to set sixdice
	 */
	public void setSixDice(boolean sixdice){
		if(player.setSixDice(sixdice)) rollAllDie();
	}
	
	/**
	 * Change playername
	 * @param name new playername
	 */
	public void setPlayerName(String name){
		this.player.setPlayerName(name);
        ge.getPlayername().setText(player.getPlayerName());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == 1){
			switchDiceHide();
			return;
		}
		if (v.getId() == 2){
			if(diceHide) {
				fx.playErrorSound();
				return;
			}
			if(askDelete){
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								delDice();
								break;
							case DialogInterface.BUTTON_NEGATIVE:
								break;
					}
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
			String name = player.getPlayerName();
			Object[] MessageArgument = {name};
			String msg = String.format(context.getString(R.string.delete_are_you_sure),MessageArgument);
			builder.setMessage(msg)
	                  .setPositiveButton(context.getString(R.string.text_yes), dialogClickListener)
	                  .setNegativeButton(context.getString(R.string.text_no), dialogClickListener).show();
			} else delDice();
			return;
		}
		if(v.getId() == 3){
			if(diceHide) {
				fx.playErrorSound();
				return;
			}
			if(player.isEmpty()) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									newMatch();
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									break;
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				String name = player.getPlayerName();
				Object[] MessageArgument = {name};
				String msg = String.format(context.getString(R.string.aks_for_new_match),MessageArgument);
				builder.setMessage(msg)
		                  .setPositiveButton(context.getString(R.string.text_yes), dialogClickListener)
		                  .setNegativeButton(context.getString(R.string.text_no), dialogClickListener).show();
				return;
			}
			rollAllDie();
			return;
		}
		
		fx.playSoundRoll();
        fx.vibration();
        int max = (player.areSixDice()?6:5);
        for(int i=0;i<max;i++) {
                if (player.isDieDeleted(i)) ge.getDgos()[i].deleteAnimation(false,null);
                else ge.getDgos()[i].rollAnimation(0, isAnimEnabled);
        }
        ge.getPlayername().setText(player.getPlayerName());

		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		rollAllDie();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
