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

package fr.escape.resources;

/**
 * An interface for Loading a Resource in Memory from JAR.
 * 
 * @param <T> A Resource
 */
public interface ResourcesLoader<T> {
	
	/**
	 * Root Container Path for Resources.
	 */
	public static final String PATH = "/fr/escape/resources";
	
	/**
	 * Load in Memory and Return the Resources.
	 */
	public T load() throws Exception;
	
	/**
	 * Return the Container Path for Resources.
	 */
	public String getPath();
	
}
