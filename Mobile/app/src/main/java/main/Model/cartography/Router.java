package main.Model.cartography;


import io.objectbox.Box;
import io.objectbox.Property;
import io.objectbox.annotation.*;
import io.objectbox.query.QueryBuilder;
import io.objectbox.relation.ToMany;
import main.Model.BDD.ObjectBox;


@Entity
public class Router {

    @Id
    private Long id;

    @Index
    @Unique
    private String bssid;

    private int level;

    private double x;
    private double y;

    @Backlink(to = "router")
    private ToMany<Calibration> calibrations;

    public Router() {
    }

    public Router(int level, String bssid) {
        this.level = level;
        this.bssid = bssid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getLevel() {
        return level;
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

    public void setLevel(int level) {
        this.level = level;
    }
    public ToMany<Calibration> getCalibrations() {
        return calibrations;
    }

    public void setCalibrations(ToMany<Calibration> calibrations) {
        this.calibrations = calibrations;
    }

    public static double getDistanceFromRssi(int rssi, int ref) {
        return Math.pow(10, (ref - rssi) / (10 * 2.0));
    }

    public static Router getRouterFromBssid(String bssid) {
        Box<Router> routerBox = ObjectBox.get().boxFor(Router.class);
        Property<Router> routerBssid = Router_.bssid;
        Router router = routerBox.query().equal(routerBssid, bssid, QueryBuilder.StringOrder.CASE_SENSITIVE).build().findFirst();
        return router;
    }

    @Override
    public String toString() {
        return "Router{" +
                "id=" + id +
                ", bssid='" + bssid + '\'' +
                ", level=" + level +
                ", calibrations=" + calibrations +
                '}';
    }
}
