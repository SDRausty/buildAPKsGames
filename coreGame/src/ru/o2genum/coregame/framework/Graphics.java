package ru.o2genum.coregame.framework;

import android.graphics.*;

public interface Graphics
{
	public int getWidth();

	public int getHeight();

	public Canvas getCanvas(); // This canvas will draw on the framebuffer
}
