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

package fr.escape.game.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;

import fr.escape.Objects;
import fr.escape.app.Graphics;
import fr.escape.app.Input;
import fr.escape.graphics.Font;
import fr.escape.graphics.Shapes;

/**
 * <p>
 * A static class for helping every implementation of {@link Screen}
 * to rendering easily.
 * 
 */
public final class Screens {

	private Screens() {}
	
	/**
	 * <p>
	 * Draw a String in the given position with a center effect.
	 * 
	 * @param graphics {@link Graphics} to use
	 * @param message String to display
	 * @param x Position X
	 * @param y Position Y
	 * @param font Font to use
	 * @param color Color to use
	 */
	public static void drawStringInCenterPosition(Graphics graphics, String message, int x, int y, Font font, int color) {
		
		Objects.requireNonNull(graphics).draw(message, (int) (x - ((message.length() / 2) * (font.getSize() / 2))), 
				(int) (y + (font.getSize() / 4)), font, color);
		
	}

	/**
	 * Draw a list of events on the screen.
	 * 
	 * @param graphics : {@link Graphics} to use.
	 * @param events : List of {@link Input}.
	 * @param color : {@link Color} to use.
	 * @return Return a List of {@link Input}.
	 */
	public static List<Input> drawEventsOnScreen(Graphics graphics, List<Input> events, int color) {
		
		Objects.requireNonNull(graphics);
		
		Input lastInput = null;
		List<Input> array = new ArrayList<Input>(events.size());
		Iterator<Input> it = events.iterator();
		
		// Find First Input
		if(it.hasNext()) {
			
			lastInput = it.next();
			array.add(lastInput);
			
			// Draw Line between Two Input 
			while(it.hasNext()) {
				
				Input input = it.next();

				graphics.draw(Shapes.createLine(lastInput.getX(), lastInput.getY(), input.getX(), input.getY()), color);
				lastInput = input;
				array.add(lastInput);
			}
			
		}

		return array;
	}
	
}
