package main.Model.cartography;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import main.App;
import main.Model.BDD.AppDatabase;

import java.util.List;

@Entity
public class GridMap {

    @PrimaryKey(autoGenerate = true)
    private long idGridMap;

    @Ignore
    private GridCell[][] grid;

    private int rows;
    private int cols;

    private String name;




    public GridMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdGridMap() {
        return idGridMap;
    }

    public void setIdGridMap(long idGridMap) {
        this.idGridMap = idGridMap;
    }

    public GridCell[][] getGrid() {
        if (grid == null) {
            grid = new GridCell[rows][cols];
            if(this.idGridMap != 0){
                List<GridCell> cells = App.appDatabase.gridMapDao().getGridMapWithGridCell(this.idGridMap).gridCells;
                if (!cells.isEmpty()) {
                    for (GridCell gridCell : cells) {
                        this.grid[gridCell.getX()][gridCell.getY()] = gridCell;
                    }
                }
                return grid;
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = new GridCell(i, j);
                }
            }
        }
        return grid;
    }

    public void setGrid(GridCell[][] grid) {
        this.grid = grid;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public GridCell getCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grid[row][col];
        }
        return null;
    }


    public void sauvegarder(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        if (this.idGridMap == 0) {
            try {
                this.idGridMap = appDatabase.gridMapDao().insert(this);
            } catch (SQLiteConstraintException e) {
                appDatabase.gridMapDao().update(this);
            }
        } else {
            appDatabase.gridMapDao().update(this);
        }

        for (GridCell[] gridCells : this.grid) {
            for (GridCell gridCell : gridCells) {
                gridCell.sauvegarder((int) this.idGridMap);
            }
        }
    }

    public static GridMap loadGridMapFromId(long id) {
        GridMap gridMap = App.appDatabase.gridMapDao().getGridMap(id);
        if (gridMap == null) {
            return new GridMap(App.ROW, App.COL);
        }
        else {
            return gridMap;
        }
    }

}
