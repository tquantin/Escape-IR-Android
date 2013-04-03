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

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import android.graphics.Rect;

import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Graphics;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.EntityContainer;
import fr.escape.graphics.Texture;
import fr.escape.resources.TextureLoader;

/**
 * This class implements the {@link ShiboleetShot}.
 * 
 * @see AbstractShot
 */
public final class ShiboleetShot extends AbstractShot {
	
	static final float CHILD_RADIUS = 0.35f;
	
	private final Texture coreShiboleet;
	private final EntityContainer entityContainer;
	private final ShotFactory shotFactory;
	
	private boolean isVisible;
	private boolean isChild;

	/**
	 * {@link ShiboleetShot} constructor.
	 * 
	 * @param body : The {@link Shot} JBox2D {@link Body}.
	 * @param isChild : true if the {@link ShiboleetShot} is a child, false otherwise.
	 * @param container : The {@link EntityContainer} that contains the {@link Shot}.
	 * @param collisionBehavior : The {@link CollisionBehavior} use by the {@link Shot}
	 * @param factory : The {@link ShotFactory}.
	 */
	public ShiboleetShot(Engine engine, Body body, boolean isChild, EntityContainer container, ShotCollisionBehavior collisionBehavior, ShotFactory factory) {
		super(engine, body, container, container, collisionBehavior, 1);

		this.coreShiboleet = engine.getResources().getTexture(TextureLoader.WEAPON_SHIBOLEET_SHOT);
		this.entityContainer = Objects.requireNonNull(container);
		this.shotFactory = Objects.requireNonNull(factory);
		this.isVisible = false;
		this.isChild = isChild;
	}
	
	@Override
	public void setPosition(float x, float y) {
		
		float size = getEngine().getConverter().toMeterY(Math.max(getHeight(), getWidth()));
		getBody().setTransform(new Vec2(x, y + size), getBody().getAngle());
		
	}

	@Override
	public void receive(int message) {
		switch(message) {
			case Shot.MESSAGE_LOAD: {
				isVisible = true;
				break;
			}
			case Shot.MESSAGE_FIRE: {
				explode();
				receive(MESSAGE_DESTROY);
				break;
			}
			case Shot.MESSAGE_CRUISE: {
				isVisible = true;
				break;
			}
			case Shot.MESSAGE_HIT: {
				getBody().setType(BodyType.STATIC);
				getBody().setLinearVelocity(new Vec2(0.0f, 0.0f));
			}
			case Shot.MESSAGE_DESTROY: {
				
				isVisible = false;
				
				getEngine().post(new Runnable() {
					
					@Override
					public void run() {
						toDestroy();
					}
					
				});
	
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown Message: "+message);
			}
		}
	}

	/**
	 * Create the mini-{@link ShiboleetShot} after firing the {@link ShiboleetShot}.
	 */
	private void explode() {
		
		ShiboleetShot s1 = createChild();
		ShiboleetShot s2 = createChild();
		ShiboleetShot s3 = createChild();
		ShiboleetShot s4 = createChild();
		
		s1.setShotConfiguration(new ShotContext(isPlayer(), getWidth(), getHeight()));
		s2.setShotConfiguration(new ShotContext(isPlayer(), getWidth(), getHeight()));
		s3.setShotConfiguration(new ShotContext(isPlayer(), getWidth(), getHeight()));
		s4.setShotConfiguration(new ShotContext(isPlayer(), getWidth(), getHeight()));
		
		s1.moveBy(new float[] {0.0f, 4.0f, (isPlayer())?-5.0f:5.0f});
		s2.moveBy(new float[] {0.0f, 1.25f, (isPlayer())?-5.0f:5.0f});
		s3.moveBy(new float[] {0.0f, -1.25f, (isPlayer())?-5.0f:5.0f});
		s4.moveBy(new float[] {0.0f, -4.0f, (isPlayer())?-5.0f:5.0f});
		
		s1.receive(MESSAGE_CRUISE);
		s2.receive(MESSAGE_CRUISE);
		s3.receive(MESSAGE_CRUISE);
		s4.receive(MESSAGE_CRUISE);
		
		entityContainer.push(s1);
		entityContainer.push(s2);
		entityContainer.push(s3);
		entityContainer.push(s4);
		
	}
	
	/**
	 * Create a smaller {@link ShiboleetShot}.
	 * 
	 * @return Return a small {@link ShiboleetShot}.
	 */
	private ShiboleetShot createChild() {
		return (ShiboleetShot) shotFactory.createShiboleetShot(getX(), getY(), true);
	}

	@Override
	public void draw(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		if(isVisible) {
			
			Rect area = getEdge();
			
			graphics.draw(coreShiboleet, area.left, area.top, area.right, area.bottom, getAngle());
		}
	}
	
	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);
		
		draw(graphics);
		
		if(!getEdgeNotifier().isInside(getEdge())) {
			getEdgeNotifier().edgeReached(this);
		}
	}

	@Override
	protected Rect getEdge() {
		
		int cx = getEngine().getConverter().toPixelX(getX());
		int cy = getEngine().getConverter().toPixelY(getY());
		
		int x;
		int y;
		int width;
		int height;
		
		if(isChild) {
			
			int tmpWidth = (int) (coreShiboleet.getWidth() * CHILD_RADIUS);
			int tmpHeight = (int) (coreShiboleet.getHeight() * CHILD_RADIUS);
			int tmpX = getEngine().getConverter().toPixelX(getBody().getPosition().x);
			int tmpY = getEngine().getConverter().toPixelY(getBody().getPosition().y);
			
			x = tmpX - (tmpWidth / 2);
			y = tmpY - (tmpHeight / 2);
			width = tmpX + (tmpWidth / 2);
			height = tmpY + (tmpHeight / 2);
			
		} else {
			
			x = cx - (coreShiboleet.getWidth() / 2);
			y = cy - (coreShiboleet.getHeight() / 2);
			width = cx + (coreShiboleet.getWidth() / 2);
			height = cy + (coreShiboleet.getHeight() / 2);
		}
		
		return new Rect(x, y, width, height);
	}

}
