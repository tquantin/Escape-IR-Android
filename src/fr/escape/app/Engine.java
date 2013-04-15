package fr.escape.app;

import java.util.LinkedList;
import java.util.Queue;

import org.jbox2d.dynamics.World;

import fr.escape.Objects;
import fr.escape.game.Escape;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.input.EventListener;
import fr.escape.resources.Resources;
import android.content.Context;
import android.util.Log;

public final class Engine implements Runnable {

	/**
	 * Class TAG
	 */
	private static final String TAG = Engine.class.getSimpleName();
	
	/**
	 * World Updating
	 */
	static final float WORLD_STEP = 1.0f / 60.0f;
	static final int WORLD_UPDATE = (int) (WORLD_STEP * 1000);
	static final int WORLD_VELOCITY_ITERATION = 6;
	static final int WORLD_POSITION_ITERATION = 2;
	
	/**
	 * Engine Properties
	 */
	final Escape game;
	final Graphics graphics;
	final Thread thread;
	final Resources resources;
	final Queue<Runnable> runnables;
	final CoordinateConverter converter;

	/**
	 * Engine World State
	 */
	int worldUpdateLeft;
	
	/**
	 * Constructor with default Configuration and a given Game.
	 * 
	 * @param context Android Context
	 * @param game The Game
	 */
	public Engine(Context context, Escape game) {
		this(context, game, new Configuration());
	}
	
	/**
	 * Default Constructor with a given Game and Configuration.
	 * 
	 * @param context Android Context
	 * @param game The Game
	 * @param configuration Configuration to use.
	 */
	public Engine(Context context, Escape game, Configuration configuration) {
		
		this.graphics = new Graphics(Objects.requireNonNull(game), Objects.requireNonNull(configuration));
		this.game = game;
		this.thread = new Thread(this);
		this.resources = new Resources(Objects.requireNonNull(context));
		this.runnables = new LinkedList<Runnable>();
		
		this.worldUpdateLeft = 0;
		
		this.converter = new CoordinateConverter(graphics.getWidth(), graphics.getHeight(), 10);
		
	}
	
	/**
	 * Create Engine Components.
	 * 
	 * @param context Android Context
	 */
	public void create(Context context) {
		thread.setName("Engine Looper");
		//thread.start();
	}
	
	/** 
	 * Logs a message to the console.
	 */
	public static void log(String tag, String message) {
		Log.i(tag, message);
	}

	/**
	 * Logs a message to the console.
	 */
	public static void log(String tag, String message, Exception exception) {
		Log.i(tag, message, exception);
	}

	/** 
	 * Logs an error message to the console.
	 */
	public static void error(String tag, String message) {
		Log.e(tag, message);
	}

	/** 
	 * Logs an error message to the console.
	 */
	public static void error(String tag, String message, Throwable exception) {
		Log.e(tag, message, exception);
	}

	/** 
	 * Logs a debug message to the console.
	 */
	public static void debug(String tag, String message) {
		Log.d(tag, message);
	}

	/** 
	 * Logs a debug message to the console.
	 */
	public static void debug(String tag, String message, Throwable exception) {
		Log.d(tag, message, exception);
	}
	
	/**
	 * Push an Event to EventListener
	 */
	public void event(final Input event) {

		Objects.requireNonNull(event);
		
	}

	@Override
	public void run() {
		try {
			
			try {
				
				getResources().load();
				Thread.sleep(2000);
				
			} catch (Exception ioe) {
				throw new RuntimeException("Unable to load Resources");
			}
			
			
			
			debug(TAG, "Application started");

			debug(TAG, "Create Game");
			getGame().create(this);
			
			while(!Thread.currentThread().isInterrupted()) {
				
				int executionTime = 0;
				
				/**
				 * May need to implements QoS in Runnable execution.
				 */
				/*if(!getRunnables().isEmpty()) {
					
					long start = System.currentTimeMillis();
					Runnable next;
					
					while((next = getRunnables().poll()) != null) {
						try {
							next.run();
						} catch(Throwable t) {
							error(TAG, "Error while executing a Runnable", t);
						}
					}
					
					executionTime = (int) (System.currentTimeMillis() - start);
				}*/
				
				try {
					
					executionTime = 0;
					
					int sleep = getGraphics().getNextWakeUp() - executionTime;
					long start = System.currentTimeMillis();
					updateWorld(sleep);
					sleep -= ((int) (System.currentTimeMillis() - start));
					
					//error(TAG, "Sleep: "+sleep);
					
					if(sleep > 0) {
						Thread.sleep(sleep);
					}
					
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				getGraphics().render();
				
			}
			
		} finally {
			debug(TAG, "Application closed");			
		}
	}
	
	/**
	 * <p>
	 * Return the Game.
	 * 
	 * @return Game
	 */
	private Escape getGame() {
		return game;
	}
	
	/**
	 * <p>
	 * Return the Graphics Engine.
	 * 
	 * @return Graphics Engine 
	 */
	public Graphics getGraphics() {
		return graphics;
	}
	
	/**
	 * <p>
	 * Return the {@link Resources}
	 * 
	 * @return Resources
	 */
	public Resources getResources() {
		return resources;
	}
	
	/**
	 * <p>
	 * Return the Game's {@link World}
	 * 
	 * @return {@link World}
	 */
	World getWorld() {
		return game.getWorld();
	}
	
	/**
	 * <p>
	 * Return Runnable Queue.
	 * 
	 * @return Runnable Queue
	 */
	Queue<Runnable> getRunnables() {
		return runnables;
	}
	
	/**
	 * <p>
	 * Return Event Listener.
	 * 
	 * @return Event Listener
	 */
	EventListener getEventListener() {
		return game;
	}
	
	/**
	 * <p>
	 * Return Coordinate Converter.
	 * 
	 * @return {@link CoordinateConverter}
	 */
	public CoordinateConverter getConverter() {
		return converter;
	}
	
	/**
	 * Push a Runnable into Execution Queue for next loop.
	 * 
	 * @param runnable Runnable to execute.
	 */
	public void post(Runnable runnable) {
		getRunnables().add(runnable);
	}
	
	/**
	 * Compute if we need to update the World Step.
	 * 
	 * @param sleep Sleep time for the given loop in {@link Engine#run()}.
	 * @see World
	 */
	private void updateWorld(int sleep) {

		worldUpdateLeft += sleep;
		
		while(worldUpdateLeft >= WORLD_UPDATE) {
			getWorld().step(WORLD_STEP, WORLD_VELOCITY_ITERATION, WORLD_POSITION_ITERATION);
			worldUpdateLeft -= WORLD_UPDATE;
		}
		
	}

}
