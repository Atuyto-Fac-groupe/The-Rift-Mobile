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

    public CartographieActivityBinding getBinding() {
        return binding;
    }

    private CartographieActivityBinding binding;

    private boolean stapeOneFinished;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = CartographieActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.stapeOneFinished = false;
        new CartographieControler(this);

    }


    public boolean isStapeOneFinished() {
        return stapeOneFinished;
    }

    public void setStapeOneFinished(boolean stapeOneFinished) {
        this.stapeOneFinished = stapeOneFinished;
    }


}
