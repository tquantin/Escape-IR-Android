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

package fr.escape;


/**
 * Static class which implements Java 7 method
 * for Java 6 Environment.
 */
public final class Objects {

	/**
	 * <p>
	 * Checks that the specified object reference is not null. 
	 * 
	 * <p>This method is designed primarily for doing parameter validation in methods and constructors, as demonstrated below:
	 * 
	 * <pre>
	 *     public Foo(Bar bar) {
	 *         this.bar = Objects.requireNonNull(bar);
	 *     }
	 * </pre>
	 * 
	 * @param o The object reference to check for nullity.
	 * @return Object if not not null.
	 * @throws NullPointerException If Object if null.
	 */
	public static <T> T requireNonNull(T o) {
		return requireNonNull(o, null);
	}
	
	/**
	 * <p>
	 * Checks that the specified object reference is not null. 
	 * 
	 * <p>This method is designed primarily for doing parameter validation in methods and constructors, as demonstrated below:
	 * 
	 * <pre>
	 *     public Foo(Bar bar) {
	 *         this.bar = Objects.requireNonNull(bar);
	 *     }
	 * </pre>
	 * 
	 * @param o The object reference to check for nullity.
	 * @return Object if not not null.
	 * @throws NullPointerException If Object if null.
	 */
	public static <T> T requireNonNull(T o, String message) {
		if(o == null) {
			throw new NullPointerException(message);
		}
		
		return o;
	}
	
}
