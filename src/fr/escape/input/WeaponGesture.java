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

import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Input;
import fr.escape.resources.TextureLoader;

/**
 * This class implements {@link Gesture} to provide the {@link WeaponGesture}
 */
public final class WeaponGesture implements Gesture {
	
	private final Engine engine;
	private final int coefficient;
	private final int shotPosition;
	
	public WeaponGesture(Engine engine) {
		this.engine = engine;
		this.shotPosition = engine.getResources().getTexture(TextureLoader.SHIP_RAPTOR).getHeight() / 2;
		this.coefficient = 5;
	}
	
	@Override
	public boolean accept(Input start, List<Input> events, Input end, float[] velocity) {
		
		Objects.requireNonNull(start);
		Objects.requireNonNull(events);
		Objects.requireNonNull(end);
		
		int y = start.getY() - shotPosition;
		if(y <= end.getY()) return false;
				
		float distanceX = engine.getConverter().toMeterX(end.getX() - start.getX());
		float distanceY = engine.getConverter().toMeterX(end.getY() - y);
		
		double cd = (double) (end.getY() - y) / (end.getX() - start.getX());
		float angle = - (float) (180 * (Math.atan(cd) - Math.atan(0)) / Math.PI);
		
		velocity[0] = (angle < 0)?270-angle:90-angle;
		float max = Math.max(Math.abs(distanceX), Math.abs(distanceY));
		float coeff = coefficient / max;
		
		velocity[1] = distanceX * coeff;
		velocity[2] = distanceY * coeff;
				
  		return true;
  		
	}

}
