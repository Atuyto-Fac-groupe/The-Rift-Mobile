package main.Model;

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
    private long id;

    @Ignore
    private GridCell[][] grid;

    private int rows;
    private int cols;

    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GridCell[][] getGrid() {
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


    public void addWifiDataToCell(int row, int col, List<Router> wifiData) {
        GridCell cell = getCell(row, col);
        if (cell != null) {
            cell.setWifiData(wifiData);
        }
    }

    public void sauvegarder(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        if (this.id == 0) {
            try {
                this.id = appDatabase.gridMapDao().insert(this);
            } catch (SQLiteConstraintException e) {
                appDatabase.gridMapDao().update(this);
            }
        } else {
            appDatabase.gridMapDao().update(this);
        }

        for (GridCell[] gridCells : this.grid) {
            for (GridCell gridCell : gridCells) {
                gridCell.sauvegarder(context, (int) this.id);
            }
        }
    }


}
