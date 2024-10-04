package main.Model.BDD.Cartography;

import androidx.room.Entity;

@Entity(primaryKeys = {"IdCalibration", "IdRouter"})
public class CalibrationRouterCrossRef {
    public long IdCalibration;
    public long IdRouter;

    public CalibrationRouterCrossRef(long IdCalibration, long IdRouter) {
        this.IdCalibration = IdCalibration;
        this.IdRouter = IdRouter;
    }
}