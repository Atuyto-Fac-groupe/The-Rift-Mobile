package main.Model.BDD.Cartography;

import androidx.room.*;
import main.Model.cartography.GridCell;

import java.util.List;

@Dao
public interface GridCellDao {

    @Insert
    long insert(GridCell gridCell);

    @Update
    void update(GridCell gridCell);

    @Delete
    void delete(GridCell gridCell);

    @Query("select * from GridCell")
    List<GridCell> getAll();

    @Query("select * from GridCell where GridCell.idGridCell = :idGridCell")
    GridCell getById(int idGridCell);



}
