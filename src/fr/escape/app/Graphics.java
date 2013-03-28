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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import fr.escape.Objects;
import fr.escape.graphics.Font;
import fr.escape.graphics.RenderListener;
import fr.escape.graphics.Shape;
import fr.escape.graphics.Paths;
import fr.escape.graphics.Texture;
import fr.escape.graphics.TextureOperator;
import fr.umlv.zen2.ApplicationContext;
import fr.umlv.zen2.ApplicationRenderCode;

/**
 * <p>
 * This class is a layer for AWT and Zen2 Library.
 * 
 * <p>
 * This class auto-tune herself for sleep with the requested FPS.
 * 
 * <p>
 * Use an Buffer in Rendering Phase and flush it when it's 
 * done to the {@link Graphics2D} used for User Screen.
 * 
 */
public final class Graphics {
	
	private final static int MINIMUM_WAKEUP_TIME = 0;
	private final static int MAXIMUM_WAKEUP_TIME = 32;
	private final static Font DEFAULT_FONT = new Font();
	private final static int DEFAULT_COLOR = Color.BLACK;
	private final static Stroke DEFAULT_STROKE = new BasicStroke(1);
	
	private final RenderListener listener;
	private final int width;
	private final int height;
	private final int displayFps;
	private final Canvas buffer;
	private final Bitmap bitmap;
	private final Paint paint;
	
	private long lastRender;
	private int rawFps;
	private int smoothFps;
	private int wakeUp;

	/**
	 * Default Listener
	 * 
	 * @param listener RenderListener
	 * @param configuration Graphics Configuration
	 */
	public Graphics(RenderListener listener, Configuration configuration) {
		
		Objects.requireNonNull(listener);
		Objects.requireNonNull(configuration);
		
		this.displayFps = configuration.getFps();
		this.listener = listener;
		this.lastRender = System.currentTimeMillis();
		this.rawFps = 0;
		this.smoothFps = 0;
		this.wakeUp = 25;
		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
	}
	
	/** 
	 * @return The width in pixels of the display.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height in pixels of the display.
	 */
	public int getHeight() {
		return height;
	}

	/** 
	 * @return The time span between the current frame and the last frame in seconds.
	 */
	public long getDeltaTime() {
		return System.currentTimeMillis() - lastRender;
	}
	
	/** 
	 * @return The average number of frames per second.
	 */
	public int getFramesPerSecond() {
		return smoothFps;
	}
	
	/**
	 * @return The requested number of frames per second.
	 */
	public int getRequestedFramesPerSecond() {
		return displayFps;
	}
	
	/**
	 * @return Wait time until next render.
	 */
	public int getNextWakeUp() {
		return wakeUp;
	}
	
	/**
	 * Return render listener for Drawing.
	 * 
	 * @return {@link RenderListener}
	 */
	RenderListener getRenderListener() {
		return listener;
	}
	
	/**
	 * Return Buffered Image as Frame.
	 * 
	 * @return {@link BufferedImage}
	 */
	BufferedImage getBufferedImage() {
		return gBuffer;
	}
	
	/**
	 * Return Buffered Graphics for Frame.
	 * 
	 * @return {@link Graphics2D}
	 */
	Graphics2D getBufferedGraphics() {
		return g2d;
	}
	
	/**
	 * <p>
	 * Core Engine for Rendering.
	 * 
	 * <p>
	 * Called by {@link Engine} at each loop.
	 * 
	 * @param canvas Canvas used with Android.
	 */
	public void render(Canvas canvas) {

		// NOTE: May need to do this operation in another thread
		// NOTE: May need to use a Callback for Render Operation
		
		// Flush and clear previous drawing
		buffer.drawColor(Color.WHITE);
		
		// Start Game Rendering
		getRenderListener().render();
		
		// END NOTE
		
		// Draw Graphics
		canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
		
		// Update Render Timing
		updateRender(System.currentTimeMillis());
		
	}
	
	/**
	 * Update the Last Rendering Timestamp
	 * 
	 * @param currentRender Last Rendering Timestamp
	 */
	void updateRender(long currentRender) {
		
		rawFps++;
		
		if((lastRender / 1000) < (currentRender / 1000)) {
			updateFramesPerSecond();
			updateWait();
		}
		
		lastRender = currentRender;
	}

	/**
	 * Update the average number of frames per second.
	 */
	private void updateFramesPerSecond() {
		smoothFps = (int) ((0.1 * smoothFps) + (0.9 * rawFps));
		rawFps = 0;
	}
	
