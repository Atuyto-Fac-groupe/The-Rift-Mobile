package main.Model.BDD.Cartography;

import androidx.room.Entity;

@Entity(primaryKeys = {"idGridMap", "idGridCell"})
public class GridCellCrossRef {
    public long idGridMap;
    public long idGridCell;

    public GridCellCrossRef(long idGridMap, long idGridCell) {
        this.idGridMap = idGridMap;
        this.idGridCell = idGridCell;
    }
}
