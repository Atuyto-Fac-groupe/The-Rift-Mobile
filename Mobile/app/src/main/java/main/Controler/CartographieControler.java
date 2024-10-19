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
    private GridMap gridMap;
    private Box<GridMap> gridMapBox;
    private GridImageView gridImageView;
    private HashMap<Point, List<Router>> pointListe;
    private List<Router> topRouter;
    private List<Point> points;

    @SuppressLint("ClickableViewAccessibility")
    public CartographieControler(CartographyActivity cartographyActivity) {
        this.cartographyActivity = cartographyActivity;
        this.wifiManager = this.cartographyActivity.getSystemService(WifiManager.class);
        this.routerBox = ObjectBox.get().boxFor(Router.class);
        this.gridMapBox = ObjectBox.get().boxFor(GridMap.class);
        this.pointListe = new HashMap<>();
        this.topRouter = new ArrayList<>();
        this.points = new ArrayList<>();
        this.gridMap = this.gridMapBox.query().equal(GridMap_.id, 1).build().findFirst();
        if (this.gridMap == null) {
            this.gridMap = new GridMap(App.ROW, App.COL);
        }
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
            Point point = new Point(x, y);
            this.cartographyActivity.getBinding().gridView.addWifiMarker(x, y);
            this.wifiManager.startScan();
            Set<Integer> uniqueLevels = new HashSet<>();
            topTreeRouterFromScan = this.wifiManager.getScanResults().stream()
                    .sorted((r1, r2) -> Integer.compare(r2.level, r1.level))
                    .filter(scanResult -> uniqueLevels.add(scanResult.level))
                    .limit(3)
                    .collect(Collectors.toList());

            topTreeRouterFromScan.forEach(s -> tmpRouter.add(new Router(s.level, s.BSSID)));

            for (Router s : tmpRouter) {
                Router router = Router.getRouterFromBssid(s.getBssid());
                int oldLevel = s.getLevel();
//                for (Router router1 : this.topRouter) {
//                    if (router1.getBssid().equals(s.getBssid())) {
//                        added = true;
//                        break;
//                    }
//                }

                if (router == null) {
                    long id = routerBox.put(s);
                    s.setId(id);
//                    this.pointListe.put(s, new double[]{x, y});
                    s.setLevel(oldLevel);
                    topRouter.add(s);
                } else {
//                    this.pointListe.put(router, new double[]{x, y});
                    router.setLevel(oldLevel);
                    topRouter.add(router);
                }
                this.pointListe.put(point, tmpRouter);
                this.points.add(point);
            }


//            coordinatesCell = Cell.convertToGrid(x, y, this.gridImageView.getWidth(), this.gridImageView.getHeight(), App.ROW, App.COL);
//            this.gridMap.getGridCells()[coordinatesCell[0]][coordinatesCell[1]] = new Cell(coordinatesCell[0], coordinatesCell[1]);
//            this.gridMap.save();

            return true;
        }
        return false;
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
        Set<Integer> uniqueLevels = new HashSet<>();
        int cpt = 0;

        for (Point p : this.points) {
            if (cpt == 3) break;
            List<Router> r = this.pointListe.get(p);
            if (r == null) break;

            for (Router routerSel : r) {
                if (routerSel.getBssid().equals(router.getBssid()) && uniqueLevels.add(routerSel.getLevel())) {
                    Router clonedRouter = new Router(routerSel.getLevel(), routerSel.getBssid());
                    routerSelcted.add(clonedRouter);
                    topPoint.add(p);
                    cpt++;
                }
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
