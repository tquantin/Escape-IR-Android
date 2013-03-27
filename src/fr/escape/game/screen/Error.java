package fr.escape.game.screen;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import fr.escape.app.Game;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.game.Escape;
import fr.escape.graphics.RepeatableScrollingTexture;
import fr.escape.resources.font.FontLoader;
import fr.escape.resources.texture.TextureLoader;

/**
 * This class define the {@link Screen} {@link Error}
 * 
 * @see Screen
 */
public class Error implements Screen {

	private final static String TAG = Error.class.getSimpleName();
	
	private final Escape game;
	private final Random random;
	private final List<String> message;

	private Font font;
	private RepeatableScrollingTexture background;
	
	private boolean fallbackBackground;
	private boolean fallbackFont;
	
	/**
	 * {@link Error} constructor
	 * 
	 * @param game : {@link Game}
	 */
	public Error(Escape game) {
		
		this.game = game;
		this.random = new Random(200);
		
		try {
			font = game.getResources().getFont(FontLoader.VISITOR_ID);
			fallbackFont = false;
		} catch(NoSuchElementException e) {
			fallbackFont = true;
			game.getActivity().error(TAG, "Cannot create Font", e);
		}
		
		try {
			background = new RepeatableScrollingTexture(game.getResources().getTexture(TextureLoader.BACKGROUND_ERROR));
			fallbackBackground = false;
		} catch(NoSuchElementException e) {
			fallbackBackground = true;
			game.getActivity().error(TAG, "Cannot create RepeatableScrollingTexture", e);
		}
		
		this.message = new ArrayList<>(4);
		
		message.add("An error has occurred :");
		message.add("");
		message.add("Please restart your Game");
		message.add("and/or Contact Support");
		
	}
	
	@Override
	public void render(long delta) {
		
		Color color = Color.WHITE;
		
		if(!fallbackBackground) {

			background.setXPercent(random.nextFloat());
			background.setYPercent(random.nextFloat());
			
			game.getGraphics().draw(background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
			
		} else {
			color = Color.BLACK;
		}
		
		if(!fallbackFont) {

			draw(25, (game.getGraphics().getHeight() / 2) - (message.size() / 2 * 20), 20, true, color);
			
		} else {
			draw(25, 20, 20, false, color);
		}

	}

	@Override
	public void show() {
		if(game.getOverlay() != null) {
			game.getOverlay().hide();
		}
	}

	@Override
	public void hide() {}

	@Override
	public boolean touch(Input i) {
		return false;
	}

	@Override
	public boolean move(Input i) {
		return false;
	}
	
	/**
	 * Draw the screen
	 * 
	 * @param x : Coordinate on X axis.
	 * @param y : Coordinate on Y axis.
	 * @param space : Space between draw on Y axis.
	 * @param useFont : Use the {@link Font} to draw.
	 * @param color : The {@link Color} use to draw.
	 */
	private void draw(int x, int y, int space, boolean useFont, Color color) {
		
		int yy = y;
		
		for(String info: message) {
			
			if(useFont) {
				game.getGraphics().draw(info, x, yy, font, color);
			} else {
				game.getGraphics().draw(info, x, yy, color);
			}
			
			yy += space;
		}
	}

}
