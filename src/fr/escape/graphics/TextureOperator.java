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
 * An interface for drawing complex Texture
 * 
 */
public interface TextureOperator {
	
	/**
	 * <p>
	 * Draw a rectangle with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * <p>
	 * The portion of the Texture is defined by the implementation of this interface.
	 * 
	 * @param canvas Use this Canvas for drawing.
	 * @param paint Use this Paint for drawing.
	 * @param x Starting Position X in Display Screen.
	 * @param y Starting Position Y in Display Screen.
	 * @param width Ending Position X in Display Screen.
	 * @param height Ending Position Y in Display Screen.
	 * @param angle Rotation to apply on Texture in Degree (Optional)
	 */
	public void draw(final Canvas canvas, final Paint paint, final int x, final int y, final int width, final int height, final float angle);
	
}
