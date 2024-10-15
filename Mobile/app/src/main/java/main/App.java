package main;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import main.Model.BDD.ObjectBox;
import main.Model.Player;
import okhttp3.Response;
import okhttp3.WebSocket;
import main.Controler.SocketObserver;
import main.Model.OnSocketListener;
import main.Model.SocketManager;

public class App extends Application implements SocketObserver {

    public static String WebSocketUrl = "ws://10.6.5.93:9001/ws?idpersonne=2";

    public static OnSocketListener socketListener;

    public static SocketManager socketManager;

    public static Player player;

    public static final int ROW = 5;
    public static final int COL = 5;


    @Override
    public void onCreate() {
        super.onCreate();
        socketListener = new OnSocketListener();
        socketListener.addObserver(this);
        socketManager = SocketManager.getInstance(socketListener);
        socketManager.startConnection();
        player = new Player();
        ObjectBox.init(this);

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
