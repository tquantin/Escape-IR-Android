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

package fr.escape.graphics;

import fr.escape.app.Graphics;

/**
 * <p>
 * An interface called by {@link Graphics} when a render is needed.
 * 
 */
public interface RenderListener {
	
	/**
	 * <p>
	 * Render a new content on the Screen.
	 */
	public void render();
}
