package main.Controler;

import java.util.List;

public class GridMap {

    private GridCell[][] grid;
    private int rows;
    private int cols;

    public GridMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new GridCell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new GridCell(i, j);
            }
        }
    }

    public GridCell getCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grid[row][col];
        }
        return null;
    }


    public void addWifiDataToCell(int row, int col, List<Router> wifiData) {
        GridCell cell = getCell(row, col);
        if (cell != null) {
            cell.setWifiData(wifiData);
        }
    }


}
