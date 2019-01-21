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

import it.ecosw.dudo.media.GenDiceAnimation;
import it.ecosw.dudo.media.GenDiceImage;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * This class allow to anchor che graphics die with controller die
 * @author Enrico Strocchi
 */
public class DiceGraphicObjects {
	
	private final static int ANIMATIONTIME = 900; 

	private int pos;
	
	private GenDiceImage gdi;
	
	private ImageView image;
	
	private ViewGroup layout;
	
	/**
	 * Constructor
	 * @param pos Position in the graphics
	 * @param image Dice image
	 * @param layout Layout
	 * @param gdi Generator Dice Image
	 */
	public DiceGraphicObjects(int pos, ImageView image, ViewGroup layout, GenDiceImage gdi, boolean animated){
		this.pos = pos;
		this.image = image;
		this.layout = layout;
		this.gdi = gdi;
		
		if(animated) image.startAnimation(GenDiceAnimation.animationRollFactory(ANIMATIONTIME));
        else image.setAnimation(null);

	}
	
	/**
	 * Return the position of dice in the layout (no from 1 to 5)
	 * @return the pos position of dice in the layout
	 */
	public int getPos() {
		return pos;
	}
	
	/**
	 * Hide current dice
	 */
	public void hide(boolean animation){
		image.setImageBitmap(gdi.getImage(0));
		if(animation) {
			Animation anim = image.getAnimation();
			if(anim!=null) {
				anim.setDuration(0);
				image.startAnimation(anim);
			} else image.startAnimation(GenDiceAnimation.animationRollFactory(0));
		}
	}
	
	/**
	 * Show current dice
	 * @param value dice value to show
	 * @param animation true if animation is enabled
	 */
	public void show(int value,boolean animation){
		image.setImageBitmap(gdi.getImage(value));
		if(animation){
			Animation anim = image.getAnimation();
			if(anim!=null) {
				anim.setDuration(0);
				image.startAnimation(anim);
			} else image.startAnimation(GenDiceAnimation.animationRollFactory(0));
		}
		
	}
	
	/**
	 * Make a new roll animation on current dice
	 * @param value value to show
	 * @param isAnimated true to show animation
	 */
	public void rollAnimation(int value, boolean isAnimated) {
		image.setImageBitmap(gdi.getImage(value));
		layout.setVisibility(View.VISIBLE);
		if(isAnimated) image.startAnimation(GenDiceAnimation.animationRollFactory(ANIMATIONTIME));
		else image.setAnimation(null);
	}
	
	/**
	 * Delete current dice
	 * @param isAnimated true to show animation
	 * @param listener animation listener to be executed
	 */
	public void deleteAnimation(boolean isAnimated,AnimationListener listener) {
		// TODO Auto-generated method stub
		if(isAnimated) {
			Animation animation = GenDiceAnimation.animationDelFactory(ANIMATIONTIME);
			animation.setAnimationListener(listener);
			image.startAnimation(animation);
		}
		else layout.setVisibility(View.GONE);
	}
	
}
