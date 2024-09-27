package main.View;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.R;
import com.example.therift.databinding.MainActivityBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SocketObserver {

    private MainActivityBinding binding;
    private TableControler tableControler;
    private Gson gson;
    private static MainActivity instance;

    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.socketListener.addObserver(this);
        instance = this;
        this.gson = new Gson();
        this.binding = MainActivityBinding.inflate(getLayoutInflater());
        this.setContentView(this.binding.getRoot());
        this.tableControler = new TableControler(this.binding.idFragContainer, this);
        this.binding.tabLayout.addOnTabSelectedListener( this.tableControler);

    }


    public void setBadgeMessageOnRead(){
        this.runOnUiThread(() -> {
            if (this.binding.tabLayout.getSelectedTabPosition() == 2) {
                TabLayout.Tab tab = this.binding.tabLayout.getTabAt(2);
                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                badgeDrawable.setVisible(false);

            }
        });
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Message message = gson.fromJson(text, Message.class);
        App.player.add(message);
        this.runOnUiThread(() -> {
            if (this.binding.tabLayout.getSelectedTabPosition() != 2) {
                TabLayout.Tab tab = this.binding.tabLayout.getTabAt(2);
                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(App.player.getNotSee());
            }
        });
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
