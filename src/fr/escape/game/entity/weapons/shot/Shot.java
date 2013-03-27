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

package fr.escape.game.entity.weapons.shot;

import fr.escape.game.entity.Entity;
import fr.escape.game.message.Receiver;

/**
 * The {@link Shot} Interface define the needed operation for a {@link Shot}.
 */
public interface Shot extends Receiver, Entity {
	
	/**
	 * @see Shot#receive(int)
	 */
	public final static int MESSAGE_LOAD = 0;
	public final static int MESSAGE_FIRE = 1;
	public final static int MESSAGE_CRUISE = 2;
	public final static int MESSAGE_HIT = 3;
	public final static int MESSAGE_DESTROY = 4;
	
	/**
	 * <p>
	 * Shot have different state depending on the situation.
	 * 
	 * <p>
	 * If you need to change its state, use this method with the given protocol:
	 * 
	 * <ul>
	 * <li>MESSAGE_LOADED: Shot loaded in Ship.</li>
	 * <li>MESSAGE_FIRE: Shot just shoot from Ship.</li>
	 * <li>MESSAGE_CRUISE: Shot in cruise state.</li>
	 * <li>MESSAGE_HIT: Shot hit something.</li>
	 * <li>MESSAGE_DESTROY: Shot need to be destroyed.</li>
	 * </ul>
	 * 
	 * <b>By default:</b> MESSAGE_LOADED.
	 * 
	 * @see Receiver#receive(int)
	 */
	@Override
	public void receive(int message);
	
	/**
	 * Get the damage done by a {@link Shot}.
	 * 
	 * @return Return the damage value.
	 */
	public int getDamage();
	
	/**
	 * Set a new configuration to the {@link Shot}.
	 * 
	 * @param configuration : The new {@link ShotContext} that defined the {@link Shot} configuration.
	 * @return Return true if the setting has been successful, false otherwise.
	 */
	public boolean setShotConfiguration(ShotContext configuration);
	
	/**
	 * Set a position to the (x,y) coordinates.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 */
	public void setPosition(float x, float y);
	
	/**
	 * This intern class define the configuration for a {@link Shot}.
	 */
	public static final class ShotContext {
		
		private final boolean player;
		private final int width;
		private final int height;
		
		/**
		 * {@link ShotContext} constructor
		 * 
		 * @param player : true if the {@link Shot} belong to the Player.
		 * @param width : The {@link Shot} width.
		 * @param height : The {@link Shot} height.
		 */
		public ShotContext(boolean player, int width, int height) {
			this.player = player;
			this.width = width;
			this.height = height;
		}
		
		/**
		 * Check if the {@link Shot} belong to the Player.
		 * 
		 * @return Return true if the {@link Shot} was launched by the Player, false otherwise.
		 */
		public boolean isPlayer() {
			return player;
		}

		/**
		 * Get the {@link Shot} width.
		 * 
		 * @return Return the {@link Shot} width.
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * Get the {@link Shot} height. 
		 * 
		 * @return Return the {@link Shot} height.
		 */
		public int getHeight() {
			return height;
		}
		
	}

	/**
	 * Set a {@link Shot} untouchable by anyone.
	 */
	public void setUntouchable();
}
