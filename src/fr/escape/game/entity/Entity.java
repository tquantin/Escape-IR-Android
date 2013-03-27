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

import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.Body;


/**
 * <p>
 * An Entity is a Components in the {@link World}.
 * 
 * <p>
 * <b>Note:</b> This is an abstract concept.
 * 
 */
public interface Entity extends Updateable, Collisionable, Drawable, Moveable {
	
	/**
	 * Get {@link Entity} JBox2D {@link Body}.
	 * 
	 * @return Return JBox2D {@link Body}
	 */
	public Body getBody();
	
	/**
	 * Set the JBox2D {@link Body}
	 * 
	 * @param body : The new {@link Body} value.
	 */
	public void setBody(Body body);
	
	/**
	 * Send to the {@link EntityContainer} a request to destroy this {@link Entity}.
	 */
	public void toDestroy();
	
}
