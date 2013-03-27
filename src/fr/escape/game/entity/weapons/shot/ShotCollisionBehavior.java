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

package fr.escape.game.entity.weapons.shot;

import java.util.Objects;

import fr.escape.app.Foundation;
import fr.escape.game.User;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.Collisionable;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.ships.Ship;

/**
 * This class implement the {@link CollisionBehavior} for any {@link Shot}.
 */
public final class ShotCollisionBehavior implements CollisionBehavior {

	private static final String TAG = ShotCollisionBehavior.class.getSimpleName();
	
	@Override
	public void applyCollision(User user, Entity handler, Entity other, int type) {
		
		Objects.requireNonNull(user);
		Objects.requireNonNull(other);
		
		Shot shot = (Shot) Objects.requireNonNull(handler);
		shot.receive(Shot.MESSAGE_HIT);
		
		switch(type) {
			case Collisionable.PLAYER_TYPE: {
				
				Foundation.ACTIVITY.debug(TAG, "Shot hit a Player.");
				
				Ship player = (Ship) other;
				if(player.damage(shot.getDamage())) {
					user.removeOneLife();
				}
				
				break;
			}
			case Collisionable.NPC_TYPE: {
				
				Foundation.ACTIVITY.debug(TAG, "Shot hit a NPC.");
				Ship ship = (Ship) other;
				
				if(ship.damage(shot.getDamage())) {
					ship.toDestroy();
				}

				user.addScore(HIT_SCORE);
				
				break;
			}
			default: {
				throw new IllegalStateException(TAG+": Unknown touch contact {"+handler+", "+other+"}");
			}
		}
	}

}
