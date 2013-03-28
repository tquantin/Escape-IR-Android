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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		graphics.flush(canvas);
	}
	
}
