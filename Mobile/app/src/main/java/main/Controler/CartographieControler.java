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

import java.util.*;
import java.util.stream.Collectors;

public class CartographieControler implements View.OnTouchListener, View.OnClickListener {

    private CartographyActivity cartographyActivity;
    private WifiManager wifiManager;
    private Box<Router> routerBox;
//    private GridMap gridMap;
//    private Box<GridMap> gridMapBox;
    private HashMap<Point, List<Router>> pointListe;
    private List<Router> topRouter;
    private List<Point> points;

    @SuppressLint("ClickableViewAccessibility")
    public CartographieControler(CartographyActivity cartographyActivity) {
        this.cartographyActivity = cartographyActivity;
        this.wifiManager = this.cartographyActivity.getSystemService(WifiManager.class);
        this.routerBox = ObjectBox.get().boxFor(Router.class);
//        this.gridMapBox = ObjectBox.get().boxFor(GridMap.class);
        this.pointListe = new HashMap<>();
        this.topRouter = new ArrayList<>();
        this.points = new ArrayList<>();
//        this.gridMap = this.gridMapBox.query().equal(GridMap_.id, 1).build().findFirst();
//        if (this.gridMap == null) {
//            this.gridMap = new GridMap(App.ROW, App.COL);
//        }
//        this.gridMapBox.put(this.gridMap);
//        this.gridImageView = this.cartographyActivity.getBinding().gridView;
        if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.cartographyActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        wifiManager = (WifiManager) this.cartographyActivity.getSystemService(Context.WIFI_SERVICE);
        cartographyActivity.getBinding().gridView.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
        if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false;

        float x = event.getX();
        float y = event.getY();
        Point point = new Point(x, y);
        this.cartographyActivity.getBinding().gridView.addWifiMarker(x, y);
        this.wifiManager.startScan();
        Set<Integer> uniqueLevels = new HashSet<>();
        List<Router> tmpRouter = this.wifiManager.getScanResults().stream()
                .sorted((r1, r2) -> Integer.compare(r2.level, r1.level))
                .filter(scanResult -> uniqueLevels.add(scanResult.level))
                .limit(3)
                .map(s -> new Router(s.level, s.BSSID))
                .collect(Collectors.toList());

        for (Router s : tmpRouter) {
            Router existingRouter = Router.getRouterFromBssid(s.getBssid());
            if (existingRouter == null) {
                long id = routerBox.put(s);
                s.setId(id);
                topRouter.add(s);
            } else {
                existingRouter.setLevel(s.getLevel());
                topRouter.add(existingRouter);
            }
        }
        this.pointListe.put(point, tmpRouter);
        this.points.add(point);

        return true;
    }


    @Override
    public void onClick(View view) {
        List<Router> tmpRouter = new ArrayList<>();
        for (Router router : this.topRouter) {
            if (router.getX() != 0 && router.getY() != 0) continue;
            if (!tmpRouter.contains(router)) {
                calculatePositionRouter(router);
                tmpRouter.add(router);
            }
        }
    }

    private void calculatePositionRouter(Router router) {
        List<Point> topPoint = new ArrayList<>();
        List<Router> routerSelcted = new ArrayList<>();
        for (Point p : this.points) {
            List<Router> routerFromPoint = this.pointListe.get(p);
            if (routerFromPoint == null) break;
            Router clonedRouter = routerFromPoint.stream().filter(r -> r.getBssid().equals(router.getBssid()))
                    .findFirst().orElse(null);
            if (clonedRouter != null) {
                routerSelcted.add(clonedRouter);
                topPoint.add(p);
            }
        }

        if (topPoint.size() < 3) {
            return;
        }

        double x1 = topPoint.get(0).x;
        double y1 = topPoint.get(0).y;
        double x2 = topPoint.get(1).x;
        double y2 = topPoint.get(1).y;
        double x3 = topPoint.get(2).x;
        double y3 = topPoint.get(2).y;
        double d1 = Router.getDistanceFromRssi(routerSelcted.get(0).getLevel(), -40);
        double d2 = Router.getDistanceFromRssi(routerSelcted.get(1).getLevel(), -40);
        double d3 = Router.getDistanceFromRssi(routerSelcted.get(2).getLevel(), -40);

        double[] newCoordinate = Calibration.triangulate(x1, y1, x2, y2, x3, y3, d1, d2, d3);
        router.setX(newCoordinate[0]);
        router.setY(newCoordinate[1]);
        this.routerBox.put(router);
    }
}
