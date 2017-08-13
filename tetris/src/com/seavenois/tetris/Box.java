package com.seavenois.tetris;

import android.graphics.drawable.Drawable;

//A box is each one of the positions in the board (200)
public class Box {
	private int top, left, side, color;
	Drawable img;

	//The color, is also indicates if the box is free (color 0 means it's free)
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	//Getters for the position
	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getSide() {
		return side;
	}
	
	public Box(int _top, int _left, int _side){
		top = _top;
		left = _left;
		side = _side;
		color = Values.COLOR_NONE;
	}
}
