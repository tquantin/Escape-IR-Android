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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.SparseArray;

import fr.escape.Objects;
import fr.escape.android.R;
import fr.escape.app.Engine;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.game.scenario.Scenario;
import fr.escape.graphics.Texture;
import fr.escape.resources.scenario.ScenarioLoader;
import fr.escape.resources.scenario.ScenarioParser;

/**
 * <p>
 * Create a Index of available Resource and load them in memory if needed.
 * 
 * <p>
 * You cannot unload them after that.
 * 
 * <p>
 * For loading: simply use <i>get...(...)</i> 
 * 
 * <p>
 * <b>Note:</b> After instantiate this Object; You <b>HAVE TO</b> call <b>load()</b>
 *
 */
public final class Resources {
	
	/**
	 * Class TAG
	 */
	static final String TAG = Resources.class.getSimpleName();
	
	final SparseArray<Typeface> font;
	final SparseArray<Texture> texture;
	final Context context;
	
	/**
	 * Is all resources loaded in memory ?
	 */
	private boolean loaded;
	
	/**
	 * <p>
	 * Default Constructor for {@link Resources}.
	 * 
	 * <p>
	 * Don't forget to call load() after instantiation.
	 * 
	 * @param android Android Native Context Reference
	 */
	public Resources(Context android) {
		font = new SparseArray<Typeface>(1);
		texture = new SparseArray<Texture>(29);
		context = Objects.requireNonNull(android);
		loaded = false;
	}
	
	/**
	 * Create and Load Resource for Minimal Environment.
	 * 
	 * @throws IOException If an error has occurred during Minimal Configuration.
	 */
	public void minimal() throws IOException {
		textureLoader(TextureLoader.BACKGROUND_SPLASH);
	}
	
	/**
	 * Create and Load Resource
	 * 
	 * @throws IOException If an error has occurred during Resources Loading.
	 */
	public void load() throws IOException {
		if(loaded == false) {
			
			// Load Font
			fontLoader(R.string.font_visitor);
			
			// Load Texture
			textureLoader(TextureLoader.BACKGROUND_ERROR);
			textureLoader(TextureLoader.BACKGROUND_LOST);
			textureLoader(TextureLoader.BACKGROUND_MENU);
			textureLoader(TextureLoader.BACKGROUND_VICTORY);
			textureLoader(TextureLoader.BACKGROUND_INTRO);
			textureLoader(TextureLoader.BACKGROUND_JUPITER);
			textureLoader(TextureLoader.BACKGROUND_MOON);
			textureLoader(TextureLoader.BACKGROUND_EARTH);
			textureLoader(TextureLoader.WEAPON_UI_ACTIVATED);
			textureLoader(TextureLoader.WEAPON_UI_DISABLED);
			textureLoader(TextureLoader.WEAPON_BLACKHOLE);
			textureLoader(TextureLoader.WEAPON_FIREBALL);
			textureLoader(TextureLoader.WEAPON_MISSILE);
			textureLoader(TextureLoader.WEAPON_SHIBOLEET);
			textureLoader(TextureLoader.WEAPON_MISSILE_SHOT);
			textureLoader(TextureLoader.WEAPON_FIREBALL_CORE_SHOT);
			textureLoader(TextureLoader.WEAPON_FIREBALL_RADIUS_SHOT);
			textureLoader(TextureLoader.WEAPON_SHIBOLEET_SHOT);
			textureLoader(TextureLoader.WEAPON_BLACKHOLE_CORE_SHOT);
			textureLoader(TextureLoader.WEAPON_BLACKHOLE_LEFT_SHOT);
			textureLoader(TextureLoader.WEAPON_BLACKHOLE_RIGHT_SHOT);
			textureLoader(TextureLoader.WEAPON_BLACKHOLE_EVENT_HORIZON_SHOT);
			textureLoader(TextureLoader.BONUS_WEAPON_MISSILE);
			textureLoader(TextureLoader.BONUS_WEAPON_FIREBALL);
			textureLoader(TextureLoader.BONUS_WEAPON_SHIBOLEET);
			textureLoader(TextureLoader.BONUS_WEAPON_BLACKHOLE);
			
			textureLoader(TextureLoader.SHIP_RAPTOR);
			textureLoader(TextureLoader.SHIP_RAPTOR_1);
			textureLoader(TextureLoader.SHIP_RAPTOR_2);
			textureLoader(TextureLoader.SHIP_RAPTOR_3);
			textureLoader(TextureLoader.SHIP_RAPTOR_4);
			textureLoader(TextureLoader.SHIP_RAPTOR_5);
			textureLoader(TextureLoader.SHIP_RAPTOR_6);
			textureLoader(TextureLoader.SHIP_RAPTOR_7);
			textureLoader(TextureLoader.SHIP_RAPTOR_8);
			textureLoader(TextureLoader.SHIP_RAPTOR_9);
			textureLoader(TextureLoader.SHIP_FALCON);
			textureLoader(TextureLoader.SHIP_VIPER);
			
			textureLoader(TextureLoader.BOSS_JUPITER);
			textureLoader(TextureLoader.BOSS_MOON);
			textureLoader(TextureLoader.BOSS_MOON_1);
			textureLoader(TextureLoader.BOSS_EARTH);
			textureLoader(TextureLoader.BOSS_EARTH_1);
			textureLoader(TextureLoader.JUPITER_SPECIAL);
			textureLoader(TextureLoader.MOON_SPECIAL);
			textureLoader(TextureLoader.EARTH_SPECIAL);
			
			textureLoader(TextureLoader.MENU_UI_BUTTON_HISTORY);
			textureLoader(TextureLoader.MENU_UI_BUTTON_CUSTOM);
			textureLoader(TextureLoader.MENU_UI_BUTTON_BUILDER);
			textureLoader(TextureLoader.OVERLAY_STAR);
			
			textureLoader(TextureLoader.INTRO_JUPITER);
			textureLoader(TextureLoader.INTRO_MOON);
			textureLoader(TextureLoader.INTRO_EARTH);
			
		}
		
		loaded = true;
	}
	
