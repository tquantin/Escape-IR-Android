package fr.escape.android;

import fr.escape.android.R;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.scenario.ScenarioBuilder;
import fr.escape.game.scenario.ScenarioBuilder.ShipInformations;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	ImageView image = null;
	
	int type = 0;
	int previous = 0;
	int nbShip = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_builder);
		
		builder.loadData();
		
		final ScrollView sLayout = (ScrollView) findViewById(R.id.builderscroll);
		final RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		bLayout.setBackgroundResource(R.drawable.bearth);
		OnTouchListener l = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(currentShip != null && currentShip.area.contains((int) event.getX(), (int) event.getY())) {
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
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						image.setImageBitmap(bImage);
						currentShip.setArea(image.getWidth() / 2, image.getHeight() / 2);
					}
				} else if(registerMovement) {
					sLayout.requestDisallowInterceptTouchEvent(true);
					currentShip.movements.add(event);
					if(event.getAction() == MotionEvent.ACTION_UP) {
						registerMovement = false;
						sLayout.requestDisallowInterceptTouchEvent(false);
						if(nbShip == 5) builder.saveData();
					}
					return true;
				} else if(event.getAction() == MotionEvent.ACTION_UP && previous < 10) {
					int timeByDpi = bLayout.getHeight() / builder.time;
					int realTime = (int) Math.floor((builder.time - (event.getY() / timeByDpi)));
					
					currentShip = addShip(realTime, event.getX(), event.getY(), type);
					++nbShip;
					registerMovement = true;
					type = 0;
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
	
	@SuppressWarnings("deprecation")
	public ShipInformations addShip(int time, float x, float y, int type) {
		RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		image = new ImageView(getBaseContext());
		Bitmap bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sfalcon);
		image.setImageBitmap(bImage);
		image.setPadding((int) (x - (bImage.getWidth() / 2)), (int) (y - (bImage.getHeight() / 2)), 0, 0);
		bLayout.addView(image);
		
		CoordinateConverter converter = new CoordinateConverter(bLayout.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
		ShipInformations infos = builder.createShip(converter.toMeterX((int) x),0.0f,type,time,bImage.getWidth() / 2,bImage.getHeight() / 2);
		
		return infos;
	}
	
}
