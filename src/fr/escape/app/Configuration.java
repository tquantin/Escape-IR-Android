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

package fr.escape.app;

/**
 * <p>
 * Configuration Object for {@link Graphics} and {@link Engine}.
 * 
 */
public final class Configuration {

	/**
	 * Frame per second, maximum and minimum.
	 */
	private static int MIN_FPS = 20;
	private static int MAX_FPS = 40;
	
	/**
	 * Environment Configuration
	 */
	private final int fps;
	
	/**
	 * Default Constructor 
	 */
	public Configuration() {
		this(Integer.MAX_VALUE);
	}
	/**
	 * Constructor with a given width, height and requested fps.
	 * 
	 * @param fps Requested Frame per second
	 */
	public Configuration(int fps) {
		this.fps = Math.min(Math.max(fps, MIN_FPS), MAX_FPS);
	}

	/**
	 * Get Requested FPS
	 * 
	 * @return Requested FPS
	 */
	public int getFps() {
		return fps;
	}
	
}
