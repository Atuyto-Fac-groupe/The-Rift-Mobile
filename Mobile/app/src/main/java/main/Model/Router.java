package main.Model;


import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import main.Model.BDD.AppDatabase;

@Entity
public class Router {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String SSID;
    private String BSSID;
    private int level;
    private int X;
    private int Y;
    private double n;

    public Router(String SSID, String BSSID, int level, int X, int Y) {

        this.SSID = SSID;
        this.BSSID = BSSID;
        this.level = level;
        this.X = X;
        this.Y = Y;
        this.n = 2.0;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getN() {
        return n;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }


    public void sauvegarder(Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        if (this.id == 0){
            this.id = appDatabase.routerDao().insert(this);
        }
        else {
            appDatabase.routerDao().update(this);
        }
    }
}
