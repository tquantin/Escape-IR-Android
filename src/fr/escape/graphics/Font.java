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

package fr.escape.graphics;

import android.graphics.Typeface;

/**
 * <p>
 * A wrapper class which handle a Typeface with a Size.
 * 
 */
public final class Font {
	
	/**
	 * Font Properties
	 */
	private final Typeface typeface;
	private final float size;
	
	/**
	 * Default Constructor
	 */
	public Font() {
		this(Typeface.create("Arial", Typeface.NORMAL));
	}
	
	/**
	 * Constructor with a Typeface
	 * 
	 * @param typeface Typeface of the Font
	 */
	public Font(Typeface typeface) {
		this(typeface, 14.0f);
	}
	
	/**
	 * Constructor with a Typeface and a Size
	 * 
	 * @param typeface Typeface of the Font
	 * @param size Size of the Font
	 */
	public Font(Typeface typeface, float size) {
		this.typeface = typeface;
		this.size = size;
	}
	
	/**
	 * Return the Typeface of this Font
	 * 
	 * @return The Typeface
	 */
	public Typeface getTypeface() {
		return typeface;
	}
	
	/**
	 * Return the Size of this Font
	 * 
	 * @return The Size
	 */
	public float getSize() {
		return size;
	}
	
}
