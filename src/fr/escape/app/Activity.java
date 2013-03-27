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

package fr.escape.app;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import org.jbox2d.dynamics.World;

import fr.escape.graphics.Texture;
import fr.escape.input.EventListener;
import fr.escape.resources.Resources;
import fr.escape.resources.texture.TextureLoader;
import fr.umlv.zen2.Application;
import fr.umlv.zen2.ApplicationCode;
import fr.umlv.zen2.ApplicationContext;
import fr.umlv.zen2.MotionEvent;
import fr.umlv.zen2.MotionEvent.Kind;

/**
 * <p>
 * Core Engine for Escape-IR
 * 
 * <p>
 * This class initialize all necessary components for the Game.
 * 
 * <p>
 * <b>Note :</b> DO NOT forget to call {@link Activity#create()} for launching it.
 */
public final class Activity {

	static final String TAG = Activity.class.getSimpleName();
	
	/**
	 * Log Level
	 */
	public static final int LOG_NONE = 0;
	public static final int LOG_DEBUG = 3;
	public static final int LOG_INFO = 2;
	public static final int LOG_ERROR = 1;
	
	/**
	 * World Updating
	 */
	private static final float WORLD_STEP = 1.0f / 60.0f;
	private static final int WORLD_UPDATE = (int) (WORLD_STEP * 1000);
	
	private final Game game;
	private final Graphics graphics;
	private final Queue<Runnable> runnables = new LinkedList<>();
	private final String title;
	
	private int worldUpdateLeft;
	private int logLevel;
	
	/**
	 * Constructor with default Configuration and a given Game.
	 * 
	 * @param game The Game
	 */
	public Activity(Game game) {
		this(game, new Configuration());
	}
	
	/**
	 * Default Constructor with a given Game and Configuration.
	 * 
	 * @param game The Game
	 * @param configuration Configuration to use.
	 */
	public Activity(Game game, Configuration configuration) {
		
		this.graphics = new Graphics(Objects.requireNonNull(game), Objects.requireNonNull(configuration));
		this.logLevel = LOG_INFO;
		this.title = configuration.getTitle();
		this.game = game;
		this.worldUpdateLeft = 0;

		debug(TAG, "Graphics Engine created");
	}
	
	/**
	 * <p>
	 * Create Activity Components (ie: {@link Foundation}) and launch it.
	 */
	public void create() {
		
		Foundation.ACTIVITY = this;
		Foundation.GRAPHICS = graphics;
		Foundation.RESOURCES = new Resources();
		
		initialize();
	}
	
	/**
	 * Initialize and Run the Game with Graphics Engine and Activity Core
	 */
	private void initialize () {
		
		game.getResources().load();
		
		// Create Splash Screen
		final Screen splash = new Screen() {

			private final Texture background = getGame().getResources().getTexture(TextureLoader.BACKGROUND_SPLASH);
			
			@Override
			public boolean touch(Input i) {
				return false;
			}

			@Override
			public boolean move(Input i) {
				return false;
			}

			@Override
			public void render(long delta) {
				getGraphics().draw(background, 0, 0, getGraphics().getWidth(), getGraphics().getHeight());
			}

			@Override
			public void show() {}

			@Override
			public void hide() {}
			
		};
		
		Application.run(this.title, graphics.getWidth(), graphics.getHeight(), new ApplicationCode() {
			
			@Override
			public void run(ApplicationContext context) {
				
				try {
					
					debug(TAG, "Application started");
					Input lastEvent = null;
					
					debug(TAG, "Show Splash Screen");
					getGame().setScreen(splash);
					getGraphics().render(context);
					
					debug(TAG, "Create Game");
					getGame().create();
					
					for(;;) {
						
						int executionTime = 0;
						
						MotionEvent mEvent = context.pollMotion();
						int mStack = 30;
						
						while(mEvent != null && mStack > 0) {
							Input event = new Input(mEvent);
							event(event, lastEvent);
							lastEvent = event;
							mEvent = context.pollMotion();
							mStack--;
						}
						
						/**
						 * May need to implements QoS in Runnable execution.
						 */
						if(!getRunnables().isEmpty()) {
							
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
						}
						
						try {
							
							int sleep = getGraphics().getNextWakeUp() - executionTime;
							long start = System.currentTimeMillis();
							updateWorld(sleep);
							sleep -= ((int) (System.currentTimeMillis() - start));
							
							if(sleep > 0) {
								Thread.sleep(sleep);
							}
							
						} catch(InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						
						getGraphics().render(context);
						
					}
					
				} finally {
					debug(TAG, "Application closed");			
				}
				
			}
			
		});
		
	}

	/** 
	 * Logs a message to the console.
	 */
	public void log(String tag, String message) {
		if(logLevel >= LOG_INFO) {
			System.out.println(tag + ": " + message);
		}
	}

	/**
	 * Logs a message to the console.
	 */
	public void log(String tag, String message, Exception exception) {
		if(logLevel >= LOG_INFO) {
			log(tag, message);
			exception.printStackTrace(System.out);
		}
	}

	/** 
	 * Logs an error message to the console.
	 */
	public void error(String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			System.err.println(tag + ": " + message);
		}
	}

