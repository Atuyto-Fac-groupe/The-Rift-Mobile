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
import main.Controler.Router;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class FragmentTest extends Fragment {


    private com.example.therift.databinding.TestActivityBinding binding;
    private WifiManager wifiManager;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        this.binding = TestActivityBinding.inflate(getLayoutInflater());
        this.wifiManager = this.getActivity().getSystemService(WifiManager.class);


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

    private double calculateDistanceFromRSSI(int rssi, int A, double n) {
        return Math.pow(10, (A - rssi) / (10 * n));
    }

    private void calculatePlayerPosition(List<ScanResult> routers) {

        if (routers.size() < 3) {
            Log.d("Triangulation", "Pas assez de routeurs pour trianguler.");
            return;
        }


        List<Router> knownRouters = getKnownRoutersFromDatabase();



        double d1 = calculateDistanceFromRSSI(routers.get(0).level);
        double d2 = calculateDistanceFromRSSI(routers.get(1).level);
        double d3 = calculateDistanceFromRSSI(routers.get(2).level);


        double x1 = knownRouters.get(0).getX();
        double y1 = knownRouters.get(0).getY();
        double x2 = knownRouters.get(1).getX();
        double y2 = knownRouters.get(1).getY();
        double x3 = knownRouters.get(2).getX();
        double y3 = knownRouters.get(2).getY();


        double[] position = triangulate(x1, y1, d1, x2, y2, d2, x3, y3, d3);

        if (position != null) {
            // Mettre à jour la position du joueur sur la carte
//            playerX = position[0];
//            playerY = position[1];
//            updatePlayerPositionOnMap();
        }
    }

    private double[] triangulate(double x1, double y1, double d1, double x2, double y2, double d2, double x3, double y3, double d3) {
        // Implémentation simplifiée de la triangulation
        // Vous pouvez utiliser des algorithmes plus sophistiqués si nécessaire

        // Calcul basique basé sur trois cercles (peut être ajusté)
        double A = 2 * (x2 - x1);
        double B = 2 * (y2 - y1);
        double C = d1 * d1 - d2 * d2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2;
        double D = 2 * (x3 - x2);
        double E = 2 * (y3 - y2);
        double F = d2 * d2 - d3 * d3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3;

        double denominator = (A * E - B * D);
        if (denominator == 0) {
            // Triangulation impossible si les cercles sont colinéaires
            return null;
        }

        double x = (C * E - B * F) / denominator;
        double y = (A * F - C * D) / denominator;

        return new double[]{x, y};
    }




}
