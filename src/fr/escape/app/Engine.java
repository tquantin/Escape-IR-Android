package fr.escape.app;

import fr.escape.Objects;
import fr.escape.resources.Resources;
import android.util.Log;

public final class Engine {

	private static final String TAG = Engine.class.getSimpleName();
	
	private final Game game;
	
	/**
	 * Constructor with default Configuration and a given Game.
	 * 
	 * @param game The Game
	 */
	public Engine(Game game) {
		this(game, new Configuration());
	}
	
	/**
	 * Default Constructor with a given Game and Configuration.
	 * 
	 * @param game The Game
	 * @param configuration Configuration to use.
	 */
	public Engine(Game game, Configuration configuration) {
		
		//this.graphics = new Graphics(Objects.requireNonNull(game), Objects.requireNonNull(configuration));
		this.game = game;

		debug(TAG, "Graphics Engine created");
	}
	
	/**
	 * Create Engine Components.
	 */
	public void create() {
		
		//Foundation.ACTIVITY = this;
		//Foundation.GRAPHICS = graphics;
		//Foundation.RESOURCES = new Resources();
		
		//initialize();
	}
	
	/** 
	 * Logs a message to the console.
	 */
	public void log(String tag, String message) {
		Log.i(tag, message);
	}

	/**
	 * Logs a message to the console.
	 */
	public void log(String tag, String message, Exception exception) {
		Log.i(tag, message, exception);
	}

	/** 
	 * Logs an error message to the console.
	 */
	public void error(String tag, String message) {
		Log.e(tag, message);
	}

	/** 
	 * Logs an error message to the console.
	 */
	public void error(String tag, String message, Throwable exception) {
		Log.e(tag, message, exception);
	}

	/** 
	 * Logs a debug message to the console.
	 */
	public void debug(String tag, String message) {
		Log.d(tag, message);
	}

	/** 
	 * Logs a debug message to the console.
	 */
	public void debug(String tag, String message, Throwable exception) {
		Log.d(tag, message, exception);
	}
	
	/**
	 * Push an Event to EventListener
	 */
	public void event(final Input event) {

		Objects.requireNonNull(event);
		
	}
	
}
