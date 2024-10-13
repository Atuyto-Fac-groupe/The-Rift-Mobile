package main.Model.cartography;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import main.App;

import java.util.List;

@Entity
public class Calibration {

    @PrimaryKey(autoGenerate = true)
    private long IdCalibration;

    private float x;
    private float y;



    private int level;

    @Ignore
    private List<Router> routers;

    public Calibration(float x, float y, int level) {
        this.x = x;
        this.y = y;
        this.level = level;
    }

    public void setIdCalibration(long idCalibration) {
        this.IdCalibration = idCalibration;
    }

    public long getIdCalibration() {
        return IdCalibration;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public List<Router> getRouters() {
        if (routers == null) {
            this.routers = App.appDatabase.calibrationDao().getCalibrationWithRouters(this.getIdCalibration()).routers;
        }
        return routers;
    }

    public void setRouters(List<Router> routers) {
        this.routers = routers;
    }
}
