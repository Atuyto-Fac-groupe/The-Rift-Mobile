package main.View;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.LoginActivityBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding binding;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = LoginActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        this.binding.titleReady.setOnClickListener((v) -> {startActivity(new Intent(LoginActivity.this, MainActivity.class));} );
    }
}
