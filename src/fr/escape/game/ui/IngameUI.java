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

import java.util.ArrayList;

import fr.escape.Objects;
import fr.escape.app.Input;
import fr.escape.app.Overlay;

/**
 * <p>
 * This Composite handle multiple Overlay.
 * 
 * <p>
 * These Overlay are used for User Interface.
 */
public final class IngameUI extends AbstractOverlay {
	
	/**
	 * List of Overlay
	 */
	private final ArrayList<Overlay> overlays;
	
	/**
	 * Default Constructor
	 */
	public IngameUI() {
		overlays = new ArrayList<Overlay>();
	}
	
	/**
	 * Add an {@link Overlay} on the Ingame Overlay.
	 * 
	 * @param overlay The Overlay
	 * @return Is successful
	 */
	public boolean add(Overlay overlay) {
		return overlays.add(Objects.requireNonNull(overlay));
	}
	
	/**
	 * Remove an {@link Overlay} inside the Ingame Overlay.
	 * 
	 * @param overlay The Overlay
	 * @return Is successful
	 */
	public boolean remove(Overlay overlay) {
		return overlays.remove(Objects.requireNonNull(overlay));
	}

	@Override
	public void render(long delta) {
		if(isVisible()) {
			for(int i = 0; i < overlays.size(); i++) {
				overlays.get(i).render(delta);
			}
		}
	}

	@Override
	public void show() {
		super.show();
		for(int i = 0; i < overlays.size(); i++) {
			overlays.get(i).show();
		}
	}

	@Override
	public void hide() {
		super.hide();
		for(int i = 0; i < overlays.size(); i++) {
			overlays.get(i).hide();
		}
	}

	@Override
	public boolean touch(Input touch) {
		
		Objects.requireNonNull(touch);
		
		if(isVisible()) {
			for(int i = 0; i < overlays.size(); i++) {
				if(overlays.get(i).touch(touch)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean move(Input move) {
		
		Objects.requireNonNull(move);
		
		if(isVisible()) {
			for(int i = 0; i < overlays.size(); i++) {
				if(overlays.get(i).touch(move)) {
					return true;
				}
			}
		}
		return false;
	}
}
