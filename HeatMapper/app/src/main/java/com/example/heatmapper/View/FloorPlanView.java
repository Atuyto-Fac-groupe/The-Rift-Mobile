package com.example.heatmapper.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.example.heatmapper.R;

import java.util.ArrayList;
import java.util.List;

public class FloorPlanView extends ImageView {

    private List<PointF> wifiMarkers = new ArrayList<>();

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
    }

    public void addWifiMarker(float x, float y) {
        wifiMarkers.add(new PointF(x, y));
        invalidate();
    }
}
