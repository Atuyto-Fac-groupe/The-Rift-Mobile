package main.Model.cartography;


import android.content.Context;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import main.Model.BDD.AppDatabase;

@Entity
public class Router {

    @PrimaryKey(autoGenerate = true)
    private long IdRouter;

    private String BSSID;
    private int level;
    private double n;

    public Router(String BSSID, int level) {
        this.BSSID = BSSID;
        this.level = level;
        this.n = 2.0;

    }

    public long getIdRouter() {
        return IdRouter;
    }

    public void setIdRouter(long idRouter) {
        this.IdRouter = idRouter;
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


    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }


    public void sauvegarder(Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        if (this.IdRouter == 0){
            try {
                this.IdRouter = appDatabase.routerDao().insert(this);
            } catch (Exception e) {
                appDatabase.routerDao().update(this);
            }
        }
        else {
            appDatabase.routerDao().update(this);
        }
    }
}
