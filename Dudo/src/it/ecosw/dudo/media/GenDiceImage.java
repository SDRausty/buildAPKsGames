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

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import it.ecosw.dudo.R;

/**
 * Generate Image
 * @author Enrico Strocchi
 */
public class GenDiceImage {
	
	public final static String CLASSIC = "CLASSIC";
	public final static String ANDROID = "ANDROID";
	
	private Bitmap[] images;
	private String current;
	private Context context;
	
	/**
	 * Constructor
	 * @param context context of the apps
	 * @param style of dice image
	 */
	public GenDiceImage(Context context,String style){
		this.context = context;
		images = new Bitmap[7];
		current = style;
		for(int i=0;i<7;i++) {
			images[i] = genImage(context,i,style);
		}
	}
	
	/**
	 * Replace current style
	 * @param style new style
	 * @return true if the style was changed
	 */
	public boolean setStyle(String style){
		if(current.equals(style)) return false;
		current = style;
		for(int i=0;i<7;i++) images[i] = genImage(context,i,style);
		return true;
	}
	
	/**
	 * Return bitmap of dice
	 * @param value 0 dice empty, 1-6 dice with 1 to 6
	 * @return bitmap of the dice
	 */
	public Bitmap getImage(int value){
		if(value<0 || value>6) return images[0];
		return images[value];
	}
	
	/**
	 * Return name of current dice style
	 * @return name of current dice style
	 */
	public String getCurrentStyle(){
		return current;
	}
	
	/**
	 * Generate a bitmap for the dice
	 * @param context app context
	 * @param value value of dice
	 * @param style style for dice
	 * @return bitmap object
	 */
	private static Bitmap genImage(Context context, int value, String style) {
		//Create a new image bitmap and attach a brand new canvas to it
		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.dice_empty,options).copy(Bitmap.Config.ARGB_8888, true);
		Bitmap dot = null;
		Bitmap lama = null;
		Bitmap removed = null;
		if (style.equals(CLASSIC)){
			dot = getBitmapFromSVG(context, R.drawable.redpoint,(int)Math.round(bm.getWidth()/5),(int)Math.round(bm.getHeight()/5));
			lama = getBitmapFromSVG(context,R.drawable.lama,(int)Math.round(bm.getWidth()/1.5),(int)Math.round(bm.getHeight()/1.5));
			removed = getBitmapFromSVG(context,R.drawable.redremove,(int)Math.round(bm.getWidth()/1.6),(int)Math.round(bm.getHeight()/1.6));
		} else if(style.equals(ANDROID)){
			dot = getBitmapFromSVG(context, R.drawable.greenpoint,(int)Math.round(bm.getWidth()/5),(int)Math.round(bm.getHeight()/5));
			lama = getBitmapFromSVG(context,R.drawable.android,(int)Math.round(bm.getWidth()/1.5),(int)Math.round(bm.getHeight()/1.5));
			removed = getBitmapFromSVG(context,R.drawable.greenremove,(int)Math.round(bm.getWidth()/1.6),(int)Math.round(bm.getHeight()/1.6));
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Canvas canvas = new Canvas(bm);
		switch(value){
		case(0):
			canvas.drawBitmap(removed,bm.getWidth()/2-lama.getWidth()/2, bm.getHeight()/2-lama.getHeight()/2, paint);
			break;
		case(1):
			canvas.drawBitmap(lama,bm.getWidth()/2-lama.getWidth()/2, bm.getHeight()/2-lama.getHeight()/2, paint);
			break;
		case(2):
			canvas.drawBitmap(dot,bm.getWidth()/3-dot.getWidth()/2, bm.getHeight()/3-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,2*bm.getWidth()/3-dot.getWidth()/2, 2*bm.getHeight()/3-dot.getHeight()/2, paint);
			break;
		case(3):
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,bm.getWidth()/2-dot.getWidth()/2, bm.getHeight()/2-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			break;
		case(4):
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			break;
		case(5):
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,bm.getWidth()/2-dot.getWidth()/2, bm.getHeight()/2-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			break;
		case(6):
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/2-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, bm.getHeight()/2-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,3*bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			canvas.drawBitmap(dot,bm.getWidth()/4-dot.getWidth()/2, 3*bm.getHeight()/4-dot.getHeight()/2, paint);
			break;
		}
		return bm;
	}
	
	/**
	 * Convert a SVG saved between the resources in a Bitmap
	 * @param context App context
	 * @param resourceId ID of svg
	 * @param width final width
	 * @param height final height
	 * @return bitmap
	 * @throws SVGParseException svg parsing exception
	 */
	private static Bitmap getBitmapFromSVG(Context context, int resourceId, int width, int height){
		Picture picture = null;
		try {
			picture = SVG.getFromResource(context, resourceId).renderToPicture();
		} catch (SVGParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		Bitmap bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawPicture(picture, new Rect(0,0,width,height));
		return bmp;
	}
}
