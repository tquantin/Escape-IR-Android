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

/**
 * <p>
 * An interface for Moveable Entity.
 * 
 */
public interface Moveable {
	
	/**
	 * Rotate the Entity with the given angle. 
	 * 
	 * @param angle Rotate the Entity.
	 * @throws UnsupportedOperationException If the {@link Moveable} cannot rotate.
	 */
	public void rotateBy(int angle);
	
	/**
	 * Set the Position of the Entity in X-Axis and Y-Axis.
	 * 
	 * @param x X Position on axis.
	 * @param y Y Position on axis.
	 * @throws UnsupportedOperationException If the {@link Moveable} move to a fixed position.
	 */
	public void moveTo(float x, float y);
	
	/**
	 * <p>
	 * Apply a velocity to this Entity.
	 * 
	 * <p>
	 * Velocity is an array with the given properties:
	 * 
	 * <p>
	 * velocity[0] : Duration of the movement.<br>
	 * velocity[1] : Force to apply on X.<br>
	 * velocity[2] : Force to apply on Y.<br>
	 * velocity[3] (optional) : Use to detect a looping.<br>
	 * 
	 * @param velocity
	 */
	public void moveBy(float[] velocity);
	
	/**
	 * Set the Rotation of the Entity in degree.
	 * 
	 * @param angle Angle in Degree.
	 * @throws UnsupportedOperationException If the {@link Moveable} cannot rotate.
	 */
	public void setRotation(int angle);
	
}
