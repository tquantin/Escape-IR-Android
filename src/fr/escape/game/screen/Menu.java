package fr.escape.game.screen;

import android.graphics.Color;
import android.graphics.Rect;
import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.Font;
import fr.escape.graphics.Texture;
import fr.escape.resources.FontLoader;
import fr.escape.resources.TextureLoader;

/**
 * <p>
 * A screen that display the Menu.
 * 
 */
public final class Menu implements Screen {

	private static final String TAG = Menu.class.getSimpleName();
	private static final String TITLE = "Escape";
	private static final String FOOTER = "Insert Coin";
	
	private static final int HEADER_MARGING = 100;
	private static final int FOOTER_MARGING = 30;
	
	private static final float FSIZE_HEADER = 22.0f;
	private static final float FSIZE_FOOTER = 18.0f;
	
	private final Font header;
	private final Font footer;
	
	private final Escape game;
	
	private final Texture background;
	private final Texture history;
	private final Texture custom;
	private final Texture builder;
	
	private final Rect touchHistory;
	private final Rect touchCustom;
	private final Rect touchBuilder;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public Menu(Escape game) {
		
		this.game = Objects.requireNonNull(game);
		
		this.header = new Font(game.getResources().getFont(FontLoader.VISITOR_ID), FSIZE_HEADER);
		this.footer = new Font(game.getResources().getFont(FontLoader.VISITOR_ID), FSIZE_FOOTER);
		
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_MENU);
		
		this.history = game.getResources().getTexture(TextureLoader.MENU_UI_BUTTON_HISTORY);
		this.custom = game.getResources().getTexture(TextureLoader.MENU_UI_BUTTON_CUSTOM);
		this.builder = game.getResources().getTexture(TextureLoader.MENU_UI_BUTTON_BUILDER);
		
		int gridWidth = Math.max(Math.max(history.getWidth(), custom.getWidth()), builder.getWidth());
		int gridHeight = history.getHeight() + custom.getHeight() + builder.getHeight();
		
		int gridX = (game.getGraphics().getWidth() / 2) - (gridWidth / 2);
		int gridY = (game.getGraphics().getHeight() / 2) - (gridHeight / 2);
		
		this.touchHistory = createTouch(0, gridY, gridX, gridX + history.getWidth(), history.getHeight());
		this.touchCustom = createTouch(1, gridY, gridX, gridX + custom.getWidth(), custom.getHeight());
		this.touchBuilder = createTouch(2, gridY, gridX, gridX + builder.getWidth(), builder.getHeight());
		
	}
	
	@Override
	public boolean touch(Input i) {
		
		if(touchHistory.contains(i.getX(), i.getY())) {
			
			Engine.log(TAG, "User Launch: History");
			history();
			return true;
			
		} else if(touchCustom.contains(i.getX(), i.getY())) {
			
			Engine.log(TAG, "User Launch: Custom Game");
			custom();
			return true;
			
		} else if(touchBuilder.contains(i.getX(), i.getY())) {
			
			Engine.log(TAG, "User Launch: Builder");
			builder();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean move(Input i) {
		return touch(i);
	}

	@Override
	public void render(long delta) {
		
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
		
		/**
		 * Draw Menu Components
		 */
		game.getGraphics().draw(history, touchHistory.left, touchHistory.top);
		game.getGraphics().draw(custom, touchCustom.left, touchCustom.top);
		game.getGraphics().draw(builder, touchBuilder.left, touchBuilder.top);
		
		Screens.drawStringInCenterPosition(game.getGraphics(), TITLE, centerXScreen, HEADER_MARGING, header, Color.WHITE);		
		
		Screens.drawStringInCenterPosition(
				game.getGraphics(), FOOTER, 
				(game.getGraphics().getWidth() / 2),
				game.getGraphics().getHeight() - FOOTER_MARGING, 
				footer, Color.WHITE);
	}

	@Override
	public void show() {
		game.getOverlay().hide();
	}

	@Override
	public void hide() { /* NOTHING TO DO */ }
	
	/**
	 * Create a Touch Components inside the Screen for the Menu.
	 * 
	 * @param position Position of this Components
	 * @param y Starting Position Y in Screen for Components 
	 * @param x1 Starting Position X in Screen for Components 
	 * @param x2 Ending Position X in Screen for Components
	 * @param height Components Height
	 * @return
	 */
	private static Rect createTouch(int position, int y, int x1, int x2, int height) {
	
		int y1 = y + (position * height);
		int y2 = y1 + height;
		
		return new Rect(x1, y1, x2, y2);
	}
	
	/**
	 * Launch a new History Game
	 */
	public void history() {
		game.setScreenID(Escape.SCREEN_NEW_GAME);
	}
	
	/**
	 * Launch a new Custom Game
	 */
	public void custom() {
		game.setScreenID(Escape.SCREEN_LEVEL_LOADER);
	}
	
	/**
	 * Launch Builder Activity
	 */
	public void builder() {
		//game.setScreenID(id)
		Engine.error(TAG, "Cannot load Builder Activity");
	}
	
}
