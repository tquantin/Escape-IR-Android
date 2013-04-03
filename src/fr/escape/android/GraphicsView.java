package fr.escape.android;

import fr.escape.app.Graphics;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public final class GraphicsView extends View {
	
	private Graphics graphics;
	
	public GraphicsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        graphics = null;
    }
	
	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(graphics != null) {
			graphics.flush(canvas);
		}
	}
	
}
