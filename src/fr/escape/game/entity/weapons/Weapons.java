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

import java.util.ArrayList;
import java.util.List;

import fr.escape.Objects;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.weapons.shot.Shot;
import fr.escape.game.entity.weapons.shot.ShotFactory;
import fr.escape.graphics.Texture;
import fr.escape.resources.Resources;
import fr.escape.resources.TextureLoader;

/**
 * Sort of factory that will create a List of Weapon use by {@link Ship}
 */
public class Weapons {

	public static final int MISSILE_ID = 0;
	public static final int FIREBALL_ID = MISSILE_ID + 1;
	public static final int SHIBOLEET_ID = FIREBALL_ID + 1;
	public static final int BLACKHOLE_ID = SHIBOLEET_ID + 1;
	
	private static final int WIDTH = 40;
	private static final int HEIGHT = 40;
	
	private static final int MISSILE_DEFAULT_AMMUNITION = 100;
	private static final int FIREBALL_DEFAULT_AMMUNITION = 50;
	private static final int SHIBOLEET_DEFAULT_AMMUNITION = 10;
	private static final int BLACKHOLE_DEFAULT_AMMUNITION = 4;
	
	private Weapons() {}
	
	/**
	 * Create a list of {@link Weapon} for the Player
	 * 
	 * @param entityContainer : The {@link EntityContainer} that will contain the {@link Weapon} {@link Shot}.
	 * @param shotFactory : The {@link ShotFactory} that we will be use by each {@link Weapon}.
	 * @return
	 */
	public static List<Weapon> createListOfWeapons(Resources resources, EntityContainer entityContainer, ShotFactory shotFactory) {
		Objects.requireNonNull(entityContainer);
		Objects.requireNonNull(shotFactory);
		
		List<Weapon> list = new ArrayList<Weapon>(4);
		
		Weapon wB = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_BLACKHOLE), 
				entityContainer, shotFactory, BLACKHOLE_DEFAULT_AMMUNITION) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createBlackholeShot(x, y);
			}

			@Override
			public boolean isAutoLoading() {
				return false;
			}
			
		};
		Weapons.validate(wB);
		
		Weapon wF = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_FIREBALL), 
				entityContainer, shotFactory, FIREBALL_DEFAULT_AMMUNITION) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createFireBallShot(x, y);
			}
			
			@Override
			public boolean isAutoLoading() {
				return true;
			}
			
		};
		Weapons.validate(wF);
		
		Weapon wS = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_SHIBOLEET), 
				entityContainer, shotFactory, SHIBOLEET_DEFAULT_AMMUNITION) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createShiboleetShot(x, y, false);
			}
			
			@Override
			public boolean isAutoLoading() {
				return true;
			}
			
		};
		Weapons.validate(wS);
		
		Weapon wM = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_MISSILE), 
				entityContainer, shotFactory, MISSILE_DEFAULT_AMMUNITION) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createMissileShot(x, y);
			}
			
			@Override
			public boolean isAutoLoading() {
				return false;
			}
			
		};
		Weapons.validate(wM);
		
		list.add(wM);
		list.add(wF);
		list.add(wS);
		list.add(wB);
		
		return list;
	}
	
	/**
	 * Check if the {@link Weapon} is valid.
	 * 
	 * @param w : The {@link Weapon} to check.
	 */
	private static void validate(Weapon w) {
		Objects.requireNonNull(w);
		checkDrawableFormat(w.getDrawable());
	}
	
	/**
	 * Check the {@link Texture} format.
	 * 
	 * @param drawable : The {@link Texture} to check
	 */
	private static void checkDrawableFormat(Texture drawable) {
		
		if(drawable.getWidth() != WIDTH) {
			throw new IllegalArgumentException("Drawable width must be equals to "+WIDTH);
		}
		
		if(drawable.getHeight() != HEIGHT) {
			throw new IllegalArgumentException("Drawable height must be equals to "+HEIGHT);
		}
	}

	/**
	 * Get drawable width.
	 * 
	 * @return Return the drawable width.
	 */
	public static int getDrawableWidth() {
		return WIDTH;
	}
	
	/**
	 * Get drawable height.
	 * 
	 * @return Return the drawable height.
	 */
	public static int getDrawableHeight() {
		return HEIGHT;
	}

	/**
	 * Create a list of {@link Weapon} with unlimited ammunition.
	 * 
	 * @param entityContainer : The {@link EntityContainer} that will contain the {@link Weapon} {@link Shot}.
	 * @param shotFactory : The {@link ShotFactory} that we will be use by each {@link Weapon}.
	 * @return
	 */
	public static List<Weapon> createListOfUnlimitedWeapons(Resources resources, EntityContainer entityContainer, ShotFactory shotFactory) {
		Objects.requireNonNull(entityContainer);
		Objects.requireNonNull(shotFactory);

		List<Weapon> list = new ArrayList<Weapon>(3);
		
		Weapon wF = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_FIREBALL), 
				entityContainer, shotFactory, 1) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createFireBallShot(x, y);
			}
			
			@Override
			public int getAmmunition() {
				return 1;
			}

			@Override
			public boolean isAutoLoading() {
				return false;
			}
			
		};
		Weapons.validate(wF);
		
		Weapon wS = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_SHIBOLEET), 
				entityContainer, shotFactory, 1) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createShiboleetShot(x, y, false);
			}
			
			@Override
			public int getAmmunition() {
				return 1;
			}
			
			@Override
			public boolean isAutoLoading() {
				return false;
			}
			
		};
		Weapons.validate(wS);
		
		Weapon wM = new AbstractWeapon(
				resources.getTexture(TextureLoader.WEAPON_MISSILE), 
				entityContainer, shotFactory, 1) {
			
			@Override
			protected Shot createShot(float x, float y) {
				return getFactory().createMissileShot(x, y);
			}
			
			@Override
			public int getAmmunition() {
				return 1;
			}
			
			@Override
			public boolean isAutoLoading() {
				return false;
			}
			
		};
		Weapons.validate(wM);
		
		list.add(wM);
		list.add(wF);
		list.add(wS);
		
		return list;
	}
	
}
