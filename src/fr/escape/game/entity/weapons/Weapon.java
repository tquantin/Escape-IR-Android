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

package fr.escape.game.entity.weapons;

import fr.escape.app.Graphics;
import fr.escape.game.entity.Updateable;
import fr.escape.game.entity.weapons.shot.Shot;
import fr.escape.game.entity.weapons.shot.Shot.ShotContext;
import fr.escape.graphics.Texture;

/**
 * The {@link Weapon} Interface define the needed operation for a {@link Weapon}.
 */
public interface Weapon extends Updateable {	
	
	/**
	 * Get the {@link Texture}.
	 * 
	 * @return Return the {@link Texture}.
	 */
	public Texture getDrawable();
	
	/**
	 * Get {@link Weapon} ammunitions.
	 * 
	 * @return Return the ammunitions of a {@link Weapon}.
	 */
	public int getAmmunition();
	
	/**
	 * Check if their is ammunitions left.
	 * 
	 * @return Return true if the ammunition value is at 0, false otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Get the {@link Weapon} {@link Shot}.
	 * 
	 * @return Return the {@link Weapon} {@link Shot}.
	 */
	public Shot getShot();
	
	/**
	 * Load the {@link Shot} in the {@link Weapon}
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @param context : The {@link ShotContext} use to initialize the {@link Shot}.
	 * @return Return true if the {@link Shot} was successfully loaded, false otherwise.
	 */
	public boolean load(float x, float y, ShotContext context);
	
	/**
	 * Reload the {@link Weapon}.
	 * 
	 * @param number : Number of ammunitions to add.
	 * @return Return true if the reloading was successful, false otherwise.
	 */
	public boolean reload(int number);
	
	/**
	 * Unload the {@link Shot} in the {@link Weapon}.
	 * 
	 * @return Return true if the unloading was a success, false otherwise.
	 */
	public boolean unload();
	
	/**
	 * Fire a {@link Shot}.
	 * 
	 * @param velocity : The {@link Shot} velocity.
	 * @param context : The {@link ShotContext} use by the {@link Shot} launched.
	 * @return Return true if the {@link Shot} was launched, false otherwise.
	 */
	public boolean fire(float[] velocity, ShotContext context);

	/**
	 * Call {@link Updateable#update(Graphics, long)} on the loaded Shot if any
	 * 
	 * @see Updateable#update(Graphics, long)
	 */
	@Override
	public void update(Graphics graphics, long delta);

	/**
	 * Reset a {@link Weapon} to is initial values.
	 * 
	 * @return Return true if the reset was done successfully, false otherwise.
	 */
	public boolean reset();
	
}
