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

package fr.escape.game.message;

/**
 * <p>
 * An interface for components which receive message from {@link Sender}.
 * 
 * <p>
 * If you need complex communication ability, <b>DO NOT</b> use this interface.
 * 
 * <p>
 * A message is an integer which have to be handle by the implementation of this Interface.
 * 
 * <p>
 * {@link Sender} and {@link Receiver} must be agreed about <i>Communication Specification</i>.
 * 
 * @see Sender
 */
public interface Receiver {
	
	/**
	 * Handle a message from a {@link Sender}
	 * 
	 * @param message The Message
	 */
	public void receive(int message);
	
}
