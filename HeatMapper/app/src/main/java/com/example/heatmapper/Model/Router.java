package com.example.heatmapper.Model;

public class Router {


    private String SSID;
    private String BSSID;
    private int level;
    private int X;
    private int Y;
    private double n;

    public Router(String SSID, String BSSID, int level, int X, int Y) {

        this.SSID = SSID;
        this.BSSID = BSSID;
        this.level = level;
        this.X = X;
        this.Y = Y;
        this.n = 2.0;

    }

    public double getN() {
        return n;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }
}
