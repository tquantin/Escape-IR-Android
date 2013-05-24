package fr.escape.game.scenario;

import java.io.File;
import java.util.NoSuchElementException;

import org.jbox2d.dynamics.World;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import fr.escape.app.Engine;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;
import fr.escape.graphics.Texture;

public class CustomStage extends AbstractStage {
	
	/**
	 * Class TAG
	 */
	private static final String TAG = CustomStage.class.getSimpleName();
	
	/**
	 * {@link CustomStage} Constructor
	 * 
	 * @param engine The Game {@link Engine}
	 * @param world The JBox2d {@link World}.
	 * @param container The {@link EntityContainer} that will contains all the {@link Entity} of this {@link Stage}.
	 * @param scenario File name in witch the {@link Scenario} is written.
	 * @param factory The {@link ShipFactory} use by the {@link Stage} to create the {@link Ship}.
	 */
	public CustomStage(Engine engine, World world, EntityContainer container, String scenario, ShipFactory factory) {
		super(engine, world, container, scenario, factory, false);
	}

	public Texture getCustomBackground() {
		
		String backgroundID = getScenario().getBackgroundID();
		
		Engine.debug(TAG, "Trying to load this background: "+backgroundID);
		
		if(backgroundID.matches("(\\+|-)?[0-9]+")) {
			
			Engine.debug(TAG, "Background is native");
			return getEngine().getResources().getTexture(Integer.valueOf(backgroundID).intValue());
		}
		
		try {
			
			String path = getCustomBackgroundPath();
			
			Engine.debug(TAG, "Trying to decode: "+path+" as Bitmap");
			
			Bitmap drawable = BitmapFactory.decodeFile(path);
			
			if(drawable == null) {
				Engine.error(TAG, "Cannot decode the given file");
				throw new NoSuchElementException(backgroundID);
			}
			
			return new Texture(drawable);
			
		} catch(Throwable t) {
			Engine.error(TAG, "An error has occurred", t);
			return null;
		}
	}
	
	private String getCustomBackgroundPath() {
		return new File(Engine.getImageStorage(), getScenario().getBackgroundID()).getAbsolutePath();
	}
	
}

