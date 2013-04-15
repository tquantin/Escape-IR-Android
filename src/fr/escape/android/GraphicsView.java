package fr.escape.android;

import java.io.IOException;

import fr.escape.app.Engine;
import fr.escape.app.Graphics;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public final class GraphicsView extends SurfaceView implements Callback {
	
	static final String TAG = GraphicsView.class.getSimpleName();
	
	private Graphics graphics;
	private Object lock;
	private Splash splash;
	
	private volatile boolean isVisible;
		
	public GraphicsView(EscapeActivity activity) {
        super(activity);
        
        graphics = activity.getEngine().getGraphics();
        lock = new Object();
        
        try {
        	splash = new Splash(activity);
        } catch(Exception e) {
        	Engine.error(TAG, "An error has occurred while creating Splash", e);
        }
        
        getHolder().addCallback(this);
    }
	
	public void setGraphics(Graphics graphics) {
		synchronized(lock) {
			this.graphics = graphics;
		}
	}
	
	public void showSplash() {
		if(splash != null) {
			
			Canvas canvas = getHolder().lockCanvas();
			
			Paint p = new Paint();
			p.setColor(Color.WHITE);
			canvas.drawText("Fuck", 10, 10, p);
			
			splash.render(canvas, getWidth(), getHeight());
			
			if(isVisible) {
				getHolder().unlockCanvasAndPost(canvas);
			} else {
				Engine.error(TAG, "GraphicsView is not visible");
			}
			
		} else {
			Engine.error(TAG, "Splash is null");
		}
	}
	
	public void render() {
		synchronized(lock) {
			if(graphics != null) {
				
				Canvas canvas = getHolder().lockCanvas();
				graphics.flush(canvas);
				
				if(isVisible) {
					getHolder().unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i(TAG, "onChange("+format+", "+width+", "+height+")");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "onCreate()");
		graphics.createView(this, getWidth(), getHeight());
		isVisible = true;
		showSplash();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "onDestroy()");
		isVisible = false;
		graphics.destroyView();
	}
	
}
