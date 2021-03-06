/*****************************************************************************
 * 
 * Copyright 2012-2013 See AUTHORS file.
 * 
 * This file is part of Escape-IR.
 * 
 * Escape-IR is free software: you can redistribute it and/or modify
 * it under the terms of the zlib license. See the COPYING file.
 * 
 *****************************************************************************/

package fr.escape.android;

import fr.escape.app.Engine;
import fr.escape.app.Input;
import fr.escape.game.Escape;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * <p>
 * Extended {@link Application} for Escape.
 * 
 * <p>
 * Share common components for {@link Activity}.
 */
public final class EscapeApplication extends Application {

	/**
	 * Class TAG
	 */
	private static final String TAG = EscapeApplication.class.getSimpleName();
	
	/**
	 * Escape Engine
	 */
	private final Engine engine;
	
	/**
	 * {@link VelocityTracker} for Touch Event
	 */
	private VelocityTracker tracker;
	
	/**
	 * Default Constructor.
	 */
	public EscapeApplication() {
		this.engine = new Engine(this, new Escape());
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Engine.debug(TAG, "Initialize Engine");
		engine.create(this);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Engine.error(TAG, "onLowMemory detected");
	}
	
	public Engine getEngine() {
		return engine;
	}
	
	public void startBuilderActivity() {
		Intent intent = new Intent(this,BuilderActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}
		
	public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
            	acquireVelocityTracker();
            	onTouch(event);
            	break;
            }
            case MotionEvent.ACTION_MOVE: {
            	onTouchVelocity(event);
            	break;
            }
            case MotionEvent.ACTION_UP: {
            	onTouchVelocity(event);
            	releaseVelocityTracker();
            	break;
            }
            case MotionEvent.ACTION_CANCEL: {
            	releaseVelocityTracker();
            	break;
            }
        }
        return true;
	}
	
	private void acquireVelocityTracker() {
		if(tracker == null) {
            tracker = VelocityTracker.obtain();
        } else {
            tracker.clear();
        }
	}
	
	private void onTouch(MotionEvent event) {
		tracker.addMovement(event);
    	engine.event(new Input(event));
	}
	
	private void onTouchVelocity(MotionEvent event) {
		tracker.addMovement(event);
    	tracker.computeCurrentVelocity(1000);
    	engine.event(new Input(event, tracker.getXVelocity(), tracker.getYVelocity()));
	}
	
	private void releaseVelocityTracker() {
		tracker.recycle();
    	tracker = null;
	}
	
}
