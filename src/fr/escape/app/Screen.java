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

import fr.escape.input.EventListener;

/**
 * Represents one of many game screens, like main menu, or settings etc...
 * 
 * @see Game
 */
public interface Screen extends EventListener {
	
	/**
	 * Called when the screen should render itself.
	 * 
	 * @param delta The time in seconds since the last render.
	 */
	public void render(long delta);

	/** 
	 * Called when this screen becomes the current screen for a {@link Game}.
	 */
	public void show();

	/** 
	 * Called when this screen is no longer the current screen for a {@link Game}.
	 */
	public void hide();

}
