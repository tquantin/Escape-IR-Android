package fr.escape.game.screen;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Rect;
import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.game.scenario.CustomStage;
import fr.escape.graphics.Font;
import fr.escape.graphics.ScrollingTexture;
import fr.escape.graphics.Texture;
import fr.escape.resources.FontLoader;
import fr.escape.resources.TextureLoader;

public final class LevelLoader implements Screen {

	private static final String TAG = LevelLoader.class.getSimpleName();
	private static final float SIZE = 18.0f;
	
	private static final String BACK = "Cancel";
	private static final String LIST_CUSTOM_GAME = "List of Custom Game :";
	
	private static final int HEADER_MARGING = 60;
	private static final int FOOTER_MARGING = 80;
	private static final int ITEMS_MARGING = 30;
	
	private static final int IGNORE_TOUCH_EVENT = 100;
	
	private final Font font;
	
	private final Escape game;
	
	private final Texture background;
	private final Texture item;
	
	private final Rect back;
	private final Rect display;
	
	private final List<Item> items;
	
	private long alive;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public LevelLoader(Escape game) {
		
		this.game = Objects.requireNonNull(game);
		
		this.font = new Font(game.getResources().getFont(FontLoader.VISITOR_ID), SIZE);
		
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_MENU);
		this.item = game.getResources().getTexture(TextureLoader.MENU_UI_LIST_ITEM);
		
		this.back = new Rect(0, game.getGraphics().getHeight() - FOOTER_MARGING, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		int itemsX = (game.getGraphics().getWidth() / 2) - (item.getWidth() / 2);
		this.display = new Rect(itemsX, HEADER_MARGING + ITEMS_MARGING, itemsX + item.getWidth(), back.top - ITEMS_MARGING);
		
		this.items = new LinkedList<Item>();
		
		this.alive = 0;
	}
	
	@Override
	public boolean touch(Input i) {
		
		/**
		 * Ignore "Dead" Input Event
		 */
		if(alive < IGNORE_TOUCH_EVENT) {
			return false;
		} 
		
		if(back.contains(i.getX(), i.getY())) {
			
			Engine.log(TAG, "User Cancel Custom Game");
			game.setScreenID(Escape.SCREEN_MENU);
			return true;
			
		}
		
		if(display.contains(i.getX(), i.getY())) {
			
			for(Item item : items) {
				
				Rect area = item.getArea();
				
				if(area.contains(i.getX(), i.getY())) {
					
					Engine.error(TAG, "Launch Custom Game: "+item.getFile());
					
					try {
						
						CustomStage stage = new CustomStage(
							game.getEngine(), 
							game.getWorld(), 
							game.getEntityContainer(), 
							item.getFile().getName(), 
							game.getShipFactory()
						);
						
						Texture texture = stage.getCustomBackground();
						
						if(texture == null) {
							game.getEngine().toast("Cannot load the Scenario's Background");
							return false;
						}
						
						game.getUser().setOneLife();
						
						Level level = new Level(game, stage, new ScrollingTexture(texture, true), Escape.SCREEN_LEVEL_LOADER, Escape.SCREEN_VICTORY);
						game.setScreen(level);
						return true;
						
					} catch (Throwable t) {
						
						Engine.error(TAG, "An error has occured", t);
						game.getEngine().toast("Cannot load the given Scenario");
						return false;
						
					}
					
				}
			}
			
		}
		
		return false;
	}

	@Override
	public boolean move(Input i) {
		return touch(i);
	}

	@Override
	public void render(long delta) {
		
		if(alive < IGNORE_TOUCH_EVENT) {
			alive += delta;
		}
		
		/**
		 * Fetch Graphics Width and Height
		 */
		int width = game.getGraphics().getWidth();
		int heigth = game.getGraphics().getHeight();
		
		/**
		 * Compute Center for Graphics Screen and Background, once.
		 */
		int centerXScreen = width / 2;
		int centerYScreen = heigth / 2;
		int centerXBackground = background.getWidth() / 2;
		int centerYBackground = background.getHeight() / 2;
		
		/**
		 * Compute srcX, srcY, srcWidth and srcHeight to apply a center effect on Background.
		 */
		int srcX = Math.max(0, centerXBackground - centerXScreen);
		int srcY = Math.max(0, centerYBackground - centerYScreen);
		int srcWidth = Math.min(background.getWidth(), srcX + width);
		int srcHeigth = Math.min(background.getHeight(), srcY + heigth);
		
		/**
		 * Draw Background
		 */
		game.getGraphics().draw(background, 0, 0, width, heigth, srcX, srcY, srcWidth, srcHeigth);
		
		Screens.drawStringInCenterPosition(game.getGraphics(), LIST_CUSTOM_GAME, centerXScreen,
				HEADER_MARGING, font, Color.WHITE);
		
		Screens.drawStringInCenterPosition(game.getGraphics(), BACK, centerXScreen,
				back.centerY(), font, Color.WHITE);
		
		for(Item i : items) {
			
			Rect area = i.getArea();
			
			if(area.bottom > display.bottom) {
				break;
			}
			
			game.getGraphics().draw(item, area.left, area.top);
			
			String name = i.getFile().getName();
			
			Screens.drawStringInCenterPosition(game.getGraphics(), name.substring(0, (name.length() > 20)?20:name.length()), area.centerX(), area.centerY(), font, Color.BLACK);
			
		}
		
	}

	@Override
	public void show() {
		fetchListOfScenario();
		game.getOverlay().hide();
	}

	@Override
	public void hide() {
		items.clear();
		alive = 0;
	}

	private void fetchListOfScenario() {
		
		File[] files = Engine.getScenarioStorage().listFiles();
		
		if(files != null) {
			for(int i = 0; i < files.length; i++) {
				
				int y = (i * item.getHeight()) + display.top;
				
				Rect area = new Rect(display.left, y, display.right, y + item.getHeight());
				
				items.add(new Item(area, files[i]));
				
			}
		}
		
	}
	
	private static class Item {
		
		private final Rect area;
		private final File file;
		
		public Item(Rect area, File file) {
			this.area = area;
			this.file = file;
		}
		
		public Rect getArea() {
			return area;
		}
		
		public File getFile() {
			return file;
		}
		
	}
	
}
