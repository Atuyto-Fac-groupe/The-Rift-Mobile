package main.Model.cartography;


import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

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


}
