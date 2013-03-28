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

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * <p>
 * A static class that help to create simple Shape
 * 
 */
public final class Shapes {

	/**
	 * Create a Circle Shape
	 * 
	 * @param centerX Center in X axis for Circle.
	 * @param centerY Center in Y axis for Circle.
	 * @param radius Radius of the Circle.
	 * @return A Circle Shape
	 */
	public static Shape createCircle(final int centerX, final int centerY, final float radius) {
		return new Shape() {
			
			@Override
			public void draw(Canvas canvas, Paint paint) {
				canvas.drawCircle(centerX, centerY, radius, paint);
			}
			
		};
	}
	
	/**
	 * Create a Rectangle Shape
	 * 
	 * @param x Bottom Right Corner in X Axis for Rectangle
	 * @param y Bottom Right Corner in Y Axis for Rectangle
	 * @return A Rectangle Shape
	 */
	public static Shape createRectangle(int x, int y) {
		return createRectangle(0, 0, x, y);
	}
	
	/**
	 * Create a Rectangle Shape
	 * 
	 * @param x Top Left Corner in X Axis for Rectangle
	 * @param y Top Left Corner in Y Axis for Rectangle
	 * @param width Bottom Right Corner in X Axis for Rectangle
	 * @param height Bottom Right Corner in Y Axis for Rectangle
	 * @return A Rectangle Shape
	 */
	public static Shape createRectangle(final int x, final int y, final int width, final int height) {
		return new Shape() {
			
			@Override
			public void draw(Canvas canvas, Paint paint) {
				canvas.drawRect(x, y, width, height, paint);
			}
			
		};
	}
	
	/**
	 * Create a Line Shape
	 * 
	 * @param x1 X Axis for Point A
	 * @param y1 Y Axis for Point A
	 * @param x2 X Axis for Point B
	 * @param y2 Y Axis for Point B
	 * @return A Line Shape
	 */
	public static Shape createLine(final int x1, final int y1, final int x2, final int y2) {
		return new Shape() {
			
			@Override
			public void draw(Canvas canvas, Paint paint) {
				canvas.drawLine(x1, x2, y1, y2, paint);
			}
			
		};
	}
	
}
