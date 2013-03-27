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

package fr.escape.game.entity.bonus;

import java.awt.Rectangle;
import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import fr.escape.app.Foundation;
import fr.escape.app.Graphics;
import fr.escape.game.User;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.notifier.EdgeNotifier;
import fr.escape.game.entity.notifier.KillNotifier;
import fr.escape.graphics.Texture;

/**
 * <p>
 * An abstract class for {@link Bonus}.
 * 
 */
abstract class AbstractBonus implements Bonus {
	
	private static int COEFFICIENT = 3;
	
	private final Texture drawable;
	private final EdgeNotifier eNotifier;
	private final KillNotifier kNotifier;
	private final CollisionBehavior collisionBehavior;
	
	private Body body;
	
	/**
	 * Default Constructor
	 * 
	 * @param body {@link Body} for this Entity
	 * @param drawable Texture used for drawing
	 * @param eNotifier EdgeNotifier
	 * @param kNotifier KillNotifier
	 * @param collisionBehavior 
	 */
	public AbstractBonus(Body body,Texture drawable, EdgeNotifier eNotifier, KillNotifier kNotifier, CollisionBehavior collisionBehavior) {
		
		this.body = Objects.requireNonNull(body);
		this.drawable = Objects.requireNonNull(drawable);
		this.eNotifier = Objects.requireNonNull(eNotifier);
		this.kNotifier = Objects.requireNonNull(kNotifier);
		this.collisionBehavior = Objects.requireNonNull(collisionBehavior);
		
	}

	@Override
	public void rotateBy(int angle) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void moveBy(float[] velocity) {
		if(body.isActive()) {
			body.setLinearVelocity(new Vec2(velocity[1], velocity[2]));
		}
	}
	
	@Override
	public void moveTo(float x, float y) {
		
		float distanceX = x - getX();
		float distanceY = y - getY();
		
		float max = Math.max(Math.abs(distanceX), Math.abs(distanceY));
		float coeff = COEFFICIENT / max;
		
		body.setLinearVelocity(new Vec2(distanceX * coeff, distanceY * coeff));
		
	}
	
	@Override
	public void setRotation(int angle) {
		throw new UnsupportedOperationException();
	}
	
	public Rectangle getEdge() {
		
		int x = CoordinateConverter.toPixelX(getX());
		int y = CoordinateConverter.toPixelY(getY());
		
		return new Rectangle(x - (drawable.getWidth() / 2), y - (drawable.getHeight() / 2), drawable.getWidth(), drawable.getHeight());
	}
	
	/**
	 * Get {@link Body} coordinate on X axis in meters.
	 * 
	 * @return Return the X coordinate.
	 */
	private float getX() {
		return body.getPosition().x;
	}

	/**
	 * Get {@link Body} coordinate on Y axis in meters.
	 * 
	 * @return Return the Y coordinate.
	 */
	private float getY() {
		return body.getPosition().y;
	}

	@Override
	public void draw(Graphics graphics) {
		
		int x = CoordinateConverter.toPixelX(body.getPosition().x) - (drawable.getWidth() / 2);
		int y = CoordinateConverter.toPixelY(body.getPosition().y) - (drawable.getHeight() / 2);
		
		graphics.draw(drawable, x, y);
	}
	
	@Override
	public void update(Graphics graphics, long delta) {
		draw(graphics);
		if(!eNotifier.isInside(getEdge())) {
			eNotifier.edgeReached(this);
		}
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public void toDestroy() {
		kNotifier.destroy(this);
	}
	
	@Override
	public void collision(final User user, final Entity e, final int whois) {
		Foundation.ACTIVITY.post(new Runnable() {
			
			@Override
			public void run() {
				getCollisionBehavior().applyCollision(user, AbstractBonus.this, e, whois);
			}
			
		});	
	}
	
	/**
	 * Get {@link CollisionBehavior}
	 * 
	 * @return CollisionBehavior
	 */
	CollisionBehavior getCollisionBehavior() {
		return collisionBehavior;
	}
}
