package fr.escape.android;

import fr.escape.app.Graphics;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public final class GraphicsView extends SurfaceView implements Callback {
	
	static final String TAG = GraphicsView.class.getSimpleName();
	
	private Graphics graphics;
	private Object lock;
	
	private volatile boolean isVisible;
	
	public GraphicsView(Context context) {
		super(context);
		throw new IllegalStateException("You cannot inflate this View from XML");
	}
	
	public GraphicsView(EscapeActivity activity) {
        super(activity);
        graphics = activity.getEngine().getGraphics();
        lock = new Object();
        getHolder().addCallback(this);
    }
	
	public void setGraphics(Graphics graphics) {
		synchronized(lock) {
			this.graphics = graphics;
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
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "onDestroy()");
		isVisible = false;
		graphics.destroyView();
	}
	
}
