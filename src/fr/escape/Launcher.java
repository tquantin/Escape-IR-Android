/*****************************************************************************
 * 
 * Copyright 2012 See AUTHORS file.
 * 
 * This file is part of Escape-IR.
 * 
 * Escape-IR is free software: you can redistribute it and/or modify
 * it under the terms of the zlib license. See the COPYING file.
 * 
 *****************************************************************************/

package fr.escape;

import fr.escape.app.Activity;
import fr.escape.game.Escape;

/**
 * Default Launcher/Main for Escape-IR
 */
public final class Launcher {

	private static final String TAG = Launcher.class.getSimpleName();

	/**
	 * Default Entry Point
	 * 
	 * @param args Options, if any.
	 */
	public static void main(String[] args) {
		
		// Create Activity with a Game
		Activity activity = new Activity(new Escape());
		
		// Launch Activity
		//activity.setLogLevel(Activity.LOG_DEBUG);
		activity.debug(TAG, "Initialize Launcher");
		activity.create();

	}
	
}
