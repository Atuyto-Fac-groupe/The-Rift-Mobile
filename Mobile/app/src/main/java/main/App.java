package main;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.Model.*;
import main.Model.BDD.ObjectBox;
import okhttp3.Response;
import okhttp3.WebSocket;
import main.Controler.SocketObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class App extends Application implements SocketObserver {

    public static String WebSocketUrl = "ws://10.6.5.93:9001/ws?idpersonne=2";

    public static OnSocketListener socketListener;

    public static SocketManager socketManager;

    public static Player player;

    public static final int ROW = 20;
    public static final int COL = 20;

    public static MutableLiveData<List<SystemMessage>> systemMessages;

    public static final int  NBENIGMA = 3;

    public static List<String> roomCode = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        socketListener = new OnSocketListener();
        socketListener.addObserver(this);
        socketManager = SocketManager.getInstance(socketListener);
        socketManager.startConnection();
        player = new Player();
        systemMessages = new MutableLiveData<>(new ArrayList<>());
        ObjectBox.init(this);

    }



    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d("Socket","Connexion etablie");
    }

    public void onMessage(WebSocket webSocket, String text) {
        Gson gson = new Gson();
        Message message = gson.fromJson(text, Message.class);
        try {
            SystemMessage systemMessage = gson.fromJson(message.getMessage(), SystemMessage.class);
            List<SystemMessage> currentMessages = systemMessages.getValue();
            if (currentMessages != null) {
                currentMessages.add(systemMessage);
                systemMessages.postValue(currentMessages);
            }
        } catch (JsonSyntaxException e) {
            Log.d("Socket", "Json syntax error");
        }
        Log.d("Socket", text);
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
