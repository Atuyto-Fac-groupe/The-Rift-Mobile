package com.example.heatmapper.Model;

import android.net.wifi.ScanResult;
import java.util.List;

public class GridCell {

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
