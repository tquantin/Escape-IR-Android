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

package fr.escape.game;

import java.util.ArrayList;
import java.util.Objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import fr.escape.app.Game;
import fr.escape.app.Input;
import fr.escape.app.Overlay;
import fr.escape.app.Screen;
import fr.escape.game.User.LifeListener;
import fr.escape.game.entity.CollisionDetector;
import fr.escape.game.entity.Collisionable;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.game.entity.weapons.shot.ShotFactory;
import fr.escape.game.scenario.Earth;
import fr.escape.game.scenario.Jupiter;
import fr.escape.game.scenario.Moon;
import fr.escape.game.scenario.Stage;
import fr.escape.game.screen.AbstractIntro;
import fr.escape.game.screen.Lost;
import fr.escape.game.screen.Menu;
import fr.escape.game.screen.AbstractStage;
import fr.escape.game.screen.Error;
import fr.escape.game.screen.Victory;
import fr.escape.game.ui.IngameUI;
import fr.escape.game.ui.UIHighscore;
import fr.escape.game.ui.UIArmorLife;
import fr.escape.game.ui.UIWeapons;
import fr.escape.graphics.ScrollingTexture;
import fr.escape.input.Booster;
import fr.escape.input.Drift;
import fr.escape.input.Gesture;
import fr.escape.input.Loop;
import fr.escape.input.Slide;
import fr.escape.resources.texture.TextureLoader;

/**
 * <p>
 * Escape Game
 * 
 * <p>
 * This class is a huge Controller that link many components together.
 * 
 */
public final class Escape extends Game implements LifeListener {
	
	/**
	 * Player Model
	 */
	private final User user;
	
	/**
	 * Screen
	 */
	private Victory victory;
	private Lost lost;
	private Menu menu;
	private Error error;
	
	/**
	 * StageScreen
	 */
	private Screen jupiter;
	private Screen moon;
	private Screen earth;
	
	/**
	 * IntroScreen 
	 */
	private Screen introJupiter;
	private Screen introMoon;
	private Screen introEarth;
	
	/**
	 * Overlay used ingame
	 */
	private IngameUI ingameUI;
	
	/**
	 * Game Factory
	 */
	private ShipFactory shipFactory;
	private ShotFactory shotFactory;
	
	/**
	 * Game Entity Container
	 */
	private EntityContainer entityContainer;
	
	/**
	 * Default Constructor for the Game.
	 */
	public Escape() {
		user = new User(this);
	}
	
	/**
	 * @see Game#create()
	 */
	@Override
	public void create() {
		try {
			
			// Create World
			World world = new World(new Vec2(0.0f,0.0f), true);
			world.setContactListener(new CollisionDetector(getUser()));
			setWorld(world);
			
			//Top Wall
			createWall(world,CoordinateConverter.toMeterX(getGraphics().getWidth() / 2),CoordinateConverter.toMeterY((getGraphics().getHeight() * 2) / 3) - 1.0f,false);
			//Bottom Wall
			createWall(world,CoordinateConverter.toMeterX(getGraphics().getWidth() / 2),CoordinateConverter.toMeterY(getGraphics().getHeight()) + 2.0f,false);
			//Left Wall
			createWall(world,-1.0f,CoordinateConverter.toMeterY(getGraphics().getHeight() / 2),true);
			//Right Wall
			createWall(world,11.0f,CoordinateConverter.toMeterY(getGraphics().getHeight() / 2),true);
			
			// Create Entity Container
			entityContainer = new EntityContainer(getWorld(), Math.max((int) (getGraphics().getWidth() * 0.1f), 
					(int) (getGraphics().getHeight() * 0.1f)));
			
			// Create Game Components
			ingameUI = new IngameUI();
			shotFactory = new ShotFactory(getWorld(), getEntityContainer());
			shipFactory = new ShipFactory(getEntityContainer(), getShotFactory());
			
			// Create Ship
			createPlayerShip();
			
			// Create Gesture
			createGestures();
			
			// Create Overlay
			createOverlay();
			
			// Create Screen
			createScreen();
			
			// Show Entry Screen
			setMenuScreen();
			
		} catch(Exception e) {
			error = new Error(this);
			getActivity().error("Escape", "Exception raised during create()", e);
			setScreen(error);
		}
		
	}
	
