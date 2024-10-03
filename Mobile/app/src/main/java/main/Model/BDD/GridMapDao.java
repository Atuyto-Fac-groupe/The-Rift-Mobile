package main.Model.BDD;

import androidx.room.*;
import main.Model.GridMap;

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

    @Query("select * from GridMap where GridMap.id = :idGridMap")
    GridMap getGridMap(int idGridMap);


}
