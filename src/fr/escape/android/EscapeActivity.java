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
import fr.escape.app.Graphics;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public final class EscapeActivity extends Activity {

	/**
	 * Class TAG
	 */
	private static final String TAG = EscapeActivity.class.getSimpleName();
	
	/**
	 * Escape Engine 
	 */
	private Engine engine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Retrieve Engine
		engine = ((EscapeApplication) getApplication()).getEngine();
		Engine.debug(TAG, "onCreate");
		
		// Configure Window Options
		configureWindow();
		
		// Configure Content View
		setContentView(new GraphicsView(this));
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        return ((EscapeApplication) getApplication()).onTouchEvent(event);
    }
	
	public Engine getEngine() {
		return engine;
	}
	
	public Graphics getGraphics() {
		return getEngine().getGraphics();
	}
	
	/**
	 * Configure Activity and Application Window.
	 */
	private void configureWindow() {

		/**
		 * Remove Action Bar
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/**
		 * Launch Activity in Fullscreen.
		 */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/**
		 * Disable Keyguard
		 */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		
	}
	
}
