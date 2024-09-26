package main.Model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import main.App;

public class SocketManager {

    private WebSocket webSocket;

    private OkHttpClient client;

    private static SocketManager instance;

    private static SocketObservable listener;

    public SocketManager(SocketObservable l) {
        listener = l;
    }

    public static SocketManager getInstance(SocketObservable listener) {
        if (instance == null) {
            instance = new SocketManager(listener);
        }
        return instance;
    }

    public void startConnection(){
        if (client == null) {
            client = new OkHttpClient();
            Request request = new Request.Builder().url(App.WebSocketUrl).build();
            webSocket = client.newWebSocket(request, listener);
        }
    }

    public void stopConnection(){
        if (webSocket != null) {
            client.dispatcher().executorService().shutdown();
        }
    }
}
