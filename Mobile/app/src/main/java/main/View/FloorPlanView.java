package main.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FloorPlanView extends ImageView {

    private List<PointF> wifiMarkers = new ArrayList<>();

    private float playerX;
    private float playerY;

    public FloorPlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        for (PointF marker : wifiMarkers) {
            canvas.drawCircle(marker.x, marker.y, 20, paint);
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
