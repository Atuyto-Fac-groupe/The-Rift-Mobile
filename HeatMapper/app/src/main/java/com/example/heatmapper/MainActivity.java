package com.example.heatmapper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.heatmapper.View.FragmentCartographie;
import com.example.heatmapper.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {


    private MainActivityBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnCarto.setOnClickListener((v -> {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(this.binding.fragmentContainerView.getId(), new FragmentCartographie())
                    .addToBackStack(this.binding.fragmentContainerView.getTransitionName())
                    .commit();
        }));




    }



}
