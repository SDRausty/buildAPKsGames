package ru.o2genum.coregame.framework.impl;

import java.util.List;

import android.view.View.OnTouchListener;

import ru.o2genum.coregame.framework.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener
{
    public boolean isTouchDown();
    
    public int getTouchX();
    
    public int getTouchY();
    
    public List<TouchEvent> getTouchEvents();
}
