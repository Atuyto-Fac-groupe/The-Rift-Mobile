package main.Model.BDD.Cartography;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import main.Model.cartography.Calibration;
import main.Model.cartography.Router;

@Dao
public interface CalibrationDao {

    @Insert
    long insertCalibration(Calibration calibration);

    @Insert
    void insertRouter(Router router);

    @Insert
    void insertCalibrationRouterCrossRef(CalibrationRouterCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM Calibration WHERE IdCalibration = :calibrationId")
    CalibrationWithRouters getCalibrationWithRouters(long calibrationId);

    @Transaction
    @Query("SELECT * FROM Router WHERE IdRouter = :routerId")
    RouterWithCalibrations getRouterWithCalibrations(long routerId);
}