	/**
	 * Update the next waiting time for next render.
	 */
	private void updateWait() {
		
		if(((getFramesPerSecond() < getRequestedFramesPerSecond()) &&
				(wakeUp > MINIMUM_WAKEUP_TIME)) || 
				((getFramesPerSecond() > getRequestedFramesPerSecond()) &&
				(wakeUp < MAXIMUM_WAKEUP_TIME))) {
			
			int factor = getRequestedFramesPerSecond() - getFramesPerSecond();
			wakeUp = 1000 / (getRequestedFramesPerSecond() + factor);
			
			if(wakeUp < MINIMUM_WAKEUP_TIME) {
				wakeUp = MINIMUM_WAKEUP_TIME;
			}
			
			if(wakeUp > MAXIMUM_WAKEUP_TIME) {
				wakeUp = MAXIMUM_WAKEUP_TIME;
			}
			
		}
		
	}

	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y having the width and height of the texture.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Position X in Display Screen
	 * @param y Position Y in Display Screen
	 */
	public void draw(final Texture texture, final int x, final int y) {
		draw(texture, x, y, 0.);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y having the width and height of the texture.
	 * 
	 * <p>
	 * Apply a Rotation on Texture with the given angle in degree.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Position X in Display Screen
	 * @param y Position Y in Display Screen
	 * @param angle Rotation to apply on Texture in Degree
	 */
	public void draw(final Texture texture, final int x, final int y, final double angle) {
		draw(texture, x, y, x + texture.getWidth(), y + texture.getHeight(), angle);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y and stretching the region to cover the given width and height.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Starting Position X in Display Screen
	 * @param y Starting Position Y in Display Screen
	 * @param width Ending Position X in Display Screen
	 * @param height Ending Position Y in Display Screen
	 */
	public void draw(final Texture texture, final int x, final int y, final int width, final int height) {
		draw(texture, x, y, width, height, 0.);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y and stretching the region to cover the given width and height.
	 * 
	 * <p>
	 * Apply a Rotation on Texture with the given angle in degree.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Starting Position X in Display Screen
	 * @param y Starting Position Y in Display Screen
	 * @param width Ending Position X in Display Screen
	 * @param height Ending Position Y in Display Screen
	 * @param angle Rotation to apply on Texture in Degree
	 */
	public void draw(final Texture texture, final int x, final int y, final int width, final int height, final double angle) {
		draw(texture, x, y, width, height, 0, 0, texture.getWidth(), texture.getHeight(), angle);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * <p>
	 * The portion of the Texture given by srcX, srcY and srcWidth, srcHeight are used.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Position X in Display Screen
	 * @param y Position Y in Display Screen
	 * @param srcX Starting Position X in Texture
	 * @param srcY Starting Position Y in Texture
	 * @param srcWidth Ending Position X in Texture
	 * @param srcHeight Ending Position Y in Texture
	 */
	public void draw(final Texture texture, final int x, final int y, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
		draw(texture, x, y, srcX, srcY, srcWidth, srcHeight, 0.);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * <p>
	 * The portion of the Texture given by srcX, srcY and srcWidth, srcHeight are used.
	 * 
	 * <p>
	 * Apply a Rotation on Texture with the given angle in degree.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Position X in Display Screen
	 * @param y Position Y in Display Screen
	 * @param srcX Starting Position X in Texture
	 * @param srcY Starting Position Y in Texture
	 * @param srcWidth Ending Position X in Texture
	 * @param srcHeight Ending Position Y in Texture
	 * @param angle Rotation to apply on Texture in Degree
	 */
	public void draw(final Texture texture, final int x, final int y, final int srcX, final int srcY, final int srcWidth, final int srcHeight, double angle) {
		draw(texture, x, y, srcWidth - srcX, srcHeight - srcY, srcX, srcY, srcWidth, srcHeight, angle);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * <p>
	 * The portion of the Texture given by srcX, srcY and srcWidth, srcHeight is used.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Starting Position X in Display Screen
	 * @param y Starting Position Y in Display Screen
	 * @param width Ending Position X in Display Screen
	 * @param height Ending Position Y in Display Screen
	 * @param srcX Starting Position X in Texture
	 * @param srcY Starting Position Y in Texture
	 * @param srcWidth Ending Position X in Texture
	 * @param srcHeight Ending Position Y in Texture
	 */
	public void draw(final Texture texture, final int x, final int y, final int width, final int height, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
		draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, 0.);
	}
	
	/**
	 * <p>
	 * Draws a rectangle with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * <p>
	 * The portion of the Texture given by srcX, srcY and srcWidth, srcHeight is used.
	 * 
	 * <p>
	 * Apply a Rotation on Texture with the given angle in degree.
	 * 
	 * @param texture Texture used for rendering
	 * @param x Starting Position X in Display Screen
	 * @param y Starting Position Y in Display Screen
	 * @param width Ending Position X in Display Screen
	 * @param height Ending Position Y in Display Screen
	 * @param srcX Starting Position X in Texture
	 * @param srcY Starting Position Y in Texture
	 * @param srcWidth Ending Position X in Texture
	 * @param srcHeight Ending Position Y in Texture
	 * @param angle Rotation to apply on Texture in Degree
	 */
	public void draw(final Texture texture, final int x, final int y, final int width, final int height, final int srcX, final int srcY, final int srcWidth, final int srcHeight, final double angle) {
		texture.draw(g2d, x, y, width, height, srcX, srcY, srcWidth, srcHeight, angle);
	}
	
	/**
	 * <p>
	 * Draw a TextureOperator with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * @param textureOp TextureOperator used for rendering.
	 * @param x Starting Position X in Display Screen
	 * @param y Starting Position Y in Display Screen
	 * @param width Ending Position X in Display Screen
	 * @param height Ending Position Y in Display Screen
	 */
	public void draw(final TextureOperator textureOp, final int x, final int y, final int width, final int height) {
		draw(textureOp, x, y, width, height, 0);
	}
	
	/**
	 * <p>
	 * Draw a TextureOperator with the top left corner at x,y having the given width and height in pixels. 
	 * 
	 * @param textureOp TextureOperator used for rendering.
	 * @param x Starting Position X in Display Screen
	 * @param y Starting Position Y in Display Screen
	 * @param width Ending Position X in Display Screen
	 * @param height Ending Position Y in Display Screen
	 * @param angle Rotation to apply on Texture in Degree (Optional)
	 */
	public void draw(final TextureOperator textureOp, final int x, final int y, final int width, final int height, final double angle) {
		textureOp.draw(g2d, x, y, width, height, angle);
	}
	
	/**
	 * <p>
	 * Draw a String with the top left corner at x,y.
	 * 
	 * @param message String to display
	 * @param x Left Position X in Display Screen
	 * @param y Bottom Position Y in Display Screen
	 */
	public void draw(String message, int x, int y) {
		draw(message, x, y, DEFAULT_FONT, DEFAULT_COLOR);
	}
	
	/**
	 * <p>
	 * Draw a String with the top left corner at x,y with a given Font.
	 * 
	 * @param message String to display
	 * @param x Left Position X in Display Screen
	 * @param y Bottom Position Y in Display Screen
	 * @param font Font used for rendering
	 */
	public void draw(String message, int x, int y, Font font) {
		draw(message, x, y, font, DEFAULT_COLOR);
	}
	
	/**
	 * <p>
	 * Draw a String with the top left corner at x,y with a given Color.
	 * 
	 * @param message String to display
	 * @param x Left Position X in Display Screen
	 * @param y Bottom Position Y in Display Screen
	 * @param color Color used for rendering
	 */
	public void draw(String message, int x, int y, int color) {
		draw(message, x, y, DEFAULT_FONT, color);
	}
	
	/**
	 * <p>
	 * Draw a String with the top left corner at x,y with a given Color and Font.
	 * 
	 * @param message String to display
	 * @param x Left Position X in Display Screen
	 * @param y Bottom Position Y in Display Screen
	 * @param font Font used for rendering
	 * @param color Color used for rendering
	 */
	public void draw(final String message, final int x, final int y, final Font font, final int color) {
		
		paint.setColor(color);
		paint.setTypeface(font.getTypeface());
		paint.setTextSize(font.getSize());
		buffer.drawText(message, x, y, paint);
		
	}
	
	/**
	 * Draw a simple Shape
	 * 
	 * @param shape Shape to draw
	 */
	public void draw(final Shape shape) {
		draw(shape, DEFAULT_COLOR, DEFAULT_STROKE);
	}
	
	/**
	 * Draw a simple Shape with the given Color
	 * 
	 * @param shape Shape to draw
	 * @param color Color to use
	 */
	public void draw(final Shape shape, final Color color) {
		draw(shape, color, DEFAULT_STROKE);
	}
	
	/**
	 * Draw a simple Shape with the given Color and Stroke
	 * 
	 * @param shape Shape to draw
	 * @param color Color to use
	 * @param stroke Stroke to use
	 */
	public void draw(final Shape shape, final int color, final Stroke stroke) {
		
		paint.setColor(color);
		buffer.drawPath(shape.getPath(), paint);
		
	}
	
}
