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
 * This class implements the {@link FireBallShot}.
 * 
 * @see AbstractShot
 */
public final class FireBallShot extends AbstractShot {

	private static final int FIREBALL_SPEED = 4000;
	private static final float MINIMAL_RADIUS = 0.4f;
	
	private final Texture coreBall;
	private final Texture radiusEffect;
	
	private boolean isVisible;
	private boolean radiusGrown;
	private float radiusSize;
	private long timer;
	
	/**
	 * {@link FireBallShot} constructor.
	 * 
	 * @param body : The {@link Shot} JBox2D {@link Body}.
	 * @param container : The {@link EntityContainer} that contains the {@link Shot}.
	 * @param collisionBehavior : The {@link CollisionBehavior} use by the {@link Shot}
	 */
	public FireBallShot(Body body, EntityContainer container, ShotCollisionBehavior collisionBehavior) {
		super(body, container, container, collisionBehavior, 3);
		
		this.coreBall = Foundation.RESOURCES.getTexture(TextureLoader.WEAPON_FIREBALL_CORE_SHOT);
		this.radiusEffect = Foundation.RESOURCES.getTexture(TextureLoader.WEAPON_FIREBALL_RADIUS_SHOT);
		
		this.isVisible = false;
		this.radiusGrown = false;
		this.radiusSize = MINIMAL_RADIUS;
	}

	@Override
	public void draw(Graphics graphics) {
		Objects.requireNonNull(graphics);
		if(isVisible) {
			drawCoreBall(graphics);
			drawRadiusEffect(graphics, (int) (timer % 100)*10);
		}
	}

	@Override
	public void update(Graphics graphics, long delta) {
		Objects.requireNonNull(graphics);
		
		timer += delta;
		draw(graphics);
		
		if(radiusGrown) {
			radiusSize = getRadiusEffectSize();
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(radiusSize, radiusSize);

			getBody().getFixtureList().m_shape = shape;
		}
		
		if(!getEdgeNotifier().isInside(getEdge())) {
			getEdgeNotifier().edgeReached(this);
		}
		
	}

	@Override
	public void receive(int message) {
		switch(message) {
			case Shot.MESSAGE_LOAD: {
				isVisible = true;
				radiusGrown = true;
				break;
			}
			case Shot.MESSAGE_FIRE: {
				radiusGrown = false;
				break;
			}
			case Shot.MESSAGE_CRUISE: {
				
				break;
			}
			case Shot.MESSAGE_HIT: {
				getBody().setType(BodyType.STATIC);
				getBody().setLinearVelocity(new Vec2(0.0f, 0.0f));
			}
			case Shot.MESSAGE_DESTROY: {
				
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

	@Override
	protected Rectangle getEdge() {
		
		int x = CoordinateConverter.toPixelX(getX());
		int y = CoordinateConverter.toPixelY(getY());
		
		int width = (int) (radiusEffect.getWidth() * radiusSize);
		int height = (int) (radiusEffect.getHeight() * radiusSize);
		
		// In case of Fire Radius is lower than CoreBall
		width = Math.max(width, coreBall.getWidth());
		height = Math.max(height, coreBall.getHeight());
		
		return new Rectangle(x - (width / 2), y - (height / 2), width, height);
	}

	/**
	 * Draw the {@link FireBallShot} core.
	 * 
	 * @param graphics : {@link Graphics} use to draw
	 */
	private void drawCoreBall(Graphics graphics) {
		Objects.requireNonNull(graphics);
		
		int x = CoordinateConverter.toPixelX(getBody().getPosition().x) - coreBall.getWidth() / 2;
		int y = CoordinateConverter.toPixelY(getBody().getPosition().y) - coreBall.getHeight() / 2;
		
		graphics.draw(coreBall, x, y, getAngle());
	}
	
	/**
	 * Draw the radius effect.
	 * 
	 * @param graphics : {@link Graphics} use to draw.
	 * @param random : A random angle.
	 */
	private void drawRadiusEffect(Graphics graphics, int random) {
		Objects.requireNonNull(graphics);
		
		int width = (int) (radiusEffect.getWidth() * radiusSize);
		int height = (int) (radiusEffect.getHeight() * radiusSize);
		
		int x = CoordinateConverter.toPixelX(getBody().getPosition().x) - (width / 2);
		int y = CoordinateConverter.toPixelY(getBody().getPosition().y) - (height / 2);
		
		graphics.draw(radiusEffect, x, y, x + width, y + height, random);
		
	}
	
	/**
	 * Get the radius effect size.
	 * 
	 * @return Return the radius effect size.
	 */
	private float getRadiusEffectSize() {
		
		long time = timer;
		
		if(time > FIREBALL_SPEED) {
			time = FIREBALL_SPEED;
		}
		
		return ((1.0f - MINIMAL_RADIUS) * (((float) time / FIREBALL_SPEED))) + MINIMAL_RADIUS;
		
	}
}
