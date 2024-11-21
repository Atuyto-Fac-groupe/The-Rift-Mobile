package main.View.Cartography;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.TestActivityBinding;
import main.Controler.MapControler;

import java.util.Timer;
import java.util.TimerTask;

public class MapFragment extends AppCompatActivity {

    private TestActivityBinding binding;
    private MapControler mapControler;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = TestActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());

       this.mapControler =  new MapControler(this.binding.gridView, this);

        new Thread(()->{
            this.mapControler.routerScan();
        }).start();

    }
}
