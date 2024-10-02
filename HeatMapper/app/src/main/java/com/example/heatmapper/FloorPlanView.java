package com.example.heatmapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FloorPlanView extends View {

    private Bitmap floorPlanBitmap;
    private List<PointF> wifiMarkers = new ArrayList<>();

    public FloorPlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        floorPlanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        @SuppressLint("DrawAllocation")
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(floorPlanBitmap, viewWidth, viewHeight, true);

        canvas.drawBitmap(resizedBitmap, 0, 0, null);

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
