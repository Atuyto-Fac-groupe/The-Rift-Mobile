package main.Model.cartography;


import io.objectbox.Box;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;
import main.Model.BDD.ObjectBox;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Calibration {

    @Id
    private Long id;

    private double x;
    private double y;


    private ToOne<Router> router;


    public Calibration() {
    }


    public Calibration(Long routerId, double y, double x, Long id) {
        this.id = routerId;
        this.y = y;
        this.x = x;
        this.id = id;
    }

    public Calibration(double x, double y, Router router) {
        this.x = x;
        this.y = y;
        this.router.setTargetId(router.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Long getRouterId() {
        return id;
    }

    public void setRouterId(Long routerId) {
        this.id = routerId;
    }

    public ToOne<Router> getRouter() {
        return router;
    }

    public void setRouter(ToOne<Router> router) {
        this.router = router;
    }

    public static List<Calibration> getCalibrationsFomRouter(Router router) {
        Box<Calibration> calibrationBox = ObjectBox.get().boxFor(Calibration.class);
        List<Calibration> calibrations = calibrationBox.query()
                .equal(Calibration_.routerId, router.getId())
                .build()
                .find();
        return calibrations;
    }

    public static double[] triangulate(double x1, double y1, double x2, double y2, double x3, double y3, double d1, double d2, double d3) {

        double A = 2 * x2 - 2 * x1;
        double B = 2 * y2 - 2 * y1;
        double C = d1 * d1 - d2 * d2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2;
        double D = 2 * x3 - 2 * x2;
        double E = 2 * y3 - 2 * y2;
        double F = d2 * d2 - d3 * d3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3;

        double x = (C * E - F * B) / (E * A - B * D);
        double y = (C * D - A * F) / (B * D - A * E);

        return new double[]{x, y};
    }


}
