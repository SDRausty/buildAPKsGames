package it.ecosw.dudo.media;

import java.util.Random;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

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

/**
 * This is a static class to generate animation for dice 
 * @author Enrico Strocchi
 */
public class GenDiceAnimation {

	private static Random rnd = new Random();
	
	/**
	 * Generate animation for dice
	 * @param duration
	 * @return Animation for dice
	 */
	public static Animation animationRollFactory(int duration){
		//Rotate Animation
		RotateAnimation ra = new RotateAnimation(0,rnd.nextInt(70)-35, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setInterpolator(new LinearInterpolator());
		ra.setDuration(duration);
		ra.setFillAfter(true);
			
		//Scale Animation
		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setInterpolator(new LinearInterpolator());
		sa.setDuration(duration);
		sa.setFillAfter(false);
		
		// Animation Set with combination of Rotate and Scale
		AnimationSet as = new AnimationSet(true);
	    as.setFillEnabled(true);
	    as.setFillAfter(true);
	    as.setInterpolator(new BounceInterpolator());
	    as.addAnimation(ra);
	    as.addAnimation(sa);
		
	    return as;
	}
	
	/**
	 * Generate a fade out animation when a dice is deleted
	 * @param duration duration of the animation
	 * @return animation
	 */
	public static Animation animationDelFactory(int duration){
		
		//Rotate Animation
		RotateAnimation ra = new RotateAnimation(0,720, 
		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setInterpolator(new LinearInterpolator());
		ra.setDuration(duration);
		ra.setFillAfter(true);
		
		// Scale Animation
		ScaleAnimation sa = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 
		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setInterpolator(new LinearInterpolator());
		sa.setDuration(duration);
		sa.setFillAfter(true);
		
		// Animation Set with combination of Rotate and Scale
		AnimationSet as = new AnimationSet(true);
	    as.setFillEnabled(true);
	    as.setFillAfter(true);
	    as.setInterpolator(new DecelerateInterpolator());
	    as.addAnimation(ra);
	    as.addAnimation(sa);
		
	    return as;
	}
	
	/**
	 * Return a drawable as sequence of dice value
	 * @param context app context
	 * @param gdi Generate Dice Image class
	 * @param numframe number of total frame
	 * @param duration duration in ms of each frame
	 * @param lastvalue last value to show
	 * @return drawable
	 */
	public static AnimationDrawable sequenceFactory(Context context, GenDiceImage gdi, int numframe, int duration, int lastvalue){
		AnimationDrawable ad = new AnimationDrawable();
		ad.setOneShot(true);
		for(int i=0;i<numframe-1;i++){
			ad.addFrame(new BitmapDrawable(context.getResources(),gdi.getImage(rnd.nextInt(6)+1)), duration);
		}
		ad.addFrame(new BitmapDrawable(context.getResources(),gdi.getImage(lastvalue)), duration);
		return ad;
	}
	
}
