package fr.escape.android;

import fr.escape.game.scenario.ScenarioBuilder;
import android.os.AsyncTask;

public class FileAsyncTask extends AsyncTask<String, Void, String[]> {
	private final BuilderActivity activity;
	
	public FileAsyncTask(BuilderActivity activity) {
		this.activity = activity;
	}
	
	@Override
	protected String[] doInBackground(String... params) {
		ScenarioBuilder builder = activity.builder;
		
		builder.name = params[0];
		String[] result = builder.loadData().split("\n");
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String[] result) {
		ScenarioBuilder builder = activity.builder;
		
		String[] generalInfos = result[1].split(" ");
		builder.time = Integer.parseInt(generalInfos[1]);
		builder.bossId = Integer.parseInt(generalInfos[2]);
		
		int backgroundId = (generalInfos[3].matches("(\\+|-)?[0-9]+")) ? Integer.parseInt(generalInfos[3]) : -1;
		activity.setBackground(backgroundId, generalInfos[3]);
		
		for(int i = 5; !result[i].equals("%%"); i++) {
			String[] shipData = result[i].split(" ");
			
			String[] xData = shipData[2].split("/");
			String[] yData = shipData[3].split("/");
			
			activity.addShip(Integer.parseInt(shipData[0]), Float.parseFloat(xData[1]), Float.parseFloat(yData[1]), Integer.parseInt(shipData[1]));
		}
		
		builder.currentShip = null;
		activity.registerMovement = false;
		activity.hideShipMenu();
	}

}
