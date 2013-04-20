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

package fr.escape.resources.scenario;

import fr.escape.Objects;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.game.scenario.Scenario;
import fr.escape.resources.ResourcesLoader;

/**
 * <p>
 * A {@link ResourcesLoader} for {@link Stage} and {@link Scenario}.
 * 
 */
public abstract class ScenarioLoader implements ResourcesLoader<Scenario> {
	
	public static final String JUPITER = "jupiter.scn";
	public static final String MOON = "moon.scn";
	public static final String EARTH = "earth.scn";
	
	private ShipFactory factory;
	
	/**
	 * Add a Ship Factory for this Scenario.
	 * 
	 * @param sf Ship Factory
	 */
	public void addShipCreator(ShipFactory sf) {
		factory = Objects.requireNonNull(sf);
	}
	
	/**
	 * Get the Ship Factory for this Scenario.
	 * 
	 * @return Ship Factory
	 */
	protected ShipFactory getShipCreator() {
		return factory;
	}
	
}