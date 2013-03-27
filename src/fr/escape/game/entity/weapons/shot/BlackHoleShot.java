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

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import fr.escape.app.Foundation;
import fr.escape.app.Graphics;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.entity.EntityContainer;
import fr.escape.graphics.Texture;
import fr.escape.resources.texture.TextureLoader;

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
	public BlackHoleShot(Body body, EntityContainer container, ShotCollisionBehavior collisionBehavior) {
		super(body, container, container, collisionBehavior, 5);
		
		this.isVisible = false;
		this.drawCoreHelix = false;
		this.drawLeftAndRightHelix = false;
		this.drawEventHorizon = false;
		
		this.coreHelix = Foundation.RESOURCES.getTexture(TextureLoader.WEAPON_BLACKHOLE_CORE_SHOT);
		this.leftHelix = Foundation.RESOURCES.getTexture(TextureLoader.WEAPON_BLACKHOLE_LEFT_SHOT);
		this.rightHelix = Foundation.RESOURCES.getTexture(TextureLoader.WEAPON_BLACKHOLE_RIGHT_SHOT);
		this.eventHorizon = Foundation.RESOURCES.getTexture(TextureLoader.WEAPON_BLACKHOLE_EVENT_HORIZON_SHOT);
		
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
				
				Foundation.ACTIVITY.post(new Runnable() {
					
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
			shapeX = CoordinateConverter.toMeterX(eventHorizon.getWidth() / 2);
			shapeY = CoordinateConverter.toMeterY(eventHorizon.getHeight() / 2);
		} else if(drawLeftAndRightHelix) {
			shapeX = CoordinateConverter.toMeterX(leftHelix.getWidth() / 2);
			shapeY = CoordinateConverter.toMeterY(leftHelix.getHeight() / 2);
		} else {
			shapeX = CoordinateConverter.toMeterX(coreHelix.getWidth() / 2);
			shapeY = CoordinateConverter.toMeterY(coreHelix.getHeight() / 2);
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
		
		int x = CoordinateConverter.toPixelX(getBody().getPosition().x) - coreHelix.getWidth() / 2;
		int y = CoordinateConverter.toPixelY(getBody().getPosition().y) - coreHelix.getHeight() / 2;
		
		graphics.draw(coreHelix, x, y, getAngle());
	}
	
	/**
	 * Draw the leftAndRightHelix {@link Texture}.
	 * 
	 * @param graphics : {@link Graphics} use to draw the {@link Shot}.
	 */
	private void drawLeftAndRightHelix(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		int centerX = CoordinateConverter.toPixelX(getBody().getPosition().x);
		int centerY = CoordinateConverter.toPixelY(getBody().getPosition().y);
		
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
		
		int centerX = CoordinateConverter.toPixelX(getBody().getPosition().x);
		int centerY = CoordinateConverter.toPixelY(getBody().getPosition().y);

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
	protected Rectangle getEdge() {
		
		int x = CoordinateConverter.toPixelX(getX());
		int y = CoordinateConverter.toPixelY(getY());
		
		if(drawEventHorizon) {
			return new Rectangle(x - (eventHorizon.getWidth() / 2), y - (eventHorizon.getHeight() / 2), eventHorizon.getWidth(), eventHorizon.getHeight());
		} else if(drawLeftAndRightHelix) {
			
			int offset = leftHelix.getWidth();
			offset = Math.max(leftHelix.getHeight(), offset);
			offset = Math.max(rightHelix.getWidth(), offset);
			offset = Math.max(rightHelix.getHeight(), offset);
			
			return new Rectangle(x - (offset / 2), y - (offset / 2), offset, offset);
			
		}
		
		return new Rectangle(x - (coreHelix.getWidth() / 2), y - (coreHelix.getHeight() / 2), coreHelix.getWidth(), coreHelix.getHeight());
	}
	
}
