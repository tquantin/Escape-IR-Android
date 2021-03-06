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

import org.jbox2d.collision.shapes.PolygonShape;
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
 * This class implements the {@link BlackHoleShot}.
 * 
 * @see AbstractShot
 */
public final class BlackHoleShot extends AbstractShot {

	private static final int ROTATION_SPEED = 600;
	private static final int EVENT_HORIZON_TIME = 5000;
	private static final int EVENT_HORIZON_SPEED = 1000;
	
	private final Texture coreHelix;
	private final Texture leftHelix;
	private final Texture rightHelix;
	private final Texture eventHorizon;
	
	private boolean isVisible;
	private boolean drawCoreHelix;
	private boolean drawLeftAndRightHelix;
	private boolean drawEventHorizon;
	
	private long timer;
	
	/**
	 * {@link BlackHoleShot} constructor.
	 * 
	 * @param body : The {@link Shot} JBox2D {@link Body}.
	 * @param container : The {@link EntityContainer} that contains the {@link Shot}.
	 * @param collisionBehavior : The {@link CollisionBehavior} use by the {@link Shot}
	 */
	public BlackHoleShot(Engine engine, Body body, EntityContainer container, ShotCollisionBehavior collisionBehavior) {
		super(engine, body, container, container, collisionBehavior, 5);
		
		this.isVisible = false;
		this.drawCoreHelix = false;
		this.drawLeftAndRightHelix = false;
		this.drawEventHorizon = false;
		
		this.coreHelix = engine.getResources().getTexture(TextureLoader.WEAPON_BLACKHOLE_CORE_SHOT);
		this.leftHelix = engine.getResources().getTexture(TextureLoader.WEAPON_BLACKHOLE_LEFT_SHOT);
		this.rightHelix = engine.getResources().getTexture(TextureLoader.WEAPON_BLACKHOLE_RIGHT_SHOT);
		this.eventHorizon = engine.getResources().getTexture(TextureLoader.WEAPON_BLACKHOLE_EVENT_HORIZON_SHOT);
		
	}