	/**
	 * Load and return Typeface from {@link Resources} 
	 * 
	 * @param id Font ID
	 * @return Typeface
	 * @throws NoSuchElementException If the Typeface is not loaded.
	 * @throws IllegalStateException If the {@link Resources} is not loaded.
	 */
	public Typeface getFont(int name) throws NoSuchElementException {
		checkIfLoaded();
		try {
			return font.get(name);
		} catch(Exception e) {
			NoSuchElementException exception = new NoSuchElementException("Cannot load the given Font: "+name);
			exception.initCause(e);
			throw exception;
		}
	}
	
	/**
	 * Load and return Texture from {@link Resources}
	 * 
	 * @param name Texture ID
	 * @return Texture
	 * @throws NoSuchElementException If the Texture is not loaded.
	 * @throws IllegalStateException If the {@link Resources} is not loaded.
	 */
	public Texture getTexture(int id) throws NoSuchElementException {
		checkIfLoaded();
		try {
			return texture.get(id);
		} catch(Exception e) {
			NoSuchElementException exception = new NoSuchElementException("Cannot load the given Texture: "+id);
			exception.initCause(e);
			throw exception;
		}
	}
	
	/**
	 * Load and return Scenario from {@link ResourcesLoader}
	 * 
	 * @param name Scenario name
	 * @param factory Ship Factory for Scenario
	 * @return Scenario
	 * @throws NoSuchElementException
	 */
	public Scenario getScenario(String name, ShipFactory factory, boolean history) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(factory);
		checkIfLoaded();
		try {
			
			ScenarioLoader loader = createScenarioLoader(name, history);
			loader.addShipCreator(factory);
			return loader.load();
			
		} catch(Exception e) {
			NoSuchElementException exception = new NoSuchElementException("Cannot load the given Scenario: "+name);
			exception.initCause(e);
			throw exception;
		}
	}
	
	/**
	 * Create a ScenarioLoader for the given Scenario name.
	 * 
	 * @param scenarioID Scenario name
	 * @return ScenarioLoader which will load the Scenario
	 */
	private ScenarioLoader createScenarioLoader(final String scenarioID, final boolean history) {
		return new ScenarioLoader() {
			
			private Scenario scenario = null;
			
			@Override
			public Scenario load() throws Exception {
				if(scenario == null) {
					Engine.debug(TAG, "Load Scenario : "+scenarioID);
					
					InputStream stream = null;
					if(history) {
						stream = getContext().getAssets().open("level/"+scenarioID);
					} else {
						File path = Environment.getExternalStoragePublicDirectory("EscapeIR/Scenario");
						stream = new FileInputStream(new File(path,scenarioID));
					}
					
					try {
						scenario = ScenarioParser.parse(getShipCreator(), stream);
					} finally {
						stream.close();
					}
				}
				
				return scenario;
			}
			
		};
	}
	
	/**
	 * Load the Texture with the given ID into memory.
	 * 
	 * @param textureID Texture ID
	 * @throws IOException If an error has occurred
	 */
	private void textureLoader(int textureID) throws IOException {
		Engine.debug(TAG, "Load Texture ID: "+textureID);
		texture.put(textureID, new Texture(getContext().getResources(), textureID));
	}
	
	/**
	 * Load the Font with the given ID into memory.
	 * 
	 * @param fontID Font ID
	 * @throws IOException If an error has occurred
	 */
	private void fontLoader(int fontID) throws IOException {	
		Engine.debug(TAG, "Load Font ID: "+fontID);
		font.put(fontID, Typeface.createFromAsset(getContext().getAssets(), getContext().getString(fontID)));
	}
	
	/**
	 * <p>
	 * Check if the {@link Resources} object is loaded.<br>
	 * (ie: {@link Resources#load()} has been called once).
	 * 
	 * <p>
	 * Otherwise, throw an {@link IllegalStateException}.
	 */
	private void checkIfLoaded() {
		if(loaded == false) {
			throw new IllegalStateException("You must load all resources before using them. Use load()");
		}
	}
	
	/**
	 * Retrieve an InputStream for the given {@link ResourcesLoader} ID.
	 * 
	 * @param file Resource ID
	 * @return InputStream of File
	 */
	InputStream getInputStream(String file) {
		return getClass().getResourceAsStream(file);
	}

	/**
	 * Retrieve the Android Context.
	 * 
	 * @return Android Context
	 */
	public Context getContext() {
		return context;
	}
	
}
