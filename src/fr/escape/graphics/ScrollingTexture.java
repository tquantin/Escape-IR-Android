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

import java.awt.Graphics2D;
import java.util.Objects;

/**
 * <p>
 * A {@link TextureOperator} which handle scroll features for a Texture.
 * 
 * <p>
 * For scrolling, use {@link ScrollingTexture#setXPercent(float)} or {@link ScrollingTexture#setYPercent(float)}
 * for X or Y axis. Percent must be between 0 and 1. If you want to progress by 1%, call these with lastPercent+0.01.
 * 
 * @see TextureOperator
 */
public class ScrollingTexture implements TextureOperator {

	/**
	 * Texture used for rendering
	 */
	private final Texture texture;
	
	/**
	 * Percent of scrolling in X axis.
	 */
	private float percentX;
	
	/**
	 * Percent of scrolling in Y axis.
	 */
	private float percentY;

	/**
	 * Reverse the scrolling Effet.
	 */
	private boolean reverse;
	
	/**
	 * A Scrolling Texture with a given Texture.
	 * 
	 * @param texture Texture to use.
	 */
	public ScrollingTexture(Texture texture) {
		this(texture, false);
	}
	
	/**
	 * A Scrolling Texture with a given Texture.
	 * 
	 * @param texture Texture to use.
	 * @param reverse Apply a reverse scrolling.
	 */
	public ScrollingTexture(Texture texture, boolean reverse) {
		this.texture = Objects.requireNonNull(texture);
		this.percentX = 0;
		this.percentY = 0;
		this.reverse = reverse;
	}

	/**
	 * <p>
	 * Draw a Scrollable Texture defined by {@link ScrollingTexture#setXPercent(float)} 
	 * and {@link ScrollingTexture#setYPercent(float)}
	 * 
	 * @see TextureOperator#draw(Graphics2D, int, int, int, int, double);
	 */
	@Override
	public void draw(Graphics2D graphics, int x, int y, int width, int height, double angle) {
		
		Objects.requireNonNull(graphics);
		
		/**
		 * Compute and Check Drawing Area
		 */
		int maxHeight = texture.getHeight() - height; 
		int maxWidth = texture.getWidth() - width;
		
		if(maxHeight < 0) {
			maxHeight = 0;
		}
		
		if(maxWidth < 0) {
			maxWidth = 0;
		}
		
		/**
		 * Compute Texture Width Area
		 */
		int srcX;
		int srcWidth;
		
		if(isReversed()) {
			srcWidth = texture.getWidth() - ((int) percentX * maxWidth);
			srcX = srcWidth - width;
		} else {
			srcX = (int) (percentX * maxWidth);
			srcWidth = srcX + width;
		}
		
		/**
		 * Check Width boundary.
		 */
		srcWidth = checkMaximumBoundary(srcWidth, texture.getWidth());
		srcX = checkMinimumBoundary(srcX);
		
		/**
		 * Compute Texture Height Area
		 */
		int srcY;
		int srcHeight;
		
		if(isReversed()) {
			srcHeight = texture.getHeight() - (int)(percentY * maxHeight);
			srcY = srcHeight - height;
		} else {
			srcY = (int) (percentY * maxHeight);
			srcHeight = srcY + height;
		}
		
		/**
		 * Check Height boundary.
		 */
		srcHeight = checkMaximumBoundary(srcHeight, texture.getHeight());
		srcY = checkMinimumBoundary(srcY);
		
		/**
		 * Draw the Texture.
		 */
		getTexture().draw(graphics, x, y, width, height, srcX, srcY, srcWidth, srcHeight);
	}
	
	/**
	 * Define the percent of Scrolling in the Texture for X-Axis 
	 * 
	 * @param percent Percent of Scroll in X-Axis
	 */
	public void setXPercent(float percent) {
		
		if(percent < 0f || percent > 1.0f) {
			throw new IllegalArgumentException("Percent must be between 0 and 1 included");
		}
		
		this.percentX = percent;
	}
	
	/**
	 * Define the percent of Scrolling in the Texture for Y-Axis
	 * 
	 * @param percent Percent of Scroll in Y-Axis
	 */
	public void setYPercent(float percent) {
		
		if(percent < 0f || percent > 1.0f) {
			throw new IllegalArgumentException("Percent must be between 0 and 1 included");
		}

		this.percentY = percent;
	}
	
	/**
	 * Get the percent of scrolling defined for X-Axis.
	 * 
	 * @return Percent of scrolling defined for X-Axis
	 */
	public float getYPercent() {
		return this.percentY;
	}
	
	/**
	 * Get the percent of scrolling defined for Y-Axis.
	 * 
	 * @return Percent of scrolling defined for Y-Axis
	 */
	public float getXPercent() {
		return this.percentX;
	}

	/**
	 * Return the Texture.
	 * 
	 * @return Texture used for Rendering
	 */
	protected Texture getTexture() {
		return texture;
	}
	
	/**
	 * Check and return the minimum X or Y axis accepted. 
	 * 
	 * @param src Axis
	 * @return Suggested Axis
	 */
	protected static int checkMinimumBoundary(int src) {
		return (src < 0)?0:src;
	}
	
	/**
	 * Check and return the maximum X or Y axis accepted.
	 * 
	 * @param src Axis
	 * @param max Maximum Axis
	 * @return Suggested Axis
	 */
	protected static int checkMaximumBoundary(int src, int max) {
		return (src > max)?max:src;
	}
	
	/**
	 * Is Scrolling Texture reversed ?
	 * 
	 * @return True if the {@link ScrollingTexture} is reversed.
	 */
	protected boolean isReversed() {
		return reverse;
	}
	
}
