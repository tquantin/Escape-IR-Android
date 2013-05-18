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
import android.view.MotionEvent;

public class ScenarioBuilder {
	public ArrayList<ShipInformations> ships;
	public String name;
	public int time;
	
	public class ShipInformations {
		public Rect area;
		public float x;
		public float y;
		public int spawnTime;
		public int type;
		public ArrayList<MotionEvent> movements;
		
		public ShipInformations(float x, float y, int type, int spawnTime, int midWidth, int midHeight) {
			this.x = x;
			this.y = y;
			this.type = type;
			this.spawnTime = spawnTime;
			this.movements = new ArrayList<MotionEvent>();
			this.area = new Rect((int) x - midWidth, (int) y - midHeight, (int) x + midWidth, (int) y + midHeight);
		}
		
		public void setArea(int midWidth, int midHeight) {
			this.area.left = (int) x - midWidth;
			this.area.top = (int) y -  midHeight;
			this.area.right = (int) x + midWidth;
			this.area.bottom = (int) y + midHeight;
			Log.i("AREA", area.left + "/" + area.top + "/" + area.right + "/" + area.bottom);
		}
	}
	
	public ScenarioBuilder(String name, int time) {
		this.name = name;
		this.time = time;
		this.ships = new ArrayList<ShipInformations>();
	}
	
	public ShipInformations createShip(float x, float y, int type, int spawnTime, int midWidth, int midHeight) {
		ShipInformations infos = new ShipInformations(x, y, type, spawnTime, midWidth, midHeight);
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
			content.append(" " + ((infos.x < 10.0f) ? f1.format(infos.x) : f2.format(infos.x)));
			content.append(" " + ((infos.y < 10.0f) ? f1.format(infos.y) : f2.format(infos.y)) + "\n");
		}
		
		content.append("%%\n");
		
		for(int i = 0; i < ships.size(); i++) {
			ShipInformations infos = ships.get(i);
			int time = infos.spawnTime;
			content.append(time + " spawn " + i + "\n");
			for(int j = 0; j < infos.movements.size(); j++) {
				MotionEvent event = infos.movements.get(j);
				content.append(++time + " move " + i);
				content.append(" " + event.getX());
				content.append(" " + event.getY() + "\n");
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
			try { if(out != null) out.close(); } catch (IOException ioe) { /*TODO : log*/ }
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
		} catch (IOException e) { /*TODO : log*/ }
	}
	
}