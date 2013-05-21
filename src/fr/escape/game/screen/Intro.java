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

import fr.escape.Objects;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.Texture;
import fr.escape.resources.TextureLoader;

/**
 * <p>
 * A Class for Intro Screen.
 * 
 */
public final class Intro implements Screen {
	
	private final static long WAIT = 3000;
	
	private final Escape game;
	private final int next;
	private final Texture drawable;
	private final Texture background;
	
	private long time;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 * @param drawable Drawable Icon for Intro
	 * @param next Action to execute after end of this Screen
	 */
	public Intro(Escape game, Texture drawable, int next) {
		
		this.game = Objects.requireNonNull(game);
		this.drawable = Objects.requireNonNull(drawable);
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_INTRO);
		this.next = next;
		
	}
	
	@Override
	public void render(long delta) {
		
		time += delta;
		
		game.getGraphics().draw(background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		int x = (game.getGraphics().getWidth() / 2) - (drawable.getWidth() / 2);
		int y = (game.getGraphics().getHeight() / 2) - (drawable.getHeight() / 2);
		
		game.getGraphics().draw(drawable, x, y);
		
		if(time > WAIT) {
			game.setScreenID(next);
		}
		
	}

	@Override
	public void show() {
		game.getOverlay().hide();
		time = 0;
	}

	@Override
	public void hide() {
		time = 0;
	}

	@Override
	public boolean touch(Input i) {
		return true;
	}

	@Override
	public boolean move(Input i) {
		return false;
	}
	
}
