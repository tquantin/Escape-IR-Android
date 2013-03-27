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

import java.awt.Rectangle;
import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import fr.escape.app.Foundation;
import fr.escape.game.User;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.notifier.EdgeNotifier;
import fr.escape.game.entity.notifier.KillNotifier;

/**
 * This class provide a skeletal implementation of any {@link Shot} in the game.
 */
public abstract class AbstractShot implements Shot {
	
	private static final int PLAYER_SHOT_MASK = NPC_TYPE;
	private static final int NPC_SHOT_MASK = PLAYER_TYPE;
	
	private final EdgeNotifier eNotifier;
	private final KillNotifier kNotifier;
	
	private CollisionBehavior collisionBehavior;
	
	private Body body;
	
	private int angle;
	private int damage;
	
	private int width;
	private int height;
	private boolean player;
	
	/**
	 * {@link AbstractShot} constructor.
	 * 
	 * @param body : The {@link Body} for the new {@link Shot}.
	 * @param edgeNotifier : The {@link EntityContainer} in which the {@link Shot} will be push.
	 * @param killNotifier : The {@link EntityContainer} in which the {@link Shot} will be push.
	 * @param collisionBehavior : {@link CollisionBehavior} that the {@link Shot} will use.
	 * @param defaultDamage : The damage done by the {@link Shot}.
	 */
	public AbstractShot(Body body, EdgeNotifier edgeNotifier, KillNotifier killNotifier, CollisionBehavior collisionBehavior, int defaultDamage) {
		
		this.body = Objects.requireNonNull(body);
		this.eNotifier = Objects.requireNonNull(edgeNotifier);
		this.kNotifier = Objects.requireNonNull(killNotifier);
		this.collisionBehavior = Objects.requireNonNull(collisionBehavior);
		
		this.angle = 0;
		this.damage = defaultDamage;
	}

	@Override
	public void rotateBy(int angle) {
		this.setRotation(this.angle + angle);
	}
	
	@Override
	public void setPosition(float x, float y) {
		getBody().setTransform(new Vec2(x, y), getBody().getAngle());
	}
	
	@Override
	public void moveTo(float x, float y) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void moveBy(float[] velocity) {
		if(body.isActive()) {
			setRotation((int)velocity[0]);
			body.setLinearVelocity(new Vec2(velocity[1], velocity[2]));
		}
	}
	
	@Override
	public void setRotation(int angle) {
		this.angle = angle % 360;
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	@Override
	public void setBody(Body body) {
		this.body = body;
	}
	
	/**
	 * Get {@link Body} coordinate on X axis in meters.
	 * 
	 * @return Return the X coordinate.
	 */
	protected float getX() {
		return getBody().getPosition().x;
	}
	
	/**
	 * Get {@link Body} coordinate on Y axis in meters.
	 * 
	 * @return Return the Y coordinate.
	 */
	protected float getY() {
		return getBody().getPosition().y;
	}
	
	/**
	 * Get the {@link Shot} rotation angle.
	 * 
	 * @return Return the rotation angle.
	 */
	protected int getAngle() {
		return this.angle;
	}
	
	@Override
	public void toDestroy() {
		kNotifier.destroy(this);		
	}
	
	/**
	 * Get {@link Shot} edge.
	 * 
	 * @return Return the {@link Rectangle} that contains the {@link Shot} {@link Body}.
	 */
	protected abstract Rectangle getEdge();

	/**
	 * Get EdgeNotifier
	 * 
	 * @return EdgeNotifier
	 */
	protected EdgeNotifier getEdgeNotifier() {
		return eNotifier;
	}
	
	@Override
	public int getDamage() {
		return damage;
	}
	
	@Override
	public void collision(final User user, final Entity e, final int whois) {
		Objects.requireNonNull(user);
		Objects.requireNonNull(e);
		
		Foundation.ACTIVITY.post(new Runnable() {
			
			@Override
			public void run() {
				getCollisionBehavior().applyCollision(user, AbstractShot.this, e, whois);
			}
			
		});
	}
	
	/**
	 * Get the {@link Shot} {@link CollisionBehavior}.
	 * 
	 * @return Return the {@link CollisionBehavior} use by the {@link Shot}.
	 */
	CollisionBehavior getCollisionBehavior() {
		return collisionBehavior;
	}
	
	@Override
	public boolean setShotConfiguration(ShotContext configuration) {
		
		int mask = (Objects.requireNonNull(configuration).isPlayer())?PLAYER_SHOT_MASK:NPC_SHOT_MASK;
		Objects.requireNonNull(getBody().getFixtureList()).m_filter.maskBits = mask;
		
		this.width = configuration.getWidth();
		this.height = configuration.getHeight();
		this.player = configuration.isPlayer();
		
		return true;
	}
	
	/**
	 * Get {@link ShotContext} width.
	 * 
	 * @return Return {@link ShotContext} width.
	 */
	int getWidth() {
		return width;
	}

	/**
	 * Get {@link ShotContext} height.
	 * 
	 * @return Return {@link ShotContext} height.
	 */
	int getHeight() {
		return height;
	}
	
	/**
	 * Check if the {@link Shot} belong to the Player.
	 * 
	 * @return Return true if th {@link Shot} was launched by the Player, false otherwise.
	 */
	boolean isPlayer() {
		return player;
	}
	
	@Override
	public void setUntouchable() {
		Objects.requireNonNull(getBody().getFixtureList()).m_filter.maskBits = 0x0001;
	}
	
}
