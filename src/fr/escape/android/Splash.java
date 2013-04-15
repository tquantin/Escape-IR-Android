package fr.escape.android;

import java.io.IOException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import fr.escape.app.Engine;
import fr.escape.graphics.Texture;
import fr.escape.resources.TextureLoader;

public final class Splash {
	
	final static String TAG = Splash.class.getSimpleName();
	
	final Texture background;
	
	/**
	 * Default Constructor for Splash Render
	 * 
	 * @param context Android Context
	 * @throws IOException If an error has occurred
	 */
	public Splash(Context context) throws IOException {
		this.background = new Texture(context.getResources(), TextureLoader.BACKGROUND_SPLASH);
	}

	public void render(Canvas canvas, int width, int height) {
		
		Engine.log(TAG, "Render");
		
		//int srcX = (int) ((width / 2.0f) - (background.getWidth() / 2.0f));
		//int srcY = (int) ((height / 2.0f) - (background.getHeight() / 2.0f));
		int srcX = 0;
		int srcY = 0;
		int srcWidth = srcX + background.getWidth();
		int srcHeight = srcY + background.getHeight();
		
		Engine.error(TAG, 0+" "+0+" "+width+" "+height);
		Engine.error(TAG, srcX+" "+srcY+" "+srcWidth+" "+srcHeight);
		
		background.draw(canvas, new Paint(Paint.ANTI_ALIAS_FLAG), 0, 0, width, height, srcX, srcY, srcWidth, srcHeight);
		
	}
	
}
