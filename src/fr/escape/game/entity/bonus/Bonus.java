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

package fr.escape.game.entity.bonus;

import fr.escape.game.entity.Entity;

/**
 * <p>
 * This Entity represent a Bonus, which add Ammunition to the User.
 * 
 */
public interface Bonus extends Entity {
	
	/**
	 * Get Which Weapon
	 * 
	 * @return Weapon
	 */
	public int getWeapon();
	
	/**
	 * Get Number of Ammunition
	 * 
	 * @return Ammunition
	 */
	public int getNumber();
	
}
