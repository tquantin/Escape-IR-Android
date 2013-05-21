package fr.escape.android;

import java.text.DecimalFormat;

import fr.escape.android.R;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.scenario.ScenarioBuilder;
import fr.escape.game.scenario.ScenarioBuilder.ShipInformations;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class BuilderActivity extends Activity {
	final ScenarioBuilder builder = new ScenarioBuilder("test",60);
	
	ShipInformations currentShip = null;
	boolean registerMovement = false;
	
	int type = 0;
	int previous = 0;
	int nbShip = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_builder);
		
		builder.loadData();
		
		final RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		bLayout.setBackgroundResource(R.drawable.bearth);
		OnTouchListener l = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(currentShip == null) {
					currentShip = builder.selectShip(event.getX(),event.getY());
				}
				
				if(currentShip != null && currentShip.contains((int) event.getX(), (int) event.getY())) {
					switchType(event.getAction());
				} else if(registerMovement) {
					registerMovement(event);
					return true;
				} else if(event.getAction() == MotionEvent.ACTION_UP && previous < 10) {
					int realTime = (int) Math.floor((builder.time - (event.getY() / (bLayout.getHeight() / builder.time))));
					addShip(realTime, event.getX(), event.getY(), type);
				}
				
				if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
					previous = 0;
				} else {
					++previous;
				}
				
				return true;
			}
		};
		bLayout.setOnTouchListener(l);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void addShip(int time, float x, float y, int type) {
		RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		Bitmap bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sfalcon);
		CoordinateConverter converter = new CoordinateConverter(bLayout.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
		currentShip = builder.createShip(x,converter.toMeterX((int) x),y,0.0f,type,time,bImage.getWidth() / 2,bImage.getHeight() / 2);
		
		currentShip.image = new ImageView(getBaseContext());
		currentShip.image.setImageBitmap(bImage);
		currentShip.image.setPadding((int) (x - (bImage.getWidth() / 2)), (int) (y - (bImage.getHeight() / 2)), 0, 0);
		bLayout.addView(currentShip.image);
		
		registerMovement = true;
		this.type = 0;
		++nbShip;
	}
	
	public void switchType(int action) {
		if(action == MotionEvent.ACTION_DOWN) {
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
			currentShip.image.setImageBitmap(bImage);
			currentShip.type = type;
		}
	}
	
	float distanceX = 0, distanceY = 0;
	int nbMovement = 0;
	
	@SuppressWarnings("deprecation")
	public void registerMovement(MotionEvent event) {
		++nbMovement;
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			distanceX = currentShip.x - event.getX();
			distanceY = currentShip.y - event.getY();
		}
		
		if(nbMovement % 4 == 0 || event.getAction() == MotionEvent.ACTION_UP) {
			DecimalFormat f1 = new DecimalFormat("0.0");
			DecimalFormat f2 = new DecimalFormat("00.0");
			
			ScrollView scroll = (ScrollView) findViewById(R.id.builderscroll);
			CoordinateConverter converter = new CoordinateConverter(scroll.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
			scroll.requestDisallowInterceptTouchEvent(true);
			
			float x =converter.toMeterX((int) (event.getX() - distanceX));
			float y = converter.toMeterY((int) ((event.getY() - distanceY) - currentShip.y));
			currentShip.movements.add(((x < 10) ? f1.format(x) : f2.format(x)) + " " + ((y < 10) ? f1.format(y) : f2.format(y)));
			
			if(event.getAction() == MotionEvent.ACTION_UP) {
				registerMovement = false;
				currentShip = null;
				scroll.requestDisallowInterceptTouchEvent(false);
				if(nbShip == 5) builder.saveData();
			}
		}
	}
	
}