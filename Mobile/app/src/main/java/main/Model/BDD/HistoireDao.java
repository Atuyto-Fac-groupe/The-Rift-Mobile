package main.Model.BDD;

import androidx.room.*;
import main.Model.Histoire;

import java.util.List;

@Dao
public interface HistoireDao {

    @Query("select * from histoire")
    List<Histoire> getAllHistoires();

    @Query("select * from histoire where histoire.displayed = 1")
    List<Histoire> getAllDisplayedHistoires();

    @Query("select * from histoire where histoire.idHistoire = :histoireId")
    Histoire getHistoireById(long histoireId);

    @Query("delete from histoire")
    void deleteAllHistoire();

    @Query("delete from histoire where histoire.idHistoire = :histoireId")
    void deleteHistoireById(long histoireId);

    @Delete
    void deleteHistoire(Histoire histoire);

    @Insert
    long insertHistoire(Histoire histoire);

    @Update
    void updateHistoire(Histoire histoire);
}


