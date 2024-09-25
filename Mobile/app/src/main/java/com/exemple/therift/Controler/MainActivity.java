package com.exemple.therift.Controler;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.LoadingActivityBinding;

public class MainActivity extends AppCompatActivity {

    private LoadingActivityBinding binding;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = LoadingActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());


        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {

                try {
                    try {
                        Thread.sleep(1000);

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            }
    });
    thread.start();

    }
}
