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
import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Graphics;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.Collisionable;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.weapons.Weapon;
import fr.escape.game.entity.weapons.Weapons;
import fr.escape.game.entity.weapons.shot.EarthShot;
import fr.escape.game.entity.weapons.shot.JupiterShot;
import fr.escape.game.entity.weapons.shot.MoonShot;
import fr.escape.game.entity.weapons.shot.Shot;
import fr.escape.game.entity.weapons.shot.Shot.ShotContext;
import fr.escape.game.entity.weapons.shot.ShotFactory;
import fr.escape.game.scenario.Scenario;
import fr.escape.game.scenario.Stage;
import fr.escape.graphics.AnimationTexture;
import fr.escape.graphics.Texture;
import fr.escape.resources.TextureLoader;

/**
 * Factory use to create {@link Ship}.
 */
public class ShipFactory {
	
	private static final int PLAYERMASK = Collisionable.NPC_TYPE | Collisionable.SHOT_TYPE | Collisionable.BONUS_TYPE | Collisionable.WALL_TYPE;
	private static final int NPCMASK = Collisionable.PLAYER_TYPE | Collisionable.SHOT_TYPE;
	
	private static final CollisionBehavior COMPUTER_COLLISION_BEHAVIOR = new ComputerShipCollisionBehavior();
	private static final CollisionBehavior PLAYER_COLLISION_BEHAVIOR = new PlayerShipCollisionBehavior();
	
	private static final int DEFAULT_ARMOR = 1;
	private static final int PLAYER_ARMOR = 50;
	private static final int JUPITER_ARMOR = 10;
	private static final int MOON_ARMOR = 25;
	private static final int EARTH_ARMOR = 50;
	
	final Engine engine;
	private final EntityContainer econtainer;
	private final List<Weapon> playerWeapons;
	private final List<Weapon> npcWeapons;
	
	final ShotFactory shotFactory;
	
	/**
	 * {@link ShipFactory} constructor
	 * 
	 * @param ec : {@link EntityContainer} in which the {@link Ship} will be push.
	 * @param factory : {@link ShotFactory} use to create the {@link Weapon} {@link Shot}.
	 */
	public ShipFactory(Engine engine, EntityContainer ec, ShotFactory factory) {
		this.engine = engine;
		this.econtainer = Objects.requireNonNull(ec);
		this.playerWeapons = Weapons.createListOfWeapons(engine.getResources(), this.econtainer, Objects.requireNonNull(factory));
		this.npcWeapons = Weapons.createListOfUnlimitedWeapons(engine.getResources(), this.econtainer, factory);
		this.shotFactory = factory;
	}
	
	/**
	 * Create a Falcon {@link Ship}
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Falcon {@link Ship} created.
	 */
	public Ship createFalcon(float x, float y) {
		
		AnimationTexture falcon = new AnimationTexture(engine.getResources().getTexture(TextureLoader.SHIP_FALCON));
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForNpc(engine, falcon);
		
		Ship ship = createNpcAbstractShip(bodyDef, fixture, falcon);
		ship.setActiveWeapon(2);
		
		return ship;
		
	}
	
	/**
	 * Create a Viper {@link Ship}
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Viper {@link Ship} created.
	 */
	public Ship createViper(float x, float y) {
		
		AnimationTexture vyper = new AnimationTexture(engine.getResources().getTexture(TextureLoader.SHIP_VIPER));
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForNpc(engine, vyper);
		
		Ship ship = createNpcAbstractShip(bodyDef, fixture, vyper);
		ship.setActiveWeapon(0);
		
		return ship;
		
	}
	
	/**
	 * Create a Raptor {@link Ship}
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Raptor {@link Ship} created.
	 */
	public Ship createRaptor(float x, float y) {
		
		AnimationTexture raptor = new AnimationTexture(engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR));
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForNpc(engine, raptor);
		
		Ship ship = createNpcAbstractShip(bodyDef, fixture, raptor);
		ship.setRotation(180);
		ship.setActiveWeapon(1);
		
