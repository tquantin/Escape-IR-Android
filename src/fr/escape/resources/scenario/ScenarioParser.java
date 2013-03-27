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

package fr.escape.resources.scenario;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.game.scenario.Scenario;

/**
 * <p>
 * A parser which parse *.scn file and create a Scenario. 
 *
 */
public final class ScenarioParser {

	/**
	 * Section 2 index
	 */
	private static final int SECTION_2_SHIP_ID = 0;
	private static final int SECTION_2_SHIP_TYPE = SECTION_2_SHIP_ID + 1;
	private static final int SECTION_2_SHIP_X = SECTION_2_SHIP_TYPE + 1;
	private static final int SECTION_2_SHIP_Y = SECTION_2_SHIP_X + 1;
	
	/**
	 * Exception default message
	 */
	private static final String EXCEPTION_MESSAGE = "Scenario file is corrupt";
	
	/**
	 * EOF Section
	 */
	private static final int END_SECTION = 5;
	
	/**
	 * Cannot be instantiate
	 */
	private ScenarioParser() {}
	
	/**
	 * <p>
	 * Parse the given file and return a {@link Scenario}.
	 * 
	 * @param factory Ship Factory for Scenario
	 * @param inputStream File to parse
	 * @return Scenario
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Scenario parse(ShipFactory factory, InputStream inputStream) throws FileNotFoundException, IOException {

		Objects.requireNonNull(factory);
		Objects.requireNonNull(inputStream);
		
		try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
			try(BufferedReader reader = new BufferedReader(inputStreamReader)) {
				
				String line;
				int section = 0;
				ScenarioConfiguration config = new ScenarioConfiguration();
					
				while((line = reader.readLine()) != null) {
					
					if(line.equals("%%")) {
						
						section++;
						
						if(section == END_SECTION) {
							return ScenarioFactory.create(config);
						}
						
					} else {
						
						switch(section) {
							case 1: {
								section1(line, config);
								break;
							}
							case 2: {
								section2(line, config);
								break;
							}
							case 3: {
								section3(line, config, factory);
								break;
							}
							case 4: {
								section4(line, config);
								break;
							}
							default: {
								throw new IOException(EXCEPTION_MESSAGE+": Unknown Section %%"+section);
							}
						}
						
					}
					
				}
				
			}
		}
		
		throw new IOException(EXCEPTION_MESSAGE+": Information for Scenario are missing");
	}
	
	/**
	 * Handle Section 1 Input: Set the Scenario ID
	 * 
	 * @param line Input Line from File
	 * @param configuration {@link ScenarioConfiguration}
	 * @throws IOException
	 */
	private static void section1(String line, ScenarioConfiguration configuration) throws IOException {
		try {
			configuration.setID(Integer.parseInt(line));
		} catch(Exception e) {
			throw new IOException(EXCEPTION_MESSAGE, e);
		}
	}
	
	/**
	 * Handle Section 2 Input: Set the Scenario Starting Time
	 *  
	 * @param line Input Line from File
	 * @param configuration {@link ScenarioConfiguration}
	 * @throws IOException
	 */
	private static void section2(String line, ScenarioConfiguration configuration) throws IOException {
		try {
			configuration.setTime(Integer.parseInt(line));
		} catch(Exception e) {
			throw new IOException(EXCEPTION_MESSAGE, e);
		}
	}
	
	/**
	 * Handle Section 3 Input: Initialize Ship
	 * 
	 * @param line Input Line from File
	 * @param configuration {@link ScenarioConfiguration}
	 * @param factory Ship Factory for creating Ship in Scenario
	 * @throws IOException
	 */
	private static void section3(String line, ScenarioConfiguration configuration, ShipFactory factory) throws IOException {
		try {
			
			String[] inputConfig = line.split("\\s+");
			
			int shipID = Integer.parseInt(inputConfig[SECTION_2_SHIP_ID]);
			int shipType = Integer.parseInt(inputConfig[SECTION_2_SHIP_TYPE]);
			float shipX = Float.parseFloat(inputConfig[SECTION_2_SHIP_X]);
			float shipY = Float.parseFloat(inputConfig[SECTION_2_SHIP_Y]);
			
			Ship ship = factory.createShipForScenario(shipType, shipX, shipY);
			
			configuration.addShip(shipID, ship);
			
		} catch(Exception e) {
			throw new IOException(EXCEPTION_MESSAGE, e);
		}
	}
	
	/**
	 * Handle Section 4 Input: Add Commandline
	 * 
	 * @param line Input Line from File
	 * @param configuration {@link ScenarioConfiguration}
	 * @throws IOException
	 */
	private static void section4(String line, ScenarioConfiguration configuration) throws IOException {
		try {
			configuration.addScript(line);
		} catch(Exception e) {
			throw new IOException(EXCEPTION_MESSAGE, e);
		}
	}
	
}
