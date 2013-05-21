package fr.escape.game.screen;

import android.graphics.Color;
import android.graphics.Rect;
import fr.escape.Objects;
import fr.escape.app.Engine;
import fr.escape.app.Graphics;
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
	private static final String NEW_GAME = "New Game";
	
	private static final int HEADER_MARGING = 100;
	private static final int FOOTER_MARGING = 30;
	private static final int SIDE_PADDING = 50;
	private static final int GRID_PADDING = 20;
	private static final int GRID_COMPONENTS_SIZE = 75;
	
	private static final float FSIZE_H1 = 22.0f;
	private static final float FSIZE_H2 = 18.0f; 
	private static final float FSIZE_H3 = 16.0f;
	
	private final Font fontH1;
	private final Font fontH2;
	private final Font fontH3;
	
	private final Escape game;
	
	private final Texture background;
	private final Texture grid;
	
	private final Rect gridArea;
	private final Rect touchArea;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public Menu(Escape game) {
		
		this.game = Objects.requireNonNull(game);
		
		this.fontH1 = new Font(game.getResources().getFont(FontLoader.VISITOR_ID), FSIZE_H1);
		this.fontH2 = new Font(game.getResources().getFont(FontLoader.VISITOR_ID), FSIZE_H2);
		this.fontH3 = new Font(game.getResources().getFont(FontLoader.VISITOR_ID), FSIZE_H3);
		
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_MENU);
		this.grid = game.getResources().getTexture(TextureLoader.MENU_UI_GRID);
		
		this.gridArea = createGrid(game.getGraphics());
		this.touchArea = createTouch(gridArea);
		
	}
	
	@Override
	public boolean touch(Input i) {
		//TODO : Fix touchArea.contains
		//if(touchArea.contains(i.getX(), i.getY())) {
			Engine.log(TAG, "User Launch: NEW_GAME");
			next();
			return true;
		//}
		
		//return false;
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
		
		game.getGraphics().draw(grid, centerXScreen - (grid.getWidth() / 2), centerYScreen - (grid.getHeight() / 2));
		
		Screens.drawStringInCenterPosition(game.getGraphics(), TITLE, centerXScreen, HEADER_MARGING, fontH1, Color.WHITE);
		
		Screens.drawStringInCenterPosition(game.getGraphics(), NEW_GAME, touchArea.centerX(), touchArea.centerY(), fontH3, Color.BLACK);		
		
		Screens.drawStringInCenterPosition(
				game.getGraphics(), FOOTER, 
				(game.getGraphics().getWidth() / 2),
				game.getGraphics().getHeight() - FOOTER_MARGING, 
				fontH2, Color.WHITE);
	}

	@Override
	public void show() {
		game.getOverlay().hide();
	}

	@Override
	public void hide() { /* NOTHING TO DO */ }

	/**
	 * Create a Touch Components inside the Grid.
	 * 
	 * @param grid Grid to use
	 * @return Touch Area
	 */
	private static Rect createTouch(Rect grid) {
		return new Rect(grid.left + GRID_PADDING, grid.centerY() - (GRID_COMPONENTS_SIZE/2),
						grid.right - (GRID_PADDING * 2), GRID_COMPONENTS_SIZE);
	}
	
	/**
	 * Create a Grid Components inside the Screen.
	 * 
	 * @param graphics Screen property.
	 * @return Grid Area
	 */
	private static Rect createGrid(Graphics graphics) {
		
		int x = SIDE_PADDING;
		int y = HEADER_MARGING * 2;
		
		return new Rect(x, y, 
				(graphics.getWidth() - (x * 2)), 
				(graphics.getHeight() - (FOOTER_MARGING * 3)) - y);
	}
	
	/**
	 * Launch a New Game
	 * 
	 * @see Escape#setNewGameScreen()
	 */
	public void next() {
		game.setScreenID(Escape.SCREEN_NEW_GAME);
	}
	
}
