package main.Controler;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import androidx.core.app.ActivityCompat;
import main.Model.cartography.Calibration;
import main.Model.cartography.Router;
import main.View.GridImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.WIFI_SERVICE;

public class MapControler {


    private WifiManager wifiManager;
    private GridImageView gridImageView;
    private Activity context;
    private List<Router> routersScan;
    private List<Router> calibrations;
    private double mapWidth;
    private double mapHeight;

    public MapControler(GridImageView gridImageView, Activity context) {
        this.gridImageView = gridImageView;
        this.context = context;
        this.wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        this.calibrations = new ArrayList<>();
        this.wifiManager.startScan();
        this.routersScan = new ArrayList<>();

    }

    public void routerScan(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.wifiManager.getScanResults().forEach(sc -> {
            Router routerbdd = Router.getRouterFromBssid(sc.BSSID);
            if (routerbdd != null) {
                Router newRouter = new Router(sc.level, sc.BSSID);
                newRouter.setX(routerbdd.getX());
                newRouter.setY(routerbdd.getY());
                newRouter.setId(routerbdd.getId());
                if (newRouter.getX() != 0 && routerbdd.getY() != 0) {
                    this.routersScan.add(newRouter);
                }
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
            Router routerBdd = Router.getRouterFromBssid(router.getBssid());
            if (routerBdd != null) {
                this.calibrations.add(router);
            }
            if (this.calibrations.size() == 3) {
                break;
            }
        }

        Router c1 = calibrations.get(0);
        Router c2 = calibrations.get(1);
        Router c3 = calibrations.get(2);
        double d1 = Router.getDistanceFromRssi(this.routersScan.get(0).getLevel(), -40);
        double d2 = Router.getDistanceFromRssi(this.routersScan.get(1).getLevel(), -40);
        double d3 = Router.getDistanceFromRssi(this.routersScan.get(2).getLevel(), -40);

        double[] coordinate = Calibration.triangulate(c1.getX(), c1.getY(), c2.getX(), c2.getY(), c3.getX(), c3.getY(), d1, d2, d3);

//        float imageWidth = gridImageView.getWidth();
//        float imageHeight = gridImageView.getHeight();
//        float scaleX = (float) (imageWidth / mapWidth);
//        float scaleY = (float) (imageHeight / mapHeight);
        float adjustedX = (float) coordinate[0] * 1;
        float adjustedY = (float) coordinate[1] * 1;

        // Déplacer le joueur dans l'image
        this.context.runOnUiThread(()->{this.gridImageView.movePlayer(adjustedX, adjustedY);});

        System.out.println("test");


    }

}
