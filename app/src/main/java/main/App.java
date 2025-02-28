package main;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import main.model.*;
import main.model.BDD.ObjectBox;
import okhttp3.Response;
import okhttp3.WebSocket;
import main.controler.SocketObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ScheduledExecutorService scheduler;

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
        scheduler = Executors.newScheduledThreadPool(1);
    }



    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d("Socket","Connexion etablie");
        this.startKeepAlive();
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
        Log.d("Socket", "Message : " + text);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d("Socket", "OnFailed : " + t.toString());

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d("Socket", "Closing : " + reason);
    }

    public void startKeepAlive() {
        // Programme un ping toutes les 30 secondes
        scheduler.scheduleWithFixedDelay(this::sendPing, 0, 30, TimeUnit.SECONDS);
    }

    // Méthode pour envoyer un message Ping
    private void sendPing() {
        if (socketManager != null) {
            try {
                Message message = new Message("Ping", "0", "0");
                Gson gson = new Gson();
                Log.d("Socket", "Ping envoyé");
                socketManager.sendMessage(gson.toJson(message));
            } catch (Exception e) {
                Log.d("Socket", "Erreur lors de l'envoi du ping: " + e.toString());
            }
        } else {
            Log.d("Socket", "Le socket n'est pas connecté.");
        }
    }

    // Pour arrêter le ping quand ce n'est plus nécessaire
    public void stopKeepAlive() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown(); // Arrête les pings périodiques
            Log.d("Socket", "Pings arrêtés.");
        }
    }
}
