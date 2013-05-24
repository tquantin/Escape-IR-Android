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

package fr.escape.graphics;

import java.io.File;
import java.io.IOException;

import fr.escape.Objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * <p>
 * A wrapper for {@link Bitmap} which represent a drawable texture.
 * 
 * <p>
 * This class is Immutable.
 * 
 */
public final class Texture {

	/**
	 * Bitmap used for rendering
	 */
	private final Bitmap texture;
	
	/**
	 * Default Constructor for a Texture
	 * 
	 * @param stream Texture file
	 * @throws IOException If we cannot create a Texture from this {@link File}
	 */
	public Texture(Resources resources, int drawable) throws IOException {
		this.texture = BitmapFactory.decodeResource(Objects.requireNonNull(resources), drawable);
	}
	
	/**
	 * Constructor for a Texture with a {@link Bitmap}
	 *  
	 * @param texture The {@link Bitmap} Texture
	 */
	public Texture(Bitmap texture) {
		this.texture = Objects.requireNonNull(texture);
	}
	
	/**
	 * Get Texture Width
	 * 
	 * @return Texture Width
	 */
	public int getWidth() {
		return texture.getWidth();
	}
	
	/**
	 * Get Texture Height
	 * 
	 * @return Texture Height
	 */
	public int getHeight() {
		return texture.getHeight();
	}
	
	/**
	 * Return the Bitmap used as Texture
	 * 
	 * @return {@link Bitmap} used as Texture
	 */
	private Bitmap getBitmap() {
		return texture;
	}

	/**
	 * <p>
	 * Draw this Texture on the given {@link Canvas}.
	 * 
	 * <p>
	 * Draw from the top left corner at x,y to the bottom right corner at width,height in
	 * the {@link Canvas}.
	 * 
	 * <p>
	 * We use the rectangle in the Texture located from the top left corner at srcX,srcY to
	 * the bottom right corner at srcWidth,srcHeight.
	 * 
	 * @param canvas {@link Canvas} used for drawing.
	 * @param paint {@link Paint} used for drawing.
	 * @param x Starting Position X in {@link Canvas}
	 * @param y Starting Position Y in {@link Canvas}
	 * @param width Ending Position X in {@link Canvas}
	 * @param height Ending Position Y in {@link Canvas}
	 * @param srcX Starting Position X in {@link Texture}
	 * @param srcY Starting Position Y in {@link Texture}
	 * @param srcWidth Ending Position X in {@link Texture}
	 * @param srcHeight Ending Position Y in {@link Texture}
	 */
	public void draw(Canvas canvas, Paint paint, int x, int y, int width, int height,
			int srcX, int srcY, int srcWidth, int srcHeight) {
		
		draw(canvas, paint, x, y, width, height, srcX, srcY, srcWidth, srcHeight, 0.0f);
	}
	
	/**
	 * <p>
	 * Draw this Texture on the given {@link Canvas}.
	 * 
	 * <p>
	 * Draw from the top left corner at x,y to the bottom right corner at width,height in
	 * the {@link Canvas}.
	 * 
	 * <p>
	 * We use the rectangle in the Texture located from the top left corner at srcX,srcY to
	 * the bottom right corner at srcWidth,srcHeight.
	 * 
	 * <p>
	 * Apply a rotation with the given Angle in Degree.
	 * 
	 * @param canvas {@link Canvas} used for drawing.
	 * @param paint {@link Paint} used for drawing.
	 * @param x Starting Position X in {@link Canvas}
	 * @param y Starting Position Y in {@link Canvas}
	 * @param width Ending Position X in {@link Canvas}
	 * @param height Ending Position Y in {@link Canvas}
	 * @param srcX Starting Position X in {@link Texture}
	 * @param srcY Starting Position Y in {@link Texture}
	 * @param srcWidth Ending Position X in {@link Texture}
	 * @param srcHeight Ending Position Y in {@link Texture}
	 * @param angle Rotation to apply on Texture in Degree.
	 */
	public void draw(Canvas canvas, Paint paint, int x, int y, int width, int height,
			int srcX, int srcY, int srcWidth, int srcHeight, float angle) {

		Objects.requireNonNull(canvas);
		
		boolean updateMatrix = false;
		
		// Create a Rotation Matrix if we need to apply a rotation on Texture
		if(angle != 0) {
			
			// Save Matrix current state
			canvas.save();
			updateMatrix = true;
			
			// Apply Rotation Matrix
			canvas.rotate(angle, (x + width) / 2, (y + height) / 2);
		}
        
		// Draw Texture on Canvas
		canvas.drawBitmap(getBitmap(), new Rect(srcX, srcY, srcWidth, srcHeight), new Rect(x, y, width, height), paint);
		
		// Restore Previous Matrix
		if(updateMatrix) {
			canvas.restore();
		}
		
	}
	
}
