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

package fr.escape.game.ui;

import fr.escape.app.Input;
import fr.escape.app.Overlay;
import fr.escape.input.EventListener;

/**
 * <p>
 * An Abstract Class for Overlay.
 * 
 * <p>
 * This handle many features required for Overlay.
 */
public abstract class AbstractOverlay implements Overlay {
	
	/**
	 * Is the Overlay visible on Screen ?
	 */
	private boolean isVisible;
	
	/**
	 * Default Constructor
	 */
	public AbstractOverlay() {
		isVisible = false;
	}
	
	/**
	 * @see Overlay#show()
	 */
	@Override
	public void show() {
		isVisible = true;
	}

	/**
	 * @see Overlay#hide()
	 */
	@Override
	public void hide() {
		isVisible = false;
	}

	/**
	 * <p>
	 * Return true if the Overlay is visible.
	 * 
	 * @return If the Overlay is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * @see EventListener#touch(Input)
	 */
	@Override
	public boolean touch(Input touch) {
		return false;
	}

	/**
	 * @see EventListener#move(Input)
	 */
	@Override
	public boolean move(Input move) {
		return false;
	}
	
}
