package main.controler;


import okhttp3.Response;
import okhttp3.WebSocket;

public interface  SocketObserver  {

    public void onOpen(WebSocket webSocket, Response response);

    public void onMessage(WebSocket webSocket, String text);

    public void onFailure(WebSocket webSocket, Throwable t, Response response);

    public void onClosing(WebSocket webSocket, int code, String reason);
}
