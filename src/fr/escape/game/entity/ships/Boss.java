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

/**
 * <p>
 * An interface for {@link Ship} who are the Boss at the end of the Stage.
 *
 */
public interface Boss extends Ship {
	
	/**
	 * Perform a Special Action/Fire.
	 */
	public void special();
	
	/**
	 * Called after performing {@link Boss#special()}.
	 */
	public boolean normal();
	
	/**
	 * Perform a Movement Action
	 */
	public void move();
	
	/**
	 * Perform a Simple Fire
	 */
	public void fire();
	
}
