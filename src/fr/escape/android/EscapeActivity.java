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
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public abstract class EscapeActivity extends Activity {

	/**
	 * Class TAG
	 */
	private static final String TAG = EscapeActivity.class.getSimpleName();
	
	/**
	 * Drawable window Width
	 */
	private int width;
	
	/**
	 * Drawable window Height
	 */
	private int height;
	
	/**
	 * View used by {@link Graphics} for rendering
	 */
	private GraphicsView view;
	
	/**
	 * Escape Engine 
	 */
	private Engine engine;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		engine = ((EscapeApplication) getApplication()).getEngine();
		engine.debug(TAG, "onCreate");
		
		// Configure Window Options
		configureWindow();
		
		// Retrieve Display Components
		Display display = getWindowManager().getDefaultDisplay();
		
		// Ignore deprecation for API Lower than 13
		width = display.getWidth();
		
		// Ignore deprecation for API Lower than 13 
		height = display.getHeight();
		
		setContentView(R.layout.escape);
		
		view = (GraphicsView) findViewById(R.id.gview);
		
		engine.getGraphics().setView(view, width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        return ((EscapeApplication) getApplication()).onTouchEvent(event);
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
