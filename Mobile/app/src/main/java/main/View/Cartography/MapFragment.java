package main.View.Cartography;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.TestActivityBinding;
import main.Controler.MapControler;

public class MapFragment extends AppCompatActivity {

    private TestActivityBinding binding;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = TestActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        new MapControler(this.binding.gridView, this);

    }
}
