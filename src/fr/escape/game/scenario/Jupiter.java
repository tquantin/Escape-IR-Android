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

import java.util.Objects;

import org.jbox2d.dynamics.World;

import fr.escape.app.Foundation;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Boss;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.resources.scenario.ScenarioLoader;

/**
 * This class implements the {@link Jupiter} {@link Stage}.
 * 
 * @see AbstractStage
 */
public class Jupiter extends AbstractStage {
	
	private static final float BOSS_SPAWN_X = 5.0f;
	private static final float BOSS_SPAWN_Y = 0.0f;
	
	private final Boss boss;

	/**
	 * {@link Jupiter} constructor
	 * 
	 * @param world : The JBox2d {@link World}.
	 * @param container : The {@link EntityContainer} that will contains all the {@link Entity} of this {@link Stage}.
	 * @param factory : The {@link ShipFactory} use by the {@link Stage} to create the {@link Ship}.
	 */
	public Jupiter(World world, EntityContainer container, ShipFactory factory) {
		
		super(world, container);
		
		boss = Objects.requireNonNull(factory.createJupiterBoss(BOSS_SPAWN_X, BOSS_SPAWN_Y));
	
		add(Foundation.RESOURCES.getScenario(ScenarioLoader.JUPITER_1, factory));
		add(Foundation.RESOURCES.getScenario(ScenarioLoader.JUPITER_2, factory));
		add(Foundation.RESOURCES.getScenario(ScenarioLoader.JUPITER_3, factory));
		add(Foundation.RESOURCES.getScenario(ScenarioLoader.JUPITER_4, factory));
	}
	
	@Override
	public long getEstimatedScenarioTime() {
		return 40;
	}

	@Override
	protected Boss getBoss() {
		return boss;
	}

	@Override
	public void resetBoss() {
		boss.reset(BOSS_SPAWN_X, BOSS_SPAWN_Y);
	}

}
