package com.example.heatmapper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloorPlanView floorPlanView;
    private WifiManager wifiManager;
    private  String currentSSID;

    private List<Signaux> signauxList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        floorPlanView = findViewById(R.id.floor_plan_view);
        signauxList = new ArrayList<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Activer la localisation", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        wifiManager.startScan();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        currentSSID = wifiInfo.getSSID();

        if (currentSSID.startsWith("\"")){
            currentSSID = currentSSID.substring(1, currentSSID.length()-1);
        }

        floorPlanView.setOnTouchListener(this::setOnTouchListener);

    }

    private boolean setOnTouchListener(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    floorPlanView.addWifiMarker(x, y);
                    handleWiFiScanAtLocation(x, y);
                    return true;
                }
                return false;
    }

    private void handleWiFiScanAtLocation(float x, float y) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<ScanResult> results = wifiManager.getScanResults();

        Signaux signal = null;
        for (ScanResult scanResult : results) {
            if (signal == null) {
                signal = new Signaux(scanResult.BSSID, scanResult.BSSID, scanResult.level);
            }
            if (scanResult.SSID.equals(currentSSID)) {
                if (signal.getLevel() > scanResult.level){
                    signal = new Signaux(scanResult.BSSID, scanResult.BSSID, scanResult.level);
                }
                Log.d("WiFi", "Point Wi-Fi proche du clic: " + scanResult.SSID + ", Force du signal: "
                        + scanResult.level + " BSSID : " + scanResult.BSSID);
            }
        }
        if (signal != null) {
            signauxList.add(signal);
        }
    }
}
