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

package fr.escape.graphics;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import android.graphics.Path;
import android.graphics.Path.Direction;

/**
 * <p>
 * A static class that help to create simple Shape
 * 
 */
public final class Paths {

	/**
	 * Create a Circle Path
	 * 
	 * @param centerX Center in X axis for Circle.
	 * @param centerY Center in Y axis for Circle.
	 * @param radius Radius of the Circle.
	 * @return A Circle Path
	 */
	public static Path createCircle(int centerX, int centerY, float radius) {
		Path circle = new Path();
		circle.addCircle(centerX, centerY, radius, Direction.CW);
		return circle;
	}
	
	/**
	 * Create a Rectangle Path
	 * 
	 * @param x Bottom Right Corner in X Axis for Rectangle
	 * @param y Bottom Right Corner in Y Axis for Rectangle
	 * @return A Rectangle Path
	 */
	public static Path createRectangle(int x, int y) {
		Path rectangle = new Path();
		rectangle.addRect(0, 0, x, y, Direction.CW);
		return rectangle;
	}
	
	/**
	 * Create a Rectangle Path
	 * 
	 * @param x Top Left Corner in X Axis for Rectangle
	 * @param y Top Left Corner in Y Axis for Rectangle
	 * @param width Bottom Right Corner in X Axis for Rectangle
	 * @param height Bottom Right Corner in Y Axis for Rectangle
	 * @return A Rectangle Path
	 */
	public static Path createRectangle(int x, int y, int width, int height) {
		Path rectangle = new Path();
		rectangle.addRect(x, y, width, height, Direction.CW);
		return rectangle;
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
	public static Shape createLine(int x1, int y1, int x2, int y2) {
		Shape line = new Line2D.Double(x1, y1, x2, y2);
		return line;
	}
	
}
