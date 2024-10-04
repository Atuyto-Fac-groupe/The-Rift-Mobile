package main.Model.BDD.Cartography;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import main.Model.cartography.GridCell;
import main.Model.cartography.GridMap;

import java.util.List;

public class GridMapWithGridCell {

    @Embedded
    public GridMap gridMap;

    @Relation(
            parentColumn = "idGridMap",
            entityColumn = "idGridCell",
            associateBy = @Junction(GridCellCrossRef.class)
    )
    public List<GridCell> gridCells;
}
