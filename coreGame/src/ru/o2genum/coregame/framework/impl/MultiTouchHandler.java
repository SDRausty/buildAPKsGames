package ru.o2genum.coregame.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.util.*;

import ru.o2genum.coregame.framework.Input.TouchEvent;
import ru.o2genum.coregame.framework.Pool;
import ru.o2genum.coregame.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
    boolean[] isTouched = new boolean[20];
    int[] touchX = new int[20];
    int[] touchY = new int[20];
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();

    public MultiTouchHandler(View view) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);
            TouchEvent touchEvent;

            switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) event
                        .getX(pointerIndex);
                touchEvent.y = touchY[pointerId] = (int) event
                        .getY(pointerIndex);
                isTouched[pointerId] = true;
				Log.d("TOUCH", "Pointer " + pointerId + " is DOWN");
                touchEventsBuffer.add(touchEvent);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_UP;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) event
                        .getX(pointerIndex);
                touchEvent.y = touchY[pointerId] = (int) event
                        .getY(pointerIndex);
                isTouched[pointerId] = false;
				Log.d("TOUCH", "Pointer " + pointerId + " is UP");
                touchEventsBuffer.add(touchEvent);
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);

                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) event
                            .getX(pointerIndex);
                    touchEvent.y = touchY[pointerId] = (int) event
                            .getY(pointerIndex);
                    touchEventsBuffer.add(touchEvent);
                }
                break;
            }

            return true;
        }
    }

    @Override
    public boolean isTouchDown() {
        synchronized (this) {
			return (getMaxPointer() >= 0);
        }
    }

    @Override
    public int getTouchX() {
        synchronized (this) {
	int pointer = getMaxPointer();
            if (pointer < 0 || pointer >= 20)
                return 0;
            else
                return touchX[pointer];
        }
    }

    @Override
    public int getTouchY() {
        synchronized (this) {
	int pointer = getMaxPointer();
            if (pointer < 0 || pointer >= 20)
                return 0;
            else
                return touchY[pointer];
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

	private int getMaxPointer()
	{
		for(int i = 20 - 1; i >= 0; i--)
		{
			if(isTouched[i])
			return i;
		}
		return -1;
	}
}
