package main.Controler;

import androidx.room.Entity;
import org.intellij.lang.annotations.Identifier;

import java.util.List;

@Entity
public class GridCell {

    private long id;
    private int row;
    private int col;
    private List<Router> wifiData;

    public GridCell(int row, int col) {
        this.row = row;
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
}
