package main.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.therift.R;
import com.example.therift.databinding.FragmentMessageBinding;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.App;
import main.controler.MessageControler;
import main.controler.SocketObserver;
import main.model.Message;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * FragmentMessage représente un fragment pour afficher les messages.
 * Il implémente l'interface SocketObserver pour recevoir des mises à jour
 * via WebSocket et afficher les messages correspondants à l'utilisateur.
 */
public class FragmentMessage extends Fragment implements SocketObserver {

    private FragmentMessageBinding binding;
    private Gson gson;

    /**
     * Appelé pour créer la vue du fragment.
     * Cette méthode initialise la mise en page, configure les observateurs
     * et met à jour la liste des messages si elle n'est pas vide.
     *
     * @param inflater           Le LayoutInflater utilisé pour gonfler la vue.
     * @param container          Le conteneur dans lequel la vue sera ajoutée.
     * @param savedInstanceState L'état de l'instance précédemment enregistré.
     * @return La vue gonflée du fragment.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        this.binding = FragmentMessageBinding.inflate(getLayoutInflater());
        this.gson = new Gson();
        App.socketListener.addObserver(this);
        if (!App.player.getMessages().isEmpty()) this.updateMessage(App.player.getMessages());

        this.binding.idEtMessage.setOnTouchListener(new MessageControler(this.binding.idEtMessage, this));

        return this.binding.getRoot();
    }

    /**
     * Met à jour l'affichage des messages dans le fragment.
     * Cette méthode efface les messages précédents et ajoute
     * les nouveaux messages au LinearLayout.
     *
     * @param messages La liste des messages à afficher.
     */
    private void updateMessage(List<Message> messages) {
        LinearLayout parentLayout = this.binding.liMessages;
        parentLayout.removeAllViews();
        for (Message message : messages) {
            TextView textView = new TextView(this.getContext());
            textView.setText(message.getMessage());
            textView.setTextSize(20);
            textView.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(20, 20, 20, 20);
            Drawable drawable = getActivity().getDrawable(R.drawable.bubble_shape);
            textView.setBackground(drawable);
            textView.setPadding(10, 10, 20, 10);
            if (message.getFrom().equals("2")) {
                textView.setGravity(Gravity.END);
                textView.setTextColor(Color.WHITE);
                layoutParams.gravity = Gravity.END;
                drawable.setTint(getActivity().getResources().getColor(R.color.Purple));
            } else {
                textView.setGravity(Gravity.START);
                textView.setTextColor(Color.BLACK);
                layoutParams.gravity = Gravity.START;
                drawable.setTint(Color.WHITE);
            }

            textView.setLayoutParams(layoutParams);
            parentLayout.addView(textView);
        }
        this.binding.idSvMain.post(()-> this.binding.idSvMain.fullScroll(View.FOCUS_DOWN));
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            this.getActivity().runOnUiThread(()->{updateMessage(App.player.getMessages());});

        } catch (JsonSyntaxException e) {
            Log.d("Message", e.getMessage());
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

    }
}
