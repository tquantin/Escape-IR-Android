package fr.escape.game.screen;

import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.Texture;
import fr.escape.resources.font.FontLoader;
import fr.escape.resources.texture.TextureLoader;

/**
 * <p>
 * Display a screen when the User lose the Game. 
 *
 * <p>
 * Behavior: Click/Touch on Screen to close and return in Main {@link Menu}.
 */
public final class Lost implements Screen {

	private static final String TAG = Lost.class.getSimpleName();
	
	/**
	 * Message displayed in {@link Screen#render(long)} for this Screen.
	 */
	private static final String MESSAGE = "You loose. Try again ?";
	
	private final Font font;
	private final Escape game;
	private final Texture background;
	
	public Lost(Escape game) {
		this.game = Objects.requireNonNull(game);
		this.font = game.getResources().getFont(FontLoader.VISITOR_ID);
		this.background = game.getResources().getTexture(TextureLoader.BACKGROUND_LOST);
	}

	@Override
	public void render(long delta) {
		
		game.getGraphics().draw(background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
		
		Screens.drawStringInCenterPosition(
				game.getGraphics(), MESSAGE, 
				(game.getGraphics().getWidth() / 2), 
				(game.getGraphics().getHeight() / 2), 
				font, Color.WHITE);
	}

	@Override
	public void show() {
		game.getOverlay().hide();
	}

	@Override
	public void hide() {}

	/**
	 * When user touch the Screen: Return to the Main {@link Menu}.
	 */
	@Override
	public boolean touch(Input i) {
		game.getActivity().debug(TAG, "User click: Go on Menu Screen");
		game.setMenuScreen();
		return true;
	}

	@Override
	public boolean move(Input i) {
		return touch(i);
	}
	
}
