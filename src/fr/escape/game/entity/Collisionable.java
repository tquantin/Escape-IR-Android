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
 * An interface for {@link Entity} which can collide 
 * which another {@link Entity}. 
 * 
 * <p>
 * Call by {@link CollisionDetector}.
 */
public interface Collisionable {
	
	/**
	 * Collisionable Type.
	 */
	public static final int BONUS_TYPE = 0x0010;
	public static final int PLAYER_TYPE = 0x0002;
	public static final int SHOT_TYPE = 0x0008;
	public static final int NPC_TYPE = 0x0004;
	public static final int WALL_TYPE = 0x0020;
	
	/**
	 * A collision between {@link Entity} has been detected and
	 * need to be handle.
	 * 
	 * @param user {@link User} User for Highscore and Life.
	 * @param whoami {@link Entity} type.
	 * @param e {@link Entity} which collide.
	 * @param whois {@link Entity} type which collide.
	 */
	public void collision(User user, Entity e, int whois);
	
}
