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

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * <p>
 * Shape used for drawing geometric objects.
 * 
 * <p>
 * Inspired from AWT Shape
 * 
 */
public abstract class Shape {

	/**
	 * Draw the Shape on the given {@link Canvas} with the given {@link Paint}.
	 * 
	 * @param canvas Canvas used drawing
	 * @param paint Paint used for drawing
	 */
	public abstract void draw(Canvas canvas, Paint paint);
	
}
