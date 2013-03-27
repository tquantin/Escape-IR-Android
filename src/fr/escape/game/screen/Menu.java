package fr.escape.game.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Objects;

import fr.escape.app.Foundation;
import fr.escape.app.Graphics;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.Texture;
import fr.escape.resources.font.FontLoader;
import fr.escape.resources.texture.TextureLoader;

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
	
	private final Rectangle gridArea;
	private final Rectangle touchArea;
	
	private final Runnable newGame;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public Menu(Escape game) {
		
		this.game = Objects.requireNonNull(game);
		
		Font baseFont = game.getResources().getFont(FontLoader.VISITOR_ID);
		
		this.fontH1 = baseFont.deriveFont(FSIZE_H1);
		this.fontH2 = baseFont.deriveFont(FSIZE_H2);
		this.fontH3 = baseFont.deriveFont(FSIZE_H3);
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_MENU);
		this.grid = game.getResources().getTexture(TextureLoader.MENU_UI_GRID);
		
		this.gridArea = createGrid(game.getGraphics());
		this.touchArea = createTouch(gridArea);
		
		this.newGame = new Runnable() {
			
			@Override
			public void run() {
				next();	
			}
			
		};
	}
	
	@Override
	public boolean touch(Input i) {
		
		if(touchArea.contains(i.getX(), i.getY())) {
			Foundation.ACTIVITY.log(TAG, "User Launch: NEW_GAME");
			Foundation.ACTIVITY.post(newGame);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean move(Input i) {
		if(touchArea.contains(i.getX(), i.getY())) {
			return touch(i);
		}
		return false;
	}

	@Override
	public void render(long delta) {
		
		game.getGraphics().draw(background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		Screens.drawStringInCenterPosition(
				game.getGraphics(), TITLE, 
				(game.getGraphics().getWidth() / 2), HEADER_MARGING, 
				fontH1, Color.WHITE);
		
		game.getGraphics().draw(grid, (int) gridArea.getX(), (int) gridArea.getY(), 
				(int) (gridArea.getX() + gridArea.getWidth()), (int) (gridArea.getY() + gridArea.getHeight()));
		
		Screens.drawStringInCenterPosition(game.getGraphics(), NEW_GAME, (int) touchArea.getCenterX(), (int) touchArea.getCenterY(), fontH3, Color.BLACK);		
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
	public void hide() {}

	/**
	 * Create a Touch Components inside the Grid.
	 * 
	 * @param grid Grid to use
	 * @return Touch Area
	 */
	private static Rectangle createTouch(Rectangle grid) {
		return new Rectangle((int) (grid.getX() + GRID_PADDING), (int) (grid.getCenterY() - (GRID_COMPONENTS_SIZE/2)), 
				(int) (grid.getWidth() - (GRID_PADDING * 2)), GRID_COMPONENTS_SIZE);
	}
	
	/**
	 * Create a Grid Components inside the Screen.
	 * 
	 * @param graphics Screen property.
	 * @return Grid Area
	 */
	private static Rectangle createGrid(Graphics graphics) {
		
		int x = SIDE_PADDING;
		int y = HEADER_MARGING * 2;
		
		return new Rectangle(x, y, 
				(graphics.getWidth() - (x * 2)), 
				(graphics.getHeight() - (FOOTER_MARGING * 3)) - y);
	}
	
	/**
	 * Launch a New Game
	 * 
	 * @see Escape#setNewGameScreen()
	 */
	public void next() {
		game.setNewGameScreen();
	}
	
}
