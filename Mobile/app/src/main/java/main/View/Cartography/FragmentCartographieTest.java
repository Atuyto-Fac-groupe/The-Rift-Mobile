package main.View.Cartography;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.therift.databinding.CartographieActivityBinding;
import main.App;
import main.Model.BDD.AppDatabase;
import main.Model.cartography.GridCell;
import main.Model.cartography.GridMap;
import main.Model.cartography.Router;
import main.View.GridImageView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FragmentCartographieTest extends Fragment {

    private CartographieActivityBinding binding;

    private GridImageView gridImageView;
    private GridMap gridMap;
    private WifiManager wifiManager;
    private  String currentSSID;


    private List<Router> signauxList;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        this.binding = CartographieActivityBinding.inflate(getLayoutInflater());
        this.gridMap = new GridMap(App.ROW, App.COL);
        this.signauxList = new ArrayList<>();
        this.wifiManager = this.getActivity().getSystemService(WifiManager.class);
        this.gridImageView = this.binding.gridView;
        return this.binding.getRoot();
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//
//        boolean succes = wifiManager.startScan();
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        currentSSID = wifiInfo.getSSID();
//
//        if (currentSSID.startsWith("\"")){
//            currentSSID = currentSSID.substring(1, currentSSID.length()-1);
//        }
//
//        gridImageView.setOnTouchListener(this::setOnTouchListener);
//
    }

//    private boolean setOnTouchListener(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            float x = event.getX();
//            float y = event.getY();
//            gridImageView.addWifiMarker(x, y);
//            handleWiFiScanAtLocation(x, y);
//            return true;
//        }
//        return false;
//    }
//
//    private void handleWiFiScanAtLocation(float x, float y) {
//        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        wifiManager.startScan();
//
//        List<ScanResult> results = wifiManager.getScanResults();
//        results.sort(new Comparator<ScanResult>() {
//            @Override
//            public int compare(ScanResult r1, ScanResult r2) {
//                return Integer.compare(r2.level, r1.level);
//            }
//        });
//        List<ScanResult> top5Results = results.subList(0, Math.min(5, results.size()));
//        List<Router> routeurs = new ArrayList<>();
//        for (ScanResult scanResult : top5Results) {
//            int[] gridPos = getGridPosition(x, y, gridImageView.getWidth(), gridImageView.getHeight(), 10, 10);
//            int row = gridPos[0];
//            int col = gridPos[1];
//            Router signal = new Router(scanResult.BSSID, scanResult.SSID, scanResult.level, row, col);
//            routeurs.add(signal);
//            signauxList.add(signal);
//            gridMap.addWifiDataToCell(row, col, routeurs);
//
//            new Thread(()->{
//                signal.sauvegarder(this.getContext());
//                gridMap.sauvegarder(this.getContext());
//                Log.d("WiFi", AppDatabase.getInstance(this.getContext()).routerDao().getAllRouter().toString());
//            }).start();
////            Log.d("WiFi", "SSID: " + scanResult.SSID + ", Force du signal: " + scanResult.level + " dBm, BSSID: " + scanResult.BSSID);
//
//        }
//    }
//
//    public int[] getGridPosition(float x, float y, int mapWidth, int mapHeight, int rows, int cols) {
//        int cellWidth = mapWidth / cols;
//        int cellHeight = mapHeight / rows;
//        int col = (int) (x / cellWidth);
//        int row = (int) (y / cellHeight);
//        return new int[]{row, col};
//    }
//
//    public void replacerPointsSurCarte(int newMapWidth, int newMapHeight) {
//        int cellWidth = newMapWidth / App.COL;
//        int cellHeight = newMapHeight / App.ROW;
//
//        for (int row = 0; row < App.ROW; row++) {
//            for (int col = 0; col < App.COL; col++) {
//                GridCell cell = gridMap.getCell(row, col);
//
//                if (cell != null && cell.getWifiData() != null && !cell.getWifiData().isEmpty()) {
//
//                    float cellX = col * cellWidth + (cellWidth / 2);
//                    float cellY = row * cellHeight + (cellHeight / 2);
//                    this.gridImageView.addWifiMarker(cellX, cellY);
//                }
//            }
//        }
//    }


}
