package main.Model.BDD.Cartography;

import androidx.room.*;
import main.Model.cartography.Router;

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

    @Query("select * from Router where Router.IdRouter = :routerId")
    Router getRouter(long routerId);

    @Query("select * from Router where Router.BSSID = :BSSID")
    Router getRouter(String BSSID);

}