	/**
	 * Create a Hidden Wall for User Ship
	 * 
	 * @param world World to use.
	 * @param x Position X
	 * @param y Position Y
	 * @param isRightOrLeft Is Right/Left or Top/Bottom
	 */
	private void createWall(World world, float x, float y, boolean isRightOrLeft) {
		
		Objects.requireNonNull(world);
		
		float shapeX = (isRightOrLeft) ? 1.0f : CoordinateConverter.toMeterX(getGraphics().getWidth());
		float shapeY = (isRightOrLeft) ? CoordinateConverter.toMeterY(getGraphics().getHeight()) : 1.0f;
		
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y - 1.0f);
		bodyDef.type = BodyType.STATIC;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(shapeX, shapeY);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 0.5f;
		fixture.friction = 0.0f;      
		fixture.restitution = 0.0f;
		fixture.filter.categoryBits = Collisionable.WALL_TYPE;
		fixture.filter.maskBits = Collisionable.PLAYER_TYPE;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixture);
	}

	/**
	 * @see Game#render()
	 */
	@Override
	public void render() {
		super.render();
		if(ingameUI != null) {
			ingameUI.render(getGraphics().getDeltaTime());
		}
	}
	
	/**
	 * @see Game#touch(Input)
	 */
	@Override
	public boolean touch(Input i) {
		if(getOverlay().touch(i)) {
			return true;
		}
		return getScreen().touch(i);
	}
	
	/**
	 * @see Game#move(Input)
	 */
	@Override
	public boolean move(Input i) {
		if(getOverlay().move(i)) {
			return true;
		}
		return getScreen().move(i);
	}

	/**
	 * <p>
	 * Return the Overlay used as Ingame UI.
	 * 
	 * @return {@link Overlay}
	 */
	public Overlay getOverlay() {
		return ingameUI;
	}
	
	/**
	 * Retrieve the {@link User} in this Game.
	 * 
	 * @return {@link User}
	 */
	public User getUser() {
		return user;
	}

	@Override
	public void restart() {
		
		Screen screen = getScreen();
		
		// Check if we should display Intro Screen
		
		if(screen == jupiter) {
			screen = introJupiter;
		}
		
		if(screen == moon) {
			screen = introMoon;
		}
		
		if(screen == earth) {
			screen = introEarth;
		}
		
		setScreen(screen);
	}

	@Override
	public void stop() {
		
		float x = CoordinateConverter.toMeterX(getGraphics().getWidth() / 2);
		float y = CoordinateConverter.toMeterY(getGraphics().getHeight() - 100);
		
		getUser().reset(x,y);
		setScreen(lost);
	}
	
	/**
	 * Update the current Screen by using Menu
	 */
	public void setMenuScreen() {
		setScreen(menu);
	}
	
	/**
	 * Update the current Screen by starting a New Game
	 */
	public void setNewGameScreen() {
		setIntroJupiterScreen();
	}
	
	public void setVictoryScreen() {
		setScreen(victory);
	}
	
	public void setLostScreen() {
		setScreen(lost);
	}
	
	public void setEarthScreen() {
		setScreen(earth);	
	}
	
	public void setJupiterScreen() {
		setScreen(jupiter);
	}
	
	public void setMoonScreen() {
		setScreen(moon);
	}
	
	public void setIntroEarthScreen() {
		setScreen(introEarth);
	}
	
	public void setIntroJupiterScreen() {
		setScreen(introJupiter);
	}
	
	public void setIntroMoonScreen() {
		setScreen(introMoon);
	}
	
	/**
	 * Get the {@link ShipFactory}
	 * 
	 * @return {@link ShipFactory}
	 */
	public ShipFactory getShipFactory() {
		return shipFactory;
	}
	
	/**
	 * Get the {@link ShotFactory}
	 * 
	 * @return {@link ShotFactory}
	 */
	public ShotFactory getShotFactory() {
		return shotFactory;
	}

	/**
	 * Get the {@link EntityContainer}
	 * 
	 * @return {@link EntityContainer}
	 */
	public EntityContainer getEntityContainer() {
		return entityContainer;
	}
	
	/**
	 * Create a Player Ship
	 */
	private void createPlayerShip() {
		
		Ship ship = getShipFactory().createPlayer(
				CoordinateConverter.toMeterX(getGraphics().getWidth() / 2), 
				CoordinateConverter.toMeterY(getGraphics().getHeight() - 100)
		);
		
		ship.createBody(getWorld());
		getUser().setShip(ship);
	}
	
	/**
	 * Create Gestures
	 */
	private void createGestures() {
		
		ArrayList<Gesture> gestures = new ArrayList<>();
		
		gestures.add(new Drift());
		gestures.add(new Slide());
		gestures.add(new Booster());
		gestures.add(new Loop());
		
		getUser().setGestures(gestures);
	}
	
	/**
	 * Create Overlay
	 */
	private void createOverlay() {
		
		UIHighscore uHighscore = new UIHighscore(this);
		UIArmorLife uLife = new UIArmorLife(this, getUser().getShip(), getUser());
		UIWeapons uWeapons = new UIWeapons(this, getUser(), getUser().getAllWeapons(), getUser().getActiveWeapon());
		
		ingameUI.add(uHighscore);
		ingameUI.add(uLife);
		ingameUI.add(uWeapons);
		
		getUser().register(uHighscore);
	}
	
	/**
	 * Create Screen
	 */
	private void createScreen() {
		
		lost = new Lost(this);
		menu = new Menu(this);
		victory = new Victory(this);
		
		jupiter = new AbstractStage(this) {

			private final Stage stage = new Jupiter(getWorld(), getEntityContainer(), getShipFactory());
			private final ScrollingTexture background = new ScrollingTexture(getResources().getTexture(TextureLoader.BACKGROUND_JUPITER), true);
			
			@Override
			protected void next() {
				Escape.this.setIntroMoonScreen();
			}

			@Override
			protected Stage getStage() {
				return stage;
			}

			@Override
			protected ScrollingTexture getBackground() {
				return background;
			}
			
		};
		
		moon = new AbstractStage(this) {
			
			private final Stage stage = new Moon(getWorld(), getEntityContainer(), getShipFactory());
			private final ScrollingTexture background = new ScrollingTexture(getResources().getTexture(TextureLoader.BACKGROUND_MOON), true);
			
			@Override
			protected void next() {
				Escape.this.setIntroEarthScreen();
			}
			
			@Override
			protected Stage getStage() {
				return stage;
			}
			
			@Override
			protected ScrollingTexture getBackground() {
				return background;
			}
			
		};
		
		earth = new AbstractStage(this) {
			
			private final Stage stage = new Earth(getWorld(), getEntityContainer(), getShipFactory());
			private final ScrollingTexture background = new ScrollingTexture(getResources().getTexture(TextureLoader.BACKGROUND_EARTH), true);
			
			@Override
			protected void next() {
				Escape.this.setVictoryScreen();
			}
			
			@Override
			protected Stage getStage() {
				return stage;
			}
			
			@Override
			protected ScrollingTexture getBackground() {
				return background;
			}
			
		};
		
		introJupiter = new AbstractIntro(this, getResources().getTexture(TextureLoader.INTRO_JUPITER)) {

			@Override
			public void next() {
				setJupiterScreen();
			}
			
		};
		
		introMoon = new AbstractIntro(this, getResources().getTexture(TextureLoader.INTRO_MOON)) {

			@Override
			public void next() {
				setMoonScreen();
			}
			
		};
		
		introEarth = new AbstractIntro(this, getResources().getTexture(TextureLoader.INTRO_EARTH)) {

			@Override
			public void next() {
				setEarthScreen();
			}
			
		};
	}
	
}
