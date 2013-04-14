package fr.escape.game.screen;

import java.io.IOException;

import android.content.Context;
import fr.escape.app.Engine;
import fr.escape.app.Graphics;
import fr.escape.app.Input;
import fr.escape.app.Screen;
import fr.escape.graphics.Texture;
import fr.escape.resources.TextureLoader;

public final class SplashScreen implements Screen {
	
	final static String TAG = SplashScreen.class.getSimpleName();
	
	final Texture background;
	final Graphics graphics;
	
	public SplashScreen(Context context, Graphics graphics) throws IOException {
		this.background = new Texture(context.getResources(), TextureLoader.BACKGROUND_SPLASH);
		this.graphics = graphics;
	}

		
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
		graphics.draw(background, 0, 0, graphics.getWidth(), graphics.getHeight());
	}

	@Override
	public void show() {
		Engine.debug(TAG, "Show Splash Screen");
	}

	@Override
	public void hide() {
		Engine.debug(TAG, "Hide Splash Screen");
	}
	
}
