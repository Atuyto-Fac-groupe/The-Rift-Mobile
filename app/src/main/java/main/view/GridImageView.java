package main.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.ImageView;
import main.App;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class GridImageView extends ImageView {

    private Paint paint;
    private int rows = App.ROW;
    private int cols = App.COL;
    private List<PointF> wifiMarkers = new ArrayList<>();
    private float playerX;
    private float playerY;


    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF00FF00);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float cellWidth = (float) width / cols;
        float cellHeight = (float) height / rows;


        for (int i = 1; i < cols; i++) {
            float x = i * cellWidth;
            canvas.drawLine(x, 0, x, height, paint);
        }


        for (int j = 1; j < rows; j++) {
            float y = j * cellHeight;
            canvas.drawLine(0, y, width, y, paint);
        }

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        for (PointF marker : wifiMarkers) {
            canvas.drawCircle(marker.x, marker.y, 10, paint);
        }

        if (playerY != 0 && playerX != 0) {
            Paint paint2 = new Paint();
            paint2.setColor(Color.BLUE);
            canvas.drawCircle(playerX, playerY, 40, paint);
        }

    }

    public void addWifiMarker(float x, float y) {
        wifiMarkers.add(new PointF(x, y));
        invalidate();
    }

    public void movePlayer(float x, float y) {
        playerX = x;
        playerY = y;
        invalidate();
    }
}