		return ship;
	}
	
	/**
	 * Create a Player {@link Ship}
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Player {@link Ship} created.
	 */
	public Ship createPlayer(float x, float y) {
		
		AnimationTexture raptor = new AnimationTexture( 
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_1),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_2),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_3),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_4),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_5),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_6),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_7),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_8),
				engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR_9)
		);
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForPlayer(engine, raptor);

		return new AbstractShip(engine, bodyDef, fixture, playerWeapons, PLAYER_ARMOR, econtainer, raptor, PLAYER_COLLISION_BEHAVIOR) {
			
			private static final int PLAYER_MASK = NPC_TYPE | SHOT_TYPE | BONUS_TYPE | WALL_TYPE;
			private static final int INVULNERABILITY_MASK = 0x0001 | BONUS_TYPE;
			private static final int LEFTLOOP = 2;
			private static final int RIGHTLOOP = 1;
			
			private boolean executeLeftLoop = false;
			private boolean executeRightLoop = false;
			
			@Override
			public boolean isPlayer() {
				return true;
			}
			
			@Override
			public void toDestroy() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void update(Graphics graphics, long delta) {
				
				Objects.requireNonNull(graphics);
				
				if(executeRightLoop || executeLeftLoop) {
					
					if(executeRightLoop) {
						getShipDrawable().forward();
					} else {
						getShipDrawable().reverse();
					}
					
					if(getShipDrawable().hasNext()) {
						getShipDrawable().next();
					} else {
						getShipDrawable().rewind();
						executeLeftLoop = false;
						executeRightLoop = false;
					}
				}
				
				super.update(graphics, delta);
				
				Shot shot = getActiveWeapon().getShot();
				
				if(shot != null) {
					shot.setPosition(getX(), getY() + engine.getConverter().toMeterY(getEdge().height()));
				}
			}
			
			@Override
			public void moveBy(float[] velocity) {
				
				if(getBody().isActive()) {
					
					doLooping(velocity);
					
					if(velocity[0] > 0) {
						
						getBody().setLinearVelocity(new Vec2(velocity[1], velocity[2]));
						velocity[0] -= Math.abs(Math.max(Math.abs(velocity[1]), Math.abs(velocity[2])));
						
					} else {
						getBody().setLinearVelocity(new Vec2(0, 0));
						velocity[1] = 0.0f;
						velocity[2] = 0.0f;
					}

				}
			}
			
			/**
			 * Make Player Invulnerable, or not.
			 * 
			 * @param invulnerable Is Player Invulnerable ?
			 */
			private void setInvulnerable(boolean invulnerable) {
				getBody().getFixtureList().m_filter.maskBits = (invulnerable)?INVULNERABILITY_MASK:PLAYER_MASK;
			}
			
			/**
			 * Player {@link Ship} execute a looping
			 * 
			 * @param velocity : parameters for the looping.
			 */
			private void doLooping(float[] velocity) {
				
				int mode = (int) velocity[3];
				switch (mode) {
					case RIGHTLOOP: {
						setInvulnerable(true);
						executeRightLoop = true;
						if(velocity[0] <= 0) {
							velocity[3] = 0.0f;
						} else {
							velocity[0] -= 2.0f;
						}
						break;
					}
					case LEFTLOOP: {
						setInvulnerable(true);
						executeLeftLoop = true;
						if(velocity[0] <= 0) {
							velocity[3] = 0.0f;
						} else {
							velocity[0] -= 2.0f;
						}
						break;
					}
					default: {
						getShipDrawable().rewind();
						executeLeftLoop = false;
						executeRightLoop = false;
						setInvulnerable(false);
						break;
					}
				}

			}
		
		};
		
	}
	
	/**
	 * Create a {@link Ship} for {@link Scenario}.
	 * 
	 * @param type : {@link Ship} type
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the created {@link Ship}.
	 */
	public Ship createShipForScenario(int type, float x, float y) {
		switch(type) {
			case 0: {
				return createRaptor(x, y);
			}
			case 1: {
				return createFalcon(x, y);
			}
			case 2: {
				return createViper(x, y);
			}
			default: {
				throw new IllegalArgumentException("Unknown Ship Type");
			}
		}
	}
	
	/**
	 * Create the {@link Boss} for {@link Scenario} Jupiter.
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Jupiter {@link Boss}.
	 */
	public Boss createJupiterBoss(float x, float y) {
		
		AnimationTexture jupiter = new AnimationTexture(engine.getResources().getTexture(TextureLoader.BOSS_JUPITER));
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForNpc(engine, jupiter);
		
		return new AbstractBoss(engine, bodyDef, fixture, npcWeapons, JUPITER_ARMOR, econtainer, jupiter, COMPUTER_COLLISION_BEHAVIOR) {

			private final Texture texture = engine.getResources().getTexture(TextureLoader.JUPITER_SPECIAL);
			
			@Override
			public int getFireWaitingTime() {
				return 3000;
			}

			@Override
			public int getSpecialWaitingTime() {
				return 10000;
			}
			
			@Override
			public void fire() {
				
				setActiveWeapon(2);
				
				engine.post(new Runnable() {
					
					@Override
					public void run() {
						fireWeapon();
					}
					
				});
				
				incActionCount();
			}
			
			@Override
			public void special() {
				
				final JupiterShot s1 = (JupiterShot) shotFactory.createJupiterShot(getX(), getY());
				final JupiterShot s2 = (JupiterShot) shotFactory.createJupiterShot(getX(), getY());
				final JupiterShot s3 = (JupiterShot) shotFactory.createJupiterShot(getX(), getY());
				final JupiterShot s4 = (JupiterShot) shotFactory.createJupiterShot(getX(), getY());
				
				s1.setShotConfiguration(new ShotContext(isPlayer(), texture.getWidth(), texture.getHeight()));
				s2.setShotConfiguration(new ShotContext(isPlayer(), texture.getWidth(), texture.getHeight()));
				s3.setShotConfiguration(new ShotContext(isPlayer(), texture.getWidth(), texture.getHeight()));
				s4.setShotConfiguration(new ShotContext(isPlayer(), texture.getWidth(), texture.getHeight()));
				
				s1.moveBy(new float[] {0.0f, 4.0f, 5.0f});
				s2.moveBy(new float[] {0.0f, 1.25f, 5.0f});
				s3.moveBy(new float[] {0.0f, -1.25f, 5.0f});
				s4.moveBy(new float[] {0.0f, -4.0f, 5.0f});
				
				s1.receive(Shot.MESSAGE_CRUISE);
				s2.receive(Shot.MESSAGE_CRUISE);
				s3.receive(Shot.MESSAGE_CRUISE);
				s4.receive(Shot.MESSAGE_CRUISE);
				
				engine.post(new Runnable() {
					@Override
					public void run() {
						container.push(s1);
						container.push(s2);
						container.push(s3);
						container.push(s4);
					}
				});
			}

			@Override
			public boolean normal() {
				return false;
			}

			@Override
			public void moveShot(float x, float y) { /**/ }
			
		};
		
	}
	
	/**
	 * Create the {@link Boss} for {@link Scenario} Moon.
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Moon {@link Boss}.
	 */
	public Boss createMoonBoss(float x, float y) {
		
		AnimationTexture moon = new AnimationTexture(
				engine.getResources().getTexture(TextureLoader.BOSS_MOON),
				engine.getResources().getTexture(TextureLoader.BOSS_MOON_1)
		);
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForNpc(engine, moon);
		
		return new AbstractBoss(engine, bodyDef, fixture, npcWeapons, MOON_ARMOR, econtainer, moon, COMPUTER_COLLISION_BEHAVIOR) {

			private final Texture texture = engine.getResources().getTexture(TextureLoader.MOON_SPECIAL);
			private final Random random = new Random();
			
			@Override
			public int getFireWaitingTime() {
				return 3000;
			}

			@Override
			public int getSpecialWaitingTime() {
				return 10000;
			}
			
			@Override
			public void fire() {
				
				setActiveWeapon(random.nextInt(3));
				
				engine.post(new Runnable() {
					
					@Override
					public void run() {
						fireWeapon();
					}
					
				});
				
				incActionCount();
			}
			
			@Override
			public void special() {
				
				getBossTexture().next();

				final MoonShot s1 = (MoonShot) shotFactory.createMoonShot(getX() - engine.getConverter().toMeterX(20), getY() - engine.getConverter().toMeterY(9));
				
				s1.setShotConfiguration(new ShotContext(isPlayer(), texture.getWidth(), texture.getHeight()));
				s1.moveBy(new float[] {0.0f, 0.0f, 2.5f});
				s1.receive(Shot.MESSAGE_CRUISE);
				
				engine.post(new Runnable() {
					
					@Override
					public void run() {
						container.push(s1);
					}
					
				});
			}

			@Override
			public boolean normal() {
				getBossTexture().rewind();
				return false;
			}

			@Override
			public void moveShot(float x, float y) { /**/ }
		};
		
	}

	/**
	 * Create the {@link Boss} for {@link Scenario} Earth.
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the Earth {@link Boss}.
	 */
	public Boss createEarthBoss(float x, float y) {
		
		AnimationTexture earth = new AnimationTexture(
				engine.getResources().getTexture(TextureLoader.BOSS_EARTH),
				engine.getResources().getTexture(TextureLoader.BOSS_EARTH_1)
		);
		
		BodyDef bodyDef = createBodyDef(x, y);
		FixtureDef fixture = createFixtureForNpc(engine, earth);
		
		return new AbstractBoss(engine, bodyDef, fixture, npcWeapons, EARTH_ARMOR, econtainer, earth, COMPUTER_COLLISION_BEHAVIOR) {
			
			private final Texture texture = engine.getResources().getTexture(TextureLoader.EARTH_SPECIAL);
			private final float VARX = engine.getConverter().toMeterY(10);
			private final float VARY = engine.getConverter().toMeterY(50);
			private Shot specialShot;
			
			@Override
			public int getFireWaitingTime() {
				return 3000;
			}
	
			@Override
			public int getSpecialWaitingTime() {
				return 10000;
			}
			
			@Override
			public void fire() {
				
				setActiveWeapon(1);
				
				engine.post(new Runnable() {
					
					@Override
					public void run() {
						fireWeapon();
					}
					
				});
				
				incActionCount();
			}
			
			@Override
			public void special() {
				
				getBossTexture().next();
				
				final EarthShot s1 = (EarthShot) shotFactory.createEarthShot(getX() - VARX, getY() + VARY);
					
				s1.setShotConfiguration(new ShotContext(isPlayer(), texture.getWidth(), texture.getHeight()));
				s1.moveBy(new float[] {0.0f, 0.0f, 0.0f});
				s1.receive(Shot.MESSAGE_CRUISE);
				
				engine.post(new Runnable() {
					@Override
					public void run() {
						container.push(s1);
					}
				});
				
				specialShot = s1;
			}

			@Override
			public boolean normal() {
				if(specialShot != null) {
					specialShot.receive(Shot.MESSAGE_HIT);
					getBossTexture().rewind();
					specialShot = null;
					return false;
				}
				return true;
			}

			@Override
			public void moveShot(float x, float y) {
				if(specialShot != null) {					
					Body speBody = specialShot.getBody();
					if(speBody != null) {
						speBody.setTransform(new Vec2(x - VARX, y + VARY), 0.0f);
					}
				}
			}
		};
		
	}
	
	/**
	 * Create the {@link BodyDef}.
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @return Return the {@link BodyDef}.
	 */
	private static BodyDef createBodyDef(float x, float y) {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		
		return bodyDef;
	}
	
	/**
	 * Create a {@link PolygonShape}.
	 * 
	 * @param drawable : The {@link AnimationTexture} use to create the {@link PolygonShape}
	 * @return Return a {@link PolygonShape}.
	 */
	private static PolygonShape createShape(Engine engine, AnimationTexture drawable) {
		
		Objects.requireNonNull(drawable);
		
		float shapeX = engine.getConverter().toMeterX(drawable.getWidth() / 2);
		float shapeY = engine.getConverter().toMeterY(drawable.getHeight() / 2);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		return shape;
	}
	
	/**
	 * Create a {@link FixtureDef}.
	 * 
	 * @param drawable : The {@link AnimationTexture} use to create the {@link FixtureDef}
	 * @return Return a {@link FixtureDef}.
	 */
	private static FixtureDef createFixtureForNpc(Engine engine, AnimationTexture drawable) {
		
		Objects.requireNonNull(drawable);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = createShape(engine, drawable);
		fixture.density = 0.5f;
		fixture.friction = 0.3f;      
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = 0x0004;
		fixture.filter.maskBits = NPCMASK;
		
		return fixture;
	}
	
	/**
	 * Create the {@link FixtureDef} for the Player {@link Ship}
	 *  
	 * @param drawable : The {@link AnimationTexture} use to create the {@link FixtureDef}
	 * @return Return the Player {@link Ship} {@link FixtureDef}.
	 */
	private static FixtureDef createFixtureForPlayer(Engine engine, AnimationTexture drawable) {
		
		FixtureDef fixture = createFixtureForNpc(engine, drawable);
		
		fixture.filter.categoryBits = 0x0002;
		fixture.filter.maskBits = PLAYERMASK;
		
		return fixture;
	}
	
	/**
	 * Create a NPC {@link Ship}.
	 * 
	 * @param bodyDef : The {@link BodyDef} needed to create the JBox2D {@link Body}
	 * @param fixture : The {@link FixtureDef} needed to create the JBox2D {@link Body}
	 * @param drawable : The {@link AnimationTexture} use for this {@link Ship}
	 * @return Return the created {@link Ship}
	 */
	private Ship createNpcAbstractShip(BodyDef bodyDef, FixtureDef fixture, AnimationTexture drawable) {
		Objects.requireNonNull(bodyDef);
		Objects.requireNonNull(fixture);
		Objects.requireNonNull(drawable);
		
		return new AbstractShip(engine, bodyDef, fixture, npcWeapons, DEFAULT_ARMOR, econtainer, drawable, COMPUTER_COLLISION_BEHAVIOR) {
			
			@Override
			public void toDestroy() {
				engine.post(new Runnable() {
					
					@Override
					public void run() {
						popBonus();
					}
					
				});
			}
			
			void popBonus() {
				getEntityContainer().pushBonus(getX(), getY());
				getEntityContainer().destroy(this);
			}

			@Override
			public boolean isPlayer() {
				return false;
			}
			
		};
	}

	/**
	 * Create the {@link Boss} for {@link Scenario} Earth.
	 * 
	 * @param bossSpawnX : Coordinate on X axis.
	 * @param bossSpawnY : Coordinate on Y axis.
	 * <p>
	 * @param bossType : Integer representing the boss type ID.<br/>
	 * 0 : Jupiter Boss<br/>
	 * 1 : Moon Boss<br/>
	 * 2 : Earth Boss
	 * </p>
	 * @return Return the {@link Boss} of a {@link Stage}.
	 */
	public Boss createBoss(float bossSpawnX, float bossSpawnY, int bossType) {
		Boss boss = null;
		
		switch(bossType) {
			case 0	: boss = this.createJupiterBoss(bossSpawnX, bossSpawnY); break;
			case 1	: boss = this.createMoonBoss(bossSpawnX, bossSpawnY); break;
			case 2	: boss = this.createEarthBoss(bossSpawnX, bossSpawnY); break;
			default	: throw new IllegalStateException("Invalid Boss ID");
		}
		
		return boss;
	}
}
