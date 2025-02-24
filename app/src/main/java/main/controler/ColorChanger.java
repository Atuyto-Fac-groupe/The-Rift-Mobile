package main.controler;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.List;
import java.util.TimerTask;

public class ColorChanger extends TimerTask {


    private int indexColor;
    private final List<Integer> colors;
    private final FrameLayout frame;
    private final Activity activity;
    private boolean pause;

    public ColorChanger(FrameLayout frame, List<Integer> colors, Activity activity) {
        this.frame = frame;
        this.colors = colors;
        this.indexColor = 0;
        this.activity = activity;
    }

    @Override
    public void run() {
        if (this.indexColor == this.colors.size()){
            this.pause = true;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Log.d("ColorChanger", e.getMessage());
            }
            this.pause = false;
            this.indexColor = 0;
        }
        else if (!this.pause) {
            this.activity.runOnUiThread(() -> {
                this.frame.setBackgroundColor(colors.get(indexColor));
                this.indexColor ++;
            });
        }

    }
}
