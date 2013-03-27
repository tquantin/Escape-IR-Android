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
 * An interface for components which send message to {@link Receiver}.
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
 * @see Receiver
 */
public interface Sender {
	
	/**
	 * Send a message to one/all {@link Receiver}.
	 * 
	 * @param message The Message
	 */
	public void send(int message);
	
	/**
	 * Register a {@link Receiver} for receiving Message.
	 * 
	 * @param receiver A Receiver
	 */
	public void register(Receiver receiver);
	
}
