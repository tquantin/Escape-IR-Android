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

import java.util.Objects;

import fr.umlv.zen2.MotionEvent;
import fr.umlv.zen2.MotionEvent.Kind;

/**
 * <p>
 * A Wrapper for User Input.
 * 
 * <p>
 * This class can handle input such as Mouse, Network and/or Keyboard.
 */
public final class Input {
	
	private static final int DELTA_FILTER = 4;
	
	/**
	 * Mouse Input.
	 */
	private final MotionEvent event;
	
	/**
	 * Default Constructor
	 * 
	 * @param event Mouse Input Event
	 */
	public Input(MotionEvent event) {
		Objects.requireNonNull(event);
		this.event = event;
	}
	
	/**
	 * Get Mouse Input X Coordinate.
	 * 
	 * @return X coordinate.
	 */
	public int getX() {
		return event.getX();
	}
	
	/**
	 * Get Mouse Input Y Coordinate.
	 * 
	 * @return Y coordinate.
	 */
	public int getY() {
		return event.getY();
	}
	
	/**
	 * Get Mouse Input Kind.
	 * 
	 * @return Mouse Kind
	 */
	public Kind getKind() {
		return event.getKind();
	}
	
	@Override
	public String toString() {
		return getX()+" "+getY()+": "+getKind();
	}
	
	/**
	 * Should we need to filter and delete this new Input from the last one.
	 */
	public boolean filter(Input input) {
		
		double mDelta = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
		double aDelta = Math.sqrt(Math.pow(input.getX(), 2) + Math.pow(input.getY(), 2));
		
		if(Math.abs(mDelta - aDelta) < DELTA_FILTER) {
			return true;
		}
		
		return false;
	}
	
}
