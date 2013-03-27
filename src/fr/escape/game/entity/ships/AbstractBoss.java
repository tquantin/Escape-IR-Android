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

package fr.escape.game.entity.ships;

import java.util.List;
import java.util.Objects;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import fr.escape.app.Foundation;
import fr.escape.app.Graphics;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.weapons.Weapon;
import fr.escape.graphics.AnimationTexture;
import fr.escape.graphics.Texture;

/**
 * This class provide a skeletal implementation of any {@link Boss} in the game.
 */
public abstract class AbstractBoss extends AbstractShip implements Boss {
	
	private boolean moveToRight;
	private long timer;
	private int actionCount;
	
	final EntityContainer container;
	private final AnimationTexture bossTexture;
	
	/**
	 * AbstractBoss Constructor using {@link AbstractShip} constructor
	 * 
	 * @param bodyDef : The body definition for the JBox2D Object.
	 * @param fixture : Fixture linked to the JBox2D Body.
	 * @param weapons : List of {@link Weapon} usable by an {@link AbstractShip}.
	 * @param life : Initial life of the {@link AbstractShip}.
	 * @param container : {@link EntityContainer} in witch the {@link AbstractShip} is contained.
	 * @param textures : {@link Texture} use for the {@link AbstractShip}.
	 * @param collisionBehavior : Behavior used by this {@link AbstractShip} to manage JBox2D Collisions.
	 */
	public AbstractBoss(BodyDef bodyDef, FixtureDef fixture, List<Weapon> weapons, 
			int life, EntityContainer container, AnimationTexture textures,
			CollisionBehavior collisionBehavior) {
		
		super(bodyDef, fixture, weapons, life, container, textures, collisionBehavior);
		
		moveToRight = false;
		timer = 0;
		actionCount = 1;
		
		this.container = container;
		this.bossTexture = textures;
	}
	
	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);

		timer += delta;
		
		if(getY() < 1.0f) bossTexture.rewind();
		
		// Handle Move
		move();
		moveShot(getX(), getY());
		
		// Do we need to trigger Special Action ?
		if(timer >= getSpecialWaitingTime()) {
			special();
			resetTimer();
			resetActionCount();
		}
		
		// Do we need to trigger Fire Action / Switch to Normal ?
		if((timer / actionCount) >= getFireWaitingTime()) {
			fire();
		}
		
		draw(graphics);
	}

	@Override
	public void toDestroy() {
		Foundation.ACTIVITY.post(new Runnable() {

			@Override
			public void run() {
				getEntityContainer().destroy(AbstractBoss.this);
			}
			
		});
	}
	
	@Override
	public boolean isPlayer() {
		return false;
	}
	
	/**
	 * Get Timer Value
	 *  
	 * @return Timer Value
	 */
	protected long getTimer() {
		return timer;
	}
	
	/**
	 * Reset the Timer
	 */
	protected void resetTimer() {
		timer = 0;
	}

	/**
	 * Get Action Count Value
	 * 
	 * @return Action Count
	 */
	protected int getActionCount() {
		return actionCount;
	}
	
	/**
	 * Reset Action Count
	 */
	protected void resetActionCount() {
		actionCount = 1;
	}
	
	/**
	 * Increment Action Count by one
	 */
	protected void incActionCount() {
		actionCount++;
	}
	
	/**
	 * Get Waiting time on each fire
	 * 
	 * @return Waiting time
	 */
	public abstract int getFireWaitingTime();
	
	/**
	 * Get Waiting time on each special
	 * 
	 * @return Waiting time
	 */
	public abstract int getSpecialWaitingTime();
	
	/**
	 * Move the Shot along with the {@link Boss}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 */
	public abstract void moveShot(float x, float y);

	@Override
	public void move() {
		
		float x = Math.abs(getBody().getLinearVelocity().x);
		
		if(!moveToRight && x <= 1.0f) {
			
			moveToRight = true;
			moveTo(1.0f, 2.0f);
			
		} else if(x <= 1.0f) {
			
			moveToRight = false;
			moveTo(9.0f, 2.0f);
			normal();
			
		}
		
	}

	/**
	 * Get {@link AnimationTexture} for the {@link Boss}.
	 * 
	 * @return Return the {@link AnimationTexture}.
	 */
	public AnimationTexture getBossTexture() {
		return bossTexture;
	}
	
}
