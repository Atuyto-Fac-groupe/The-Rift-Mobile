package main.View.Cartography;

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
    private CartographieControler controler;

    private boolean stapeOneFinished;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = CartographieActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.stapeOneFinished = false;
        this.controler = new CartographieControler(this);
        this.binding.button.setOnClickListener(this.controler);
    }

}
