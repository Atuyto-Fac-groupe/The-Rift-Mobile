package main.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.R;
import com.example.therift.databinding.MainActivityBinding;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.App;
import main.controler.NavControler;
import main.controler.SocketObserver;
import main.model.Message;
import main.model.SystemMessage;
import okhttp3.Response;
import okhttp3.WebSocket;

import java.util.Objects;


/**
 * MainActivity sert de point d'entrée principal pour l'application.
 * Elle gère la communication par socket, les interactions avec l'interface utilisateur,
 * et affiche différents fragments en fonction des interactions de l'utilisateur.
 */
public class MainActivity extends AppCompatActivity implements SocketObserver {

    private MainActivityBinding binding;
    private Gson gson;
    private static MainActivity instance;

    public MainActivity() {
        super();
        instance = this;
    }
    /**
     * Récupère l'instance unique de MainActivity.
     *
     * @return L'instance unique de MainActivity.
     */
    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }
    /**
     * Appelé lors de la création de l'activité. Cette méthode initialise
     * l'écouteur de socket, gonfle la mise en page, et configure
     * la mise en page des onglets avec le fragment associé.
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
        NavControler navControler;
        App.socketListener.addObserver(this);
        this.gson = new Gson();
        this.binding = MainActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.binding.tvTitleWindow.setText("HISTOIRE");
        this.binding.btHistoire.setBackground(getDrawable(R.drawable.background_buton_navigation_ative));
        this.binding.btHistoire.setImageDrawable(getDrawable(R.drawable.ic_key_white));
        navControler = new NavControler(this.binding.idFragContainer, this.binding,this);
        this.binding.btMessages.setOnClickListener(navControler);
        this.binding.btNote.setOnClickListener(navControler);
        this.binding.btHistoire.setOnClickListener(navControler);
        this.getSupportFragmentManager().beginTransaction()
                .replace(this.binding.idFragContainer.getId(), new FragmentHistoire())
                .addToBackStack(this.binding.idFragContainer.getTransitionName())
                .commit();

        TextView firstStageText = findViewById(R.id.first_stage);
        TextView secondStageText = findViewById(R.id.second_stage);
        ImageView imMap = findViewById(R.id.im_map);

        imMap.setOnClickListener(v -> {
            if (imMap.getDrawable().getConstantState().equals(getDrawable(R.drawable.second_stage).getConstantState())) {
                imMap.setImageDrawable(getDrawable(R.drawable.first_stage));
                firstStageText.setVisibility(View.VISIBLE);
                secondStageText.setVisibility(View.GONE);
            } else {
                imMap.setImageDrawable(getDrawable(R.drawable.second_stage));
                firstStageText.setVisibility(View.GONE);
                secondStageText.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Met à jour le badge de message pour indiquer un statut lu
     * pour l'onglet actuel. Si l'onglet sélectionné n'est pas l'onglet
     * d'index 2, le badge sera défini comme invisible.
     */
    @SuppressLint("ResourceType")
    public void setBadgeMessageOnRead(){
        this.runOnUiThread(() -> {
            if (this.binding.idFragContainer.getId() != R.layout.fragment_histoire && App.player.getNotSee() > 0){
                this.binding.btMessages.setColorFilter(getResources().getColor(R.color.Red, getTheme()));
            }
        });
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

    }

    /**
     * Appelé lorsqu'un message est reçu via WebSocket.
     *
     * @param webSocket Le WebSocket à partir duquel le message est reçu.
     * @param text Le texte du message reçu.
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Message message = gson.fromJson(text, Message.class);
        if (Objects.equals(message.getFrom(), "0") || Objects.equals(message.getTo(), "0")) {
            return;
        }
        try {
            gson.fromJson(message.getMessage(), SystemMessage.class);
        } catch (JsonSyntaxException e) {
            App.player.add(message);
        }
        this.setBadgeMessageOnRead();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

    }

    public MainActivityBinding getBinding() {
        return binding;
    }
}
