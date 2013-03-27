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

import fr.escape.app.Input;

/**
 * This class implements {@link Gesture} to provide the BackOff and Forward {@link Gesture}.
 */
public final class Booster implements Gesture {
	private static int DEFAULT_FAULT_TOLERANCE = 25;
	private final static int COEFFICIENT = 1;
	private static double COEFFDIR = 4.0;
	private static float VELOCITY = 4.0f;

	@Override
	public boolean accept(Input start, List<Input> events, Input end,float[] velocity) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(events);
		Objects.requireNonNull(end);
		
		boolean isFront = start.getY() > end.getY();
		int faultTolerence = DEFAULT_FAULT_TOLERANCE;
  		
  		double div = end.getX()-start.getX();
  		double cd = (end.getY()-start.getY())/((div!=0)?div:1);
  		
  		if(cd > COEFFDIR || cd < -COEFFDIR) {
  			double pUp = (end.getY() + faultTolerence) - (cd * (end.getX() + faultTolerence));
      		double pDown = (end.getY() - faultTolerence) - (cd * (end.getX() - faultTolerence));
      		for(Input event : events) {
      			double yUp = cd * event.getX() + Math.max(pUp, pDown);
      			double yDown = cd * event.getX() + Math.min(pUp, pDown);
      			
      			if(event.getY() > yUp || event.getY() < yDown)
      				return false;
      		}
      		
      		velocity[0] = Math.abs((end.getY() - start.getY()) / COEFFICIENT);
      		velocity[1] = 0.0f;
      		velocity[2] = (isFront)?-VELOCITY:VELOCITY;
      		
      		return true;
  		}
  		
		return false;
	}
}
