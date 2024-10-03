package main.View;


import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.therift.databinding.TestActivityBinding;
import main.Model.BDD.AppDatabase;
import main.Model.Player;
import main.Model.Router;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class FragmentTest extends Fragment {


    private TestActivityBinding binding;
    private WifiManager wifiManager;
    private FloorPlanView floorPlanView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        this.binding = TestActivityBinding.inflate(getLayoutInflater());
        this.wifiManager = this.getActivity().getSystemService(WifiManager.class);
        this.floorPlanView = this.binding.floorPlanView;

        new Thread(()->{
            while(true){
                startWifiScan();
            }
        }).start();
        return this.binding.getRoot();
    }

    private void startWifiScan() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();

        handleWiFiScanResults(scanResults);
    }

    private void handleWiFiScanResults(List<ScanResult> results) {
        results.sort(new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult r1, ScanResult r2) {
                return Integer.compare(r2.level, r1.level);
            }
        });
        List<ScanResult> top3Routers = results.subList(0, Math.min(3, results.size()));
        calculatePlayerPosition(top3Routers);
    }

    private double calculateDistanceFromRSSI(ScanResult scanResult, Router router) {
        int rssi = scanResult.level; // Niveau RSSI capté du routeur
        double txPower = router.getLevel(); // Niveau RSSI mesuré à 1m
        double n = router.getN(); // Facteur d'atténuation

        // Formule pour calculer la distance
        return Math.pow(10, (txPower - rssi) / (10 * n));
    }


    private Router getRouterFromDb(ScanResult routeurDetect, List<Router> allRouter) {
        return allRouter.stream()
                .filter(e -> e.getSSID().equals(routeurDetect.BSSID))
                .findFirst()
                .orElse(null);
    }

    private void calculatePlayerPosition(List<ScanResult> routers) {
        AppDatabase appDatabase = AppDatabase.getInstance(this.getContext());
        List<Router> allRouter = appDatabase.routerDao().getAllRouter();
        Router router1 = getRouterFromDb(routers.get(0), allRouter);
        Router router2 = getRouterFromDb(routers.get(1), allRouter);
        Router router3 = getRouterFromDb(routers.get(2), allRouter);
        double d1;
        double d2;
        double d3;

        if (routers.size() < 3) {
            Log.d("Triangulation", "Pas assez de routeurs pour trianguler.");
            return;
        }

       try {
           d1 = calculateDistanceFromRSSI(routers.get(0),router1);
           d2 = calculateDistanceFromRSSI(routers.get(1), router2);
           d3 = calculateDistanceFromRSSI(routers.get(2), router3);

       } catch (Exception e) {
           return;
       }


        double x1 = router1.getX();
        double y1 = router1.getY();
        double x2 = router2.getX();
        double y2 = router2.getY();
        double x3 = router3.getX();
        double y3 = router3.getY();

        double[] position = triangulate(x1, y1, d1, x2, y2, d2, x3, y3, d3);

        if (position != null) {

            this.getActivity().runOnUiThread(()-> {
                this.floorPlanView.movePlayer((float) position[0], (float) position[1]);
            });

        }
    }

    private double[] triangulate(double x1, double y1, double d1, double x2, double y2, double d2, double x3, double y3, double d3) {
        // Vérification si les points sont trop proches
        if (Math.abs(x1 - x2) < 1e-6 && Math.abs(y1 - y2) < 1e-6) {
            return null; // Les points sont trop proches, triangulation non valide
        }

        double A = 2 * (x2 - x1);
        double B = 2 * (y2 - y1);
        double C = d1 * d1 - d2 * d2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2;
        double D = 2 * (x3 - x2);
        double E = 2 * (y3 - y2);
        double F = d2 * d2 - d3 * d3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3;

        double denominator = (A * E - B * D);
        if (Math.abs(denominator) < 1e-6) { // Vérification du dénominateur
            return null; // Si le dénominateur est proche de zéro, la triangulation échoue
        }

        double x = (C * E - B * F) / denominator;
        double y = (A * F - C * D) / denominator;
        return new double[]{x, y};
    }





}
