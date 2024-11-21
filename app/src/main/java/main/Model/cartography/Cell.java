package main.Model.cartography;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Cell {

    @Id
    private long id;
    private int row;
    private int col;

    private ToMany<Calibration> calibrations;
    private ToOne<GridMap> gridpMap;

    public Cell(long id, int row, int col, ToMany<Calibration> calibrations, ToOne<GridMap> gridpMap) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.calibrations = calibrations;
        this.gridpMap = gridpMap;
    }

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Cell() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ToMany<Calibration> getCalibrations() {
        return calibrations;
    }

    public void setCalibrations(ToMany<Calibration> calibrations) {
        this.calibrations = calibrations;
    }

    public ToOne<GridMap> getGridpMap() {
        return gridpMap;
    }

    public void setGridpMap(ToOne<GridMap> gridpMap) {
        this.gridpMap = gridpMap;
    }

    public static int[] convertToGrid(double xPixel, double yPixel, double mapWidth, double mapHeight, int gridRows, int gridCols) {
        int xGrid = (int) (xPixel / (mapWidth / gridCols));
        int yGrid = (int) (yPixel / (mapHeight / gridRows));
        return new int[]{xGrid, yGrid};
    }
}
