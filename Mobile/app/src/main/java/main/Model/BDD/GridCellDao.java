package main.Model.BDD;

import androidx.room.*;
import main.Model.GridCell;

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

    @Query("select * from GridCell where GridCell.id = :idGridCell")
    GridCell getById(int idGridCell);

}
