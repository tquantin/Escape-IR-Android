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
 * This class implements {@link Gesture} to provide the LeftSlide and RightSlide {@link Gesture}.
 */
public class Slide implements Gesture {
	
	private static int DEFAULT_FAULT_TOLERANCE = 25;
	private final static int COEFFICIENT = 1;
	private static double COEFFDIR = 0.2;
	private static float VELOCITY = 4.0f;

	@Override
	public boolean accept(Input start, List<Input> events, Input end, float[] velocity) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(events);
		Objects.requireNonNull(end);
		
		int faultTolerance = DEFAULT_FAULT_TOLERANCE;
		boolean isRight = start.getX() < end.getX();

		double div = end.getX()-start.getX();
  		double cd = (end.getY()-start.getY())/((div!=0)?div:1);
		
  		if(cd < COEFFDIR && cd > -COEFFDIR) {
  			double pUp = (end.getY() + faultTolerance) - (cd * (end.getX() + faultTolerance));
      		double pDown = (end.getY() - faultTolerance) - (cd * (end.getX() - faultTolerance));
      		for(Input event : events) {
      			double yUp = cd * event.getX() + pUp;
      			double yDown = cd * event.getX() + pDown;
      			if(event.getY() > yUp || event.getY() < yDown) 
      				return false;
      		}
      		
      		velocity[0] = Math.abs((end.getX() - start.getX())) / COEFFICIENT;
      		velocity[1] = (isRight)?VELOCITY:-VELOCITY;
      		velocity[2] = 0.0f;
      		
      		return true;
  		}
  		
		return false;
	}

}
