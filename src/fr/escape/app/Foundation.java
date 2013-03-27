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

package fr.escape.app;

import fr.escape.resources.Resources;

/**
 * <p>
 * Foundation is a Wrapper which contains all Core Components for the Game
 * 
 * <p>
 * All Components are accessible anywhere after {@link Activity} is created.
 */
public class Foundation {
	
	/**
	 * Game Activity
	 */
	public static Activity ACTIVITY;
	
	/**
	 * Game Graphics
	 */
	public static Graphics GRAPHICS;
	
	/**
	 * Game Resources
	 */
	public static Resources RESOURCES;
	
}