	/** 
	 * Logs an error message to the console.
	 */
	public void error(String tag, String message, Throwable exception) {
		if(logLevel >= LOG_ERROR) {
			error(tag, message);
			exception.printStackTrace(System.err);
		}
	}

	/** 
	 * Logs a debug message to the console.
	 */
	public void debug(String tag, String message) {
		if(logLevel >= LOG_DEBUG) {
			System.out.println(tag + ": " + message);
		}
	}

	/** 
	 * Logs a debug message to the console.
	 */
	public void debug(String tag, String message, Throwable exception) {
		if(logLevel >= LOG_DEBUG) {
			debug(tag, message);
			exception.printStackTrace(System.out);
		}
	}

	/** 
	 * Sets the log level. 
	 * 
	 * {@link #LOG_NONE} will mute all log output. 
	 * {@link #LOG_ERROR} will only let error messages through.
	 * {@link #LOG_INFO} will let all non-debug messages through, and {@link #LOG_DEBUG} will let all messages through.
	 * 
	 * @param logLevel {@link #LOG_NONE}, {@link #LOG_ERROR}, {@link #LOG_INFO}, {@link #LOG_DEBUG}. 
	 */
	public void setLogLevel(int logLevel) {
		switch(logLevel) {
			case LOG_NONE: case LOG_ERROR: case LOG_INFO: case LOG_DEBUG: {
				this.logLevel = logLevel;
				break;
			}
			default: {
				throw new IllegalArgumentException("Unknown Log Level");
			}
		}
		
	}
	
	/**
	 * <p>
	 * Return the Game.
	 * 
	 * <p>
	 * Wrapper for Anonymous Class in {@link Activity#initialize()}.
	 * 
	 * @return Game
	 */
	Game getGame() {
		return game;
	}
	
	/**
	 * <p>
	 * Return the Graphics Engine.
	 * 
	 * <p>
	 * Wrapper for Anonymous Class in {@link Activity#initialize()}.
	 * 
	 * @return Graphics Engine 
	 */
	Graphics getGraphics() {
		return graphics;
	}
	
	/**
	 * <p>
	 * Return Runnable Queue.
	 * 
	 * <p>
	 * Wrapper for Anonymous Class in {@link Activity#initialize()}.
	 * 
	 * @return Runnable Queue
	 */
	Queue<Runnable> getRunnables() {
		return runnables;
	}
	
	/**
	 * <p>
	 * Return Runnable Queue.
	 * 
	 * <p>
	 * Wrapper for Anonymous Class in {@link Activity#initialize()}.
	 * 
	 * @return Event Listener
	 */
	EventListener getEventListener() {
		return this.game;
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
	 * Push an Event to EventListener
	 */
	public void event(final Input event, final Input lastEvent) {

		Objects.requireNonNull(event);
		
		switch(event.getKind()) {
			case ACTION_DOWN: {
				break;
			}
			case ACTION_MOVE: {
				
				if(!lastEvent.filter(event)) {
					post(new Runnable() {
						
						@Override
						public void run() {
							getEventListener().move(lastEvent);
						}
						
					});
				}
				
				break;
			}
			case ACTION_UP: {
				
				if(lastEvent.getKind() == Kind.ACTION_DOWN) {
					
					post(new Runnable() {
						
						@Override
						public void run() {
							getEventListener().touch(lastEvent);
						}
						
					});
					
				} else {
					
					post(new Runnable() {
						
						@Override
						public void run() {
							getEventListener().move(event);
						}
						
					});
					
				}
				
				break;
			}
			default: {
				throw new AssertionError("Unknown Event");
			}
		}
	}

	/**
	 * Compute if we need to update the World Step.
	 * 
	 * @param sleep Sleep time for the given loop in {@link Activity#initialize()}.
	 * @see World
	 */
	void updateWorld(int sleep) {
		
		worldUpdateLeft += sleep;
		
		while(worldUpdateLeft >= WORLD_UPDATE) {
			game.getWorld().step(WORLD_STEP, 6, 2);
			worldUpdateLeft -= WORLD_UPDATE;
		}
		
	}

}
