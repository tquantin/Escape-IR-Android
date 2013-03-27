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

package fr.escape.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import fr.escape.app.Game;
import fr.escape.game.User;
import fr.escape.game.entity.ships.Ship;
import fr.escape.resources.font.FontLoader;

/**
 * <p>
 * An Overlay which show the User Life and Ship Armor at the given time.
 * 
 * <p>
 * Information are received from {@link User} and {@link Ship}.
 */
public final class UIArmorLife extends AbstractOverlay {

	/**
	 * Rendering
	 */
	private static final int TOP_PADDING = 35;
	private static final int LEFT_MARGIN = 10;
	
	private final Ship player;
	private final User user;
	private final Game game;
	private final Font font;
	private final Color color;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Game
	 * @param player Ship played by User
	 * @param user User himself
	 */
	public UIArmorLife(Game game, Ship player, User user) {
		this.game = Objects.requireNonNull(game);
		this.user = Objects.requireNonNull(user);
		this.player = Objects.requireNonNull(player);
		this.font = game.getResources().getFont(FontLoader.VISITOR_ID);
		this.color = Color.WHITE;
	}
	
	@Override
	public void render(long delta) {
		if(isVisible()) {
			game.getGraphics().draw("Armor: "+getArmor()+"%", LEFT_MARGIN, TOP_PADDING, font, color);
			game.getGraphics().draw("Life: "+getLife(), LEFT_MARGIN, TOP_PADDING + 15, font, color);
		}
	}
	
	/**
	 * Get User Life
	 * 
	 * @return User Life
	 */
	private int getLife() {
		return user.getLife();
	}
	
	/**
	 * Get Ship Armor
	 * 
	 * @return Ship Armor
	 */
	private int getArmor() {
		
		int cLife = player.getCurrentLife();
		int iLife = player.getInitialLife();
				
		return (int) (((float) cLife / iLife) * 100);
	}

}
