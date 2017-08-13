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
import it.ecosw.dudo.gui.GraphicsElement;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

/**
 * Class to change background image or color
 * @author Enrico Strocchi
 *
 */
public class Background {
	
	/**
	 * Enum with the list of background images
	 * @author Enrico Strocchi
	 */
	private enum EnumImageBackground {
		GREENCARPET(R.drawable.green_carpet),
		PARTYLIGHT(R.drawable.partylight),
		WOOD(R.drawable.wood),
		GALAXY(R.drawable.galaxy),
		LAVA(R.drawable.lava),
		ROCK(R.drawable.rock),
		SOCCER(R.drawable.soccer);
		
		private int imageid;
		
		/**
		 * Constructors
		 * @param id Image id
		 */
		private EnumImageBackground(int id){
			this.imageid = id;
		}
		
		/**
		 * Get ID of image
		 * @return id of image
		 */
		public int getImageId(){
			return imageid;
		}
		
	}
	
	private Context context;
	
	private View parentLayout;
	
	private GraphicsElement ge;
	
	/**
	 * Constructor
	 * @param context App context
	 */
	public Background(Context context,View parentLayout, GraphicsElement ge){
		this.context = context;
		this.parentLayout = parentLayout;
		this.ge = ge;
	}
	
	/**
	 * Set image or solid color
	 * solor color if string start with '#' otherwise is considered as image
	 * image shall be indicated with the same name in EnumImageBackground class
	 * @param isimage true if is a image
	 * @param color Solid color
	 * @param image Image name
	 */
	public void setBackground(BackgroundStatus status){
		if(!status.isImage()) setSolidColor(status.getColorBackground(),status.getColorText());
		else {
			for (EnumImageBackground st : EnumImageBackground.values()) {
				if (status.getImage().equals(st.name())) {
		        	setImageBackground(st,status.getColorText());
		        	break;
		        }
		    }
		}
	}
	
	/**
	 * Set one image as background
	 * @param ib image to set
	 * @param textcolor for text in the background
	 */
	private void setImageBackground(EnumImageBackground ib, int textcolor){
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),ib.getImageId());
	    BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(),bmp);
	    bitmapDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		parentLayout.setBackgroundDrawable(bitmapDrawable);
		if(ge!=null){
			ge.getPlayername().setTextColor(textcolor);
			ge.getChrono().setTextColor(textcolor);
		}
	}
	
	/**
	 * Set the background to the solid color passed by parameters
	 * @param color new color of background
	 * @param textcolor for text in the background
	 */
	private void setSolidColor(int color, int textcolor){
		//parentLayout.setBackgroundColor(Color.parseColor(color));
		parentLayout.setBackgroundColor(color);
		ge.getPlayername().setTextColor(textcolor);
		ge.getChrono().setTextColor(textcolor);
	}
	
}
