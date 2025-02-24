package main.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.databinding.LoadingActivityBinding;
/**
 * LoadingActivity est l'activité de chargement qui s'affiche pendant que
 * l'application effectue des opérations de démarrage. Elle attend un
 * certain temps avant de passer à l'activité de connexion.
 */
public class LoadingActivity extends AppCompatActivity {

    private LoadingActivityBinding binding;

    /**
     * Appelé lors de la création de l'activité. Cette méthode initialise
     * la mise en page, configure les éléments de l'interface utilisateur,
     * et démarre un fil d'exécution pour simuler une opération de chargement.
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
        this.binding = LoadingActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    Log.d("Loading", e.getMessage());
                }
            }
        });
        thread.start();
    }
}