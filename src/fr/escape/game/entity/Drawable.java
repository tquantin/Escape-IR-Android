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

package fr.escape.game.entity;

import fr.escape.app.Graphics;

/**
 * <p>
 * An interface for {@link Entity} which can draw/render themselves without external help.
 * 
 * <p>
 * Kind of Flag for {@link Entity}
 * 
 * @see Graphics
 */
public interface Drawable {
	
	/**
	 * Render the Entity.
	 * 
	 * @param graphics Graphics used for rendering.
	 */
	public void draw(Graphics graphics);
	
}
