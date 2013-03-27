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

import fr.escape.game.User;

/**
 * <p>
 * An interface for Behavior when 
 * {@link Collisionable#collision(fr.escape.game.User, int, Entity, int)} happened.
 * 
 * <p>
 * Call by {@link Collisionable}.
 */
public interface CollisionBehavior {

	public static final int HIT_SCORE = 100;
	public static final int HIT_DAMAGE = 1;
	
	/**
	 * Apply a behavior when a collision happened.
	 * 
	 * @param user Current {@link User}.
	 * @param handler {@link Entity} which handle the collision.
	 * @param other {@link Entity} which collide with the handler.
	 * @param whois {@link Entity} type for other.
	 */
	public void applyCollision(User user, Entity handler, Entity other, int type);
	
}
