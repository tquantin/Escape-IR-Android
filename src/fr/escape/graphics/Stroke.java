/*****************************************************************************
 * 
 * Copyright 2012-2013 See AUTHORS file.
 * 
 * This file is part of Escape-IR.
 * 
 * Escape-IR is free software: you can redistribute it and/or modify
 * it under the terms of the zlib license. See the COPYING file.
 * 
 *****************************************************************************/

package fr.escape.graphics;

import fr.escape.Objects;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;

/**
 * <p>
 * Stroke used for drawing {@link Shape}.
 * 
 * <p>
 * Inspired from AWT Stroke.
 * 
 */
public final class Stroke {

	private static final int DEFAULT_WIDTH = 1;
	private static final Style DEFAULT_STYLE = Style.STROKE;
	private static final Join DEFAULT_JOIN = Join.MITER;
	private static final Cap DEFAULT_CAP = Cap.BUTT;
	
	private final int width;
	private final Style style;
	private final Join join;
	private final Cap cap;
	
	public Stroke() {
		this(DEFAULT_WIDTH);
	}
	
	public Stroke(int width) {
		this(width, DEFAULT_STYLE);
	}
	
	public Stroke(int width, Style style) {
		this(width, style, DEFAULT_JOIN, DEFAULT_CAP);
	}
	
	public Stroke(int width, Style style, Join join, Cap cap) {
		this.width = width;
		this.style = Objects.requireNonNull(style);
		this.join = Objects.requireNonNull(join);
		this.cap = Objects.requireNonNull(cap);
	}

	public int getWidth() {
		return width;
	}

	public Style getStyle() {
		return style;
	}

	public Cap getCap() {
		return cap;
	}

	public Join getJoin() {
		return join;
	}
	
}
