package fr.escape.game.scenario;

import org.jbox2d.dynamics.World;

import fr.escape.app.Engine;
import fr.escape.game.entity.Entity;
import fr.escape.game.entity.EntityContainer;
import fr.escape.game.entity.ships.Ship;
import fr.escape.game.entity.ships.ShipFactory;

public final class HistoryStage extends AbstractStage {
	
	/**
	 * {@link HistoryStage} Constructor
	 * 
	 * @param engine The Game {@link Engine}
	 * @param world The JBox2d {@link World}.
	 * @param container The {@link EntityContainer} that will contains all the {@link Entity} of this {@link Stage}.
	 * @param scenario File name in witch the {@link Scenario} is written.
	 * @param factory The {@link ShipFactory} use by the {@link Stage} to create the {@link Ship}.
	 */
	public HistoryStage(Engine engine, World world, EntityContainer container, String scenario, ShipFactory factory) {
		super(engine, world, container, scenario, factory, true);
	}

}
