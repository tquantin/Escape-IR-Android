package fr.escape.game.scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class ScenarioBuilder {
	public ArrayList<ShipInformations> ships;
	public String name;
	public int time;
	
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
	
	public ScenarioBuilder(String name, int time) {
		this.name = name;
		this.time = time;
		this.ships = new ArrayList<ShipInformations>();
	}
	
	public ShipInformations createShip(float x, float worldX, float y, float worldY, int type, int spawnTime, int midWidth, int midHeight) {
		ShipInformations infos = new ShipInformations(x, worldX, y, worldY, type, spawnTime, midWidth, midHeight);
		ships.add(infos);
		return infos;
	}
	
	public void saveData() {
		File path = Environment.getExternalStoragePublicDirectory("EscapeIR/Scenario");
		File scFile = new File(path,name + ".scn");
		
		DecimalFormat f1 = new DecimalFormat("0.0");
		DecimalFormat f2 = new DecimalFormat("00.0");
		
		Log.i("SAVEDATE", "Path : " + path.getAbsolutePath());
		Log.i("SAVEDATE", "File : " + scFile.getAbsolutePath());
		
		StringBuilder content = new StringBuilder("%%\n1\n%%\n1\n%%\n");
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			content.append(i + " " + infos.type);
			content.append(" " + ((infos.worldX < 10.0f) ? f1.format(infos.worldX) : f2.format(infos.worldX)) + "/" + infos.x);
			content.append(" " + ((infos.worldY < 10.0f) ? f1.format(infos.worldY) : f2.format(infos.worldY)) + "/" + infos.y + "\n");
		}
		
		content.append("%%\n");
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			int time = infos.spawnTime;
			content.append(time + " spawn " + i + "\n");
			for(int j = 0; j < infos.movements.size(); j++) {
				String position = infos.movements.get(j);
				content.append(++time + " move " + i);
				content.append(" " + position + "\n");
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
		} catch (IOException e) { Log.e("SAVEDATA", e.getMessage()); } finally {
			try { if(out != null) out.close(); } catch (IOException ioe) { /*TODO ; log*/ }
		}
	}
	
	public void loadData() {
		File path = Environment.getExternalStoragePublicDirectory("EscapeIR/Scenario");
		File scFile = new File(path,name + ".scn");
		
		FileInputStream in = null;
		try {
			int length = (int) scFile.length();
			byte[] buffer = new byte[length];
			
			in = new FileInputStream(scFile);
			in.read(buffer);
			String content = new String(buffer);
			Log.i("LOADDATA", content);
		} catch (IOException e) { /*TODO ; log*/ }
	}
	
	public ShipInformations selectShip(float x, float y) {
		ShipInformations ship = null;
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			if(infos.area.contains((int) x, (int) y)) {
				ship = infos;
			}
		}
		
		return ship;
	}
	
}