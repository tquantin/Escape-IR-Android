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
 * A {@link TextureOperator} which handle repeatable scroll features for a Texture.
 * 
 * <p>
 * For scrolling, use {@link ScrollingTexture#setXPercent(float)} or {@link ScrollingTexture#setYPercent(float)}
 * for X or Y axis. Percent must be between 0 and 1. If you want to progress by 1%, call these with lastPercent+0.01.
 * 
 * @see TextureOperator
 * @see ScrollingTexture
 */
public final class RepeatableScrollingTexture extends ScrollingTexture {

	/**
	 * A Repeatable ScrollingTexture with a given Texture.
	 * 
	 * @see ScrollingTexture#ScrollingTexture(Texture)
	 * @param texture Texture to use.
	 */
	public RepeatableScrollingTexture(Texture texture) {
		super(texture);
	}
	
	/**
	 * A Repeatable ScrollingTexture with a given Texture.
	 * 
	 * @param texture Texture to use.
	 * @param reverse Apply a reverse scrolling.
	 */
	public RepeatableScrollingTexture(Texture texture, boolean reverse) {
		super(texture, reverse);
	}
	
	/**
	 * Draw the RepeatableScrollingTexture in 4 part :
	 * 
	 * +-------+--------+
	 * |   1   |    2   |
	 * +-------+--------+
	 * |   3   |    4   |
	 * +-------+--------+
	 * 
	 * Part 1 Normal Texture
	 * Part 2 Beginning of the Texture in X-axis
	 * Part 3 Beginning of the Texture in Y-axis 
	 * Part 4 The leftover.
	 * 
	 * @see TextureOperator#draw(Graphics2D, int, int, int, int, double);
	 */
	@Override
	public void draw(Graphics2D graphics, int x, int y, int width, int height, double angle) {
	
		Objects.requireNonNull(graphics);
		
		boolean repeatX = true;
		boolean repeatY = true;
		
		/**
		 * Check if we can draw safely without scaling.
		 */
		if(width > getTexture().getWidth()) {
			repeatX = false;
		}
		if(height > getTexture().getHeight()) {
			repeatY = false;
		}
		
		/**
		 * Check if we can repeat at least one axis.
		 * 
		 * Otherwise, use ScrollingTexture.
		 */
		if(repeatX || repeatY) {
			
			/**
			 * Compute Texture Width Area
			 */
			int srcX = 0;
			if(repeatX) {
				if(isReversed()) {
					srcX = (int) ((1.0f - getXPercent()) * getTexture().getWidth());
					srcX %= getTexture().getWidth(); 
				} else {
					srcX = (int) (getXPercent() * getTexture().getWidth());
				}
			}
			
			int srcWidth = srcX + width;
			
			/**
			 * Check Width boundary.
			 */
			srcWidth = checkMaximumBoundary(srcWidth, getTexture().getWidth());
			srcX = checkMinimumBoundary(srcX);
			
			/**
			 * Compute Texture Height Area
			 */
			int srcY = 0;
			if(repeatY) {
				if(isReversed()) {
					srcY = (int) ((1.0f - getYPercent()) * getTexture().getHeight());
					srcY %= getTexture().getHeight();
				} else {
					srcY = (int) (getYPercent() * getTexture().getHeight());
				}
			}
			
			int srcHeight = srcY + height;
			
			/**
			 * Check Height boundary.
			 */
			srcHeight = checkMaximumBoundary(srcHeight, getTexture().getHeight());
			srcY = checkMinimumBoundary(srcY);
			
			/**
			 * Compute what is drawn
			 */
			int deltaHeight = srcHeight - srcY;
			int deltaWidth = srcWidth - srcX;
			
			if(!repeatX) {
				deltaWidth = width;
			}
			if(!repeatY) {
				deltaHeight = height;
			}
			
			/**
			 * Draw the Part 1 Texture.
			 */
			getTexture().draw(graphics, x, y, deltaWidth, deltaHeight, srcX, srcY, srcWidth, srcHeight);
			
			/**
			 * Compute Texture Width Area for Part 2
			 */
			int srcX2 = 0;
			int srcWidth2 = width - deltaWidth;
			
			/**
			 * Draw the Part 2 Texture.
			 */
			if(repeatX) {
				getTexture().draw(graphics, deltaWidth, y, width, deltaHeight, srcX2, srcY, srcWidth2, srcHeight);
			}
			
			
			/**
			 * Compute Texture Height Area for Part 3
			 */
			int srcY3 = 0;
			int srcHeight3 = height - deltaHeight;
			
			/**
			 * Draw the Part 3 Texture.
			 */
			if(repeatY) {
				getTexture().draw(graphics, x, deltaHeight, deltaWidth, height, srcX, srcY3, srcWidth, srcHeight3);
			}
			
			/**
			 * Draw the Final Part, the Part 4 Texture. 
			 */
			if(repeatX && repeatY) {
				getTexture().draw(graphics, deltaWidth, deltaHeight, width, height, srcX2, srcY3, srcWidth2, srcHeight3);
			}
			
		} else {
			super.draw(graphics, x, y, width, height, 0);
		}
		
	}
	
	/**
	 * Tiny Wrapper for RepeatableScrollingTexture :
	 * 100% == 0% ( We reach the end )
	 * 
	 * @see ScrollingTexture#setXPercent(float)
	 */
	@Override
	public void setXPercent(float percent) {
		super.setXPercent(percent % 1.0f);
	}
	
	/**
	 * Tiny Wrapper for RepeatableScrollingTexture :
	 * 100% == 0% ( We reach the end )
	 * 
	 * @see ScrollingTexture#setXPercent(float)
	 */
	@Override
	public void setYPercent(float percent) {
		super.setYPercent(percent % 1.0f);
	}

}
