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
import java.io.InputStream;

import fr.escape.Objects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * <p>
 * A wrapper for {@link Image} which represent a drawable texture.
 * 
 * <p>
 * This class is Immutable.
 */
public final class Texture {

	/**
	 * Image used for rendering
	 */
	private final Drawable drawable;
	
	/**
	 * Default Constructor for a Texture
	 * 
	 * @param stream Texture file
	 * @throws IOException If we cannot create a Texture from this {@link File}
	 */
	public Texture(Resources resources, int id) throws IOException {
		drawable = Objects.requireNonNull(resources).getDrawable(id);
	}
	
	/**
	 * Get Texture Width
	 * 
	 * @return Texture Width
	 */
	public int getWidth() {
		return drawable.getWidth();
	}
	
	/**
	 * Get Texture Height
	 * 
	 * @return Texture Height
	 */
	public int getHeight() {
		return drawable.getHeight();
	}
	
	/**
	 * Return the Image used as Texture
	 * 
	 * @return Image used as Texture
	 */
	private Image getImage() {
		return drawable;
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
	 * @param graphics {@link Canvas} used for drawing.
	 * @param x Starting Position X in {@link Canvas}
	 * @param y Starting Position Y in {@link Canvas}
	 * @param width Ending Position X in {@link Canvas}
	 * @param height Ending Position Y in {@link Canvas}
	 * @param srcX Starting Position X in {@link Texture}
	 * @param srcY Starting Position Y in {@link Texture}
	 * @param srcWidth Ending Position X in {@link Texture}
	 * @param srcHeight Ending Position Y in {@link Texture}
	 */
	public void draw(Canvas canvas, int x, int y, int width, int height,
			int srcX, int srcY, int srcWidth, int srcHeight) {
		
		draw(canvas, x, y, width, height, srcX, srcY, srcWidth, srcHeight, 0.);
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
	 * @param graphics {@link Canvas} used for drawing.
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
	public void draw(Canvas canvas, int x, int y, int width, int height,
			int srcX, int srcY, int srcWidth, int srcHeight, double angle) {

		Objects.requireNonNull(canvas);
		
		AffineTransform transformMatrix = null;
		boolean updateMatrix = false;
		
		// Create a Transform Matrix if we need to apply a rotation on Texture
		if(angle != 0) {
			
			transformMatrix = graphics.getTransform();
			updateMatrix = true;
			
			AffineTransform rotationMatrix = new AffineTransform();
			
			// Create Transform Matrix
			rotationMatrix.rotate(Math.toRadians(angle), (x + width) / 2, (y + height) / 2);
			
			// Apply Transform Matrix
			graphics.setTransform(rotationMatrix);

		}
        
		// Draw Texture on Graphics
		graphics.drawImage(getImage(), x, y, width, height, srcX, srcY, srcWidth, srcHeight, null);
		
		canvas.drawBitmap(drawable, x, y,  width, height, srcX, srcY, srcWidth, srcHeight);
		
		// Restore Previous Matrix
		if(updateMatrix) {
			assert transformMatrix != null;
			graphics.setTransform(transformMatrix);
		}
		
	}
	
}
