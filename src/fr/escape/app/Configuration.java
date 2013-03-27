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

/**
 * <p>
 * Configuration Object for {@link Graphics} and {@link Activity}.
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
	private final int width;
	private final int height;
	private final String title;
	private final int fps;
	
	/**
	 * Default Constructor 
	 */
	public Configuration() {
		this(400, 600);
	}
	
	/**
	 * Constructor with a given width and height
	 * 
	 * @param width Window Width
	 * @param height Window Height
	 */
	public Configuration(int width, int height) {
		this(width, height, Integer.MAX_VALUE);
	}
	
	/**
	 * Constructor with a given width, height and requested fps.
	 * 
	 * @param width Window Width
	 * @param height Window Height
	 * @param fps Requested Frame per second
	 */
	public Configuration(int width, int height, int fps) {
		this("Escape-IR", width, height, fps);
	}
	
	/**
	 * Constructor with a given width, height, requested fps and title.
	 * 
	 * @param title Window Title
	 * @param width Window Width
	 * @param height Window Height
	 * @param fps Requested Frame per second
	 */
	public Configuration(String title, int width, int height, int fps) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fps = Math.min(Math.max(fps, MIN_FPS), MAX_FPS);
	}
	
	/**
	 * Get Window Width
	 * 
	 * @return Window Width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get Window Height
	 * 
	 * @return Window Height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get Window Title
	 * 
	 * @return Window Title
	 */
	public String getTitle() {
		return title;
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
