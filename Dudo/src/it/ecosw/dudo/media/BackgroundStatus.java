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

import java.io.Serializable;

public class BackgroundStatus implements Serializable {

	private static final long serialVersionUID = 151317204284453887L;

	private boolean isimage;
	private int colorbackground;
	private int colortext;
	private String image;
	
	/**
	 * Constructor to set background as solid colorbackground
	 * @param colorbackground solid colorbackground
	 * @param colortext color of text in the background
	 */
	public BackgroundStatus(int colorBackground, int colorText){
		isimage = false;
		this.colorbackground = colorBackground;
		this.colortext = colorText;
	}
	
	/**
	 * Constructor to set background as image
	 * @param image id of background image
	 */
	public BackgroundStatus(String image, int colorText){
		isimage = true;
		this.image = image;
		this.colortext = colorText;
	}

	/**
	 * Return true if background is image
	 * @return true if background is image
	 */
	public boolean isImage() {
		return isimage;
	}

	/**
	 * Return the solid colorbackground of background
	 * @return the colorbackground
	 */
	public int getColorBackground() {
		return colorbackground;
	}
	
	/**
	 * Return color for text in the background
	 * @return color for text in the background
	 */
	public int getColorText(){
		return colortext;
	}

	/**
	 * Return the id of image background
	 * @return the id of imagebackground
	 */
	public String getImage() {
		return image;
	}
	
}
