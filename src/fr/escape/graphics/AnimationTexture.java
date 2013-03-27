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
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * <p>
 * A composite of Texture for drawing a Animation (for example) or a Texture with state.
 * 
 * @see Texture
 */
public final class AnimationTexture implements TextureOperator {

	private final Texture[] textures;
	
	private int index;
	private int width;
	private int height;
	
	private boolean reverse;
	
	/**
	 * Default Constructor
	 * 
	 * @param textures Use these {@link Texture} for this {@link AnimationTexture}
	 */
	public AnimationTexture(Texture... textures) {
		this.textures = Objects.requireNonNull(textures);
		this.index = 0;
		this.width = 0;
		this.height = 0;
		this.reverse = false;
	}
	
	/**
	 * Check if we have Texture for the next iteration.
	 * 
	 * @return True if a Texture is available
	 */
	public boolean hasNext() {
		return index < textures.length | index >= 0;
	}
	
	/**
	 * Rewind to the initial position
	 */
	public void rewind() {
		this.index = 0;
	}
	
	/**
	 * Go Backward in Texture Iteration
	 */
	public void reverse() {
		reverse = true;
	}
	
	/**
	 * Go Forward in Texture Iteration
	 */
	public void forward() {
		reverse = false;
	}
	
	/**
	 * Go to the next Texture
	 */
	public void next() {
		
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
				
		if(reverse) {
			index--;
			if(index < 0) index += textures.length;
		} else {
			index = (index + 1) % textures.length;
		}
		
	}
	
	/**
	 * Get the maximum Width of all {@link Texture}
	 * 
	 * @return Width
	 */
	public int getWidth() {
		
		if(width == 0) {
			for(Texture texture: textures) {
				width = Math.max(width, texture.getWidth());
			}
		}
		
		return width;
	}
	
	/**
	 * Get the maximum Height off all {@link Texture}.
	 * 
	 * @return Height
	 */
	public int getHeight() {
		
		if(height == 0) {
			for(Texture texture: textures) {
				height = Math.max(height, texture.getHeight());
			}
		}
		
		return height;
	}
	
	@Override
	public void draw(Graphics2D graphics, int x, int y, int width, int height, double angle) {
	
		Objects.requireNonNull(graphics);
		
		Texture texture = textures[index];
		texture.draw(graphics, x, y, width, height, 0, 0, texture.getWidth(), texture.getHeight(), angle);
	}

}
