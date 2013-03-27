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

package fr.escape.game.entity.bonus;

import java.util.Objects;

import fr.escape.app.Foundation;
import fr.escape.game.User;
import fr.escape.game.entity.CollisionBehavior;
import fr.escape.game.entity.Collisionable;
import fr.escape.game.entity.Entity;

/**
 * <p>
 * A {@link CollisionBehavior} for {@link Bonus}
 * 
 */
public final class BonusCollisionBehavior implements CollisionBehavior {

	private static final String TAG = BonusCollisionBehavior.class.getSimpleName();

	@Override
	public void applyCollision(User user, Entity handler, Entity other, int type) {
		
		Objects.requireNonNull(user);
		Objects.requireNonNull(other);
		Bonus bonus = (Bonus) Objects.requireNonNull(handler);
		
		switch(type) {
			case Collisionable.PLAYER_TYPE: {
				
				Foundation.ACTIVITY.error(TAG, "Bonus hit by Player.");
				user.addBonus(bonus.getWeapon(), bonus.getNumber());
				bonus.toDestroy();
				
				break;
			}
			default: {
				throw new IllegalStateException(TAG+": Unknown touch contact {"+handler+", "+other+"}");
			}
		}
	}

}
