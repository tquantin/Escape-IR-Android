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

package fr.escape.game.scenario;

import fr.escape.game.entity.EntityContainer;

/**
 * <p>
 * A interface which represent a {@link Scenario} in {@link Stage}
 * 
 */
public interface Scenario {
	
	public int getID();
	
	public int getStart();
	
	public boolean setContainer(EntityContainer container);
	
	public boolean hasFinished();
	
	/**
	 * Execute Action in the Scenario for a specific time.
	 * 
	 * @param time Seconds elapsed since beginning of the Game.
	 */
	public void action(int time);
	
	public boolean reset();
	
}
