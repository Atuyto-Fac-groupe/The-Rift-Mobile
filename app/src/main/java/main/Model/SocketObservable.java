package main.Model;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import main.Controler.SocketObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class SocketObservable extends WebSocketListener {

    private List<SocketObserver> observers = new ArrayList<>();

    public void addObserver(SocketObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(SocketObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        observers.forEach(observer -> observer.onClosing(webSocket, code, reason));
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        observers.forEach(observer -> observer.onFailure(webSocket, t, response));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        observers.forEach(observer -> observer.onMessage(webSocket, text));
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        observers.forEach(observer -> observer.onOpen(webSocket, response));
    }
}