	@Override
	public void receive(int message) {
		switch(message) { 
			case MESSAGE_LOAD: {
				isVisible = true;
				drawCoreHelix = true;
				setShapeRadius();
				break;
			}
			case MESSAGE_FIRE: {
				drawLeftAndRightHelix = true;
				setShapeRadius();
				break;
			}
			case MESSAGE_CRUISE: {
				break;
			}
			case MESSAGE_HIT: {
				if(!drawEventHorizon) {
					drawEventHorizon = true;
					timer = 0;
					setShapeRadius();
					getBody().setType(BodyType.STATIC);
					getBody().setLinearVelocity(new Vec2(0.0f, 0.0f));
				}
				break;
			}
			case MESSAGE_DESTROY: {
				
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
	 * Resize the {@link PolygonShape} size of the {@link Body}.
	 */
	private void setShapeRadius() {
		float shapeX, shapeY;
		
		if(drawEventHorizon) {
			shapeX = getEngine().getConverter().toMeterX(eventHorizon.getWidth() / 2);
			shapeY = getEngine().getConverter().toMeterY(eventHorizon.getHeight() / 2);
		} else if(drawLeftAndRightHelix) {
			shapeX = getEngine().getConverter().toMeterX(leftHelix.getWidth() / 2);
			shapeY = getEngine().getConverter().toMeterY(leftHelix.getHeight() / 2);
		} else {
			shapeX = getEngine().getConverter().toMeterX(coreHelix.getWidth() / 2);
			shapeY = getEngine().getConverter().toMeterY(coreHelix.getHeight() / 2);
		}

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		getBody().getFixtureList().m_shape = shape;
	}

	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);
		
		timer += delta;
		draw(graphics);
		
		if(!getEdgeNotifier().isInside(getEdge())) {
			getEdgeNotifier().edgeReached(this);
		}
		
		if(drawEventHorizon && timer > ((EVENT_HORIZON_SPEED * 2) + EVENT_HORIZON_TIME)) {
			receive(Shot.MESSAGE_DESTROY);
		}
		
	}
	
	@Override
	public void draw(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		if(isVisible) {

			if(drawLeftAndRightHelix) {
				drawLeftAndRightHelix(graphics);
			}
			if(drawCoreHelix) {
				drawCoreHelix(graphics);
			}
			if(drawEventHorizon) {
				drawEventHorizon(graphics);
			}
		}
	}

	/**
	 * Draw the coreHelix {@link Texture}.
	 * 
	 * @param graphics : {@link Graphics} use to draw the {@link Shot}.
	 */
	private void drawCoreHelix(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		int x = getEngine().getConverter().toPixelX(getBody().getPosition().x) - coreHelix.getWidth() / 2;
		int y = getEngine().getConverter().toPixelY(getBody().getPosition().y) - coreHelix.getHeight() / 2;
		
		graphics.draw(coreHelix, x, y, getAngle());
	}
	
	/**
	 * Draw the leftAndRightHelix {@link Texture}.
	 * 
	 * @param graphics : {@link Graphics} use to draw the {@link Shot}.
	 */
	private void drawLeftAndRightHelix(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		int centerX = getEngine().getConverter().toPixelX(getBody().getPosition().x);
		int centerY = getEngine().getConverter().toPixelY(getBody().getPosition().y);
		
		int x = centerX - (leftHelix.getWidth() / 2);
		int y = centerY - (leftHelix.getHeight() / 2);
		int angle = (int) (((float) timer / ROTATION_SPEED) * 360);
		
		graphics.draw(leftHelix, x, y, angle);
		
		x = centerX - (rightHelix.getWidth() / 2);
		y = centerY - (rightHelix.getHeight() / 2);
		angle = 360 - angle;
		
		graphics.draw(rightHelix, x, y, angle);
	}
	
	/**
	 * Draw th eventHorizon {@link Texture}.
	 * 
	 * @param graphics : {@link Graphics} use to draw the {@link Shot}.
	 */
	private void drawEventHorizon(Graphics graphics) {
		Objects.requireNonNull(graphics);

		float size = getEventHorizonSize();
		
		int centerX = getEngine().getConverter().toPixelX(getBody().getPosition().x);
		int centerY = getEngine().getConverter().toPixelY(getBody().getPosition().y);

		int width = (int) (eventHorizon.getWidth() * size);
		int height = (int) (eventHorizon.getHeight() * size);
		int x = centerX - (width / 2);
		int y = centerY - (height / 2);
		
		graphics.draw(eventHorizon, x, y, x + width, y + height);
	}
	
	/**
	 * Get the size of the eventHorizon {@link Texture}.
	 * 
	 * @return Return the eventHorizon size.
	 */
	private float getEventHorizonSize() {
		
		long time = timer;
		
		if(timer > EVENT_HORIZON_SPEED + EVENT_HORIZON_TIME) {

			time = EVENT_HORIZON_SPEED - (timer - (EVENT_HORIZON_SPEED + EVENT_HORIZON_TIME));
			time = (time < 0)?0:time;

		}

		if(time <= EVENT_HORIZON_SPEED) {
			return ((float) time / EVENT_HORIZON_SPEED);
		}
		
		drawLeftAndRightHelix = false;
		drawCoreHelix = false;
		
		return 1.0f;
	}

	@Override
	protected Rect getEdge() {
		
		int x = getEngine().getConverter().toPixelX(getX());
		int y = getEngine().getConverter().toPixelY(getY());
		
		if(drawEventHorizon) {
			return new Rect(x - (eventHorizon.getWidth() / 2), y - (eventHorizon.getHeight() / 2), x + (eventHorizon.getWidth() / 2), y + (eventHorizon.getHeight() / 2));
		} else if(drawLeftAndRightHelix) {
			
			int offset = leftHelix.getWidth();
			offset = Math.max(leftHelix.getHeight(), offset);
			offset = Math.max(rightHelix.getWidth(), offset);
			offset = Math.max(rightHelix.getHeight(), offset);
			
			return new Rect(x - (offset / 2), y - (offset / 2), x + (offset / 2), y + (offset / 2));
			
		}
		
		return new Rect(x - (coreHelix.getWidth() / 2), y - (coreHelix.getHeight() / 2), x + (coreHelix.getWidth() / 2), y + (coreHelix.getHeight() / 2));
	}
	
}
