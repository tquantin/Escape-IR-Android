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

package fr.escape.resources.texture;

import fr.escape.graphics.Texture;
import fr.escape.resources.ResourcesLoader;

/**
 * <p>
 * A {@link ResourcesLoader} for {@link Texture}.
 * 
 */
public abstract class TextureLoader implements ResourcesLoader<Texture> {
	
	public static final String BACKGROUND_ERROR = "berror.png";
	public static final String BACKGROUND_LOST = "blost.png";
	public static final String BACKGROUND_MENU = "bmenu.png";
	public static final String BACKGROUND_VICTORY = "bvictory.png";
	public static final String BACKGROUND_INTRO = "bintro.png";
	public static final String BACKGROUND_JUPITER = "bjupiter.png";
	public static final String BACKGROUND_MOON = "bmoon.png";
	public static final String BACKGROUND_EARTH = "bearth.png";
	public static final String BACKGROUND_SPLASH = "bsplash.png";
	
	public static final String WEAPON_UI_DISABLED = "wuidisabled.png";
	public static final String WEAPON_UI_ACTIVATED = "wuiactivated.png";
	
	public static final String WEAPON_MISSILE = "wmissile.png";
	public static final String WEAPON_FIREBALL = "wfireball.png";
	public static final String WEAPON_SHIBOLEET = "wshiboleet.png";
	public static final String WEAPON_BLACKHOLE = "wblackhole.png";
	
	public static final String WEAPON_MISSILE_SHOT = "wsmissile.png";
	public static final String WEAPON_FIREBALL_CORE_SHOT = "wsfireballcore.png";
	public static final String WEAPON_FIREBALL_RADIUS_SHOT = "wsfireballradius.png";
	public static final String WEAPON_SHIBOLEET_SHOT = "wsshiboleet.png";
	public static final String WEAPON_BLACKHOLE_CORE_SHOT = "wsblackholecore.png";
	public static final String WEAPON_BLACKHOLE_LEFT_SHOT = "wsblackholehl.png";
	public static final String WEAPON_BLACKHOLE_RIGHT_SHOT = "wsblackholehr.png";
	public static final String WEAPON_BLACKHOLE_EVENT_HORIZON_SHOT = "wsblackholeeh.png";
	
	public static final String BONUS_WEAPON_MISSILE = "bwmissile.png";
	public static final String BONUS_WEAPON_FIREBALL = "bwfireball.png";
	public static final String BONUS_WEAPON_SHIBOLEET = "bwshiboleet.png";
	public static final String BONUS_WEAPON_BLACKHOLE = "bwblackhole.png";
	
	public static final String SHIP_RAPTOR = "sraptor.png";
	public static final String SHIP_RAPTOR_1 = "sraptor_1.png";
	public static final String SHIP_RAPTOR_2 = "sraptor_2.png";
	public static final String SHIP_RAPTOR_3 = "sraptor_3.png";
	public static final String SHIP_RAPTOR_4 = "sraptor_4.png";
	public static final String SHIP_RAPTOR_5 = "sraptor_5.png";
	public static final String SHIP_RAPTOR_6 = "sraptor_6.png";
	public static final String SHIP_RAPTOR_7 = "sraptor_7.png";
	public static final String SHIP_RAPTOR_8 = "sraptor_8.png";
	public static final String SHIP_RAPTOR_9 = "sraptor_9.png";
	public static final String SHIP_FALCON = "sfalcon.png";
	public static final String SHIP_VIPER = "sviper.png";
	
	public static final String BOSS_JUPITER = "bsjupiter.png";
	public static final String BOSS_MOON = "bsmoon.png";
	public static final String BOSS_MOON_1 = "bsmoon_1.png";
	public static final String BOSS_EARTH = "bsearth.png";
	public static final String BOSS_EARTH_1 = "bsearth_1.png";
	public static final String JUPITER_SPECIAL = "bssjupiter.png";
	public static final String MOON_SPECIAL = "bssmoon.png";
	public static final String EARTH_SPECIAL = "bssearth.png";
	
	public static final String MENU_UI_GRID = "muigrid.png";
	public static final String OVERLAY_STAR = "ostar.png";
	
	public static final String INTRO_JUPITER = "ijupiter.png";
	public static final String INTRO_MOON = "imoon.png";
	public static final String INTRO_EARTH = "iearth.png";
	
	@Override
	public String getPath() {
		return PATH+"/texture";
	}
	
}