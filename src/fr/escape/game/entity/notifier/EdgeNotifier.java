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

package fr.escape.game.entity.notifier;

import java.awt.Rectangle;


import fr.escape.game.entity.Entity;

/**
 * <p>
 * A Component used to detect when a {@link Entity} exceed 
 * the World Edge and need to be removed.
 * 
 * <p>
 * For detecting, use the {@link EdgeNotifier#isInside(Rectangle)} method.
 * 
 * <p>
 * For removing, use the {@link EdgeNotifier#edgeReached(Entity)} method.
 */
public interface EdgeNotifier {

	/**
	 * Notify the Component that we need to remove this {@link Entity}
	 * 
	 * @param e Entity
	 * @return True if removing is successful
	 */
	public boolean edgeReached(Entity e);
	
	/**
	 * Check if the {@link Entity} Edge is inside the World Edge.
	 * 
	 * @param edge Entity Edge
	 * @return True if the Entity Edge is inside World Edge
	 */
	public boolean isInside(Rectangle edge);

}
