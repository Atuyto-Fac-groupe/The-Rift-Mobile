package main.Controler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import main.Model.cartography.GridMap;
import main.View.Cartography.CartographyActivity;

public class CartographieControler extends BroadcastReceiver {

    private CartographyActivity cartographyActivity;
    private WifiManager wifiManager;
    private GridMap gridMap;

    public CartographieControler(CartographyActivity cartographyActivity) {
        this.cartographyActivity = cartographyActivity;
        this.wifiManager = this.cartographyActivity.getSystemService(WifiManager.class);

        if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.cartographyActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        WifiManager wifiManager = (WifiManager) this.cartographyActivity.getSystemService(Context.WIFI_SERVICE);
        boolean success = wifiManager.startScan();
        if (!success) {
            handleScanFailure();
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
        if (success) {
            handleScanResults();
        } else {
            handleScanFailure();
        }
    }


    private void handleScanResults() {
        if (ActivityCompat.checkSelfPermission(this.cartographyActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d("WIFI", this.wifiManager.getScanResults().toString());
    }
    private void handleScanFailure() {
    }

}
