package main;

import android.app.Application;
import android.util.Log;
import main.Model.Player;
import okhttp3.Response;
import okhttp3.WebSocket;
import main.Controler.SocketObserver;
import main.Model.OnSocketListener;
import main.Model.SocketManager;

public class App extends Application implements SocketObserver {

    public static String WebSocketUrl = "ws://192.168.1.34:9001/ws?idpersonne=2";

    public static OnSocketListener socketListener;

    public static SocketManager socketManager;

    public static Player player;

    @Override
    public void onCreate() {
        super.onCreate();
        socketListener = new OnSocketListener();
        socketListener.addObserver(this);
        socketManager = SocketManager.getInstance(socketListener);
        socketManager.startConnection();
        player = new Player();


    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d("Socket","Connexion etablie");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
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
