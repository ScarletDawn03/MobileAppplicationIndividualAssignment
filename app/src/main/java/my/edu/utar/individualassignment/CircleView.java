package my.edu.utar.individualassignment;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * The CircleView class is a custom View that draws a solid colored circle
 * centered within the view’s bounds.
 */
public class CircleView extends View {
    private Paint paint;

    /**
     * Constructor used when creating the view from XML layout files.
     *
     * @param context The Context the view is running in.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initializes the Paint object with anti-aliasing and color settings.
     */
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xFF6200EE); // Change color as needed
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Called when the view should be drawn.
     * This draws a circle centered in the view.
     *
     * @param canvas The Canvas on which the background will be drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(getWidth(), getHeight()) / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }
}
