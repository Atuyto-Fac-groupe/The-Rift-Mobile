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
import java.util.List;
import java.util.stream.Collectors;

public class CartographieControler implements View.OnTouchListener {

    private CartographyActivity cartographyActivity;
    private WifiManager wifiManager;
    private Box<Router> routerBox;
    private GridMap gridMap;
    private Box<GridMap> gridMapBox;
    private GridImageView gridImageView;
    private Box<Calibration> calibrationBox;

    @SuppressLint("ClickableViewAccessibility")
    public CartographieControler(CartographyActivity cartographyActivity) {
        this.cartographyActivity = cartographyActivity;
        this.wifiManager = this.cartographyActivity.getSystemService(WifiManager.class);
        this.routerBox = ObjectBox.get().boxFor(Router.class);
        this.gridMapBox = ObjectBox.get().boxFor(GridMap.class);
        this.calibrationBox = ObjectBox.get().boxFor(Calibration.class);
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
        List<Router> topRouter = new ArrayList<>();
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
            topTreeRouterFromScan.forEach(s -> topRouter.add(new Router(-40, s.BSSID)));
            topRouter.forEach(s -> {
                Router router = Router.getRouterFromBssid(s.getBssid());
                Calibration newPoint;
                if (router == null) {
                    long id = routerBox.put(s);
                    s.setId(id);
                    newPoint = new Calibration(x, y, s);
                }
                else {
                    newPoint = new Calibration(x, y, router);
                }

                this.calibrationBox.put(newPoint);
            });
            coordinatesCell = Cell.convertToGrid(x, y, this.gridImageView.getWidth(), this.gridImageView.getHeight(), App.ROW, App.COL);
            this.gridMap.getGridCells()[coordinatesCell[0]][coordinatesCell[1]] = new Cell(coordinatesCell[0], coordinatesCell[1]);
            this.gridMap.save();
            return true;
        }
        return false;
    }


//    private List<Router> get3Routers(List<ScanResult> scanners){
//        List<Router> routers = new ArrayList<>();
//        for (ScanResult scanResult : scanners) {
//            routers.add(new Router(scanResult.BSSID, scanResult.level));
//        }
//
//        routers.sort((r1, r2) -> Integer.compare(r2.getLevel(), r1.getLevel()));
//        return routers.stream().limit(3).collect(Collectors.toList());
//    }
}
