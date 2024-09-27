package main.View;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import main.Controler.MessageControler;
import main.Controler.SocketObserver;
import main.Model.Message;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

public class FragmentMessage extends Fragment implements SocketObserver {

    private FragmentMessageBinding binding;
    private Gson gson;

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

    private void updateMessage(List<Message> messages) {
        LinearLayout parentLayout = this.binding.liMessages;
        parentLayout.removeAllViews();
        for (Message message : messages) {
            TextView textView = new TextView(this.getContext());
            textView.setText(message.getMessage());
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(20, 20, 20, 20);

            if (message.getFrom().equals("2")) {
                layoutParams.gravity = Gravity.END;
            }
            else {
               layoutParams.gravity = Gravity.START;
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

    }
}
