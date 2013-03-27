package fr.escape.game.screen;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import fr.escape.app.Foundation;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.Texture;
import fr.escape.resources.font.FontLoader;
import fr.escape.resources.texture.TextureLoader;

/**
 * <p>
 * A screen that display "Victory !!!", Highscore and Credits.
 * 
 */
public final class Victory implements Screen {

	private static final long WAIT = 1337;
	private static final String TAG = Victory.class.getSimpleName();
	
	private static final float FSIZE_H2 = 18.0f;
	
	private final Font font;
	private final Escape game;
	private final Texture background;
	private final Texture user;
	private final Runnable confirm;
	
	private long time = 0;
	
	/**
	 * Default Constructor
	 * 
	 * @param game Escape Game
	 */
	public Victory(Escape game) {
		
		this.game = Objects.requireNonNull(game);
		this.font = game.getResources().getFont(FontLoader.VISITOR_ID).deriveFont(FSIZE_H2);
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_VICTORY);
		this.user = game.getResources().getTexture(TextureLoader.SHIP_RAPTOR);
		this.time = 0;
		this.confirm = new Runnable() {
			
			@Override
			public void run() {
				next();
			}
			
		};
	}
	
	@Override
	public boolean touch(Input i) {
		
		if(time >= WAIT) {
			Foundation.ACTIVITY.log(TAG, "User Launch: MENU_SCREEN");
			Foundation.ACTIVITY.post(confirm);
			return true;
		}
		
		Foundation.ACTIVITY.debug(TAG, "User may miss-click");
		return false;
	}

	@Override
	public boolean move(Input i) {
		return touch(i);
	}

	@Override
	public void render(long delta) {
		
		time += delta;
		game.getGraphics().draw(background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
	
		int x = (game.getGraphics().getWidth() / 2);
		int y = (game.getGraphics().getHeight() / 4) + (int) (font.getSize() * 1.4);
		
		Screens.drawStringInCenterPosition(game.getGraphics(), "Highscore: "+game.getUser().getHighscore(), x, y, font, Color.WHITE);
		
		x = (game.getGraphics().getWidth() / 2) - (user.getWidth() / 2);
		y = (game.getGraphics().getHeight() / 2) - (user.getWidth() / 2);
		
		game.getGraphics().draw(user, x, y);
		
	}

	@Override
	public void show() {
		game.getOverlay().hide();
		time = 0;
	}

	@Override
	public void hide() {}

	/**
	 * Launch Menu when the User touch the Screen.
	 */
	public void next() {
		game.getUser().setHighscore(0);
		game.setMenuScreen();
	}
	
}
