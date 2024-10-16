package main.Controler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.app.ActivityCompat;
import io.objectbox.Box;
import main.App;
import main.Model.BDD.ObjectBox;
import main.Model.cartography.*;
import main.View.Cartography.CartographyActivity;
import main.View.GridImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CartographieControler implements View.OnTouchListener, View.OnClickListener {

    private CartographyActivity cartographyActivity;
    private WifiManager wifiManager;
    private Box<Router> routerBox;
    private GridMap gridMap;
    private Box<GridMap> gridMapBox;
    private GridImageView gridImageView;
    private HashMap<Router, double[]> pointListe;
    private List<Router> topRouter;

    @SuppressLint("ClickableViewAccessibility")
    public CartographieControler(CartographyActivity cartographyActivity) {
        this.cartographyActivity = cartographyActivity;
        this.wifiManager = this.cartographyActivity.getSystemService(WifiManager.class);
        this.routerBox = ObjectBox.get().boxFor(Router.class);
        this.gridMapBox = ObjectBox.get().boxFor(GridMap.class);
        this.pointListe = new HashMap<>();
        this.topRouter = new ArrayList<>();
        this.gridMap = this.gridMapBox.query().equal(GridMap_.id, 1).build().findFirst();
        if (this.gridMap == null) { this.gridMap = new GridMap(App.ROW, App.COL); }
        this.gridMapBox.put(this.gridMap);
        this.gridImageView = this.cartographyActivity.getBinding().gridView;
        if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.cartographyActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        wifiManager = (WifiManager) this.cartographyActivity.getSystemService(Context.WIFI_SERVICE);
        cartographyActivity.getBinding().gridView.setOnTouchListener(this);

    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        List<Router> tmpRouter = new ArrayList<>();
        List<ScanResult> topTreeRouterFromScan;
        float x;
        float y;
        int[] coordinatesCell;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            x = event.getX();
            y = event.getY();

            this.cartographyActivity.getBinding().gridView.addWifiMarker(x, y);
            this.wifiManager.startScan();
            topTreeRouterFromScan = this.wifiManager.getScanResults().stream()
                    .sorted((r1, r2) -> Integer.compare(r2.level, r1.level))
                    .limit(3)
                    .collect(Collectors.toList());
            topTreeRouterFromScan.forEach(s -> tmpRouter.add(new Router(-40, s.BSSID)));
            tmpRouter.forEach(s -> {
                Router router = Router.getRouterFromBssid(s.getBssid());
                Calibration newPoint;
                if (router == null) {
                    long id = routerBox.put(s);
                    s.setId(id);
                    this.pointListe.put(s, new double[]{x, y});
                }
                else this.pointListe.put(router, new double[]{x, y});

//                if(this.topRouter.isEmpty()){
//                    topRouter.add(s);
//                }
//                for (Router router1 : this.topRouter) {
//                    if (!router1.getBssid().equals(s.getBssid())) {
//                        topRouter.add(s);
//                    }
//                }
            });
//            coordinatesCell = Cell.convertToGrid(x, y, this.gridImageView.getWidth(), this.gridImageView.getHeight(), App.ROW, App.COL);
//            this.gridMap.getGridCells()[coordinatesCell[0]][coordinatesCell[1]] = new Cell(coordinatesCell[0], coordinatesCell[1]);
//            this.gridMap.save();

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        for (Router router : this.topRouter) {
            if (router.getX() != 0 && router.getY() != 0) continue;
            calculatePositionRouter(router);
        }
    }

    private void calculatePositionRouter(Router router) {
        List<double[]> topPoint = new ArrayList<>();
        List<Double> topDistance = new ArrayList<>();
        Calibration calibration;
        int cpt = 0;
        for (Router r : this.topRouter) {
            if(cpt < 3 && r.getBssid().equals(router.getBssid())) {
                topPoint.add(pointListe.get(r));
                topDistance.add(Router.getDistanceFromRssi(r.getLevel(), -40));
            }
            cpt++;
        }
        double x1 = topPoint.get(0)[0];
        double y1 = topPoint.get(0)[1];
        double x2 = topPoint.get(1)[0];
        double y2 = topPoint.get(1)[1];
        double x3 = topPoint.get(2)[0];
        double y3 = topPoint.get(2)[1];
        double d1 = topDistance.get(0);
        double d2 = topDistance.get(1);
        double d3 = topDistance.get(2);

        double[] newCoordinate = Calibration.triangulate(x1, y1, x2, y2, x3, y3, d1, d2, d3);
        router.setX(newCoordinate[0]);
        router.setY(newCoordinate[1]);
        this.routerBox.put(router);

    }
}
