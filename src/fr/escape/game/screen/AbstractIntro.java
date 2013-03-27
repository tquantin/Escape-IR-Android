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

import java.util.Objects;

import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.Texture;
import fr.escape.resources.texture.TextureLoader;

/**
 * <p>
 * An abstract Class for Intro Screen.
 * 
 */
public abstract class AbstractIntro implements Screen {
	
	private final static long WAIT = 3000;
	
	private final Escape game;
	private final Runnable next;
	private final Texture drawable;
	private final Texture background;
	
	private long time;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 * @param drawable Drawable Icon for Intro
	 */
	public AbstractIntro(Escape game, Texture drawable) {
		
		this.game = Objects.requireNonNull(game);
		this.drawable = Objects.requireNonNull(drawable);
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_INTRO);
		this.next = new Runnable() {
			
			@Override
			public void run() {
				next();
			}
			
		};
	}
	
	@Override
	public void render(long delta) {
		
		time += delta;
		
		game.getGraphics().draw(background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		int x = (game.getGraphics().getWidth() / 2) - (drawable.getWidth() / 2);
		int y = (game.getGraphics().getHeight() / 2) - (drawable.getHeight() / 2);
		
		game.getGraphics().draw(drawable, x, y);
		
		if(time > WAIT) {
			game.getActivity().post(next);
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

	public abstract void next();
	
}
