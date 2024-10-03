package main.Model;

import android.content.Context;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import main.Model.BDD.AppDatabase;

import java.util.List;

@Entity
public class GridCell {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int idGridMap;

    private int row;
    private int col;

    @Ignore
    private List<Router> wifiData;


    public GridCell(int row, int col) {
        this.row = row;
        this.col = col;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdGridMap() {
        return idGridMap;
    }

    public void setIdGridMap(int idGridMap) {
        this.idGridMap = idGridMap;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setWifiData(List<Router> wifiData) {
        this.wifiData = wifiData;
    }

    public List<Router> getWifiData() {
        return wifiData;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void sauvegarder(Context context, int idGridMap){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        this.idGridMap = idGridMap;
        if(this.id == 0){
            try {
                this.id = appDatabase.gridCellDao().insert(this);
            }
            catch (Exception e){
                appDatabase.gridCellDao().update(this);
            }
        }
        else {
            appDatabase.gridCellDao().update(this);
        }
    }
}
