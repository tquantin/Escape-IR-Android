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

package fr.escape.game.entity.ships;

import java.awt.Rectangle;
import java.util.List;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import fr.escape.game.entity.Entity;
import fr.escape.game.entity.weapons.Weapon;
import fr.escape.game.entity.weapons.shot.Shot;

/**
 * The {@link Ship} Interface define the needed operation for a {@link Ship} which can move and fire {@link Shot}.
 */
public interface Ship extends Entity {
	
	/**
	 * Get all the {@link Weapon} initialized for a {@link Ship}.
	 * 
	 * @return {@link List} of {@link Weapon} used by a {@link Ship}.
	 */
	public List<Weapon> getAllWeapons();
	
	/**
	 * Method that set the current activate {@link Weapon}.
	 * 
	 * @param which : The ID of the {@link Weapon} which will be the new active {@link Weapon}.
	 */
	public void setActiveWeapon(int which);
	
	/**
	 * Get the active {@link Weapon}
	 * 
	 * @return The {@link Weapon} currently used by the {@link Ship}.
	 */
	public Weapon getActiveWeapon();
	
	/**
	 * Get the {@link BodyDef}.
	 * 
	 * @return The initial JBox2D {@link BodyDef} that set the JBox2D {@link Body} of a {@link Ship}.
	 */
	public BodyDef getBodyDef();
	
	/**
	 * Get body coordinate on X axis.
	 * 
	 * @return The coordinate on X axis in meters.
	 */
	public float getX();
	
	/**
	 * Get body coordinate on Y axis.
	 * 
	 * @return The coordinate on Y axis in meters.
	 */
	public float getY();
	
	/**
	 * Check if this {@link Ship} belong to the Player.
	 * 
	 * @return Return true if the current {@link Ship} is the player {@link Ship}, false otherwise.
	 */
	public boolean isPlayer();
	
	/**
	 * Check if a {@link Shot} is loaded in the active {@link Weapon}
	 * 
	 * @return Return true if the active {@link Weapon} has loaded her {@link Shot} and is ready to fire, flase otherwise.
	 */
	public boolean isWeaponLoaded();
	
	/**
	 * Method that create the {@link Ship} {@link Body} in the JBox2D {@link World}.
	 * 
	 * @param world : The {@link World} in which the {@link Ship} will be add.
	 */
	public void createBody(World world);
	
	/**
	 * Load a {@link Shot} in the activate {@link Weapon}.
	 * 
	 * @return Return true if the {@link Weapon} is successfully loaded, false otherwise.
	 */
	public boolean loadWeapon();
	
	/**
	 * Method use to reload ammunitions of a {@link Weapon}.
	 * 
	 * @param which : The ID of the {@link Weapon} concerned by the reload.
	 * @param number : Quantity of ammunitions to add.
	 * @return Return true if the reload have been done successfully, false otherwise.
	 */
	public boolean reloadWeapon(int which, int number);
	
	/**
	 * Method to fire a {@link Shot} previously loaded.
	 * 
	 * @return Return true if the {@link Weapon} has successfully fired, false otherwise.
	 */
	public boolean fireWeapon();
	
	/**
	 * Method to fire a {@link Shot} previously loaded.
	 * 
	 * @param velocity : Array which contains the linear velocity parameters to launch a {@link Shot}.
	 * @return Return true if the {@link Weapon} has successfully fired, false otherwise.
	 */
	public boolean fireWeapon(float[] velocity);

	/**
	 * Get the {@link Body} edge.
	 * 
	 * @return A {@link Rectangle} in which the JBox2D {@link Body} is contained.
	 */
	public Rectangle getEdge();
	
	/**
	 * Reset a {@link Ship} at is initial state (position, {@link Weapon}, life).
	 * 
	 * @param x : Position in meters on X axis.
	 * @param y : Position in meters on Y axis.
	 * @return Return true if the {@link Ship} was successfully reset, false otherwise.
	 */
	public boolean reset(float x, float y);
	
	/**
	 * Method use to reduce the life after taking damage.
	 * 
	 * @param value : Damage taken by the {@link Ship}.
	 */
	public boolean damage(int value);
	
	/**
	 * Get current life.
	 * 
	 * @return Current life of the {@link Ship}
	 */
	public int getCurrentLife();
	
	/**
	 * Get initial life.
	 * 
	 * @return Initial life of the {@link Ship}
	 */
	public int getInitialLife();
	
}
