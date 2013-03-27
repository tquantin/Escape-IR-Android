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

package fr.escape.app;

import org.jbox2d.dynamics.World;

import fr.escape.graphics.RenderListener;
import fr.escape.input.EventListener;
import fr.escape.resources.Resources;

/**
 * <p>
 * A class which delegates rendering to a {@link Screen}. 
 * 
 * <p>
 * Allowing multiple screens for a Game.
 * 
 * <p>
 * This is the main Controller of the Game.
 */
public abstract class Game implements RenderListener, EventListener {
	
	/**
	 * Current Screen
	 */
	private Screen screen;
	
	/**
	 * Game's World
	 */
	private World world;
	
	/**
	 * Called when {@link Activity} start
	 */
	public abstract void create();
	
	@Override
	public void render() {
		if(screen != null) {
			screen.render(getGraphics().getDeltaTime());
		}
	}

	/**
	 * Set a new active {@link Screen}.
	 * 
	 * @param screen {@link Screen} to display.
	 */
	public void setScreen(Screen screen) {
		
		if(this.screen != null) {
			this.screen.hide();
		}
		
		this.screen = screen;
		
		screen.show();
	}

	/**
	 * Return the current active {@link Screen}.
	 *
	 * @return active {@link Screen}.
	 */
	public Screen getScreen() {
		return screen;
	}
	
	/**
	 * Return the {@link Activity} which created this Game.
	 * 
	 * @return {@link Activity}.
	 */
	public Activity getActivity() {
		return Foundation.ACTIVITY;
	}
	
	/**
	 * Return the {@link Graphics} for the Game.
	 * 
	 * @return {@link Graphics}
	 */
	public Graphics getGraphics() {
		return Foundation.GRAPHICS;
	}

	/**
	 * Return the {@link Resources} for the Game.
	 * 
	 * @return {@link Resources}
	 */
	public Resources getResources() {
		return Foundation.RESOURCES;
	}
	
	/**
	 * Return the Game's {@link World}
	 * 
	 * @return {@link World}
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * Set the Game's {@link World}
	 * 
	 * @param world Game's World
	 */
	public void setWorld(World world) {
		this.world = world;
	}
	
}
