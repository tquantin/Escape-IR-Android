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

/**
 * <p>
 * A interface which represent a Stage in Game
 * 
 */
public interface Stage {

	/**
	 * Start the current Stage
	 */
	public void start();
	
	/**
	 * Update the Stage by at the given time (in seconds)
	 * 
	 * @param time Time in seconds.
	 */
	public void update(int time);
	
	/**
	 * Reset the Stage
	 */
	public void reset();
	
	/**
	 * Spawn the Boss of the Stage
	 */
	public void spawn();
	
	/**
	 * Know if the Stage has finished
	 * 
	 * @return True if the Stage has finished
	 */
	public boolean hasFinished();
	
	/**
	 * Return the estimated time for executing all {@link Scenario} of this {@link Stage}
	 * 
	 * @return Estimated time for {@link Scenario}
	 */
	public long getEstimatedScenarioTime();
	
}
