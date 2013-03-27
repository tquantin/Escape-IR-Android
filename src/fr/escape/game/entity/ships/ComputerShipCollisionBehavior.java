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

package fr.escape.game.entity.ships;

import java.util.Objects;

import fr.escape.app.Foundation;
import fr.escape.game.User;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.Collisionable;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.weapons.shot.Shot;

/**
 * {@link CollisionBehavior} for Computer/NPC Ship.
 * 
 */
public final class ComputerShipCollisionBehavior implements CollisionBehavior {

	private static final String TAG = ComputerShipCollisionBehavior.class.getSimpleName();
	
	@Override
	public void applyCollision(User user, Entity handler, Entity other, int type) {
		
		Objects.requireNonNull(user);
		Objects.requireNonNull(other);
		Ship ship = (Ship) Objects.requireNonNull(handler);
		
		switch(type) {
			case Collisionable.SHOT_TYPE: { 
				
				Foundation.ACTIVITY.debug(TAG, "NPC hit a Shot.");
				
				Shot shot = (Shot) other;
				shot.receive(Shot.MESSAGE_HIT);

				if(ship.damage(shot.getDamage())) {
					ship.toDestroy();
				}
				
				user.addScore(HIT_SCORE);
				
				break;
			}
			case Collisionable.PLAYER_TYPE: {
				
				Foundation.ACTIVITY.debug(TAG, "NPC hit a Player.");
				user.addScore(HIT_SCORE / 2);
				
				Ship player = (Ship) other;
				
				if(player.damage(HIT_DAMAGE)) {
					user.removeOneLife();
				}
				
				if(ship.damage(HIT_DAMAGE)) {
					ship.toDestroy();
				}
				
				break;
			}
			default: { 
				throw new IllegalStateException("Unknown touch contact {"+handler+", "+other+"}");
			}
		}
	}
	
}
