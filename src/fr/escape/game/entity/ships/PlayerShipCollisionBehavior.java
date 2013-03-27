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
import fr.escape.game.entity.bonus.Bonus;
import fr.escape.game.entity.weapons.shot.Shot;

/**
 * {@link CollisionBehavior} for Player Ship.
 * 
 */
public final class PlayerShipCollisionBehavior implements CollisionBehavior {

	private static final String TAG = PlayerShipCollisionBehavior.class.getSimpleName();
	
	@Override
	public void applyCollision(User user, Entity handler, Entity other, int type) {

		Objects.requireNonNull(user);
		Objects.requireNonNull(other);
		Ship player = (Ship) Objects.requireNonNull(handler);
		
		switch(type) {
			case Collisionable.SHOT_TYPE: { 
				
				Foundation.ACTIVITY.debug(TAG, "Player hit a Shot.");
				
				Shot shot = (Shot) other;
				shot.receive(Shot.MESSAGE_HIT);
				
				if(player.damage(shot.getDamage())) {
					user.removeOneLife();
				}
				
				break;
			}
			case Collisionable.BONUS_TYPE: {
				
				Foundation.ACTIVITY.debug(TAG, "Player hit a Bonus.");
					
				Bonus bonus = (Bonus) other;
				user.addBonus(bonus.getWeapon(), bonus.getNumber());
					
				bonus.toDestroy();
				
				break;
			}
			case Collisionable.NPC_TYPE: {
				
				Foundation.ACTIVITY.debug(TAG, "Player hit a NPC.");
				
				Ship ship = (Ship) other;
				
				if(player.damage(HIT_DAMAGE)) {
					user.removeOneLife();
				}
				
				if(ship.damage(HIT_DAMAGE)) {
					ship.toDestroy();
				}
				
				user.addScore(HIT_SCORE / 2);
				
				break;
			}
			default: { 
				throw new IllegalStateException("Unknown touch contact {"+handler+", "+other+"}");
			}
		}
	}

}
