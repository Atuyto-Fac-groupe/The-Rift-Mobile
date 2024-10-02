package com.example.heatmapper;

public class Signaux {


    private String SSID;
    private String BSSID;
    private int level;

    public Signaux(String SSID, String BSSID, int level) {

        this.SSID = SSID;
        this.BSSID = BSSID;
        this.level = level;
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
