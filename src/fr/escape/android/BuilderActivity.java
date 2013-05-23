package fr.escape.android;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import fr.escape.android.R;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.scenario.ScenarioBuilder;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BuilderActivity extends Activity {
	
	final ScenarioBuilder builder = new ScenarioBuilder();
	
	private MenuItem changeButton;
	private MenuItem removeButton;
	private MenuItem saveButton;
	
	boolean registerMovement = false;
	
	int scenarioId = -1;
	int previous = 0;
	int itemId = 0;
	int type = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_builder_options);
		
		final ListView imgList = (ListView) findViewById(R.id.images_list);
		final ListView scnList = (ListView) findViewById(R.id.scenario_list);
		imgList.setSelector(R.drawable.blist);
		scnList.setSelector(R.drawable.blist);
				
		File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).listFiles();
		ArrayList<String> images = new ArrayList<String>();
		
		File[] scnPath = Environment.getExternalStoragePublicDirectory("EscapeIR/Scenario").listFiles();
		ArrayList<String> scenarios = new ArrayList<String>();
		
		images.add("Jupiter Background");
		images.add("Moon Background");
		images.add("Earth Background");
		
		for(int i = 0; i < files.length; i++) {
			if(!files[i].isDirectory()) {
				images.add(files[i].getName());
			}
		}
		
		for(int i = 0; i < scnPath.length; i++) {
			if(!scnPath[i].isDirectory()) {
				scenarios.add(scnPath[i].getName().replaceAll(".scn", ""));
			}
		}
		
		ListAdapter imgAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, images);
		ListAdapter scnAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, scenarios);
		
		imgList.setAdapter(imgAdapter);
		scnList.setAdapter(scnAdapter);
		
		OnItemClickListener imgListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				imgList.setSelected(true);
				itemId = position;
			}
		};
		
		OnItemClickListener scnListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				scnList.setSelected(true);
				scenarioId = position;
			}
		};
		
		imgList.setOnItemClickListener(imgListener);
		scnList.setOnItemClickListener(scnListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.builder, menu);
		
		changeButton = menu.findItem(R.id.change_type);
		removeButton = menu.findItem(R.id.remove_ship);
		saveButton   = menu.findItem(R.id.save_button);
		
		saveButton.setEnabled(false);
		hideShipMenu();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.change_type : switchType(); break;
			case R.id.remove_ship : removeShip(); break;
			case R.id.save_button : builder.saveData();
				Toast.makeText(getBaseContext(), "Scénario sauvegardé.", Toast.LENGTH_SHORT).show();
				break;
			default : break;
		}
		return true;
	}
	
	public void next() {
		EditText scenarioName = (EditText) findViewById(R.id.scenario_name);
		EditText scenarioTime = (EditText) findViewById(R.id.scenario_time);
		
		ListView imgList = (ListView) findViewById(R.id.images_list);
		ListView scnList = (ListView) findViewById(R.id.scenario_list);
		
		String name = scenarioName.getText().toString();
		String time = scenarioTime.getText().toString();
		String backgroundName = (String) imgList.getItemAtPosition(itemId);
		
		if((name.equals("")  || time.equals("")) && scenarioId == -1) {
			Toast.makeText(getBaseContext(), "Saisissez un nom de scénario, ainsi que sa durée.", Toast.LENGTH_LONG).show();
		} else {
			setContentView(R.layout.activity_builder);
			saveButton.setEnabled(true);
			
			RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
			
			if(scenarioId != -1) {
				loadData((String) scnList.getItemAtPosition(scenarioId));
				bLayout.setOnTouchListener(getOnTouchListener(bLayout.getHeight()));
				return;
			}
			
			builder.name = name;
			builder.time = Integer.parseInt(time);

			int backgroundId = -1;
			switch(itemId) {
				case 0 : backgroundId = R.drawable.bjupiter; break;
				case 1 : backgroundId = R.drawable.bmoon; break;
				case 2 : backgroundId = R.drawable.bearth; break;
				default: break;
			}
			
			setBackground(backgroundId, backgroundName);
			bLayout.setOnTouchListener(getOnTouchListener(bLayout.getHeight()));
		}
	}
	
	private void loadData(String scenarioName) {
		builder.name = scenarioName;
		String[] content = builder.loadData().split("\n");
		
		String[] generalInfos = content[1].split(" ");
		builder.time = Integer.parseInt(generalInfos[1]);
		
		int backgroundId = (generalInfos[2].matches("(\\+|-)?[0-9]+")) ? Integer.parseInt(generalInfos[2]) : -1;
		setBackground(backgroundId, generalInfos[2]);
		
		for(int i = 5; !content[i].equals("%%"); i++) {
			String[] shipData = content[i].split(" ");
			
			String[] xData = shipData[2].split("/");
			String[] yData = shipData[3].split("/");
			
			addShip(Integer.parseInt(shipData[0]), Float.parseFloat(xData[1]), Float.parseFloat(yData[1]), Integer.parseInt(shipData[1]));
		}
		
		builder.currentShip = null;
		registerMovement = false;
		hideShipMenu();
	}

	@SuppressWarnings("deprecation")
	public void addShip(int time, float x, float y, int type) {
		RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		Bitmap bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sfalcon);
		CoordinateConverter converter = new CoordinateConverter(bLayout.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
		builder.currentShip = builder.createShip(x,converter.toMeterX((int) x),y,0.0f,type,time,bImage.getWidth() / 2,bImage.getHeight() / 2);
		
		builder.currentShip.image = new ImageView(getBaseContext());
		builder.currentShip.image.setImageBitmap(bImage);
		builder.currentShip.image.setPadding((int) (x - (bImage.getWidth() / 2)), (int) (y - (bImage.getHeight() / 2)), 0, 0);
		bLayout.addView(builder.currentShip.image);
		
		registerMovement = true;
		showShipMenu();
		this.type = 0;
	}
	
	public void removeShip() {
		builder.currentShip.image.setVisibility(View.GONE);
		builder.currentShip.image.setEnabled(false);
		builder.ships.remove(builder.currentShip);
		builder.currentShip = null;
		registerMovement = false;
		hideShipMenu();
		
		Toast.makeText(getBaseContext(), "Vaisseau supprimé.", Toast.LENGTH_SHORT).show();
	}
	
	public void switchType() {
		Bitmap bImage = null;
		switch(type) {
			case 0 : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sraptor);
				type = 1;
				break;
			case 1 : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sfalcon);
				type = 2;
				break;
			case 2 : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sviper);
				type = 0;
				break;
			default : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sraptor);
				break;
		}
		builder.currentShip.image.setImageBitmap(bImage);
		builder.currentShip.type = type;
	}
	
	float distanceX = 0, distanceY = 0;
	int nbMovement = 0;
	
	@SuppressWarnings("deprecation")
	public void registerMovement(MotionEvent event) {
		++nbMovement;
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			distanceX = builder.currentShip.x - event.getX();
			distanceY = builder.currentShip.y - event.getY();
		}
		
		if(nbMovement % 10 == 0 || event.getAction() == MotionEvent.ACTION_UP) {
			DecimalFormat f1 = new DecimalFormat("0.0");
			DecimalFormat f2 = new DecimalFormat("00.0");
			
			ScrollView scroll = (ScrollView) findViewById(R.id.builderscroll);
			CoordinateConverter converter = new CoordinateConverter(scroll.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
			scroll.requestDisallowInterceptTouchEvent(true);
			
			float x = converter.toMeterX((int) (event.getX() + distanceX));
			float y = converter.toMeterY((int) ((event.getY() + distanceY) - builder.currentShip.y));
			
			builder.currentShip.movements.add(((x < 10) ? f1.format(x) : f2.format(x)) + " " + ((y < 10) ? f1.format(y) : f2.format(y)));
			
			if(event.getAction() == MotionEvent.ACTION_UP) {
				hideShipMenu();
				registerMovement = false;
				builder.currentShip = null;
				scroll.requestDisallowInterceptTouchEvent(false);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setBackground(int backgroundId, String backgroundName) {
		RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		if(backgroundId != -1) {
			bLayout.setBackgroundResource(backgroundId);
			builder.background = Integer.toString(backgroundId);
		} else {
			File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File image = new File(directory, backgroundName);
			
			BitmapDrawable background = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(image.getAbsolutePath()));
			if(Build.VERSION.SDK_INT >= 16) {
				bLayout.setBackground(background);
			} else {
				bLayout.setBackgroundDrawable(background);
			}
			
			builder.background = backgroundName;
		}
	}
	
	private OnTouchListener getOnTouchListener(final int height) {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP && builder.currentShip == null) {
					builder.currentShip = builder.selectShip(event.getX(),event.getY());
					if(builder.currentShip != null) {
						Toast.makeText(getBaseContext(), "Vaisseau sélectionné : vous pouvez tracer ces déplacements.", Toast.LENGTH_SHORT).show();
						registerMovement = true;
						showShipMenu();
						return true;
					}
				}
				
				if(registerMovement) {
					registerMovement(event);
					return true;
				} else if(event.getAction() == MotionEvent.ACTION_UP && previous < 10) {
					int realTime = (int) Math.floor((builder.time - (event.getY() / (height / builder.time))));
					addShip(realTime, event.getX(), event.getY(), type);
					Toast.makeText(getBaseContext(), "Vaisseau ajouté : vous pouvez tracer ces déplacements.", Toast.LENGTH_SHORT).show();
				}
				
				if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
					previous = 0;
				} else {
					++previous;
				}
				
				return true;
			}
		};
	}
	
	void showShipMenu() {
		changeButton.setEnabled(true);
		removeButton.setEnabled(true);
	}
	
	void hideShipMenu() {
		changeButton.setEnabled(false);
		removeButton.setEnabled(false);
	}
	
}