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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import org.jbox2d.dynamics.World;

import fr.escape.app.Foundation;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Boss;
import fr.escape.game.entity.ships.Ship;

/**
 * This class provide a skeletal implementation of any {@link Ship} in the game.
 */
public abstract class AbstractStage implements Stage {

	private static final String TAG = AbstractStage.class.getSimpleName();
	
	private final List<Scenario> active;
	private final List<Scenario> scenarios;
	private final TreeMap<Integer, Scenario> waiting;
	private final EntityContainer container;
	private final World world;
	
	private int lastTime;
	private boolean spawn;
	
	public AbstractStage(World world, EntityContainer container) {
		this.world = Objects.requireNonNull(world);
		this.container = Objects.requireNonNull(container);
		this.active = new LinkedList<>();
		this.scenarios = new LinkedList<>();
		this.waiting = new TreeMap<>();
		this.lastTime = -1;
		this.spawn = false;
	}
	
	/**
	 * Get a List of active Scenario
	 * 
	 * @return Active Scenario
	 */
	private List<Scenario> getActiveScenario() {
		return active;
	}
	
	/**
	 * Add a Scenario on Stage
	 * 
	 * @param scenario Scenario to add on Stage
	 * @return True if successful
	 */
	protected boolean add(Scenario scenario) {
		return this.scenarios.add(Objects.requireNonNull(scenario));
	}
	
	/**
	 * Get All Waiting Scenario
	 * 
	 * @return Waiting Scenario
	 */
	private TreeMap<Integer, Scenario> getWaitingScenario() {
		return waiting;
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
		
		Foundation.ACTIVITY.debug(TAG, "Start Stage");
		
		for(Scenario scenario : scenarios) {
			Foundation.ACTIVITY.debug(TAG, "Add "+scenario+" on Waiting Scenario");
			getWaitingScenario().put(Integer.valueOf(scenario.getStart()), scenario);
		}
	}
	
	@Override
	public void update(int time) {
		if(time != getLastUpdateTime()) {
		
			fetchActivableScenario(time);
			
			Iterator<Scenario> it = getActiveScenario().iterator();
			
			while(it.hasNext()) {
				
				Scenario sc = it.next();
				
				if(sc.hasFinished()) {
					it.remove();
				} else {
					sc.action(time);
				}
			}
			
			if((time >= getEstimatedScenarioTime()) && !spawn) {
				spawn();
			}
			
			setLastUpdateTime(time);
		}
	}
	
	/**
	 * Perform a query on Waiting Scenario for retrieving activable Scenario
	 * for the given Stage Time.
	 * 
	 * @param time Stage Time
	 * @return Scenario
	 */
	private Collection<Scenario> getListOfActivableScenario(int time) {
		return waiting.headMap(Integer.valueOf(time), true).values();
	}
	
	/**
	 * Fetch Waiting Scenario which can be active.
	 * 
	 * @param time Stage Time
	 */
	private void fetchActivableScenario(int time) {

		Iterator<Scenario> it = getListOfActivableScenario(time).iterator();
		
		while(it.hasNext()) {
			Scenario scenario = it.next();
			scenario.setContainer(container);
			active.add(scenario);
			it.remove();
		}
		
	}
	
	/**
	 * <p>
	 * This method is called by {@link AbstractStage#reset()}
	 * 
	 */
	public abstract void resetBoss();
	
	@Override
	public void reset() {
		
		Foundation.ACTIVITY.debug(TAG, "Reset Stage");
		
		spawn = false;
		
		
		
		getActiveScenario().clear();
		getWaitingScenario().clear();
		
		for(Scenario scenario : scenarios) {
			scenario.reset();
		}
		
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
	
	/**
	 * Get the {@link Boss} of the Stage
	 * 
	 * @return Boss of the Stage
	 */
	protected abstract Boss getBoss();
	
}
