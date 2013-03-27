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

package fr.escape.game.screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.scenario.Stage;
import fr.escape.graphics.RepeatableScrollingTexture;
import fr.escape.graphics.ScrollingTexture;
import fr.escape.input.Gesture;
import fr.escape.input.WeaponGesture;
import fr.escape.resources.texture.TextureLoader;

/**
 * <p>
 * Display a Screen which handle Game Stage.
 * 
 */
public abstract class AbstractStage implements Screen {
	
	private final static String TAG = AbstractStage.class.getSimpleName();
	
	private final static int MAX_ACTIVE_EVENT_TIME = 1337;
	private final static long STAR_SPEED = 5000;
	
	private final Escape game;
	private final ScrollingTexture star;
	private final LinkedList<Input> events;
	
	private long time;
	private float[] velocity = {0, 0, 0, 0};

	private List<Input> activeEvents;
	private long activeEventTime;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public AbstractStage(Escape game) {
		
		this.game = Objects.requireNonNull(game);
		this.star = new RepeatableScrollingTexture(game.getResources().getTexture(TextureLoader.OVERLAY_STAR), true);
        this.events = new LinkedList<>();
        
	}
	
	@Override
	public void render(long delta) {

		time += delta;
		activeEventTime += delta;
		
		float percent = ((float) time) / (getStage().getEstimatedScenarioTime() * 1000);

		if(percent >= 1.0f) {
			percent = 1.0f;
		}
		
		getBackground().setYPercent(percent);
		game.getGraphics().draw(getBackground(), 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		star.setYPercent(((float) time) / STAR_SPEED);
		game.getGraphics().draw(star, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		game.getUser().getShip().update(game.getGraphics(), delta);
		game.getUser().getShip().moveBy(velocity);
		
		getStage().update((int) (time / 1000));
		
		game.getEntityContainer().update(game.getGraphics(), delta);
		
		if(!events.isEmpty()) {
			activeEvents = Screens.drawEventsOnScreen(game.getGraphics(), events, Color.WHITE);
			activeEventTime = 0;
		}
		
		if(events.isEmpty() && activeEvents != null) {
			Screens.drawEventsOnScreen(game.getGraphics(), activeEvents, Color.GREEN);
		}
		
		if(activeEventTime > MAX_ACTIVE_EVENT_TIME) {
			activeEvents = null;
		}
		
		game.getEntityContainer().flush();
		game.getActivity().post(new Runnable() {
			
			@Override
			public void run() {
				if(getStage().hasFinished()) {
					next();
				}
			}
			
		});
	}

	/**
	 * Get the Game used for this Abstract Stage
	 * 
	 * @return Game linked to the {@link AbstractStage}
	 */
	protected Escape getGame() {
		return game;
	}
	
	/**
	 * Triggered when we need to go on the next Screen 
	 * in Game Lifecycle.
	 */
	protected abstract void next();
	
	@Override
	public void show() {
		
		game.getOverlay().show();
		game.getEntityContainer().reset();
		
		time = 0;
		getStage().start();
		
		if(activeEvents != null) {
			activeEvents.clear();
		}
		activeEventTime = 0;
		events.clear();
		
		float x = CoordinateConverter.toMeterX(game.getGraphics().getWidth() / 2);
		float y = CoordinateConverter.toMeterY(game.getGraphics().getHeight() - 100);
		
		velocity[0] = 0.0f;
		game.getUser().getShip().reset(x, y);

	}

	@Override
	public void hide() {
		
		float x = CoordinateConverter.toMeterX(game.getGraphics().getWidth() / 2);
		float y = CoordinateConverter.toMeterY(game.getGraphics().getHeight() - 100);
		
		game.getOverlay().hide();
		game.getEntityContainer().reset();
		
		velocity[0] = 0.0f;
		game.getUser().getShip().reset(x, y);
		
		getStage().reset();
	}

	@Override
	public boolean touch(Input i) {
		
		Ship ship = game.getUser().getShip();
		
		int x = CoordinateConverter.toPixelX(ship.getX());
		int y = CoordinateConverter.toPixelY(ship.getY());
		int errorX = ship.getEdge().width / 2;
		int errorY = ship.getEdge().height / 2;
		
		if((i.getX() > x - errorX && i.getX() < x + errorX) && (i.getY() > y - errorY && i.getY() < y + errorY)) {
			
			if(ship.loadWeapon()) {
				game.getActivity().debug(TAG, "Weapon Gesture Accept : Load");
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean move(final Input i) {
		
		Objects.requireNonNull(i);
		LinkedList<Input> events = this.events;
		ArrayList<Gesture> gestures = game.getUser().getGestures();
		
		switch(i.getKind()) {
			case ACTION_UP: {
				
				Iterator<Input> it = events.iterator();
				
				if(it.hasNext()) {
					
					Input start = it.next(); it.remove();
					boolean accept = false;
					
					if(touch(start)) {
						
						WeaponGesture wg = new WeaponGesture();
						Ship ship = game.getUser().getShip();
						
						float[] weaponVelocity = new float[3];
						
						if(wg.accept(start, events, i, weaponVelocity) && ship.isWeaponLoaded()) {
							game.getActivity().debug(TAG, "Weapon Gesture Accept : Fire");
							ship.fireWeapon(weaponVelocity);
							accept = true;
						}
						
					} else {
						
						for(Gesture g : gestures) {
							if(g.accept(start, events, i, velocity)) {
								accept = true;
								break;
							}
						}
						
					}
					
					if(!accept) {
						activeEvents = null;
					}
					events.clear();
					
				}
				break;
			}
			default: {
				activeEvents = null;
				events.add(i);
			}
		}
		
		return false;
	}

	/**
	 * Get the Stage used for this {@link AbstractStage}
	 * 
	 * @return Stage
	 */
	protected abstract Stage getStage();
	
	/**
	 * Get the Scrolling Texture used for this {@link AbstractStage} 
	 * 
	 * @return Background used for this Screen
	 */
	protected abstract ScrollingTexture getBackground();
	
}
