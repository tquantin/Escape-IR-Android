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
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import fr.escape.app.Game;
import fr.escape.app.Input;
import fr.escape.game.entity.weapons.Weapon;
import fr.escape.game.entity.weapons.Weapons;
import fr.escape.game.message.Receiver;
import fr.escape.game.message.Sender;
import fr.escape.graphics.Texture;
import fr.escape.resources.font.FontLoader;
import fr.escape.resources.texture.TextureLoader;

/**
 * <p>
 * An Overlay which show the available Weapons.
 * 
 */
public final class UIWeapons extends AbstractOverlay implements Sender {
	
	/**
	 * Rendering
	 */
	private final static int ITEM_TOP_MARGING = 3;
	private final static int ITEM_BOTTOM_MARGING = 3;
	private final static int ITEM_LEFT_MARGING = 5;
	private final static int ITEM_RIGHT_MARGING = 3;
	private final static int OVERLAY_TOP_MARGING = 50;
	private final static float FONT_SIZE = 10.0f;
	private final static Color FONT_COLOR = Color.WHITE;
	
	private final Game game;
	private final Font font;
	private final List<Weapon> weapons;
	private final List<Rectangle> touchArea;
	private final Receiver receiver;
	private final Texture active;
	private final Texture disable;
	
	private int x;
	private int y;
	private int width;
	private Weapon activeWeapon;
	
	public<L extends List<Weapon>, RandomAccess> UIWeapons(Game game, Receiver receiver, L weapons, Weapon activeWeapon) {
		
		if(Objects.requireNonNull(weapons).isEmpty()) {
			throw new IllegalArgumentException("weapons");
		}
		
		if(!weapons.contains(Objects.requireNonNull(activeWeapon))) {
			throw new IllegalArgumentException("activeWeapon");
		}
		
		this.game = Objects.requireNonNull(game);
		this.receiver = Objects.requireNonNull(receiver);
		
		this.font = game.getResources().getFont(FontLoader.VISITOR_ID).deriveFont(FONT_SIZE);
		
		this.weapons = weapons;
		this.activeWeapon = activeWeapon;
		
		this.active = game.getResources().getTexture(TextureLoader.WEAPON_UI_ACTIVATED);
		this.disable = game.getResources().getTexture(TextureLoader.WEAPON_UI_DISABLED);
		
		this.width = game.getGraphics().getWidth();
		this.y = OVERLAY_TOP_MARGING;
		this.x = this.width - (Weapons.getDrawableWidth() + ITEM_LEFT_MARGING + ITEM_RIGHT_MARGING);
		
		this.touchArea = new LinkedList<>();
		
		int offset = this.y;
		for(int i = 0; i < weapons.size(); i++) {
			
			Rectangle r = new Rectangle(
					this.x + ITEM_LEFT_MARGING, 
					offset + ITEM_TOP_MARGING,
					Weapons.getDrawableWidth() + ITEM_RIGHT_MARGING,
					Weapons.getDrawableHeight() + ITEM_BOTTOM_MARGING
			);
			
			offset += ITEM_TOP_MARGING + Weapons.getDrawableHeight() + ITEM_BOTTOM_MARGING;
			touchArea.add(r);
			
		}

	}

	@Override
	public void render(long delta) {

		int offset = this.y;
		for(Weapon w : weapons) {
			
			offset += ITEM_TOP_MARGING;
			
			renderWeaponDrawable(w.getDrawable(), this.x + ITEM_LEFT_MARGING, offset);
			renderWeaponAmmunition(String.valueOf(w.getAmmunition()), this.x + ITEM_LEFT_MARGING, (offset + Weapons.getDrawableHeight()), ITEM_LEFT_MARGING);
			renderWeaponOverlay(this.x + ITEM_LEFT_MARGING, offset, w.equals(activeWeapon), w.isEmpty());
			
			offset += (Weapons.getDrawableHeight() + ITEM_BOTTOM_MARGING);
		}
	}

	/**
	 * <p>
	 * Send the Weapons ID to the Receiver.
	 * 
	 * @param weaponsID Weapon ID
	 */
	@Override
	public void send(int weaponsID) {
		receiver.receive(weaponsID);
	}

	@Override
	public boolean touch(Input touch) {
		
		int i = 0;
		
		for(Rectangle rectangle : touchArea) {
			if(rectangle.contains(touch.getX(), touch.getY())) {
				Weapon w = weapons.get(i);
				if(!w.isEmpty()) {
					activeWeapon = w;
					send(i);
				}
				return true;
			}
			i++;
		}
		
		return false;
	}

	@Override
	public void register(Receiver receiver) {}
	
	/**
	 * Render a Weapon Icon.
	 * 
	 * @param weapon Texture to use
	 * @param x Position in X Axis
	 * @param y Position in Y Axis
	 */
	private void renderWeaponDrawable(Texture weapon, int x, int y) {
		game.getGraphics().draw(weapon, x, y);
	}
	
	/**
	 * Render number of Weapon Ammunition.
	 * 
	 * @param ammunition Current Weapon Ammunition
	 * @param x Position in X Axis
	 * @param y Position in Y Axis
	 * @param offset Use a offset (Font Size)
	 */
	private void renderWeaponAmmunition(String ammunition, int x, int y, int offset) {
		game.getGraphics().draw(ammunition, x + offset, y - offset, font, FONT_COLOR);
	}
	
	/**
	 * Render a Overlay for the Weapon Icon.
	 * 
	 * @param x Starting Position X
	 * @param y Starting Position Y
	 * @param actived Draw "Active" Texture 
	 * @param disabled Draw "Disable" Texture
	 */
	private void renderWeaponOverlay(int x, int y, boolean actived, boolean disabled) {
		if(disabled) {
			game.getGraphics().draw(disable, x, y);
		}
		if(actived) {
			game.getGraphics().draw(active, x, y);
		}
	}
	
}
