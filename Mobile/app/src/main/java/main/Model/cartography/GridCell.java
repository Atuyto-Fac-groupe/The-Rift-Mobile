package main.Model.cartography;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import main.App;
import main.Model.BDD.AppDatabase;

@Entity
public class GridCell {

    @PrimaryKey(autoGenerate = true)
    private long idGridCell;



    private int x;
    private int y;


    public GridCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public long getIdGridCell() {
        return idGridCell;
    }

    public void setIdGridCell(long idGridCell) {
        this.idGridCell = idGridCell;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    public void sauvegarder(int idGridMap){
        AppDatabase appDatabase = App.appDatabase;
//        this.idGridCell = idGridMap;
        if(this.idGridCell == 0){
            try {
                this.idGridCell = appDatabase.gridCellDao().insert(this);
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
