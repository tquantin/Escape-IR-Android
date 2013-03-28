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

import org.jbox2d.dynamics.World;

import fr.escape.game.entity.Entity;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.resources.scenario.ScenarioLoader;

/**
 * This class implements the {@link Earth} {@link Stage}.
 * 
 * @see AbstractStage
 */
public final class Earth extends AbstractStage {
	/**
	 * {@link Earth} constructor
	 * 
	 * @param world : The JBox2d {@link World}.
	 * @param container : The {@link EntityContainer} that will contains all the {@link Entity} of this {@link Stage}.
	 * @param factory : The {@link ShipFactory} use by the {@link Stage} to create the {@link Ship}.
	 */
	public Earth(World world, EntityContainer container, ShipFactory factory) {
		super(world, container, ScenarioLoader.EARTH_1, factory, 52); 
	}

}
