package main.Controler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.app.ActivityCompat;
import main.Model.cartography.GridMap;
import main.Model.cartography.Router;
import main.View.Cartography.CartographyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CartographieControler implements View.OnTouchListener {

    private CartographyActivity cartographyActivity;
    private WifiManager wifiManager;
    private GridMap gridMap;

    @SuppressLint("ClickableViewAccessibility")
    public CartographieControler(CartographyActivity cartographyActivity) {
        this.cartographyActivity = cartographyActivity;
        this.wifiManager = this.cartographyActivity.getSystemService(WifiManager.class);

        if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.cartographyActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        wifiManager = (WifiManager) this.cartographyActivity.getSystemService(Context.WIFI_SERVICE);
        cartographyActivity.getBinding().gridView.setOnTouchListener(this);

    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        List<Router> topRouter = new ArrayList<>();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            float x = event.getX();
            float y = event.getY();
            cartographyActivity.getBinding().gridView.addWifiMarker(x, y);
            this.wifiManager.startScan();
            topRouter = get3Routers(this.wifiManager.getScanResults());
            Log.d("WIFI", topRouter.toString());

            for (Router router : topRouter) {
                Log.d("WIFI", "Diststance en vous et le router : " + router.getBSSID() + " : " + router.calculateDistanceFromRSSI(router.getLevel(), -40) + "m");
            }
            //TODO
            /*
            * faire l'enregistrement des routers si il existe pas
            * faire l'enregistrement des points de calibration / et gridcell dans gridmap
            * */

//            handleWiFiScanAtLocation(x, y);
//            return true;
        }
        return false;
    }


    private List<Router> get3Routers(List<ScanResult> scanners){
        List<Router> routers = new ArrayList<>();
        for (ScanResult scanResult : scanners) {
            routers.add(new Router(scanResult.BSSID, scanResult.level));
        }

        routers.sort((r1, r2) -> Integer.compare(r2.getLevel(), r1.getLevel()));
        return routers.stream().limit(3).collect(Collectors.toList());
    }
}
