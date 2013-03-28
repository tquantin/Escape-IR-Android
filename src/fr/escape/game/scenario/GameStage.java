package fr.escape.game.scenario;

import org.jbox2d.dynamics.World;

import fr.escape.game.entity.Entity;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;

public class GameStage extends AbstractStage {
	
	/**
	 * {@link GameStage} constructor
	 * 
	 * @param world : The JBox2d {@link World}.
	 * @param container : The {@link EntityContainer} that will contains all the {@link Entity} of this {@link Stage}.
	 * @param scenario : File name in witch the {@link Scenario} is written.
	 * @param factory : The {@link ShipFactory} use by the {@link Stage} to create the {@link Ship}.
	 * @param duration : Duration in seconds of the {@link Stage}.
	 * @param bossType : Boss to use for this {@link Stage}
	 */
	public GameStage(World world, EntityContainer container, String scenario, ShipFactory factory, int duration, int bossType) {
		super(world, container, scenario, factory, duration, bossType);
	}

}
