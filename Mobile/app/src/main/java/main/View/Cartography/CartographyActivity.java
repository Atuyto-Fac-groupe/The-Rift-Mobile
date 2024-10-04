package main.View.Cartography;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.CartographieActivityBinding;
import main.Controler.CartographieControler;

public class CartographyActivity extends AppCompatActivity {

    private CartographieActivityBinding binding;

    private boolean stapeOneFinished;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = CartographieActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.stapeOneFinished = false;
        this.receiver = new CartographieControler(this);

    }


    public boolean isStapeOneFinished() {
        return stapeOneFinished;
    }

    public void setStapeOneFinished(boolean stapeOneFinished) {
        this.stapeOneFinished = stapeOneFinished;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(this.receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }
}
