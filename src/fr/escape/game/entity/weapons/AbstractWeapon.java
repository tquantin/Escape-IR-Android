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

package fr.escape.game.entity.weapons;

import java.util.Objects;

import fr.escape.app.Graphics;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.weapons.shot.Shot;
import fr.escape.game.entity.weapons.shot.ShotFactory;
import fr.escape.game.entity.weapons.shot.Shot.ShotContext;
import fr.escape.graphics.Texture;

/**
 * This class provide a skeletal implementation of any {@link Weapon} in the game.
 */
public abstract class AbstractWeapon implements Weapon {
	
	private static final int MAX_AMMUNITION = 250;
	
	private final Texture drawable;
	private final EntityContainer container;
	private final ShotFactory factory;
	private final int defaultAmmunition;
	
	private int ammunition;
	private Shot shot;
	
	/**
	 * {@link AbstractWeapon} constructor
	 * 
	 * @param texture : The {@link Texture}
	 * @param eContainer : The {@link EntityContainer} that will contains the {@link Weapon} {@link Shot}.
	 * @param sFactory : The {@link ShotFactory} to create the {@link Weapon} {@link Shot}.
	 * @param defaultAmmunition : The default ammunitions for the {@link Weapon}
	 */
	public AbstractWeapon(Texture texture, EntityContainer eContainer, ShotFactory sFactory, int defaultAmmunition) {
		this.drawable = Objects.requireNonNull(texture);
		this.container = Objects.requireNonNull(eContainer);
		this.factory = Objects.requireNonNull(sFactory);
		this.ammunition = defaultAmmunition;
		this.defaultAmmunition = defaultAmmunition;
	}

	@Override
	public Texture getDrawable() {
		return drawable;
	}

	@Override
	public int getAmmunition() {
		return ammunition;
	}
	
	@Override
	public boolean isEmpty() {
		return getAmmunition() <= 0;
	}
	
	/**
	 * Create a new {@link Shot}
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return the created {@link Shot}
	 */
	protected abstract Shot createShot(float x, float y);

	/**
	 * Get the {@link ShotFactory}.
	 * 
	 * @return Return the {@link ShotFactory}.
	 */
	protected ShotFactory getFactory() {
		return factory;
	}
	
	@Override
	public boolean load(float x, float y, ShotContext context) {
		if(!isEmpty() && shot == null) {
			
			shot = Objects.requireNonNull(createShot(x, y));
			shot.setShotConfiguration(Objects.requireNonNull(context));
			shot.setUntouchable();
			shot.receive(Shot.MESSAGE_LOAD);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean reload(int number) {
		
		if(number <= 0) {
			return false;
		}
		
		int ammunition = number + this.ammunition;
		
		if(ammunition > MAX_AMMUNITION) {
			ammunition = MAX_AMMUNITION;
		}
		
		this.ammunition = ammunition;
		
		return true;
	}
	
	@Override
	public boolean unload() {
		if(shot != null) {
			shot.receive(Shot.MESSAGE_DESTROY);
			shot = null;
		}
		return false;
	}
	
	@Override
	public boolean fire(float[] velocity, ShotContext context) {

		if(shot != null) {
			
			shot.moveBy(velocity);
			shot.setShotConfiguration(Objects.requireNonNull(context));
			
			if(!context.isPlayer()) {
				shot.rotateBy(180);
			}
			
			container.push(shot);
			shot.receive(Shot.MESSAGE_FIRE);
			
			shot.receive(Shot.MESSAGE_CRUISE);
			
			shot = null;
			ammunition--;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public Shot getShot() {
		return shot;
	}
	
	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);
		if(shot != null) {
			shot.update(graphics, delta);
		}
	}
	
	@Override
	public boolean reset() {
		ammunition = defaultAmmunition;
		return true;
	}
}
