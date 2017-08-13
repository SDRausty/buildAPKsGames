package ru.o2genum.coregame.framework.impl;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import ru.o2genum.coregame.framework.Input;

public class AndroidInput implements Input
{
    OrientationHandler orientHandler;
    KeyboardHandler keyHandler;
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view)
    {
    	orientHandler = new AndroidOrientationHandler(context);
    	keyHandler = new KeyboardHandler(view);
    	if(Integer.parseInt(VERSION.SDK) < 5)
    		touchHandler = new SingleTouchHandler(view);
    	else
    		touchHandler = new MultiTouchHandler(view);
    }

    @Override
    public boolean isKeyPressed(int keyCode)
    {
    	return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public int getTouchX()
    {
    	return touchHandler.getTouchX();
    }

    @Override
    public int getTouchY()
    {
    	return touchHandler.getTouchY();
    }

    @Override
    public boolean isTouchDown()
    {
    	return touchHandler.isTouchDown();
    }

    @Override
    public float getAzimuth()
    {
		return orientHandler.getAzimuth();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}
