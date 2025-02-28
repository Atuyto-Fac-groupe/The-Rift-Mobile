package main.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.LoginActivityBinding;

import main.App;

/**
 * LoginActivity gère l'interface utilisateur pour la connexion à l'application.
 * Elle permet à l'utilisateur de se connecter et de passer à l'activité principale de l'application.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding binding;

    /**
     * Appelé lors de la création de l'activité. Cette méthode initialise
     * la mise en page, configure les éléments de l'interface utilisateur,
     * et définit les actions des boutons.
     *
     * @param savedInstanceState Si l'activité est en cours de réinitialisation
     *                           après avoir été précédemment arrêtée,
     *                           ce Bundle contient les données qu'elle
     *                           a fournies le plus récemment dans
     *                           onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = LoginActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        this.binding.btPlay.setOnClickListener((v) -> {
            if (this.binding.menuSelectedPlayer.getText().toString() != ""){
                App.player.setName(this.binding.menuSelectedPlayer.getText().toString());
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));} );
    }
}
