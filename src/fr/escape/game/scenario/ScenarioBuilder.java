package fr.escape.game.scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import fr.escape.app.Engine;

import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

public class ScenarioBuilder {
	public ArrayList<ShipInformations> ships;
	public ShipInformations currentShip = null;
	
	public String name;
	public int time;
	public int bossId;
	public String background;
	
	public class ShipInformations {
		public Rect area;
		public float x, worldX;
		public float y, worldY;
		public int spawnTime, type;
		public ArrayList<String> movements;
		public ImageView image = null;
		
		public ShipInformations(float x, float worldX, float y, float worldY, int type, int spawnTime, int midWidth, int midHeight) {
			this.x = x; this.worldX = worldX;
			this.y = y; this.worldY = worldY;
			this.type = type;
			this.spawnTime = spawnTime;
			this.movements = new ArrayList<String>();
			this.area = new Rect((int) x - midWidth, (int) y - midHeight, (int) x + midWidth, (int) y + midHeight);
		}
		
		public boolean contains(int x, int y) {
			return area.contains(x, y);
		}
	}
	
	public ScenarioBuilder() {
		this.ships = new ArrayList<ShipInformations>();
	}
	
	public ShipInformations createShip(float x, float worldX, float y, float worldY, int type, int spawnTime, int midWidth, int midHeight) {
		ShipInformations infos = new ShipInformations(x, worldX, y, worldY, type, spawnTime, midWidth, midHeight);
		ships.add(infos);
		return infos;
	}
	
	public void saveData() {
		File path = Engine.getScenarioStorage();
		File scFile = new File(path,name + ".scn");
		
		DecimalFormat f1 = new DecimalFormat("0.0");
		DecimalFormat f2 = new DecimalFormat("00.0");
		
		StringBuilder content = new StringBuilder("%%\n1 ");
		content.append(time); content.append(" "); content.append(bossId); content.append(" "); content.append(background); content.append("\n%%\n1\n%%\n");
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			content.append(i); content.append(" "); content.append(infos.type);
			content.append(" "); content.append(((infos.worldX < 10.0f) ? f1.format(infos.worldX).replaceAll(",", ".") : f2.format(infos.worldX)).replaceAll(",", ".")); content.append("/"); content.append(infos.x);
			content.append(" "); content.append(((infos.worldY < 10.0f) ? f1.format(infos.worldY).replaceAll(",", ".") : f2.format(infos.worldY)).replaceAll(",", ".")); content.append("/"); content.append(infos.y);
			content.append("\n");
		}
		
		content.append("%%\n");
		
		StringBuilder[] strings = new StringBuilder[time];
		Arrays.fill(strings, null);
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			int time = infos.spawnTime;
			
			if(strings[time] == null) strings[time] = new StringBuilder();
			strings[time].append(time); strings[time].append(" spawn ");
			strings[time].append(i); strings[time].append("\n");
			
			for(int j = 0; j < infos.movements.size(); j++) {
				String position = infos.movements.get(j); ++time;
				
				if(strings[time] == null) strings[time] = new StringBuilder();
				
				strings[time].append(time); strings[time].append(" move "); strings[time].append(i);
				strings[time].append(" "); strings[time].append(position.replaceAll(",", ".")); strings[time].append("\n");
				strings[time].append(time); strings[time].append(" fire "); strings[time].append(i); strings[time].append("\n");
			}
			
			++time;
			if(strings[time] == null) strings[time] = new StringBuilder();
			
			strings[time].append(time); strings[time].append(" move "); strings[time].append(i); strings[time].append(" 5.0 13.0\n");
			strings[time].append(time); strings[time].append(" fire "); strings[time].append(i); strings[time].append("\n");
		}
		
		for(int i = 0; i < time; i++) {
			if(strings[i] != null) {
				content.append(strings[i].toString());
			}
		}
		
		content.append("%%");
		Log.i("SAVEDATE", content.toString());
		
		FileOutputStream out = null;
		try {
			path.mkdirs();
			out = new FileOutputStream(scFile);
			out.write(content.toString().getBytes());
			out.close();
		} catch (IOException e) {
			Log.e("ScenarioBuilder", "Erreur lors de la sauvegarde du fichier.");
		} finally {
			try { if(out != null) out.close(); } catch (IOException ioe) { /*Nothing*/ }
		}
	}
	
	public String loadData() {
		File path = Engine.getScenarioStorage();
		File scFile = new File(path,name + ".scn");
		
		FileInputStream in = null;
		try {
			int length = (int) scFile.length();
			byte[] buffer = new byte[length];
			
			in = new FileInputStream(scFile);
			in.read(buffer);
			String content = new String(buffer);
			
			return content;
		} catch (IOException e) {
			Log.e("ScenarioBuilder", "Erreur lors du chargement du fichier.");
		} finally {
			try { if(in != null) in.close(); } catch (IOException e) { /*Nothing*/ }
		}
		
		return "";
	}
	
	public ShipInformations selectShip(float x, float y) {
		ShipInformations ship = null;
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			if(infos.contains((int) x, (int) y)) {
				ship = infos;
				ship.movements.clear();
				break;
			}
		}
		
		return ship;
	}
	
}