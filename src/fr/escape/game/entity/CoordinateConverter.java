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

package fr.escape.game.entity;

import org.jbox2d.dynamics.World;

import fr.escape.app.Foundation;
import fr.escape.app.Graphics;

/**
 * <p>
 * A static class which convert some {@link Graphics} Coordinate to {@link World} Coordinate for {@link Entity}, and vice versa.
 * 
 */
public final class CoordinateConverter {
	
	private static final float X_COEFF = Foundation.GRAPHICS.getWidth();
	private static final float Y_COEFF = Foundation.GRAPHICS.getHeight();
	private static final float W_COEFF = 10;
	
	/**
	 * You cannot instantiate this Class
	 */
	private CoordinateConverter() {}
	
	/**
	 * Convert a {@link Graphics} Coordinate to {@link World} Coordinate for X axis. 
	 * 
	 * @param x Graphics Coordinate
	 * @return World Coordinate
	 */
	public static float toMeterX(int x) {
		return (x / X_COEFF) * W_COEFF;
	}
	
	/**
	 * Convert a {@link Graphics} Coordinate to {@link World} Coordinate for Y axis. 
	 * 
	 * @param y Graphics Coordinate
	 * @return World Coordinate
	 */
	public static float toMeterY(int y) {
		return (y / Y_COEFF) * W_COEFF;
	}
	
	/**
	 * Convert a {@link World} Coordinate to {@link Graphics} Coordinate for X axis. 
	 * 
	 * @param x World Coordinate
	 * @return Graphics Coordinate
	 */
	public static int toPixelX(float x) {
		return (int) ((x / W_COEFF) * X_COEFF);
	}
	
	/**
	 * Convert a {@link World} Coordinate to {@link Graphics} Coordinate for Y axis. 
	 * 
	 * @param y World Coordinate
	 * @return Graphics Coordinate
	 */
	public static int toPixelY(float y) {
		return (int) ((y / W_COEFF) * Y_COEFF);
	}
}
