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

package fr.escape.resources.font;

import java.awt.Font;

import fr.escape.resources.ResourcesLoader;

/**
 * <p>
 * A {@link ResourcesLoader} for {@link Font}.
 * 
 */
public abstract class FontLoader implements ResourcesLoader<Font> {
	
	public static final String VISITOR_ID = "visitor.ttf";
	
	@Override
	public String getPath() {
		return PATH+"/font";
	}
	
}
