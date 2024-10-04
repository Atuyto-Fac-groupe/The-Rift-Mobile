package main.Model.BDD.Cartography;

import androidx.room.*;
import main.Model.cartography.GridMap;

import java.util.List;

@Dao
public interface GridMapDao {

    @Insert
    long insert(GridMap gridMap);

    @Delete
    void delete(GridMap gridMap);

    @Update
    void update(GridMap gridMap);

    @Query("select * from GridMap")
    List<GridMap> getAllGridMap();

    @Query("select * from GridMap where GridMap.idGridMap = :idGridMap")
    GridMap getGridMap(long idGridMap);


    @Transaction
    @Query("Select * from GridMap where idGridMap = :idGridMap")
    GridMapWithGridCell getGridMapWithGridCell(long idGridMap);
}
