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
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.game.entity.EntityContainer;
import fr.escape.graphics.Texture;
import fr.escape.resources.Resources;
import fr.escape.resources.TextureLoader;

/**
 * Factory use to create {@link Shot}.
 */
public final class ShotFactory {
	
	private static final int MASK = 0x0001;
	private static final ShotCollisionBehavior COLLISION_BEHAVIOR = new ShotCollisionBehavior();
	
	private final Engine engine;
	private final Resources resources;
	private final World world;
	private final EntityContainer entityContainer;

	/**
	 * {@link ShotFactory} constructor.
	 * 
	 * @param world : The {@link World} that will contain the {@link Shot}
	 * @param entityContainer : The {@link EntityContainer} in which the {@link Shot} will be push.
	 */
	public ShotFactory(Engine engine, World world, EntityContainer entityContainer) {
		this.engine = engine;
		this.resources = engine.getResources();
		this.world = Objects.requireNonNull(world);
		this.entityContainer = Objects.requireNonNull(entityContainer);
	}

	/**
	 * Create a {@link BlackHoleShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return {@link BlackHoleShot}.
	 */
	public Shot createBlackholeShot(float x, float y) {
		Texture texture = resources.getTexture(TextureLoader.WEAPON_BLACKHOLE_CORE_SHOT);

		float shapeX = engine.getConverter().toMeterX(texture.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(texture.getHeight() / 2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
				
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 0.0f;       
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
		
		Shot shot = new BlackHoleShot(engine, body, entityContainer, COLLISION_BEHAVIOR);
		
		body.setUserData(shot);
		
		return shot;
	}
	
	/**
	 * Create a {@link FireBallShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return {@link FireBallShot}.
	 */
	public Shot createFireBallShot(float x, float y) {
		Texture texture = resources.getTexture(TextureLoader.WEAPON_FIREBALL_CORE_SHOT);

		float shapeX = engine.getConverter().toMeterX(texture.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(texture.getHeight() / 2);

		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 1.0f;
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);

		Shot shot = new FireBallShot(engine, body, entityContainer, COLLISION_BEHAVIOR);
		
		body.setUserData(shot);
		
		return shot;
	}
	
	/**
	 * Create a {@link MissileShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return {@link MissileShot}.
	 */
	public Shot createMissileShot(float x, float y) {
		Texture texture = resources.getTexture(TextureLoader.WEAPON_MISSILE_SHOT);

		float shapeX = engine.getConverter().toMeterX(texture.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(texture.getHeight() / 2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 1.0f;       
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);

		Shot shot = new MissileShot(engine, body, entityContainer, COLLISION_BEHAVIOR);
		
		body.setUserData(shot);
		
		return shot;
	}

	/**
	 * Create a {@link ShiboleetShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @param isChild : true if is a small {@link ShiboleetShot}, false otherwise.
	 * @return Return {@link ShiboleetShot}.
	 */
	public Shot createShiboleetShot(float x, float y, boolean isChild) {
		Texture coreShiboleet = resources.getTexture(TextureLoader.WEAPON_SHIBOLEET_SHOT);
		float shapeX, shapeY;

		float shipSize = engine.getConverter().toMeterY(resources.getTexture(TextureLoader.SHIP_RAPTOR).getHeight());
		if(isChild) {
			shapeX = engine.getConverter().toMeterX(coreShiboleet.getWidth() / 2 - 10);
			shapeY = engine.getConverter().toMeterY(coreShiboleet.getHeight() / 2 - 10);
		} else {
			shapeX = engine.getConverter().toMeterX((int) ((coreShiboleet.getWidth() / 2) * ShiboleetShot.CHILD_RADIUS));
			shapeY = engine.getConverter().toMeterY((int) ((coreShiboleet.getHeight() / 2) * ShiboleetShot.CHILD_RADIUS));
		}
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y + ((isChild)?0:shipSize));
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 1.0f;       
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);

		Shot shot = new ShiboleetShot(engine, body, isChild, entityContainer, COLLISION_BEHAVIOR, this);
		
		body.setUserData(shot);
		
		return shot;
	}
	
	/**
	 * Create a {@link JupiterShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return {@link JupiterShot}.
	 */
	public Shot createJupiterShot(float x, float y) {
		Texture coreJupiter = resources.getTexture(TextureLoader.JUPITER_SPECIAL);
		float shapeX = engine.getConverter().toMeterX(coreJupiter.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(coreJupiter.getHeight() / 2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 1.0f;       
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);

		Shot shot = new JupiterShot(engine, body, entityContainer, COLLISION_BEHAVIOR);
		
		body.setUserData(shot);
		
		return shot;
	}
	
	/**
	 * Create a {@link MoonShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return {@link MoonShot}.
	 */
	public Shot createMoonShot(float x, float y) {
		Texture coreJupiter = resources.getTexture(TextureLoader.MOON_SPECIAL);
		float shapeX = engine.getConverter().toMeterX(coreJupiter.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(coreJupiter.getHeight() / 2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 1.0f;       
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);

		Shot shot = new MoonShot(engine, body, entityContainer, COLLISION_BEHAVIOR);
		
		body.setUserData(shot);
		
		return shot;
	}
	
	/**
	 * Create a {@link EarthShot}.
	 * 
	 * @param x : Coordinate on X axis in meters.
	 * @param y : Coordinate on Y axis in meters.
	 * @return Return {@link EarthShot}.
	 */
	public Shot createEarthShot(float x, float y) {
		Texture coreJupiter = resources.getTexture(TextureLoader.EARTH_SPECIAL);
		float shapeX = engine.getConverter().toMeterX(coreJupiter.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(engine.getGraphics().getHeight()) - y;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 1.0f;
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0008;
		fixture.filter.maskBits = MASK;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);

		Shot shot = new EarthShot(engine, body, entityContainer, COLLISION_BEHAVIOR);
		
		body.setUserData(shot);
		
		return shot;
	}
	
}
