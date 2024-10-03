package main.Model.BDD;

import androidx.room.*;
import main.Model.Router;

import java.util.List;

@Dao
public interface RouterDao {

    @Insert
    long insert(Router router);

    @Delete
    void delete(Router router);

    @Update
    void update(Router router);

    @Query("select * from Router")
    List<Router> getAllRouter();

    @Query("select * from Router where Router.id = :routerId")
    Router getRouter(long routerId);

}
