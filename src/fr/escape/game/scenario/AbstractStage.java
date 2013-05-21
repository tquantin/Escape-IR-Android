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

import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Boss;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;

/**
 * This class provide a skeletal implementation of any {@link Ship} in the game.
 */
public abstract class AbstractStage implements Stage {
	
	private static final float BOSS_SPAWN_X = 5.0f;
	private static final float BOSS_SPAWN_Y = 0.0f;
	
	private final Boss boss;
	
	private final Scenario scenario;
	
	private final EntityContainer container;
	private final World world;
	
	private final int duration;
	private int lastTime;
	private boolean spawn;
	
	public AbstractStage(Engine engine, World world, EntityContainer container, String scenario, ShipFactory factory, int duration, int bossType, boolean history) {
		this.world = Objects.requireNonNull(world);
		this.container = Objects.requireNonNull(container);
		this.boss = Objects.requireNonNull(factory.createBoss(BOSS_SPAWN_X, BOSS_SPAWN_Y, bossType));
		this.scenario = engine.getResources().getScenario(scenario, factory, history);
		this.duration = duration;
		this.lastTime = -1;
		this.spawn = false;
	}
	
	/**
	 * Get Last Update Time
	 * 
	 * @return Last Update Time
	 */
	private int getLastUpdateTime() {
		return lastTime;
	}
	
	/**
	 * Set Last Update Time
	 * 
	 * @param updateTime Last Update Time
	 */
	private void setLastUpdateTime(int updateTime) {
		lastTime = updateTime;
	}
	
	@Override
	public void start() {
		Engine.log("AbstractStage", "Load Container");
		this.scenario.setContainer(container);
	}
	
	@Override
	public void update(int time) {
		if(time != getLastUpdateTime()) {
			if(!scenario.hasFinished()) {
				scenario.action(time);
			}
			
			if((time >= getEstimatedScenarioTime()) && !spawn) {
				spawn();
			}
			
			setLastUpdateTime(time);
		}
	}
	
	/**
	 * <p>
	 * This method is called by {@link AbstractStage#reset()}
	 * 
	 */
	public void resetBoss() {
		boss.reset(BOSS_SPAWN_X, BOSS_SPAWN_Y);
	}
	
	@Override
	public void reset() {
		spawn = false;
		
		scenario.reset();
	}
	
	@Override
	public boolean hasFinished() {
		
		if(spawn) {
			return !container.contains(getBoss());
		}
		
		return false;
	}
	
	@Override
	public void spawn() {	
		Boss boss = Objects.requireNonNull(getBoss());
		
		boss.createBody(world);
		container.push(boss);
		
		spawn = true;
	}
	
	@Override
	public long getEstimatedScenarioTime() {
		return duration;
	}
	
	/**
	 * Get the {@link Boss} of the Stage
	 * 
	 * @return Boss of the Stage
	 */
	protected Boss getBoss() {
		return boss;
	}
	
}
