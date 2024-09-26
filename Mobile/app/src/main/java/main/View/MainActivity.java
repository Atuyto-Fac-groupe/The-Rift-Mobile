package main.View;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.R;
import com.example.therift.databinding.MainActivityBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.App;
import main.Controler.SocketObserver;
import main.Controler.TableControler;
import main.Model.Message;
import main.Model.OnSocketListener;
import okhttp3.Response;
import okhttp3.WebSocket;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SocketObserver {

    private MainActivityBinding binding;
    private TableControler tableControler;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.socketListener.addObserver(this);
        this.gson = new Gson();
        this.binding = MainActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.tableControler = new TableControler(this.binding.idFragContainer, this);
        this.binding.tabLayout.addOnTabSelectedListener( this.tableControler);

    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Message message = gson.fromJson(text, Message.class);
        App.player.add(message);
        if (this.binding.tabLayout.getSelectedTabPosition() != 2) {
            BadgeDrawable badge = this.binding.tabLayout.getTabAt(2).getOrCreateBadge();
            badge.setNumber(App.player.getNotSee());
            badge.setVisible(true);
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

    }
}
