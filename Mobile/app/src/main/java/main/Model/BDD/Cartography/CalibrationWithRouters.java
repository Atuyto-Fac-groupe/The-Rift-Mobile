package main.Model.BDD.Cartography;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;
import main.Model.cartography.Calibration;
import main.Model.cartography.Router;

import java.util.List;

public class CalibrationWithRouters {
    @Embedded
    public Calibration calibration;

    @Relation(
            parentColumn = "IdCalibration",
            entityColumn = "IdRouter",
            associateBy = @Junction(CalibrationRouterCrossRef.class)
    )
    public List<Router> routers;
}