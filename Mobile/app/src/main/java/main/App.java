package main;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import main.Model.BDD.ObjectBox;
import main.Model.Message;
import main.Model.Player;
import okhttp3.Response;
import okhttp3.WebSocket;
import main.Controler.SocketObserver;
import main.Model.OnSocketListener;
import main.Model.SocketManager;

import java.util.List;
import java.util.Objects;

public class App extends Application implements SocketObserver {

    public static String WebSocketUrl = "ws://10.6.5.93:9001/ws?idpersonne=2";

    public static OnSocketListener socketListener;

    public static SocketManager socketManager;

    public static final int ROW = 10;
    public static final int COL = 10;


    public static LiveData<List<Message>> systemMessages;
    public static Player player;

    public static final int  NBENIGMA = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        socketListener = new OnSocketListener();
        socketListener.addObserver(this);
        socketManager = SocketManager.getInstance(socketListener);
        socketManager.startConnection();
        player = new Player();
        systemMessages = new MutableLiveData<List<Message>>();
        ObjectBox.init(this);

    }



    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d("Socket","Connexion etablie");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Gson gson = new Gson();
        Message message = gson.fromJson(text, Message.class);
        //TODO faire vérif que bien messages système
        Objects.requireNonNull(systemMessages.getValue()).add(message);
        Log.d("Socket",text);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d("test", t.toString());

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d("Socket",reason);
    }
}
