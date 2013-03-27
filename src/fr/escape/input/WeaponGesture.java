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
import java.util.Objects;

import fr.escape.app.Foundation;
import fr.escape.app.Input;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.resources.texture.TextureLoader;

/**
 * This class implements {@link Gesture} to provide the {@link WeaponGesture}
 */
public final class WeaponGesture implements Gesture {
	private static int COEFFICIENT = 5;
	private static int SHOT_POS = Foundation.RESOURCES.getTexture(TextureLoader.SHIP_RAPTOR).getHeight() / 2;
	
	@Override
	public boolean accept(Input start, List<Input> events, Input end, float[] velocity) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(events);
		Objects.requireNonNull(end);
		
		int y = start.getY() - SHOT_POS;
		if(y <= end.getY()) return false;
				
		float distanceX = CoordinateConverter.toMeterX(end.getX() - start.getX());
		float distanceY = CoordinateConverter.toMeterX(end.getY() - y);
		
		double cd = (double) (end.getY() - y) / (end.getX() - start.getX());
		float angle = - (float) (180 * (Math.atan(cd) - Math.atan(0)) / Math.PI);
		
		velocity[0] = (angle < 0)?270-angle:90-angle;
		float max = Math.max(Math.abs(distanceX), Math.abs(distanceY));
		float coeff = COEFFICIENT / max;
		
		velocity[1] = distanceX * coeff;
		velocity[2] = distanceY * coeff;
				
  		return true;
	}

}
