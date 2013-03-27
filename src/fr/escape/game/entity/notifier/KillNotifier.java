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

import fr.escape.game.entity.Entity;

/**
 * <p>
 * A Component used to remove a {@link Entity} in the World when this {@link Entity} is
 * no longer Alive.
 * 
 */
public interface KillNotifier {
	
	/**
	 * Notify the Component that we need to remove this {@link Entity}
	 * 
	 * @param e Entity
	 * @return True if removing is successful
	 */
	public boolean destroy(Entity e);
	
}
