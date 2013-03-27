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

import fr.escape.app.Input;

/**
 * <p>
 * An interface for Listener which need to handle {@link Input} Event such touch or move
 * 
 */
public interface EventListener {
	
	/**
	 * Touch Input Event
	 * 
	 * @param i Input Event of type touch.
	 * @return True if the EventListener handle it. 
	 */
	public boolean touch(Input i);
	
	/**
	 * Move Input Event
	 * 
	 * @param i Input Event of type move.
	 * @return True if the EventListener handle it. 
	 */
	public boolean move(Input i);
	
}
