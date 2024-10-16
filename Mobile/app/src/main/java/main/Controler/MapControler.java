package main.Controler;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import androidx.core.app.ActivityCompat;
import io.objectbox.Box;
import main.Model.BDD.ObjectBox;
import main.Model.cartography.Calibration;
import main.Model.cartography.Router;
import main.Model.cartography.Router_;
import main.View.Cartography.FloorPlanView;
import main.View.GridImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.WIFI_SERVICE;

public class MapControler {


    private WifiManager wifiManager;
    private GridImageView gridImageView;
    private Context context;
    private List<Router> routersScan;
    private HashMap<Router, Calibration> calibrations;

    public MapControler(GridImageView gridImageView, Context context) {
        this.gridImageView = gridImageView;
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        this.calibrations = new HashMap<>();
        this.wifiManager.startScan();
        this.routersScan = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.wifiManager.getScanResults().stream().forEach(sc -> {
                Router routerbdd = Router.getRouterFromBssid(sc.BSSID);
                if (routerbdd != null) {
                    Router newRouter = new Router(sc.level, sc.BSSID);
                    newRouter.setId(routerbdd.getId());
                    this.routersScan.add(newRouter);
                }
        });

        this.routersScan = this.routersScan.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getLevel(), r1.getLevel()))
                .limit(3)
                .collect(Collectors.toList());

        if (this.routersScan.size() < 3) {
            return;
        }

        for (Router router : this.routersScan) {
            List<Calibration> calibration = Calibration.getCalibrationsFomRouter(router);
            Calibration pointCalibration;
            if (!calibration.isEmpty()) {
                pointCalibration = calibration.stream()
                        .sorted((r1, r2) -> Integer.compare(r2.getRouter().getTarget().getLevel(), r1.getRouter().getTarget().getLevel()))
                        .collect(Collectors.toList())
                        .get(0);
                this.calibrations.put(router, pointCalibration);
            }
        }

        List<Calibration> calibrationsList = new ArrayList<>(calibrations.values());
        Calibration c1 = calibrationsList.get(0);
        Calibration c2 = calibrationsList.get(1);
        Calibration c3 = calibrationsList.get(2);
        double d1 = Router.getDistanceFromRssi(this.routersScan.get(0).getLevel(), -40);
        double d2 = Router.getDistanceFromRssi(this.routersScan.get(1).getLevel(), -40);
        double d3 = Router.getDistanceFromRssi(this.routersScan.get(2).getLevel(), -40);

        double [] coordoninate = Calibration.triangulate(c1.getX(), c1.getY(), c2.getX(), c2.getY(), c3.getX(), c3.getY(), d1, d2, d3);

        System.out.println("test");


    }

}
