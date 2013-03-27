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

package fr.escape.input;

import java.util.List;

import fr.escape.app.Input;

/**
 * <p>
 * An interface which represent an User Gesture ingame.
 * 
 */
public interface Gesture {
	
	/**
	 * Verify if the given {@link Gesture} was valid.
	 * 
	 * @param start : First {@link Input} for this {@link Gesture}
	 * @param events : List of all {@link Input} use for this {@link Gesture}
	 * @param end : Last {@link Input} for this {@link Gesture}.
	 * @param velocity : Array in which the velocity for the {@link Gesture} will be set.
	 * @return True if the Gesture is detected as successful.
	 */
	public boolean accept(Input start, List<Input> events, Input end, float[] velocity);
}
